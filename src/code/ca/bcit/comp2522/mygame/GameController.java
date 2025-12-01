package ca.bcit.comp2522.mygame;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

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
    private static final double ENEMY_MAX_HEALTH                     = 100.0;
    private static final double NO_DAMAGE                            = 0.0;
    private static final double MIN_HEALTH                           = 0.0;
    private static final double MAX_ENEMY_HEALTH_INCREASE            = 30.0;
    private static final double MIN_ENEMY_HEALTH_INCREASE            = 5.0;
    private static final int    ONE_ENEMY_DEFEATED                   = 1;
    private static final int    UPGRADE_COST_SUBTRACTION             = -1;
    private static final int    MIN_UPGRADE_COST                     = 1;
    private static final int    MAX_KILL_AWARD_UPGRADE_POINT         = 3;
    private static final int    MIN_KILL_AWARD_UPGRADE_POINT         = 1;
    private static final double HALF_HEALTH_RATIO                    = 0.5;
    private static final double QUARTER_HEALTH_RATIO                 = 0.25;
    private static final int    DPS_SAMPLES                          = 60;
    private static final double INITIAL_DAMAGE_THIS_TICK             = 0.0;
    private static final double PLAYER_DAMAGE_START_TIMER_SECONDS    = 0.0;
    private static final double PLAYER_DAMAGE_TAKEN_INTERVAL_SECONDS = 1.5;
    private static final double PLAYER_DAMAGE_TAKEN_AMOUNT           = 2.0;
    private static final long   INITIAL_NANOSECONDS                  = 0L;
    private static final double HEAL_AMOUNT                          = 4.5;

    private final GameView     view;
    private final Player       player;
    private final List<Double> recentDpsSamples;

    private AnimationTimer gameLoop;
    private boolean        gameOver;

    private double  timeSinceLastPlayerHit;
    private double  damageThisTick;
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

        player           = new Player();
        recentDpsSamples = new ArrayList<>();

        initializeHighScore();
        initializeEnemy();
        initializeHandlers();

        damageThisTick         = INITIAL_DAMAGE_THIS_TICK;
        timeSinceLastPlayerHit = PLAYER_DAMAGE_START_TIMER_SECONDS;

        updateStatsLabels();
        updateEnemyBar();
        updatePlayerBar();
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

        view.getHealButton().setOnAction(event ->
                                         {
                                             if (gameOver)
                                             {
                                                 return;
                                             }

                                             healPlayer(HEAL_AMOUNT);
                                         });

    }

    /**
     * Start the main game loop using AnimationTimer.
     */
    public void startGameLoop()
    {
        lastTimeNanos = INITIAL_NANOSECONDS;
        gameOver      = false;

        gameLoop = new AnimationTimer()
        {

            private static final double CONVERSION_FACTOR_FROM_NANO_TO_SECONDS = 1_000_000_000.0;
            private static final int    NO_NANOSECONDS                         = 0;

            @Override
            public void handle(final long now)
            {
                if (gameOver)
                {
                    stop();
                    return;
                }

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

        // Periodic player damage handling
        timeSinceLastPlayerHit += deltaSeconds;
        if (timeSinceLastPlayerHit >= PLAYER_DAMAGE_TAKEN_INTERVAL_SECONDS)
        {
            damagePlayer(PLAYER_DAMAGE_TAKEN_AMOUNT);
            timeSinceLastPlayerHit = PLAYER_DAMAGE_START_TIMER_SECONDS;
        }

        // simple linear enemy regen or decay could also go here if you want
        updateEnemyBar();
        updateDpsLabel(deltaSeconds);

        // Reset damage this tick for next tick
        damageThisTick = INITIAL_DAMAGE_THIS_TICK;
    }

    /**
     * Update the DPS label in the UI.
     *
     * @param deltaSeconds time elapsed since last update in seconds
     */
    private void updateDpsLabel(final double deltaSeconds)
    {
        final double dpsThisTick;

        if (deltaSeconds > INITIAL_DAMAGE_THIS_TICK)
        {
            dpsThisTick = damageThisTick / deltaSeconds;
        }
        else
        {
            dpsThisTick = INITIAL_DAMAGE_THIS_TICK;
        }

        recentDpsSamples.add(dpsThisTick);

        if (recentDpsSamples.size() > DPS_SAMPLES)
        {
            recentDpsSamples.removeFirst();
        }

        final double averageDps;

        averageDps = recentDpsSamples
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(INITIAL_DAMAGE_THIS_TICK);

        view.getDpsLabel()
            .setText(String.format("DPS (avg): %.2f", averageDps));
    }

    /**
     * Apply damage to the player and update the UI.
     *
     * @param amount positive damage amount to subtract from player health
     */
    private void damagePlayer(final double amount)
    {
        final Clicker clicker;
        final double currentHealth;
        final double newHealth;
        final double delta;

        clicker = player.getClicker();

        currentHealth = clicker.getCurrentHealth();
        newHealth     = Math.max(MIN_HEALTH, currentHealth - amount);
        delta         = newHealth - currentHealth;

        clicker.changeHealth(delta);

        updatePlayerBar();

        if (newHealth <= MIN_HEALTH)
        {
            gameOver();
        }
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

        damageThisTick += damageAmount;

        if (enemyHealth == MIN_HEALTH)
        {
            resetEnemy();
        }
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
     * Update a health bar in the UI.
     *
     * @param currentHealth the current health value
     * @param maxHealth     the maximum health value
     * @param bar           the ProgressBar to update
     * @param label         the Label to update
     */
    private void updateHealthBar(final double currentHealth,
                                 final double maxHealth,
                                 final ProgressBar bar,
                                 final Label label)
    {
        final double ratio;

        if (maxHealth <= MIN_HEALTH)
        {
            bar.setProgress(MIN_HEALTH);

            final StringBuilder healthText;
            healthText = new StringBuilder();

            healthText.append("HP: ");
            healthText.append(MIN_HEALTH);
            healthText.append(" / ");
            healthText.append(MIN_HEALTH);
            label.setText(healthText.toString());
            return;
        }

        ratio = currentHealth / maxHealth;
        bar.setProgress(ratio);

        // Set color based on health ratio
        if (ratio > HALF_HEALTH_RATIO)
        {
            bar.setStyle("-fx-accent: green;");
        }
        else if (ratio > QUARTER_HEALTH_RATIO)
        {
            bar.setStyle("-fx-accent: orange;");
        }
        else
        {
            bar.setStyle("-fx-accent: red;");
        }

        final StringBuilder healthText;
        healthText = new StringBuilder();
        healthText.append("HP: ");
        healthText.append(String.format("%.1f", currentHealth));
        healthText.append(" / ");
        healthText.append(String.format("%.1f", maxHealth));
        label.setText(healthText.toString());
    }

    /**
     * Update the enemy health bar in the UI.
     */
    private void updateEnemyBar()
    {
        updateHealthBar(enemyHealth,
                        enemyMaxHealth,
                        view.getEnemyHealthBar(),
                        view.getEnemyHealthLabel());
    }

    /**
     * Update the player health bar in the UI.
     */
    private void updatePlayerBar()
    {
        final Clicker clicker;
        clicker = player.getClicker();

        updateHealthBar(clicker.getCurrentHealth(),
                        clicker.getMaxHealth(),
                        view.getPlayerHealthBar(),
                        view.getPlayerHealthLabel());
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

    /**
     * Handle game over scenario when player health reaches zero.
     */
    private void gameOver()
    {
        if (gameOver)
        {
            return;
        }

        gameOver = true;

        if (gameLoop != null)
        {
            gameLoop.stop();
        }


        final StringBuilder gameOverMessage;

        gameOverMessage = new StringBuilder();
        gameOverMessage.append("Your HP reached ")
                       .append(MIN_HEALTH)
                       .append("\n")
                       .append("Enemies defeated: ")
                       .append(player.getStat("enemiesDefeated"))
                       .append("\n");

        Platform.runLater(() ->
                          {
                              final ButtonType restartButton;
                              final ButtonType quitButton;
                              final Alert alert;

                              restartButton = new ButtonType("Restart");
                              quitButton    = new ButtonType("Quit");

                              alert = new Alert(Alert.AlertType.INFORMATION,
                                                "",
                                                restartButton,
                                                quitButton);
                              alert.setTitle("Game Over");
                              alert.setHeaderText("You died!");
                              alert.setContentText(gameOverMessage.toString());

                              final ButtonType result;
                              result = alert.showAndWait().orElse(quitButton);

                              if (result == restartButton)
                              {
                                  restartGame();
                                  return;
                              }

                              // Close the window after the alert (Quit)
                              final Stage stage;
                              stage = (Stage) view.getScene().getWindow();
                              stage.close();
                          });
    }

    /**
     * Restart the game by closing the current window and launching a new instance.
     */
    private void restartGame()
    {
        final Stage stage;
        stage = (Stage) view.getScene().getWindow();

        stage.close();  // close old window

        final AutoClickerApp newApp;
        newApp = new AutoClickerApp();

        try
        {
            newApp.start(new Stage());
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Heals the player by a positive amount, without exceeding max health.
     *
     * @param healAmount the amount to heal
     */
    private void healPlayer(final double healAmount)
    {
        final Clicker clicker;
        clicker = player.getClicker();

        clicker.changeHealth(healAmount);  // let Clicker clamp max HP internally if needed

        updatePlayerBar();             // sync the UI using your generic update method
    }


}
