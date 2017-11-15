package Pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import cs015.fnl.PacmanSupport.BoardLocation;
import java.util.Arrays;

/* This class creates the pacmanboard that fills the pane, _pacmanPane
 * of the borderPane. It instantiates a new array according to the
 * constants for the width and height, and this array is filles with
 * MapSquares.
 */

public class PacmanBoard{

  private MapSquare[][] _mapSquares;
  private Pane _pacmanPane;
  private Ghost _ghost1;
  private Ghost _ghost2;
  private Ghost _ghost3;
  private Ghost _ghost4;
  private BoardCoordinate _ghostCoordinate;

  public PacmanBoard(Pane pane){
    _mapSquares = new MapSquare[Constants.NUM_Y_SQRS][Constants.NUM_X_SQRS];
    _pacmanPane = pane;
    this.setUpBoard();
  }

  /* This method creates the board out of the support code map. It iterates through
   * the map and depending on the enum in the support code, it passes one of the
   * pacman pieces to a new mapsquare that's placed at that location. However, we
   * instantiate pacman in the pacmangame class rather than here to make interacting
   * with it a lot easier. Also, we factor out the code for creating the ghosts into
   * its own helper method because we're only given one location for the ghosts and so
   * we must manually place the other ghosts from that location. By passing in the
   * piecetype enum that corresponds with the pacman piece that we pass in, we allow
   * the mapsquares to know which pieces are inside of them initially.
   */

  public void setUpBoard(){
    for (int row=0; row<Constants.NUM_Y_SQRS; row++){
      for (int col=0; col<Constants.NUM_X_SQRS; col++){
        //Depending on the enum in the support code, one of the pacman pieces is passed to
        //a new MapSquare that's placed in that location
        if (cs015.fnl.PacmanSupport.SupportMap.getMap()[row][col] == BoardLocation.WALL){
          Wall wall = new Wall(col, row);
          MapSquare mapSquare = new MapSquare(col, row, _pacmanPane, wall, PieceType.WALL);
          //This is the mapsquare's method to tell it it's a wall
          mapSquare.setWall();
          _mapSquares[row][col] = mapSquare;
        } else if (cs015.fnl.PacmanSupport.SupportMap.getMap()[row][col] == BoardLocation.FREE){
          Free free = new Free(col, row);
          MapSquare mapSquare = new MapSquare(col, row, _pacmanPane, free, PieceType.FREE);
          _mapSquares[row][col] = mapSquare;
        } else if (cs015.fnl.PacmanSupport.SupportMap.getMap()[row][col] == BoardLocation.DOT){
          Dot dot = new Dot(col, row);
          MapSquare mapSquare = new MapSquare(col, row, _pacmanPane, dot, PieceType.DOT);
          _mapSquares[row][col] = mapSquare;
        } else if (cs015.fnl.PacmanSupport.SupportMap.getMap()[row][col] == BoardLocation.ENERGIZER){
          Energizer energizer = new Energizer(col, row);
          MapSquare mapSquare = new MapSquare(col, row, _pacmanPane, energizer, PieceType.ENERGIZER);
          _mapSquares[row][col] = mapSquare;
        } else if (cs015.fnl.PacmanSupport.SupportMap.getMap()[row][col] == BoardLocation.GHOST_START_LOCATION){
          Free free = new Free(col, row);
          MapSquare mapSquare = new MapSquare(col, row, _pacmanPane, free, PieceType.FREE);
          _mapSquares[row][col] = mapSquare;
          _ghostCoordinate = new BoardCoordinate(row, col, false);
        } else if (cs015.fnl.PacmanSupport.SupportMap.getMap()[row][col] == BoardLocation.PACMAN_START_LOCATION){
          Free free = new Free(col, row);
          MapSquare mapSquare = new MapSquare(col, row, _pacmanPane, free, PieceType.FREE);
          _mapSquares[row][col] = mapSquare;
        }
      }
    }
    PacmanBoard.this.setUpGhosts();
    //Manually patching up the wall around the ghost pen because of glitch
    Wall newWall = new Wall(8, 11);
    MapSquare newMapSquare = new MapSquare(8, 11, _pacmanPane, newWall, PieceType.WALL);
    newMapSquare.setWall();
    _mapSquares[11][8] = newMapSquare;
    Wall newWall1 = new Wall(10, 12);
    MapSquare newMapSquare1 = new MapSquare(10, 12, _pacmanPane, newWall1, PieceType.WALL);
    newMapSquare1.setWall();
    _mapSquares[12][10] = newMapSquare1;
  }

  /* This is the method that sets up all the ghosts from the single given start location.
   * It sets each of the ghosts to a different color and passes the mapsquare the ghost
   * piecetype enum. 
   */

  public void setUpGhosts(){
    //Set location of first ghost from starting location
    _ghost1 = new Ghost(_ghostCoordinate.getColumn(), _ghostCoordinate.getRow(), _mapSquares);
    _ghost1.setColor(Color.RED);
    MapSquare mapSquare1 = new MapSquare(_ghostCoordinate.getColumn(), _ghostCoordinate.getRow(),
    _pacmanPane, _ghost1, PieceType.GHOST);
    _mapSquares[_ghostCoordinate.getRow()][_ghostCoordinate.getColumn()] = mapSquare1;
    // Once we determine the location of the first ghost, we have to manually set up the other 3 ghosts
    _ghost2 = new Ghost(_ghostCoordinate.getColumn()-1, _ghostCoordinate.getRow(), _mapSquares);
    _ghost2.setColor(Color.GREEN);
    MapSquare mapSquare2 = new MapSquare(_ghostCoordinate.getColumn()-1, _ghostCoordinate.getRow(),
    _pacmanPane, _ghost2, PieceType.GHOST);
    _mapSquares[_ghostCoordinate.getRow()][_ghostCoordinate.getColumn()-1] = mapSquare2;
    //Setting up the third ghost to the right
    _ghost3 = new Ghost(_ghostCoordinate.getColumn()+1, _ghostCoordinate.getRow(), _mapSquares);
    _ghost3.setColor(Color.ORANGE);
    MapSquare mapSquare3 = new MapSquare(_ghostCoordinate.getColumn()+1, _ghostCoordinate.getRow(),
    _pacmanPane, _ghost3, PieceType.GHOST);
    _mapSquares[_ghostCoordinate.getRow()][_ghostCoordinate.getColumn()+1] = mapSquare3;
    //Setting up the fourth ghost two spaces above
    _ghost4 = new Ghost(_ghostCoordinate.getColumn(), _ghostCoordinate.getRow()-2, _mapSquares);
    _ghost4.setColor(Color.LIGHTPINK);
    MapSquare mapSquare4 = new MapSquare(_ghostCoordinate.getColumn(), _ghostCoordinate.getRow()-2,
    _pacmanPane, _ghost4, PieceType.GHOST);
    _mapSquares[_ghostCoordinate.getRow()-2][_ghostCoordinate.getColumn()] = mapSquare4;
  }

  //This method returns _ghost1
  public Ghost getGhost1(){
    return _ghost1;
  }

  //This method returns _ghost2
  public Ghost getGhost2(){
    return _ghost2;
  }

  //This method returns _ghost3
  public Ghost getGhost3(){
    return _ghost3;
  }

  //This method returns _ghost4
  public Ghost getGhost4(){
    return _ghost4;
  }

  //This method allows the array _mapSquares to be accessed elsewhere
  public MapSquare[][] getMapSquares(){
    return _mapSquares;
  }

}
