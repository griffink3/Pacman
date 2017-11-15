package Pacman;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.animation.Animation;

/* This class organizes the entire app graphically. In the constructor,
 * it instantiates a new BorderPane (_root) that will contain all other Panes.
 * It then instantiates a new PacmanGame object and sets the _pacmanPane
 * as the center of the borderPane. After, it calls the method
 * createInstructionsPane. The game starts on easy.
 */

public class PaneOrganizer {

	private BorderPane _root;
	private PacmanGame _pacmanGame;
	private int _timeCounter;
	private String _difficultyLevel;
	private Label _instructions1;

	public PaneOrganizer() {
		_root = new BorderPane();
		_pacmanGame = new PacmanGame();
		_root.setCenter(_pacmanGame.getPacmanPane());
		_difficultyLevel = "EASY";
		this.createLabelsPane();
		this.setupTimeline();
	}

	// This method just allows the _root pane to be accessed in App.java
	public BorderPane getRoot() {
		return _root;
	}

	/* createLabelsPane method organizes the instructions, quit button, and restart button.
	 * It instantiates a new Hbox to contain the two buttons, and a new Vbox to contain
	 * this hbox, the instructions, and the hbox buttonsPane that contains the difficulty
	 * buttons. Finally it adds the VBox to the bottom of _root.
	 */

	private void createLabelsPane() {
		VBox labelsPane = new VBox();
		labelsPane.setAlignment(Pos.CENTER);
		labelsPane.setPrefSize(200,100);
		Button quit = new Button("Quit");
		Button restart = new Button("Restart");
		HBox buttons = new HBox();
		quit.setFocusTraversable(false);
		restart.setFocusTraversable(false);
		buttons.getChildren().addAll(quit, restart);
		buttons.setSpacing(15);
		buttons.setAlignment(Pos.CENTER);
		//Here we set the spacing between the buttons and the border
		buttons.setMargin(quit, new Insets(5,5,10,5));
		buttons.setMargin(restart, new Insets(5,5,10,5));
		quit.setOnAction(new QuitHandler());
		restart.setOnAction(new RestartHandler());
		Label instructions = new Label("Use arrows keys to move Pacman, P to pause, and U to unpause.");
		instructions.setFont(new Font("Arial Black", 16));
		_instructions1 = new Label("Choose difficulty at beginning of game. Diffculty: " + _difficultyLevel);
		_instructions1.setFont(new Font("Arial Black", 16));
		labelsPane.setSpacing(5);
		PaneOrganizer.this.createButtonsPane(labelsPane);
		labelsPane.getChildren().addAll(instructions, _instructions1, buttons);
		labelsPane.setMargin(quit, new Insets(5,5,10,5));
		labelsPane.setStyle("-fx-background-color: gray;");
		_root.setBottom(labelsPane);
	}

	//createButtons method organizes the difficulty buttons
	private void createButtonsPane(VBox labelsPane){
		HBox buttonsPane = new HBox();
		Button easy = new Button("EASY");
		easy.setFont(new Font("Arial Black", 12));
		easy.setTextFill(Color.BLUE);
		easy.setFocusTraversable(false);
		Button medium = new Button("MEDIUM");
		medium.setFont(new Font("Arial Black", 12));
		medium.setTextFill(Color.BLUE);
		medium.setFocusTraversable(false);
		Button hard = new Button("HARD");
		hard.setFont(new Font("Arial Black", 12));
		hard.setTextFill(Color.BLUE);
		hard.setFocusTraversable(false);
		buttonsPane.getChildren().addAll(easy, medium, hard);
		buttonsPane.setMargin(easy, new Insets(10,5,5,5));
		buttonsPane.setMargin(medium, new Insets(10,5,5,5));
		buttonsPane.setMargin(hard, new Insets(10,5,5,5));
		buttonsPane.setSpacing(20);
		buttonsPane.setAlignment(Pos.CENTER);
		buttonsPane.setStyle("-fx-background-color: gray;");
		easy.setOnAction(new EasyHandler());
		medium.setOnAction(new MediumHandler());
		hard.setOnAction(new HardHandler());
		labelsPane.getChildren().add(buttonsPane);
	}

	//This QuitHandler adds functionality to the "Quit" button
	private class QuitHandler implements EventHandler<ActionEvent>{

	  @Override
	  public void handle(ActionEvent event){
	    Platform.exit();
	  }

	}

	//Puts the game into easy mode when the easy button is pressed
	private class EasyHandler implements EventHandler<ActionEvent>{

	  @Override
	  public void handle(ActionEvent event){
			if (_timeCounter < 3*Constants.SECONDS){
				//Ghosts are released every 10 seconds and frightened mode lasts 10 seconds
				_pacmanGame.setReleaseTimer(10*Constants.SECONDS);
				_pacmanGame.setFrightenedTimer(10*Constants.SECONDS);
				_difficultyLevel = "EASY";
				_instructions1.setText("Difficulty level: " + _difficultyLevel);
			}
	  }

	}

	//Puts the game into medium mode when the medium button is pressed
	private class MediumHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event){
			if (_timeCounter < 3*Constants.SECONDS){
				//Ghosts are released every 5 seconds and frightened mode lasts 5 seconds
				_pacmanGame.setReleaseTimer(5*Constants.SECONDS);
				_pacmanGame.setFrightenedTimer(5*Constants.SECONDS);
				_difficultyLevel = "MEDIUM";
				_instructions1.setText("Difficulty level: " + _difficultyLevel);
			}
		}

	}

	//Puts the game into hard mode when the medium button is pressed
	private class HardHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event){
			if (_timeCounter < 3*Constants.SECONDS){
				//Ghosts are released after a second and frightened mode lasts 1 second
				_pacmanGame.setReleaseTimer(Constants.SECONDS);
				_pacmanGame.setFrightenedTimer(Constants.SECONDS);
				_difficultyLevel = "HARD";
				_instructions1.setText("Difficulty level: " + _difficultyLevel);
			}
		}

	}

	//Starts a new game if restart is pressed
	private class RestartHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent event){
			_root.getChildren().remove(_pacmanGame.getPacmanPane());
			_pacmanGame = new PacmanGame();
			_root.setCenter(_pacmanGame.getPacmanPane());
			_timeCounter = 0;
		}

	}

	//We have to set up a new timeline to be able to keep track of time separately
	//from the pacmangame (since only able to choose difficulty at beginning)
	private void setupTimeline(){
    KeyFrame kf = new KeyFrame(Duration.millis(Constants.DURATION), new TimeHandler());
    Timeline timeline = new Timeline(kf);
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  private class TimeHandler implements EventHandler<ActionEvent>{

    public void handle(ActionEvent event){
      _timeCounter = _timeCounter + Constants.DURATION;
    }
  }

}
