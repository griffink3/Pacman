package Pacman;

import javafx.scene.layout.Pane;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import java.util.ArrayList;

/* This class contains the bulk of the game's action and is thus
 * aptly named PacmanGame. In the constructor, we instantiate a new Pane
 * that contains the PacmanBoard, instantiate a new pacman, and set the
 * initial direction for pacman to up. Then we call the method to set up
 * the timeline and add a KeyHandler to the Pacman Pane. We also set the
 * score to 0 and lives to 3 initially (and create labels to keep track
 * of them visually). We instantiate variables to keep track of time and
 * time in frightened mode, as well as variables to keep track of whether
 * the game is paused or is in motion. Finanlly we created an arraylist to
 * hold the ghosts (and call a method to add them), and we instantiate a new
 * ghostpen object.
 */

public class PacmanGame{

  private Pane _pacmanPane;
  private PacmanBoard _pacmanBoard;
  private Pacman _pacman;
  private Direction _direction;
  private Direction _nextDirection;
  private MapSquare _currentSquare;
  private MapSquare _nextSquare;
  private BoardCoordinate _pacmanCoordinate;
  private int _score;
  private int _lives;
  private int _timeCounter;
  private Text _scoreKeeper;
  private Text _livesKeeper;
  private Text _ready;
  private Text _go;
  private Ghost _ghost1;
  private Ghost _ghost2;
  private Ghost _ghost3;
  private Ghost _ghost4;
  private ArrayList<Ghost> _ghosts;
  private BoardCoordinate _ghost1Last;
  private BoardCoordinate _ghost2Last;
  private BoardCoordinate _ghost3Last;
  private BoardCoordinate _ghost4Last;
  private boolean _isInMotion;
  private boolean _isFrightened;
  private int _frightenedCounter;
  private GhostPen _ghostPen;
  private boolean _isPaused;
  private int _frightenedTimer;
  private int _releaseTimer;

  public PacmanGame(){
    _pacmanPane = new Pane();
    _pacmanPane.setFocusTraversable(true);
    _pacmanBoard = new PacmanBoard(_pacmanPane);
    _pacman = new Pacman(Constants.PACMAN_STARTX, Constants.PACMAN_STARTY);
    _pacmanPane.getChildren().add(_pacman.getNode());
    //Set Pacman's initial direction to up so he doesn't move
    _direction = Direction.UP;
    _score = 0;
    _lives = 3;
    //This variable keeps track of time
    _timeCounter = 0;
    //This variable keeps track of how much time the ghosts have been in frightened mode
    _frightenedCounter = 0;
    //This variable keeps track of how much time the ghosts should be in frightened mode
    _frightenedTimer = 10*Constants.SECONDS;
    _ghosts = new ArrayList<Ghost>();
    //We need two booleans to keep track of motion because there are times when the game
    //is not paused but is also not in motion (i.e. at the beginning of the game)
    _isInMotion = true;
    _isPaused = false;
    //we also have a boolean to keep track of whether the ghosts are in frightened mode
    _isFrightened = false;
    //This variable is not in used in this class but exists here so that it can be set
    //from the paneorganizer to affect when the ghosts are released in the ghostpen
    _releaseTimer = 10*Constants.SECONDS;
    this.setGhosts();
    _ghostPen = new GhostPen(_ghost1, _ghost2, _ghost3, _pacmanBoard);
    this.setupTimeline();
    this.createLabels();
    _pacmanPane.addEventHandler(KeyEvent.KEY_PRESSED, new KeyHandler());
  }

  //This method allows the Pacman pane to be accessed elsewhere
  public Pane getPacmanPane(){
    return _pacmanPane;
  }

  //This method organizes the labels that keep track of score and lives
  //and adds them to the scene graph
  private void createLabels(){
    _scoreKeeper = new Text("Score: " + _score);
    _scoreKeeper.setFont(new Font("Arial Black", 22));
    _scoreKeeper.setFill(Color.WHITE);
    _scoreKeeper.setX(Constants.SQR_SIZE);
    _scoreKeeper.setY(0.75*Constants.SQR_SIZE);
    _livesKeeper = new Text("Lives: " + _lives);
    _livesKeeper.setFont(new Font("Arial Black", 22));
    _livesKeeper.setFill(Color.WHITE);
    _livesKeeper.setX(19*Constants.SQR_SIZE);
    _livesKeeper.setY(0.75*Constants.SQR_SIZE);
    _pacmanPane.getChildren().addAll(_scoreKeeper,_livesKeeper);
  }

  //This method sets the ghosts to the ones from the pacmanboard and adds them
  //to an arraylist
  private void setGhosts(){
    _ghost1 = _pacmanBoard.getGhost1();
    _ghost2 = _pacmanBoard.getGhost2();
    _ghost3 = _pacmanBoard.getGhost3();
    _ghost4 = _pacmanBoard.getGhost4();
    _ghost1Last =  _ghost1.getGhostCoordinate();
    _ghost2Last =  _ghost2.getGhostCoordinate();
    _ghost3Last =  _ghost3.getGhostCoordinate();
    _ghost4Last =  _ghost4.getGhostCoordinate();
    _ghosts.add(_ghost1);
    _ghosts.add(_ghost2);
    _ghosts.add(_ghost3);
    _ghosts.add(_ghost4);
  }

  //This method allows the ghostpen's release timer to be set
  //from another class that only has the pacmangame object
  public void setReleaseTimer(int releaseTimer){
    _releaseTimer = releaseTimer;
    _ghostPen.setReleaseTimer(_releaseTimer);
  }

  //This method allows another class to set the frightenedTimer
  public void setFrightenedTimer(int frightenedTimer){
    _frightenedTimer = frightenedTimer;
  }

  /* This method stores the current coordinates of Pacman in a
   * BoardCoordinate object and sets the currentSquare to the
   * MapSquare currently containing Pacman. It's a helper method
   * for the movepacman method.
   */

  private void setPacmanCoordinate(){
    int x = _pacman.getXLoc();
    int y = _pacman.getYLoc();
    //Iterating through the array of MapSquares to find the
    //coordinates that match with those of Pacman
    for (int row=0; row<Constants.NUM_Y_SQRS; row++){
      for (int col=0; col<Constants.NUM_X_SQRS; col++){
        if (_pacmanBoard.getMapSquares()[row][col].getXLoc() == x &&
        _pacmanBoard.getMapSquares()[row][col].getYLoc() == y){
          //Set currentSquare to the one that matches
          _currentSquare = _pacmanBoard.getMapSquares()[row][col];
          //Store in BoardCoordinate
          _pacmanCoordinate = new BoardCoordinate(row, col, false);
        }
      }
    }
  }

  //This method checks if the next square would be a wall
  //given a direction and returns a boolean
  private boolean isNextSquareWall(Direction direction){
    //Storing the current coordinates in usable variables
    int row = _pacmanCoordinate.getRow();
    int col = _pacmanCoordinate.getColumn();
    switch (direction){
      case UP:
        //If given up, checking to see if the one above be a wall
        if (_pacmanBoard.getMapSquares()[row-1][col].isWall()){
          return true;
        } else {
          return false;
        }
      case DOWN:
        //If given down, checking to see if the one below be a wall
        if (_pacmanBoard.getMapSquares()[row+1][col].isWall()){
          return true;
        } else {
          return false;
        }
      case RIGHT:
        //If given right checking to see if the one to the right would
        //be a wall
        if (_pacmanBoard.getMapSquares()[row][col+1].isWall()){
          return true;
        } else {
          return false;
        }
      case LEFT:
        //If given right checking to see if the one to the left would
        //be a wall
        if (_pacmanBoard.getMapSquares()[row][col-1].isWall()){
          return true;
        } else {
          return false;
        }
      default:
        return false;
    }
  }

  /* This method moves Pacman based on a given direction (which changed based
   * on key input from the player). Pacman is only moved if the next square
   * isn't a wall. If pacman is at the edge and is about to move off the board,
   * he's moved to the other side, performing wrapping. Before Pacman is moved,
   * we check to see if the stored direction is valid. If the stored direction
   * is in fact valid, we move it in that direction.
   */

  public void movePacman(){
    //If the boolean isInMotionmotion is not true (i.e. at the beginning) then ghosts
    //won't move
    if (!_isInMotion){
      return;
    }
    //Storing the current location and coordinates in usable variables
    int x = _pacman.getXLoc();
    int y = _pacman.getYLoc();
    int row = _pacmanCoordinate.getRow();
    int col = _pacmanCoordinate.getColumn();
    //Only checks the stored direction if the direction isn't null
    if (_nextDirection != null){
      switch (_nextDirection){
        //In each case, checks to see if the nextdirection is a valid move
        case UP:
          _nextSquare = _pacmanBoard.getMapSquares()[row-1][col];
          if (_nextSquare.isWall()){
            break;
          } else {
            _direction = _nextDirection;
            break;
          }
        case DOWN:
          _nextSquare = _pacmanBoard.getMapSquares()[row+1][col];
          if (_nextSquare.isWall()){
            break;
          } else {
            _direction = _nextDirection;
            break;
          }
        case RIGHT:
          if (_pacman.getXLoc() == 22*Constants.SQR_SIZE){
            break;
          }
          _nextSquare = _pacmanBoard.getMapSquares()[row][col+1];
          if (_nextSquare.isWall()){
            break;
          } else {
            _direction = _nextDirection;
            break;
          }
        case LEFT:
          if (_pacman.getXLoc() == 0){
            break;
          }
          _nextSquare = _pacmanBoard.getMapSquares()[row][col-1];
          if (_nextSquare.isWall()){
            break;
          } else {
            _direction = _nextDirection;
            break;
          }
      }
    }
    switch (_direction){
      //In each case, checks to see if the next one would be a wall before
      //setting the new location in the direction given
      case UP:
        _nextSquare = _pacmanBoard.getMapSquares()[row-1][col];
        if (_nextSquare.isWall()){
          return;
        } else {
          _pacman.setLocation(x, (y-Constants.SQR_SIZE));
        }
        break;
      case DOWN:
        _nextSquare = _pacmanBoard.getMapSquares()[row+1][col];
        if (_nextSquare.isWall()){
          return;
        } else {
          _pacman.setLocation(x, (y+Constants.SQR_SIZE));
        }
        break;
      case RIGHT:
        if (_pacman.getXLoc() == 22*Constants.SQR_SIZE){
          _pacman.setLocation(0, _pacman.getYLoc());
          break;
        }
        _nextSquare = _pacmanBoard.getMapSquares()[row][col+1];
        if (_nextSquare.isWall()){
          return;
        } else {
          _pacman.setLocation((x+Constants.SQR_SIZE), y);
        }
        break;
      case LEFT:
        if (_pacman.getXLoc() == 0){
          _pacman.setLocation(22*Constants.SQR_SIZE, _pacman.getYLoc());
          break;
        }
        _nextSquare = _pacmanBoard.getMapSquares()[row][col-1];
        if (_nextSquare.isWall()){
          return;
        } else {
          _pacman.setLocation((x-Constants.SQR_SIZE), y);
        }
        break;
    }
  }

  /* This method moves the ghost given a target, returns the last coordinate. It
   * uses the BFS in the ghost class to return a direction (based on a target
   * passed in) and then it stores the lastsquare based on the ghost's location
   * before its moved. We also remove the ghost piecetype enum from the previous
   * square's arraylist of piecetype enums and add it to the new square.
   */

  private BoardCoordinate moveGhost(Ghost ghost, BoardCoordinate target, BoardCoordinate
  lastSquare){
    //Storing the current location and coordinates in usable variables
    int x = ghost.getXLoc();
    int y = ghost.getYLoc();
    int row = ghost.getGhostCoordinate().getRow();
    int col = ghost.getGhostCoordinate().getColumn();
    //Gets a direction from the BFS in the ghost class; the BFS needs to know about the
    //target, the lastsquare, and whether or not the ghosts are frightened in order to
    //return a direction
    Direction direction = ghost.findDirection(target, lastSquare, _isFrightened);
    //Updating the _lastSquare
    lastSquare = new BoardCoordinate(row, col, false);
    //If the boolean isInMotionmotion is not true (i.e. at the beginning) then ghosts
    //won't move
    if (!_isInMotion){
      return lastSquare;
    }
    //Removing the ghost PieceType enum from the mapsquare
    for(int i=0; i<_pacmanBoard.getMapSquares()[row][col].getPieceTypes().size(); i++){
      if (_pacmanBoard.getMapSquares()[row][col].getPieceTypes().get(i) == PieceType.GHOST){
        _pacmanBoard.getMapSquares()[row][col].getPieceTypes().remove(i);
      }
    }
    switch (direction){
      //In each case, moves the ghost to the new location and adds the ghost
      //PieceType enum to the new mapsquare
      case UP:
        MapSquare nextSquareUp = _pacmanBoard.getMapSquares()[row-1][col];
        if (nextSquareUp.isWall()){
          return lastSquare;
        } else {
          ghost.setLocation(x, (y-Constants.SQR_SIZE));
          _pacmanBoard.getMapSquares()[row-1][col].addToSquare(PieceType.GHOST);
          return lastSquare;
        }
      case DOWN:
        MapSquare nextSquareDown = _pacmanBoard.getMapSquares()[row+1][col];
        if (nextSquareDown.isWall()){
          return lastSquare;
        } else {
          ghost.setLocation(x, (y+Constants.SQR_SIZE));
          _pacmanBoard.getMapSquares()[row+1][col].addToSquare(PieceType.GHOST);
          return lastSquare;
        }
      case RIGHT:
        //If the ghost is at the right edge, we move it to the other end of the board
        //for the purpose of wrapping
        if (ghost.getXLoc() == 22*Constants.SQR_SIZE){
          ghost.setLocation(0, y);
          _pacmanBoard.getMapSquares()[row][0].addToSquare(PieceType.GHOST);
          return lastSquare;
        }
        MapSquare nextSquareRight = _pacmanBoard.getMapSquares()[row][col+1];
        if (nextSquareRight.isWall()){
          return lastSquare;
        } else {
          ghost.setLocation((x+Constants.SQR_SIZE), y);
          _pacmanBoard.getMapSquares()[row][col+1].addToSquare(PieceType.GHOST);
          return lastSquare;
        }
      case LEFT:
        //If the ghost is at the left edge, we move it to the other end of the board
        //for the purpose of wrapping
        if (ghost.getXLoc() == 0*Constants.SQR_SIZE){
          ghost.setLocation(22*Constants.SQR_SIZE, y);
          _pacmanBoard.getMapSquares()[row][22].addToSquare(PieceType.GHOST);
          return lastSquare;
        }
        MapSquare nextSquareLeft = _pacmanBoard.getMapSquares()[row][col-1];
        if (nextSquareLeft.isWall()){
          return lastSquare;
        } else {
          ghost.setLocation((x-Constants.SQR_SIZE), y);
          _pacmanBoard.getMapSquares()[row][col-1].addToSquare(PieceType.GHOST);
          return lastSquare;
        }
    }
    return lastSquare;
  }

  /* This method moves all the ghosts based on what mode they're currently in. In the
   * normal mode, their target is pacman (or near pacman), while in scatter mode,
   * their targets are the corners of the board. In frightened mode, they move in
   * a random direction- we put pacmancoordinate as the target but the ghost's BFS
   * will still return a random direction becuase it knows whether the ghosts are in
   * frightened mode.
   */

  private void moveAllGhosts(){
    //If Pacman has eatened an energizer, the ghosts are thrown into frightened mode
    //in which they run away from Pacman
    if (_isFrightened){
      _ghost1Last = PacmanGame.this.moveGhost(_ghost1, _pacmanCoordinate, _ghost1Last);
      _ghost2Last = PacmanGame.this.moveGhost(_ghost2, _pacmanCoordinate, _ghost2Last);
      _ghost3Last = PacmanGame.this.moveGhost(_ghost3, _pacmanCoordinate, _ghost3Last);
      _ghost4Last = PacmanGame.this.moveGhost(_ghost4, _pacmanCoordinate, _ghost4Last);
      return;
    }
    //Time goes in 27 seconds loops, resetting after each 27 seconds
    if (_timeCounter > 33*Constants.SECONDS){
      //Must reset to above 6 seconds so that labels to start game don't reappear
      _timeCounter = 6*Constants.SECONDS+1;
    }
    //This is chase mode
    if (_timeCounter < 26*Constants.SECONDS){
      //First ghost targets two spaces to the right of pacman
      BoardCoordinate target1 = new BoardCoordinate(_pacmanCoordinate.getRow(),
      _pacmanCoordinate.getColumn()+2, true);
      //Second ghost targets 4 spaces above pacman
      BoardCoordinate target2 = new BoardCoordinate(_pacmanCoordinate.getRow()-4,
      _pacmanCoordinate.getColumn(), true);
      //Third ghost targets 3 spaces to the left of pacman and 1 space down
      BoardCoordinate target3 = new BoardCoordinate(_pacmanCoordinate.getRow()+1,
      _pacmanCoordinate.getColumn()-3, true);
      _ghost1Last = PacmanGame.this.moveGhost(_ghost1, target1, _ghost1Last);
      _ghost2Last = PacmanGame.this.moveGhost(_ghost2, target2, _ghost2Last);
      _ghost3Last = PacmanGame.this.moveGhost(_ghost3, target3, _ghost3Last);
      //Fourth ghost targets pacman's location
      _ghost4Last = PacmanGame.this.moveGhost(_ghost4, _pacmanCoordinate, _ghost4Last);
    } else {
      BoardCoordinate corner1 = new BoardCoordinate(0, 0, true);
      BoardCoordinate corner2 = new BoardCoordinate(0, 22, true);
      BoardCoordinate corner3 = new BoardCoordinate(22, 0, true);
      BoardCoordinate corner4 = new BoardCoordinate(22, 22, true);
      _ghost1Last = PacmanGame.this.moveGhost(_ghost1, corner1, _ghost1Last);
      _ghost2Last = PacmanGame.this.moveGhost(_ghost2, corner2, _ghost2Last);
      _ghost3Last = PacmanGame.this.moveGhost(_ghost3, corner3, _ghost3Last);
      _ghost4Last = PacmanGame.this.moveGhost(_ghost4, corner4, _ghost4Last);
    }
  }

  /* This method checks collisions and specifices what occurs when pacman
   * collides with dots, energizers, and ghosts. It does this by checking
   * whether the object's piecetype enum is in pacman's current square's
   * arraylist of piecetype enums.
   */

  private void checkCollisions(){
    for(int i=0; i<_currentSquare.getPieceTypes().size(); i++){
      //If pacman collides with a dot, he eats it and score increases by 10
      if (_currentSquare.getPieceTypes().get(i) == PieceType.DOT){
        _pacmanPane.getChildren().remove(_currentSquare.getInitialPiece().getNode());
        _currentSquare.getPieceTypes().remove(i);
        _score = _score + 10;
        _scoreKeeper.setText("Score: " + _score);
      } else if (_currentSquare.getPieceTypes().get(i) == PieceType.ENERGIZER){
      //If pacman collides with an energizer, he eats it and turns on frightened
      //mode, and scores increases by 100
        _pacmanPane.getChildren().remove(_currentSquare.getInitialPiece().getNode());
        _currentSquare.getPieceTypes().remove(i);
        _score = _score + 100;
        _scoreKeeper.setText("Score: " + _score);
        _isFrightened = true;
        _ghost1.setColor(Color.LIGHTBLUE);
        _ghost2.setColor(Color.LIGHTBLUE);
        _ghost3.setColor(Color.LIGHTBLUE);
        _ghost4.setColor(Color.LIGHTBLUE);
      }
    }
    /* If pacman collides with a ghost in frightened mode, he eats the ghost and gains
     * points and the ghost is sent back to the ghost pen. But if not in frightened
     * mode, pacman returns to the starting location and loses a life.
     */
    for(int i=0; i<_currentSquare.getPieceTypes().size(); i++){
      if (_currentSquare.getPieceTypes().get(i) == PieceType.GHOST){
        if (_isFrightened){
          for (int n=0; n<_ghosts.size(); n++){
            if (_ghosts.get(n).getGhostCoordinate().getColumn() == _pacmanCoordinate.getColumn()
            && _ghosts.get(n).getGhostCoordinate().getRow() == _pacmanCoordinate.getRow()){
              for (int k=0; k<_pacmanBoard.getMapSquares()[_ghosts.get(n).getGhostCoordinate().getRow()][_ghosts.get(n).getGhostCoordinate().getColumn()].getPieceTypes().size(); k++){
                _pacmanBoard.getMapSquares()[_ghosts.get(n).getGhostCoordinate().getRow()]
                [_ghosts.get(n).getGhostCoordinate().getColumn()].getPieceTypes().remove(k);
              }
              _ghostPen.addToPen(_ghosts.get(n));
            }
          }
        } else {
          _pacman.setLocation(11*Constants.SQR_SIZE, 17*Constants.SQR_SIZE);
          _timeCounter = -Constants.DURATION;
          _lives--;
          _livesKeeper.setText("Lives: " + _lives);
          //Removes all the ghosts from the scene graph as well as removing their
          //piecetype enums from the squares they were in
          for (int n=0; n<_ghosts.size(); n++){
            _pacmanPane.getChildren().remove(_ghosts.get(n).getNode());
            for (int k=0; k<_pacmanBoard.getMapSquares()[_ghosts.get(n).getGhostCoordinate().getRow()][_ghosts.get(n).getGhostCoordinate().getColumn()].getPieceTypes().size(); k++){
              if (_pacmanBoard.getMapSquares()[_ghosts.get(n).getGhostCoordinate().getRow()][_ghosts.get(n).getGhostCoordinate().getColumn()].getPieceTypes().get(k) == PieceType.GHOST){
                _pacmanBoard.getMapSquares()[_ghosts.get(n).getGhostCoordinate().getRow()][_ghosts.get(n).getGhostCoordinate().getColumn()].getPieceTypes().remove(k);
              }
            }
          }
          //Adding all the ghosts again
          _pacmanBoard.setUpGhosts();
          //Setting all the ghosts again
          PacmanGame.this.setGhosts();
          _ghostPen = new GhostPen(_ghost1, _ghost2, _ghost3, _pacmanBoard);
        }
      }
    }
  }

  //This method ends the ghosts' frightened mode after 10 seconds
  private void endFrightened(){
    if (_frightenedCounter >= _frightenedTimer){
      _isFrightened = false;
      _frightenedCounter = 0;
      _ghost1.setColor(Color.RED);
      _ghost2.setColor(Color.GREEN);
      _ghost3.setColor(Color.ORANGE);
      _ghost4.setColor(Color.LIGHTPINK);
    }
    //increases the frightened counter variable to keep track of passage
    //of time
    if (_isFrightened && _frightenedCounter < _frightenedTimer){
      _frightenedCounter = _frightenedCounter + Constants.DURATION;
    }
  }


  //This method adds the labels that instruct the player to start the game and
  //then removes them after
  private void startGame(){
    if (_timeCounter == 0){
      _pacmanPane.getChildren().remove(_go);
      _ready = new Text("Ready?");
      _ready.setFont(new Font("Arial Black", 20));
      _ready.setFill(Color.WHITE);
      _ready.setX(10.5*Constants.SQR_SIZE);
      _ready.setY(14.75*Constants.SQR_SIZE);
      _pacmanPane.getChildren().add(_ready);
    } else if (_timeCounter == 3*Constants.SECONDS){
      _pacmanPane.getChildren().remove(_ready);
      _go = new Text("Go!");
      _go.setFont(new Font("Arial Black", 20));
      _go.setFill(Color.WHITE);
      _go.setX(11*Constants.SQR_SIZE);
      _go.setY(14.75*Constants.SQR_SIZE);
      _pacmanPane.getChildren().add(_go);
    } else if (_timeCounter == 6*Constants.SECONDS){
      _pacmanPane.getChildren().remove(_go);
    }
  }

  //This method checks to see if the game is won, i.e. if there are still any
  //dots left; then it returns a boolean
  private boolean isGameWon(){
    for (int row=0; row<Constants.NUM_Y_SQRS; row++){
      for (int col=0; col<Constants.NUM_X_SQRS; col++){
        for (int i=0; i<_pacmanBoard.getMapSquares()[row][col].getPieceTypes().size(); i++){
          if (_pacmanBoard.getMapSquares()[row][col].getPieceTypes().get(i) == PieceType.DOT){
            return false;
          }
        }
      }
    }
    return true;
  }

  //This method creates a new label if the game is won
  private void checkGameWon(){
    if (PacmanGame.this.isGameWon()){
      Label gameWon = new Label("YOU WIN!");
      gameWon.setFont(new Font("Arial Black", 40));
      gameWon.setTextFill(Color.RED);
      gameWon.setAlignment(Pos.CENTER);
      _pacmanPane.getChildren().addAll(gameWon);
      _isInMotion = false;
      _isPaused = true;
      _ghostPen.setIsPaused(_isPaused);
    }
  }

  /* This method specifies when the game is in motion. It delays the game at the
   * beginning by setting the boolean _ininmotion to false so that the methods
   * that move pacman and the ghosts don't work. Otherwise, it sets the boolean
   * to false unless the game isn't paused.
   */

  private void delayGame(){
    //Game is delayed at the beginning for 3 seconds
    if (_timeCounter < 3*Constants.SECONDS){
      _isInMotion = false;
    } else if (_timeCounter >= 3*Constants.SECONDS){
      if (!_isPaused){
        _isInMotion = true;
      }
    }
  }

  //This method ends the game once the player runs out of lives and notifies the player
  private void countLives(){
    if (_lives == 0){
      _isInMotion = false;
      Text gameOver = new Text("GAME OVER!");
      gameOver.setFont(new Font("Arial Black", 50));
      gameOver.setFill(Color.RED);
      gameOver.setX(5.5*Constants.SQR_SIZE);
      gameOver.setY(12*Constants.SQR_SIZE);
      _pacmanPane.getChildren().add(gameOver);
    }
  }

  //Pauses the game
  private void pauseGame(){
    _isInMotion = false;
    _isPaused = true;
    _ghostPen.setIsPaused(_isPaused);
  }

  //Unpauses the game
  private void unpauseGame(){
    _isInMotion = true;
    _isPaused = false;
    _ghostPen.setIsPaused(_isPaused);
  }

  //This method sets up the timeline and allows it to run indefinitely
  private void setupTimeline(){
    KeyFrame kf = new KeyFrame(Duration.millis(Constants.DURATION), new TimeHandler());
    Timeline timeline = new Timeline(kf);
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  //This method specifies what occurs at the end of each keyframe
  private class TimeHandler implements EventHandler<ActionEvent>{

    public void handle(ActionEvent event){
      //We call the method setPacmanCoordinate() twice because we need
      //the coordinates to be set at the beginning in order to move pacman
      PacmanGame.this.startGame();
      PacmanGame.this.delayGame();
      PacmanGame.this.countLives();
      PacmanGame.this.setPacmanCoordinate();
      PacmanGame.this.movePacman();
      PacmanGame.this.setPacmanCoordinate();
      PacmanGame.this.checkCollisions();
      PacmanGame.this.endFrightened();
      PacmanGame.this.moveAllGhosts();
      PacmanGame.this.checkCollisions();
      PacmanGame.this.setReleaseTimer(_releaseTimer);
      PacmanGame.this.checkGameWon();
      PacmanGame.this.delayGame();
      if (!_isPaused){
        _timeCounter = _timeCounter + Constants.DURATION;
      }
    }
  }

  //This method specifies what occurs when the user presses the left, right
  //down, and up keys
  private class KeyHandler implements EventHandler<KeyEvent>{

    @Override
    public void handle(KeyEvent keyEvent){
        if (keyEvent.getCode() == KeyCode.LEFT){
          //First calls the isNextSquareWall method to check if Pacman
          //can move in this direction
          if (PacmanGame.this.isNextSquareWall(Direction.LEFT)){
            //If Pacman can't, the direction is set as the next direction to be
            //moved when he can
            _nextDirection = Direction.LEFT;
          } else {
            //If Pacman can move in this new direction, the direction enum
            //passed to the move method is now changed to this new direction
            _direction = Direction.LEFT;
            //Reset the stored next direction
            _nextDirection = null;
          }
        }
        if (keyEvent.getCode() == KeyCode.RIGHT){
          if (PacmanGame.this.isNextSquareWall(Direction.RIGHT)){
            _nextDirection = Direction.RIGHT;
          } else {
            _direction = Direction.RIGHT;
            _nextDirection = null;
          }
        }
        if (keyEvent.getCode() == KeyCode.DOWN){
          if (PacmanGame.this.isNextSquareWall(Direction.DOWN)){
            _nextDirection = Direction.DOWN;
          } else {
            _direction = Direction.DOWN;
            _nextDirection = null;
          }
        }
        if (keyEvent.getCode() == KeyCode.UP){
          if (PacmanGame.this.isNextSquareWall(Direction.UP)){
            _nextDirection = Direction.UP;
          } else {
            _direction = Direction.UP;
            _nextDirection = null;
          }
        }
        if (keyEvent.getCode() == KeyCode.P){
          //If the p button is pressed, the game is paused
          PacmanGame.this.pauseGame();
        }
        if (keyEvent.getCode() == KeyCode.U){
          //If the p button is pressed, the game is unpaused
          PacmanGame.this.unpauseGame();
        }
    }
  }

}
