package ca.bcit.comp2522.numbergame;

import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;

import java.util.Optional;

/**
 * JavaFX application for the Number Game.
 *
 * @author David Martinez
 * @version 1.0
 */
public class NumberGameApp extends Application
{
    private static final int EMPTY             = 0;
    private static final int BUTTON_PADDING_PX = 10;

    private Stage primaryStage;

    private Button[] cells;
    private Label    statusLabel;
    private Label    nextNumberLabel;

    private NumberGameFX game;

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(final Stage primaryStage)
    {
        validateStage(primaryStage);

        this.primaryStage = primaryStage;

        createGameLabels();

        final GridPane grid;
        final VBox root;

        grid = createGrid();
        root = createLayout(grid);

        initializeGameLogic();

        final Scene scene;
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("2D Number Challenge");
        primaryStage.show();
        primaryStage.requestFocus();

        game.startNewGame();
    }

    /*
     * Validates the primary stage.
     * @param stage the primary stage to validate
     * @throws IllegalArgumentException if the stage is null
     */
    private void validateStage(final Stage stage)
    {
        if (stage == null)
        {
            throw new IllegalArgumentException("Primary stage cannot be null.");
        }
    }

    /**
     * Creates the game labels for status and next number.
     */
    private void createGameLabels()
    {
        statusLabel     = new Label("Click \"Try Again\" to start a new game.");
        nextNumberLabel = new Label("Next number: -");
    }

    /**
     * Creates the grid of buttons for the game.
     *
     * @return the constructed GridPane
     */
    private GridPane createGrid()
    {
        final int cellWidthPx;
        final int cellHeightPx;
        final int cellGapPx;

        cellWidthPx  = 80;
        cellHeightPx = 40;
        cellGapPx    = 5;

        final GridPane grid;
        grid = new GridPane();
        grid.setHgap(cellGapPx);
        grid.setVgap(cellGapPx);

        cells = new Button[AbstractNumberGame.CELL_COUNT];

        for (int i = 0; i < AbstractNumberGame.CELL_COUNT; i++)
        {
            final Button btn;
            btn = new Button("[]");
            btn.setPrefSize(cellWidthPx, cellHeightPx);

            final int finalI = i;
            btn.setOnAction(e -> game.handleCellClick(finalI));

            cells[i] = btn;
            grid.add(btn, i % AbstractNumberGame.COLS, i / AbstractNumberGame.COLS);
        }

        return grid;
    }

    /**
     * Creates the main layout of the application.
     *
     * @param grid the grid pane containing the game cells
     * @return the constructed VBox layout
     */
    private VBox createLayout(final GridPane grid)
    {
        final Button tryAgainBtn;
        tryAgainBtn = new Button("Try Again");
        tryAgainBtn.setOnAction(e -> game.startNewGame());

        final Button quitBtn;
        quitBtn = createQuitButton();

        final HBox controls;
        controls = new HBox(BUTTON_PADDING_PX, tryAgainBtn, quitBtn);

        final VBox root;
        root = new VBox(BUTTON_PADDING_PX, statusLabel, nextNumberLabel, grid, controls);
        root.setPadding(new Insets(BUTTON_PADDING_PX));

        return root;
    }

    /**
     * Creates the Quit button with its action handler.
     *
     * @return the constructed Quit button
     */
    private Button createQuitButton()
    {
        final Button quitBtn;
        quitBtn = new Button("Quit");
        quitBtn.setOnAction(e ->
                            {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Game Over");
                                alert.setHeaderText(null);
                                alert.setContentText(game.getStatsSummary());
                                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                alert.showAndWait();

                                primaryStage.close();
                            });

        return quitBtn;
    }

    /**
     * Initializes the game logic and sets up UI callbacks.
     */
    private void initializeGameLogic()
    {
        game = new NumberGameFX();
        game.setUi(this::updateGrid,
                   this::updateNumber,
                   this::handleGameOver);
    }

    /**
     * Updates the grid display based on the current grid values.
     *
     * @param gridValues the current values of the grid cells
     */
    private void updateGrid(final int[] gridValues)
    {
        for (int i = 0; i < gridValues.length; i++)
        {
            final int value;
            value = gridValues[i];

            if (value == EMPTY)
            {
                cells[i].setText("[]");
            }
            else
            {
                cells[i].setText(String.valueOf(value));
            }
        }
    }

    /**
     * Updates the next number display.
     *
     * @param currentNumber the current number to be placed
     */
    private void updateNumber(final int currentNumber)
    {
        nextNumberLabel.setText("Next number: " + currentNumber);
    }

    /**
     * Handles the game over event by displaying a message and options to try again or quit.
     *
     * @param won              true if the player won, false otherwise
     * @param impossibleNumber the number that could not be placed
     */
    private void handleGameOver(final boolean won,
                                final int impossibleNumber)
    {
        final String message;
        message = getMessage(won, impossibleNumber);

        final Alert alert;
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        final ButtonType tryAgain;
        tryAgain = new ButtonType("Try Again", ButtonBar.ButtonData.OK_DONE);

        final ButtonType quit;
        quit = new ButtonType("Quit", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(tryAgain, quit);

        final Optional<ButtonType> result;
        result = alert.showAndWait();

        if (result.isPresent())
        {
            if (result.get() == tryAgain)
            {
                game.startNewGame();
            }
            else if (result.get() == quit)
            {
                System.out.println("\nGame stats: " + game.getStatsSummary() + "\n");
                primaryStage.close();
            }
        }
    }

    /**
     * Constructs the game over message.
     *
     * @param won              true if the player won, false otherwise
     * @param impossibleNumber the number that could not be placed
     * @return the constructed game over message
     */
    private String getMessage(final boolean won,
                              final int impossibleNumber)
    {
        final StringBuilder messageBuilder;

        messageBuilder = new StringBuilder();

        if (won)
        {
            messageBuilder.append("You placed all numbers. You win!\n");
            messageBuilder.append(game.getStatsSummary());
        }
        else
        {
            messageBuilder.append("Game Over. Impossible to place the next number: ");
            messageBuilder.append(impossibleNumber);
            messageBuilder.append(".\n");
            messageBuilder.append(game.getStatsSummary());
        }

        return messageBuilder.toString();
    }
}
