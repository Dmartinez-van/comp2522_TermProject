package ca.bcit.comp2522.mygame;

import ca.bcit.comp2522.mygame.model.*;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;

/**
 * Controller class for managing game logic and interactions.
 *
 * @author David Martinez
 * @version 1.0
 */
public class GameController
{
    private static final double   ENEMY_MAX_HEALTH                 = 100.0;
    private static final double   NO_DAMAGE                        = 0.0;
    private static final double   MIN_HEALTH                       = 0.0;
    private static final double   NEW_ENEMY_HEALTH_INCREASE_AMOUNT = 20.0;
    private static final int      ONE_ENEMY_DEFEATED               = 1;
    private static final int      GOLD_10_PIECES                   = 10;
    private static final int      UPGRADE_COST_SUBTRACTION         = -1;
    private static final int      MIN_UPGRADE_COST                 = 1;
    private final        GameView view;
    private final        Player   player;

    private AnimationTimer gameLoop;

    private double enemyHealth;
    private double enemyMaxHealth;

    private long lastTimeNanos;

    /**
     * Constructs a GameController with the specified GameView.
     *
     * @param view the GameView instance
     */
    public GameController(final GameView view)
    {
        validateView(view);
        this.view = view;

        player = new Player();
        player.setStat("upgradePoints", 5); // give some starting upgrade points for testing

        initializeEnemy();
        initializeHandlers();
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
    }

    /**
     *
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
     * @param amount amount of damage to deal
     */
    private void dealDamage(final double amount)
    {
        enemyHealth -= amount;

        if (enemyHealth < MIN_HEALTH)
        {
            enemyHealth = MIN_HEALTH;
        }

        if (enemyHealth == MIN_HEALTH)
        {
            System.out.println("Enemy defeated");
            resetEnemy();
        }
    }

    /**
     * Reset the enemy for the next round.
     */
    private void resetEnemy()
    {
        // simple reset for now, later you can scale difficulty
        enemyMaxHealth = enemyMaxHealth + NEW_ENEMY_HEALTH_INCREASE_AMOUNT;
        enemyHealth    = enemyMaxHealth;

        player.addToStat("enemiesDefeated", ONE_ENEMY_DEFEATED);
        player.addToStat("gold", GOLD_10_PIECES);
    }

    /**
     * Update the enemy health bar in the UI.
     */
    private void updateEnemyBar()
    {
        final double ratio;

        ratio = enemyHealth / enemyMaxHealth;

        Platform.runLater(() -> view.getEnemyHealthBar().setProgress(ratio));
    }

    /**
     * Update the stats labels in the UI.
     */
    private void updateStatsLabels()
    {
        final Clicker clicker;
        final int gold;
        final int enemiesDefeated;
        final int upgradePoints;

        clicker         = player.getClicker();
        gold            = player.getStat("gold");
        enemiesDefeated = player.getStat("enemiesDefeated");
        upgradePoints   = player.getStat("upgradePoints");

        view.getDamageLabel()
            .setText("Damage per click: " + clicker.getDamagePerClick());

        view.getAutoDamageLabel()
            .setText("Auto damage per second: " + clicker.getAutoDamagePerSecond());

        // Update overall stats - could be extended if more stats are added later
        final StringBuilder statsText;
        statsText = new StringBuilder();
        statsText.append("Gold: ").append(gold)
                 .append("  Enemies: ").append(enemiesDefeated)
                 .append("  Upgrade Points: ").append(upgradePoints);
        view.getStatsLabel().setText(statsText.toString());
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
}
