package Pacman;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
  * This is the  main class where the Pacman game will start.
  * The main method of this application calls the App constructor.

  * Design Header: This is the main class App that instantiates a new paneorganizer.
  * Paneorganizer organizes the borderpane that serves as the framework for the program;
  * Instantiating a new pacmangame and setting the pacman pane as the center. It's within
  * paneorganizer that we organize all the instruction labels (the labels for score and lives
  * are organized in the pacmangame class so that it's easier to constantly update them) and
  * the buttons (like for difficulty level and for restarting). Then in pacmangame, we have
  * bulk of the game action, like the methods to move pacman and the ghosts and check for
  * collisions (and their helper methods); however the BFS is factored out into the ghost
  * class and returns a direction based on a target passed in when the BFS is called in the
  * pacmangame class. I decided to put the move methods for pacman and the ghosts in the game
  * class rather than their respective object classes so it'd be easier to let them know when
  * the game is paused or not in motion or in frightened/scatter/chase mode. The ghostpen code
  * is factored out into its own object, so that it can se up its own timeline, as is the code
  * to set up the pacmanboard. In the pacmanboard class, a board is created out of the support
  * code map with intelligent mapsquares that know which pieces are passed into them based on
  * the map. In order to keep track of piece types, I created a piecetype enum. Similarly, I
  * created a direction enum to be utilized in the BFS and the moving of pacman/ghosts. To be
  * able to access all the pacmanpieces as the same type I created an interface called
  * pacmanpiece which the pacman pieces (not pacman though) implement. This allows the mapsquare
  * to manipulate them no matter what specific piece they are.
  *
  * Bells and whistles: difficulty levels, pause/restart game, better turning (stored direction)
  *
  * Bugs: sometimes, when pacman is eaten, the ghosts aren't set up in the exact right positions
  */

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Create top-level object, set up the scene, and show the stage here.
        PaneOrganizer organizer = new PaneOrganizer();
        Scene scene = new Scene(organizer.getRoot());
        stage.setScene(scene);
        stage.setTitle("Pacman!");
        stage.show();
    }


    public static void main(String[] argv) {
        // launch is a method inherited from Application
        launch(argv);
    }
}
