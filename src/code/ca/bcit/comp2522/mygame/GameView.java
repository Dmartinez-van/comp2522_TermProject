package ca.bcit.comp2522.mygame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
    private static final int    UPGRADE_AREA_GAP_PADDING_PX          = 10;

    private ProgressBar enemyHealthBar;
    private Button      attackButton;
    private Button      levelOneAutoUpgradeButton;
    private Button      levelTwoAutoUpgradeButton;
    private Button      levelOneHealthUpgradeButton;
    private Label       damageLabel;
    private Label       autoDamageLabel;

    /**
     * Creates and returns the main scene for the game.
     *
     * @return the constructed Scene
     */
    public Scene createScene()
    {
        final VBox root;
        final HBox upgradeRow;
        final Scene scene;

        enemyHealthBar = new ProgressBar();
        enemyHealthBar.setPrefWidth(ENEMY_HEALTH_BAR_WIDTH_PX);
        enemyHealthBar.setProgress(ENEMY_HEALTH_BAR_STARTING_NORMALIZED);

        attackButton = new Button("Attack");
        attackButton.setPrefWidth(ATTACK_BTN_WIDTH_PX);

        damageLabel     = new Label("Damage per Click: 1.0 (Magic num)");
        autoDamageLabel = new Label("Auto damage per second: 0.0 (Magic num)");

        levelOneAutoUpgradeButton   = new Button("Buy Level 1 Auto Click Upgrade");
        levelTwoAutoUpgradeButton   = new Button("Buy Level 2 Auto Click Upgrade");
        levelOneHealthUpgradeButton = new Button("Buy Level 1 Health Upgrade");

        upgradeRow = new HBox(UPGRADE_AREA_GAP_PADDING_PX,
                              levelOneAutoUpgradeButton,
                              levelTwoAutoUpgradeButton,
                              levelOneHealthUpgradeButton);
        upgradeRow.setAlignment(Pos.CENTER);

        root = new VBox(ROOT_GAP_PADDING_PX,
                        enemyHealthBar,
                        damageLabel,
                        autoDamageLabel,
                        attackButton,
                        upgradeRow);
        root.setPadding(new Insets(ROOT_MARGIN_PX));
        root.setAlignment(Pos.CENTER);

        scene = new Scene(root);
        return scene;
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
}

