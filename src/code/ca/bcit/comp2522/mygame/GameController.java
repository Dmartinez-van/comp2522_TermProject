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
    private final        GameView view;

    private Clicker        clicker;
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
        this.view = view;

        initializePlayer();
        initializeEnemy();
        initializeHandlers();
        updateStatsLabels();
        updateEnemyBar();
    }

    /**
     * Initialize the player clicker.
     */
    private void initializePlayer()
    {
        clicker = new StarterClicker();
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
        view.getAttackButton().setOnAction(event -> handleAttackClick());

        view.getLevelOneAutoUpgradeButton().setOnAction(event -> applyLevelOneAutoUpgrade());
        view.getLevelTwoAutoUpgradeButton().setOnAction(event -> applyLevelTwoAutoUpgrade());
        view.getLevelOneHealthUpgradeButton().setOnAction(event -> applyLevelOneHealthUpgrade());
    }

    /**
     *
     */
    public void startGameLoop()
    {
        gameLoop = new AnimationTimer()
        {
            @Override
            public void handle(final long now)
            {
                if (lastTimeNanos == 0)
                {
                    lastTimeNanos = now;
                    return;
                }

                final double deltaSeconds;

                deltaSeconds  = (now - lastTimeNanos) / 1_000_000_000.0;
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
        final double damage;
        damage = clicker.getDamagePerClick();

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
        view.getDamageLabel()
            .setText("Damage per click: " + clicker.getDamagePerClick());

        view.getAutoDamageLabel()
            .setText("Auto damage per second: " + clicker.getAutoDamagePerSecond());
    }

    /**
     * Apply the level one auto upgrade to the clicker.
     */
    private void applyLevelOneAutoUpgrade()
    {
        clicker = new L1AutoClickUpgrade(clicker);
        updateStatsLabels();
    }

    /**
     * Apply the level two auto upgrade to the clicker.
     */
    private void applyLevelTwoAutoUpgrade()
    {
        clicker = new L2AutoClickUpgrade(clicker);
        updateStatsLabels();
    }

    /**
     * Apply the level one health upgrade to the clicker.
     */
    private void applyLevelOneHealthUpgrade()
    {
        clicker = new L1HealthUpgrade(clicker);
        updateStatsLabels();
    }
}
