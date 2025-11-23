package ca.bcit.comp2522.numbergame;

import javafx.application.Application;
import javafx.scene.layout.GridPane;
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
    private static final int EMPTY = 0;

    private Button[] cells;
    private Label    statusLabel;
    private Label    nextNumberLabel;

    private NumberGameFX game;

    /**
     * Launches the JavaFX application.
     */
    public static void launchGame()
    {
        Application.launch(NumberGameApp.class);  // proper JavaFX startup
    }

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if an error occurs during startup
     */
    @Override
    public void start(final Stage primaryStage) throws Exception
    {
        final int cellWidthPx;
        final int cellHeightPx;
        final int cellGapPx;
        final int buttonPaddingPx;

        cellWidthPx     = 80;
        cellHeightPx    = 40;
        cellGapPx       = 5;
        buttonPaddingPx = 10;

        statusLabel     = new Label("Click \"Try Again\" to start a new game.");
        nextNumberLabel = new Label("Next number: -");

        GridPane grid = new GridPane();
        grid.setHgap(cellGapPx);
        grid.setVgap(cellGapPx);

        cells = new Button[AbstractNumberGame.CELL_COUNT];

        for (int i = EMPTY; i < AbstractNumberGame.CELL_COUNT; i++)
        {
            final int index = i;
            Button btn = new Button("[]");
            btn.setPrefSize(cellWidthPx, cellHeightPx);

            btn.setOnAction(e -> game.handleCellClick(index));

            cells[i] = btn;
            grid.add(btn, i % AbstractNumberGame.COLS, i / AbstractNumberGame.COLS);
        }

        final Button tryAgainBtn;

        tryAgainBtn = new Button("Try Again");
        tryAgainBtn.setOnAction(e -> game.startNewGame());

        Button quitBtn = new Button("Quit");
        quitBtn.setOnAction(e ->
                            {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Game Over");
                                alert.setHeaderText(null);
                                alert.setContentText(game.getStatsSummary());
                                alert.showAndWait();

                                primaryStage.close();
                            });

        final HBox controls;
        controls = new HBox(buttonPaddingPx, tryAgainBtn, quitBtn);

        final VBox root;
        root = new VBox(buttonPaddingPx, statusLabel, nextNumberLabel, grid, controls);
        root.setPadding(new Insets(buttonPaddingPx));

        game = new NumberGameFX();
        game.setUi(this::updateGrid,
                   this::updateNumber,
                   this::handleGameOver);

        final Scene scene;
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("2D Number Challenge");
        primaryStage.show();

        // optional: auto start first game
        game.startNewGame();
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
    private void updateNumber(int currentNumber)
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

        final ButtonType tryAgain;
        tryAgain = new ButtonType("Try Again", ButtonBar.ButtonData.OK_DONE);
        ButtonType quit = new ButtonType("Quit", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(tryAgain, quit);

        final Optional<ButtonType> result;
        result = alert.showAndWait();

        if (result.isPresent() && result.get() == tryAgain)
        {
            game.startNewGame();
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


    public static void main(String[] args)
    {
        launch(args);
    }
}
