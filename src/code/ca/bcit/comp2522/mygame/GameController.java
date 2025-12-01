package ca.bcit.comp2522.mygame;

import ca.bcit.comp2522.mygame.model.*;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Controller class for managing game logic and interactions.
 *
 * @author David Martinez
 * @version 1.0
 */
public class GameController
{
    private static final double ENEMY_MAX_HEALTH             = 100.0;
    private static final double NO_DAMAGE                    = 0.0;
    private static final double MIN_HEALTH                   = 0.0;
    private static final double MAX_ENEMY_HEALTH_INCREASE    = 30.0;
    private static final double MIN_ENEMY_HEALTH_INCREASE    = 5.0;
    private static final int    ONE_ENEMY_DEFEATED           = 1;
    private static final int    UPGRADE_COST_SUBTRACTION     = -1;
    private static final int    MIN_UPGRADE_COST             = 1;
    private static final int    MAX_KILL_AWARD_UPGRADE_POINT = 3;
    private static final int    MIN_KILL_AWARD_UPGRADE_POINT = 1;
    private static final int    STARTING_UPGRADE_POINTS      = 5;
    private static final double HALF_HEALTH_RATIO            = 0.5;
    private static final double QUARTER_HEALTH_RATIO         = 0.25;
    private static final double DPS_WINDOW_SECONDS           = 5.0;
    private static final double INITIAL_ELAPSED_TIME_SECONDS = 0.0;
    private static final long NANOS_TO_SECONDS_CONVERT_FACTOR = 1_000_000_000L;

    private final GameView          view;
    private final Player            player;
    private final List<DamageEvent> damageEvents;

    private AnimationTimer gameLoop;

    private double  elapsedTimeSeconds;
    private double  enemyHealth;
    private double  enemyMaxHealth;
    private long    lastTimeNanos;
    private boolean newHighScoreAchieved;
    private int     highScore;

    /**
     * Constructs a GameController with the specified GameView.
     *
     * @param view the GameView instance
     */
    public GameController(final GameView view)
    {
        validateView(view);
        this.view = view;

        player       = new Player();
        damageEvents = new ArrayList<>();

        initializeHighScore();
        initializeEnemy();
        initializeHandlers();

        elapsedTimeSeconds = INITIAL_ELAPSED_TIME_SECONDS;

        updateStatsLabels();
        updateEnemyBar();
    }

    /**
     * Validate the provided GameView.
     *
     * @param view the GameView to validate
     * @throws IllegalArgumentException if view is null
     */
    private void validateView(final GameView view)
    {
        if (view == null)
        {
            throw new IllegalArgumentException("GameView cannot be null.");
        }
    }

    /**
     * Initialize the high score from persistent storage.
     */
    private void initializeHighScore()
    {
        highScore = HighScoreManager.load();
    }

    /**
     * Initialize the enemy stats.
     */
    private void initializeEnemy()
    {
        enemyMaxHealth = ENEMY_MAX_HEALTH;
        enemyHealth    = enemyMaxHealth;
    }

    /**
     * Initialize event handlers for user interactions.
     */
    private void initializeHandlers()
    {
        view.getAttackButton().
            setOnAction(event -> handleAttackClick());

        view.getLevelOneAutoUpgradeButton()
            .setOnAction(event -> applyLevelOneAutoUpgrade());

        view.getLevelTwoAutoUpgradeButton()
            .setOnAction(event -> applyLevelTwoAutoUpgrade());

        view.getLevelOneHealthUpgradeButton()
            .setOnAction(event -> applyLevelOneHealthUpgrade());

        view.getLevelOneClickUpgradeButton()
            .setOnAction(event -> applyLevelOneClickUpgrade());
    }

    /**
     * Start the main game loop using AnimationTimer.
     */
    public void startGameLoop()
    {
        gameLoop = new AnimationTimer()
        {

            private static final double CONVERSION_FACTOR_FROM_NANO_TO_SECONDS = 1_000_000_000.0;
            private static final int    NO_NANOSECONDS                         = 0;

            @Override
            public void handle(final long now)
            {
                if (lastTimeNanos == NO_NANOSECONDS)
                {
                    lastTimeNanos = now;
                    return;
                }

                final double deltaSeconds;

                deltaSeconds  = (now - lastTimeNanos) / CONVERSION_FACTOR_FROM_NANO_TO_SECONDS;
                lastTimeNanos = now;

                tick(deltaSeconds);
            }
        };

        gameLoop.start();
    }

    /**
     * Update the game state based on elapsed time.
     *
     * @param deltaSeconds time elapsed since last update in seconds
     */
    private void tick(final double deltaSeconds)
    {
        final Clicker clicker;

        clicker = player.getClicker();
        clicker.tick(deltaSeconds);

        final double autoDamage;
        autoDamage = clicker.getAutoDamagePerSecond() * deltaSeconds;

        if (autoDamage > NO_DAMAGE)
        {
            dealDamage(autoDamage);
        }

        // simple linear enemy regen or decay could also go here if you want
        updateEnemyBar();
        updateDpsLabel();
    }

    /**
     * Update the DPS label in the UI.
     */
    private void updateDpsLabel()
    {
        final long nowNanos;
        final long windowNanos;

        nowNanos = System.nanoTime();
        windowNanos = (long) (DPS_WINDOW_SECONDS * NANOS_TO_SECONDS_CONVERT_FACTOR);

        // Remove events older than DPS_WINDOW_SECONDS
        damageEvents.removeIf(event ->
                                  elapsedTimeSeconds - event.getTimestampNanos() > DPS_WINDOW_SECONDS);

        final double totalRecentDamage;

        totalRecentDamage = damageEvents
            .stream()
            .mapToDouble(DamageEvent::getDamageAmount)
            .sum();

        final double dps;

        dps = totalRecentDamage / DPS_WINDOW_SECONDS;

        view.getDpsLabel()
            .setText(String.format("DPS (last %.0f s): %.2f",
                                   DPS_WINDOW_SECONDS,
                                   dps));
    }

    /**
     * Handle the attack button click event.
     */
    private void handleAttackClick()
    {
        final Clicker clicker;
        final double damage;

        clicker = player.getClicker();
        damage  = clicker.getDamagePerClick();

        dealDamage(damage);
        updateEnemyBar();
    }

    /**
     * Deal damage to the enemy.
     *
     * @param damageAmount amount of damage to deal
     */
    private void dealDamage(final double damageAmount)
    {
        enemyHealth -= damageAmount;

        if (enemyHealth < MIN_HEALTH)
        {
            enemyHealth = MIN_HEALTH;
        }

        recordDamageEvent(damageAmount);

        if (enemyHealth == MIN_HEALTH)
        {
            resetEnemy();
        }
    }

    /**
     * Record a damage event for DPS calculation.
     *
     * @param damageAmount amount of damage dealt
     */
    private void recordDamageEvent(final double damageAmount)
    {
        final long nowNanos;
        final DamageEvent event;

        nowNanos = System.nanoTime();
        event = new DamageEvent(nowNanos, damageAmount);

        damageEvents.add(event);
    }

    /**
     * Reset the enemy for the next round.
     * Enemy health increase and award for killing are randomized.
     */
    private void resetEnemy()
    {
        final RandomGenerator rng;
        final double rngHealthBonus;
        final int rngUpgradeBonus;

        rng = RandomGenerator.getDefault();

        rngHealthBonus = rng.nextDouble(MIN_ENEMY_HEALTH_INCREASE,
                                        MAX_ENEMY_HEALTH_INCREASE);
        System.out.println("DEBUGGING: new enemy max health bonus: " + rngHealthBonus);

        rngUpgradeBonus = rng.nextInt(MIN_KILL_AWARD_UPGRADE_POINT,
                                      MAX_KILL_AWARD_UPGRADE_POINT);
        System.out.println("DEBUGGING: upgrade points awarded: " + rngUpgradeBonus);

        enemyMaxHealth += rngHealthBonus;
        enemyHealth = enemyMaxHealth;

        player.addToStat("upgradePoints", rngUpgradeBonus);
        player.addToStat("enemiesDefeated", ONE_ENEMY_DEFEATED);

        updateStatsLabels();
        updateHighScoreIfNeeded();
    }

    /**
     * Update the high score if the current enemies defeated exceeds it.
     */
    private void updateHighScoreIfNeeded()
    {
        final int enemiesDefeated;

        enemiesDefeated = player.getStat("enemiesDefeated");

        System.out.println("Current high score: " + highScore +
                           ", Enemies defeated: " + enemiesDefeated);
        if (enemiesDefeated > highScore)
        {
            highScore            = enemiesDefeated;
            newHighScoreAchieved = true;
        }
    }

    /**
     * Update the enemy health bar in the UI.
     */
    private void updateEnemyBar()
    {
        final double ratio;

        ratio = enemyHealth / enemyMaxHealth;

        view.getEnemyHealthBar().setProgress(ratio);

        // Set color based on health ratio
        if (ratio > HALF_HEALTH_RATIO)
        {
            view.getEnemyHealthBar().setStyle("-fx-accent: green;");
        }
        else if (ratio > QUARTER_HEALTH_RATIO)
        {
            view.getEnemyHealthBar().setStyle("-fx-accent: orange;");
        }
        else
        {
            view.getEnemyHealthBar().setStyle("-fx-accent: red;");
        }

        final StringBuilder enemyHealthText;
        enemyHealthText = new StringBuilder();
        enemyHealthText.append("HP: ")
                       .append(String.format("%.1f", enemyHealth))
                       .append(" / ")
                       .append(String.format("%.1f", enemyMaxHealth));

        view.getEnemyHealthLabel().setText(enemyHealthText.toString());
    }

    /**
     * Update the stats labels in the UI.
     */
    private void updateStatsLabels()
    {
        final Clicker clicker;
        final int enemiesDefeated;
        final int upgradePoints;

        clicker         = player.getClicker();
        enemiesDefeated = player.getStat("enemiesDefeated");
        upgradePoints   = player.getStat("upgradePoints");

        view.getDamageLabel()
            .setText("Damage per click: " + clicker.getDamagePerClick());

        view.getAutoDamageLabel()
            .setText("Auto damage per second: " + clicker.getAutoDamagePerSecond());

        // Update overall stats - could be extended if more stats are added later
        final StringBuilder statsText;
        statsText = new StringBuilder();
        statsText
            .append("  Enemies: ").append(enemiesDefeated)
            .append("  Upgrade Points: ").append(upgradePoints);
        view.getStatsLabel().setText(statsText.toString());
    }

    /**
     * Apply a generic upgrade to the clicker.
     *
     * @param upgradeName    the name of the upgrade
     * @param upgradeFactory the factory function to create the upgraded clicker
     */
    private void applyUpgrade(final String upgradeName,
                              final ClickerUpgrade upgradeFactory)
    {
        final int currentUpgradePoints;

        currentUpgradePoints = player.getStat("upgradePoints");

        if (currentUpgradePoints < MIN_UPGRADE_COST)
        {
            System.out.println("Not enough upgrade points for " + upgradeName + ".");
            return;
        }

        final Clicker baseClicker;
        final Clicker upgradedClicker;

        baseClicker     = player.getClicker();
        upgradedClicker = upgradeFactory.apply(baseClicker);

        player.setClicker(upgradedClicker);
        player.addToStat("upgradePoints", UPGRADE_COST_SUBTRACTION);

        updateStatsLabels();
    }

    /**
     * Exit handler to save high score if needed.
     * And show alert if a new high score was achieved.
     */
    public void handleExit()
    {
        saveHighScore();
        showHighScoreAlertIfNeeded();
    }

    /**
     * Save the high score to persistent storage.
     */
    private void saveHighScore()
    {
        System.out.println("Calling high score: to save high score: " + highScore);
        HighScoreManager.save(highScore);
    }

    /**
     * Show an alert if a new high score was achieved.
     */
    private void showHighScoreAlertIfNeeded()
    {
        if (!newHighScoreAchieved)
        {
            return;
        }

        final int enemiesDefeated;
        enemiesDefeated = player.getStat("enemiesDefeated");

        final Alert alert;

        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("New High Score!");
        alert.setHeaderText("You achieved a new high score!");
        alert.setContentText("Enemies defeated: " + enemiesDefeated);

        alert.showAndWait();
    }

    /**
     * Apply the level one auto upgrade to the clicker.
     */
    private void applyLevelOneAutoUpgrade()
    {
        applyUpgrade("Level 1 Auto",
                     baseClicker -> new L1AutoClickUpgrade(baseClicker));
    }

    /**
     * Apply the level two auto upgrade to the clicker.
     */
    private void applyLevelTwoAutoUpgrade()
    {
        applyUpgrade("Level 2 Auto",
                     baseClicker -> new L2AutoClickUpgrade(baseClicker));
    }

    /**
     * Apply the level one health upgrade to the clicker.
     */
    private void applyLevelOneHealthUpgrade()
    {
        applyUpgrade("Level 1 Health",
                     baseClicker -> new L1HealthUpgrade(baseClicker));
    }

    /**
     * Apply the level one click upgrade to the clicker.
     */
    private void applyLevelOneClickUpgrade()
    {
        applyUpgrade("Level 1 Click",
                     baseClicker -> new L1ClickUpgrade(baseClicker));
    }

}
