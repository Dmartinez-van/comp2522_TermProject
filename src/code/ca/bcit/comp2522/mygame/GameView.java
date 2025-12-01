package ca.bcit.comp2522.mygame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * GameView class for Auto Clicker Battler.
 *
 * @author David Martinez
 * @version 1.0
 */
public class GameView
{
    private static final int    ENEMY_HEALTH_BAR_WIDTH_PX        = 300;
    private static final double STARTING_HEALTH_RATIO_NORMALIZED = 1.0;
    private static final int    BASIC_BTN_WIDTH_PX               = 150;
    private static final int    ROOT_GAP_PADDING_PX              = 15;
    private static final int    ROOT_MARGIN_PX                   = 20;
    private static final int    HBOX_GAP_PADDING_PX              = 10;
    private static final int    STARTING_ENEMY_DEFEATED_LABEL    = 0;
    private static final int    STARTING_UPGRADE_POINTS_LABEL    = 0;
    private static final double PLAYER_HEALTH_BAR_HEIGHT_PX      = 20.0;
    private static final double PLAYER_HEALTH_BAR_WIDTH_PX       = 150.0;
    private static final int    PLAYER_HEALTH_GAP_PADDING_PX     = 5;
    private static final double ROTATE_CCW_90_DEGREES            = -90.0;
    private static final double SCENE_WIDTH_PX                   = 750;
    private static final double SCENE_HEIGHT_PX                  = 350.0;
    private static final double SHIFT_PLAYER_HP                  = -65.0;

    private Scene scene;

    private ProgressBar enemyHealthBar;
    private ProgressBar playerHealthBar;

    private Button attackButton;
    private Button healButton;
    private Button levelOneAutoUpgradeButton;
    private Button levelTwoAutoUpgradeButton;
    private Button levelOneHealthUpgradeButton;
    private Button levelOneClickUpgradeButton;

    private Label dpsLabel;
    private Label damageLabel;
    private Label autoDamageLabel;
    private Label statsLabel;
    private Label enemyHealthLabel;
    private Label playerHealthLabel;

    /**
     * Creates and returns the main scene for the game.
     *
     * @return the constructed Scene
     */
    public Scene createScene()
    {
        final VBox centerColumn;
        final HBox upgradeRow;
        final HBox healthRow;
        final BorderPane root;

        enemyHealthBar = new ProgressBar();
        enemyHealthBar.setPrefWidth(ENEMY_HEALTH_BAR_WIDTH_PX);
        enemyHealthBar.setProgress(STARTING_HEALTH_RATIO_NORMALIZED);
        enemyHealthBar.setStyle("-fx-accent: green;");

        enemyHealthLabel = new Label("HP: -- / --");
        dpsLabel         = new Label("DPS (last 5s): --");

        healthRow = new HBox(HBOX_GAP_PADDING_PX,
                             new Label("Enemy Health: "),
                             enemyHealthBar,
                             enemyHealthLabel);
        healthRow.setAlignment(Pos.CENTER);

        playerHealthBar = new ProgressBar();
        playerHealthBar.setProgress(STARTING_HEALTH_RATIO_NORMALIZED);
        playerHealthBar.setRotate(ROTATE_CCW_90_DEGREES);
        playerHealthBar.setPrefHeight(PLAYER_HEALTH_BAR_HEIGHT_PX);
        playerHealthBar.setPrefWidth(PLAYER_HEALTH_BAR_WIDTH_PX);
        playerHealthBar.setStyle("-fx-accent: blue;");

        playerHealthLabel = new Label("Player HP: -- / --");
        playerHealthLabel.setTranslateY(SHIFT_PLAYER_HP);

        attackButton = new Button("Attack");
        attackButton.setPrefWidth(BASIC_BTN_WIDTH_PX);
        attackButton.getStyleClass().add("attack-button");

        damageLabel     = new Label("Damage per Click: --");
        autoDamageLabel = new Label("Auto damage per second: --");

        healButton = new Button("Heal");
        healButton.setPrefWidth(BASIC_BTN_WIDTH_PX);
        healButton.getStyleClass().add("heal-button");

        final StringBuilder statsLabelString;
        statsLabelString = new StringBuilder();
        statsLabelString.append("Upgrade Points: ");
        statsLabelString.append(STARTING_UPGRADE_POINTS_LABEL);
        statsLabelString.append(" | Enemies Defeated: ");
        statsLabelString.append(STARTING_ENEMY_DEFEATED_LABEL);
        statsLabel = new Label(statsLabelString.toString());

        levelOneAutoUpgradeButton   = new Button("Buy L1 Auto Click");
        levelTwoAutoUpgradeButton   = new Button("Buy L2 Auto Click");
        levelOneHealthUpgradeButton = new Button("Buy L1 Health");
        levelOneClickUpgradeButton  = new Button("Buy L1 Click");
        levelOneAutoUpgradeButton.getStyleClass().add("basic-button");
        levelTwoAutoUpgradeButton.getStyleClass().add("basic-button");
        levelOneHealthUpgradeButton.getStyleClass().add("basic-button");
        levelOneClickUpgradeButton.getStyleClass().add("basic-button");

        final Label upgradeLabel;
        upgradeLabel = new Label("Upgrades:");
        upgradeRow = new HBox(HBOX_GAP_PADDING_PX,
                                upgradeLabel,
                              levelOneAutoUpgradeButton,
                              levelTwoAutoUpgradeButton,
                              levelOneHealthUpgradeButton,
                              levelOneClickUpgradeButton);
        upgradeRow.setAlignment(Pos.CENTER);

        centerColumn = new VBox(ROOT_GAP_PADDING_PX,
                                healthRow,
                                damageLabel,
                                autoDamageLabel,
                                dpsLabel,
                                statsLabel,
                                attackButton,
                                healButton,
                                upgradeRow);
        centerColumn.setAlignment(Pos.CENTER);

        final VBox playerHealthBoxColumn;

        playerHealthBoxColumn = new VBox(PLAYER_HEALTH_GAP_PADDING_PX,
                                         playerHealthLabel,
                                         playerHealthBar);
        playerHealthBoxColumn.setAlignment(Pos.CENTER);

        root = new BorderPane();
        root.setCenter(centerColumn);
        root.setLeft(playerHealthBoxColumn);
        BorderPane.setAlignment(playerHealthBoxColumn, Pos.CENTER);
        root.setPadding(new Insets(ROOT_MARGIN_PX));

        // Create the scene
        scene = new Scene(root, SCENE_WIDTH_PX, SCENE_HEIGHT_PX);

        final String cssPath;
        cssPath = getClass().getResource("gameStyles.css").toExternalForm();
        scene.getStylesheets().add(cssPath);

        return scene;
    }

    /**
     * Gets the heal button.
     *
     * @return the heal button
     */
    public Button getHealButton()
    {
        return healButton;
    }

    /**
     * Gets the player health bar.
     *
     * @return the player health bar
     */
    public ProgressBar getPlayerHealthBar()
    {
        return playerHealthBar;
    }

    /**
     * Gets the player health label.
     *
     * @return the player health label
     */
    public Label getPlayerHealthLabel()
    {
        return playerHealthLabel;
    }


    /**
     * Gets the enemy health label.
     *
     * @return the enemy health label
     */
    public Label getEnemyHealthLabel()
    {
        return enemyHealthLabel;
    }

    /**
     * Gets the enemy health bar.
     *
     * @return the enemy health bar
     */
    public ProgressBar getEnemyHealthBar()
    {
        return enemyHealthBar;
    }

    /**
     * Gets the attack button.
     *
     * @return the attack button
     */
    public Button getAttackButton()
    {
        return attackButton;
    }

    /**
     * Gets the Level 1 Auto Upgrade button.
     *
     * @return the Level 1 Auto Upgrade button
     */
    public Button getLevelOneAutoUpgradeButton()
    {
        return levelOneAutoUpgradeButton;
    }

    /**
     * Gets the Level 2 Auto Upgrade button.
     *
     * @return the Level 2 Auto Upgrade button
     */
    public Button getLevelTwoAutoUpgradeButton()
    {
        return levelTwoAutoUpgradeButton;
    }

    /**
     * Gets the Level 1 Health Upgrade button.
     *
     * @return the Level 1 Health Upgrade button
     */
    public Button getLevelOneHealthUpgradeButton()
    {
        return levelOneHealthUpgradeButton;
    }

    /**
     * Gets the Level 1 Click Upgrade button.
     *
     * @return the Level 1 Click Upgrade button
     */
    public Button getLevelOneClickUpgradeButton()
    {
        return levelOneClickUpgradeButton;
    }

    /**
     * Gets the damage label.
     *
     * @return the damage label
     */
    public Label getDamageLabel()
    {
        return damageLabel;
    }

    /**
     * Gets the auto damage label.
     *
     * @return the auto damage label
     */
    public Label getAutoDamageLabel()
    {
        return autoDamageLabel;
    }

    /**
     * Gets the stats label.
     *
     * @return the stats label
     */
    public Label getStatsLabel()
    {
        return statsLabel;
    }

    /**
     * Gets the DPS label.
     *
     * @return the DPS label
     */
    public Labeled getDpsLabel()
    {
        return dpsLabel;
    }

/**
     * Gets the main scene for the game.
     *
     * @return the constructed Scene
     */
    public Scene getScene()
    {
        return scene;
    }
}

