package Pacman;

import java.util.LinkedList;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/* The main purpose of this class is to keep track of the ghosts in the ghostpen.
 * The ghostpen stores the ghosts that are currently in it in a queue, releasing
 * the first one in the queue every 10 seconds. If a ghost gets eaten, it gets
 * sent back to the ghostpen and added to the queue. Since the ghosts contained
 * in a queue, it's "first in, first out".
 */

public class GhostPen{

  private LinkedList<Ghost> _ghosts;
  private PacmanBoard _pacmanBoard;
  private int _timeCounter;
  private int _startDelay;
  private int _releaseTimer;
  private boolean _isPaused;

  public GhostPen(Ghost ghost1, Ghost ghost2, Ghost ghost3, PacmanBoard pacmanBoard){
    _ghosts = new LinkedList<Ghost>();
    _ghosts.add(ghost1);
    _ghosts.add(ghost2);
    _ghosts.add(ghost3);
    _pacmanBoard = pacmanBoard;
    _timeCounter = 0;
    _startDelay = 0;
    _releaseTimer = 10*Constants.SECONDS;
    _isPaused = false;
    this.setupTimeline();
  }

  //This method gets called when a ghost is eaten- it places the ghost back into the
  //ghost pen and adds it to the queue of ghosts waiting to be released
  public void addToPen(Ghost ghost){
    _ghosts.add(ghost);
    ghost.setLocation(11*Constants.SQR_SIZE, 10*Constants.SQR_SIZE);
    _pacmanBoard.getMapSquares()[10][11].addToSquare(PieceType.GHOST);
  }

  //This method releases a ghost based on the releasetimer by setting its location
  //to outside the pen (also adds the ghost piecetype enum there)
  private void releaseGhost(){
    if (_timeCounter != 0 && _timeCounter%(_releaseTimer) == 0){
      if (!_ghosts.isEmpty()){
        Ghost ghost = _ghosts.remove();
        //Must make sure to remove the Ghost piecetype enum when moving the ghosts
        for (int i=0; i<_pacmanBoard.getMapSquares()[ghost.getGhostCoordinate().getRow()][ghost.getGhostCoordinate().getColumn()].getPieceTypes().size(); i++){
          if (_pacmanBoard.getMapSquares()[ghost.getGhostCoordinate().getRow()][ghost.getGhostCoordinate().getColumn()].getPieceTypes().get(i)
          == PieceType.GHOST){
            _pacmanBoard.getMapSquares()[ghost.getGhostCoordinate().getRow()][ghost.getGhostCoordinate().getColumn()].getPieceTypes().remove(i);
          }
        }
        ghost.setLocation(11*Constants.SQR_SIZE, 8*Constants.SQR_SIZE);
        _pacmanBoard.getMapSquares()[8][11].addToSquare(PieceType.GHOST);
      }
    } else {
      return;
    }
  }

  //This method allows another class to set the isPaused boolean
  public void setIsPaused(boolean isPaused){
    _isPaused = isPaused;
  }

  //This method allows the releasetimer to be set from anywhere
  public void setReleaseTimer(int releaseTimer){
    _releaseTimer = releaseTimer;
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
      _startDelay = _startDelay + Constants.DURATION;
      if (!_isPaused && _startDelay >= 3*Constants.SECONDS){
        _timeCounter = _timeCounter + Constants.DURATION;
      }
      GhostPen.this.releaseGhost();
    }
  }

}
