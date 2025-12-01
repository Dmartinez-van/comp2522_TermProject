package ca.bcit.comp2522.mygame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;
import javafx.scene.control.ProgressBar;
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
    private static final int    ENEMY_HEALTH_BAR_WIDTH_PX            = 300;
    private static final double ENEMY_HEALTH_BAR_STARTING_NORMALIZED = 1.0;
    private static final int    ATTACK_BTN_WIDTH_PX                  = 150;
    private static final int    ROOT_GAP_PADDING_PX                  = 15;
    private static final int    ROOT_MARGIN_PX                       = 20;
    private static final int    HBOX_GAP_PADDING_PX                  = 10;
    private static final int    STARTING_ENEMY_DEFEATED_LABEL        = 0;
    private static final int    STARTING_UPGRADE_POINTS_LABEL        = 0;

    private ProgressBar enemyHealthBar;

    private Button attackButton;
    private Button levelOneAutoUpgradeButton;
    private Button levelTwoAutoUpgradeButton;
    private Button levelOneHealthUpgradeButton;
    private Button levelOneClickUpgradeButton;

    private Label dpsLabel;
    private Label damageLabel;
    private Label autoDamageLabel;
    private Label statsLabel;
    private Label enemyHealthLabel;


    /**
     * Creates and returns the main scene for the game.
     *
     * @return the constructed Scene
     */
    public Scene createScene()
    {
        final VBox root;
        final HBox upgradeRow;
        final HBox healthRow;
        final Scene scene;

        enemyHealthBar = new ProgressBar();
        enemyHealthBar.setPrefWidth(ENEMY_HEALTH_BAR_WIDTH_PX);
        enemyHealthBar.setProgress(ENEMY_HEALTH_BAR_STARTING_NORMALIZED);
        enemyHealthBar.setStyle("-fx-accent: green;");

        enemyHealthLabel = new Label("HP: -- / --");
        dpsLabel = new Label("DPS (last 5s): --");

        healthRow = new HBox(HBOX_GAP_PADDING_PX,
                             dpsLabel,
                             new Label("Enemy Health: "),
                             enemyHealthBar,
                             enemyHealthLabel);
        healthRow.setAlignment(Pos.CENTER);

        attackButton = new Button("Attack");
        attackButton.setPrefWidth(ATTACK_BTN_WIDTH_PX);

        damageLabel     = new Label("Damage per Click: 1.0 (Magic num)");
        autoDamageLabel = new Label("Auto damage per second: 0.0 (Magic num)");

        final StringBuilder statsLabelString;
        statsLabelString = new StringBuilder();
        statsLabelString.append("Upgrade Points: ");
        statsLabelString.append(STARTING_UPGRADE_POINTS_LABEL);
        statsLabelString.append(" | Enemies Defeated: ");
        statsLabelString.append(STARTING_ENEMY_DEFEATED_LABEL);
        statsLabel = new Label(statsLabelString.toString());

        levelOneAutoUpgradeButton   = new Button("Buy Level 1 Auto Click Upgrade");
        levelTwoAutoUpgradeButton   = new Button("Buy Level 2 Auto Click Upgrade");
        levelOneHealthUpgradeButton = new Button("Buy Level 1 Health Upgrade");
        levelOneClickUpgradeButton  = new Button("Buy Level 1 Click Upgrade");

        upgradeRow = new HBox(HBOX_GAP_PADDING_PX,
                              levelOneAutoUpgradeButton,
                              levelTwoAutoUpgradeButton,
                              levelOneHealthUpgradeButton,
                              levelOneClickUpgradeButton);
        upgradeRow.setAlignment(Pos.CENTER);

        root = new VBox(ROOT_GAP_PADDING_PX,
                        healthRow,
                        damageLabel,
                        autoDamageLabel,
                        statsLabel,
                        attackButton,
                        upgradeRow);
        root.setPadding(new Insets(ROOT_MARGIN_PX));
        root.setAlignment(Pos.CENTER);

        scene = new Scene(root);
        return scene;
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
}

