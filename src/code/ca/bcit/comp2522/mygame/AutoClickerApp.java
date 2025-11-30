package ca.bcit.comp2522.mygame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Runner class for My Game
 *
 * @author David Martinez
 * @version 1.0
 */
public class AutoClickerApp extends Application
{
    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(final Stage primaryStage)
    {
        final GameView view;
        final Scene scene;
        final GameController controller;

        view       = new GameView();
        scene      = view.createScene();
        controller = new GameController(view);

        primaryStage.setTitle("Auto Clicker Battler");
        primaryStage.setScene(scene);

        // Checks for highscore, saves if new highscore is reached
        primaryStage.setOnCloseRequest(event -> controller.handleExit());

        primaryStage.show();

        controller.startGameLoop();
    }
}
