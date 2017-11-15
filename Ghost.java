package Pacman;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import java.util.LinkedList;
import java.util.ArrayList;

/* A type of pacmanpiece, ghosts try to eat pacman. This class contains the
 * BFS algorithm, determining where to move the ghosts. In the constructor,
 * we set the _shortestDist to some arbitrary large number that will immediately
 * change if the method findDirection is called.
 */

public class Ghost implements PacmanPiece{

  private Rectangle _rect;
  private MapSquare[][] _mapSquares;
  private BoardCoordinate _ghostCoordinate;
  private BoardCoordinate _closestCoordinate;
  private double _shortestDist;

  public Ghost(int x, int y, MapSquare[][] mapSquares){
    _rect = new Rectangle();
    _rect.setWidth(Constants.SQR_SIZE);
    _rect.setHeight(Constants.SQR_SIZE);
    _rect.setX(x*Constants.SQR_SIZE);
    _rect.setY(y*Constants.SQR_SIZE);
    _mapSquares = mapSquares;
    _shortestDist = 100000;
  }

  /* This method contains the bulk of the BFS. It's implemented a little differently
   * in that instead of using recursion to search for the valid neighbors, we use two
   * arraylists. This way makes it convenient to search for all the neighbors of the
   * squares we already examined before we move onto the neighbors of those neighbors.
   * We simply call the method searchneighbors while the queue of uncheckedsquares
   * is not empty. Each time we've checked a square, we place it immediately into
   * immediateneighbors and assign it a direction enum in the arraylist of direction
   * enums (this keeps track of whether we've already checked a square). Searchneighbors
   * find the neighbors of the squares based on which ones are in immediate neighbors.
   * We then later remove the immediateneighbors and place them into the queue
   * uncheckedsquares so that their distance from the target can be calculated. And then
   * we transfer the squares in nextNeighbors to immediateneighbors so that their
   * neighbors can be checked. This process is repeated while there are still unchecked
   * squares. Finally it returns the direction enum assigned to the closest square from
   * the array of direction enums.
   */

  public Direction findDirection(BoardCoordinate target, BoardCoordinate lastSquare, Boolean isFrightened){
    //Each time, we want to reset the shortestDist and the closestCoordinate
    _shortestDist = 100000;
    _closestCoordinate = null;
    //Here we instantiate a 2d array of direction enums to keep track of which coordinates
    //we've already visited and which coordinates contain which direction
    Direction[][] visitedSquares = new Direction[Constants.NUM_Y_SQRS][Constants.NUM_X_SQRS];
    //We create a new queue to contain all the boardcoordinates that aren't walls
    LinkedList<BoardCoordinate> uncheckedSquares = new LinkedList<BoardCoordinate>();
    //we also create new arraylists to keep track of the valid neighbors
    ArrayList<BoardCoordinate> immediateNeighbors =  new ArrayList<BoardCoordinate>();
    ArrayList<BoardCoordinate> nextNeighbors =  new ArrayList<BoardCoordinate>();
    //We set the ghosts location to a boardcoordinate and add it to the queue
    Ghost.this.setGhostCoordinate();
    //If the ghost is already at the closest coordinate, we would get find the
    //special direction enum NONE
    visitedSquares[_ghostCoordinate.getRow()][_ghostCoordinate.getColumn()] = Direction.NONE;
    uncheckedSquares.add(_ghostCoordinate);
    //If the square above isn't a wall and isn't checked, we set its direction enum to up and
    //add its coordinate to the arraylist immediateneighbors (to be added to queue later)
    if (!_mapSquares[_ghostCoordinate.getRow()-1][_ghostCoordinate.getColumn()].isWall()
    && visitedSquares[_ghostCoordinate.getRow()-1][_ghostCoordinate.getColumn()] == null){
      visitedSquares[_ghostCoordinate.getRow()-1][_ghostCoordinate.getColumn()] = Direction.UP;
      BoardCoordinate upSquareCoordinate = new BoardCoordinate(_ghostCoordinate.getRow()-1,
      _ghostCoordinate.getColumn(), false);
      //Only adds to the arraylist if this coordinate isn't the lastsquare
      if (lastSquare.getRow() == _ghostCoordinate.getRow()-1 && lastSquare.getColumn() ==
      _ghostCoordinate.getColumn()){
        uncheckedSquares.add(upSquareCoordinate);
      } else {
        immediateNeighbors.add(upSquareCoordinate);
      }
    }
    if (!_mapSquares[_ghostCoordinate.getRow()+1][_ghostCoordinate.getColumn()].isWall()
    && visitedSquares[_ghostCoordinate.getRow()+1][_ghostCoordinate.getColumn()] == null){
      visitedSquares[_ghostCoordinate.getRow()+1][_ghostCoordinate.getColumn()] = Direction.DOWN;
      BoardCoordinate downSquareCoordinate = new BoardCoordinate(_ghostCoordinate.getRow()+1,
      _ghostCoordinate.getColumn(), false);
      if (lastSquare.getRow() == _ghostCoordinate.getRow()+1 && lastSquare.getColumn() ==
      _ghostCoordinate.getColumn()){
        uncheckedSquares.add(downSquareCoordinate);
      } else {
        immediateNeighbors.add(downSquareCoordinate);
      }
    }
    if (_ghostCoordinate.getColumn() > 0){
      if (!_mapSquares[_ghostCoordinate.getRow()][_ghostCoordinate.getColumn()-1].isWall()
      && visitedSquares[_ghostCoordinate.getRow()][_ghostCoordinate.getColumn()-1] == null){
        visitedSquares[_ghostCoordinate.getRow()][_ghostCoordinate.getColumn()-1] = Direction.LEFT;
        BoardCoordinate leftSquareCoordinate = new BoardCoordinate(_ghostCoordinate.getRow(),
        _ghostCoordinate.getColumn()-1, false);
        if (lastSquare.getRow() == _ghostCoordinate.getRow() && lastSquare.getColumn() ==
        _ghostCoordinate.getColumn()-1){
          uncheckedSquares.add(leftSquareCoordinate);
        } else {
          immediateNeighbors.add(leftSquareCoordinate);
        }
      }
    }
    if (_ghostCoordinate.getColumn() < 22){
      if (!_mapSquares[_ghostCoordinate.getRow()][_ghostCoordinate.getColumn()+1].isWall()
      && visitedSquares[_ghostCoordinate.getRow()][_ghostCoordinate.getColumn()+1] == null){
        visitedSquares[_ghostCoordinate.getRow()][_ghostCoordinate.getColumn()+1] = Direction.RIGHT;
        BoardCoordinate rightSquareCoordinate = new BoardCoordinate(_ghostCoordinate.getRow(),
        _ghostCoordinate.getColumn()+1, false);
        if (lastSquare.getRow() == _ghostCoordinate.getRow() && lastSquare.getColumn() ==
        _ghostCoordinate.getColumn()+1){
          uncheckedSquares.add(rightSquareCoordinate);
        } else {
          immediateNeighbors.add(rightSquareCoordinate);
        }
      }
    }
    //If the ghost is already at the end of the board, it should still calculate
    //the closest pathway starting on the opposite end
    if (_ghostCoordinate.getColumn() == 0){
      visitedSquares[11][22] = Direction.LEFT;
      BoardCoordinate leftSquareCoordinate = new BoardCoordinate(11, 22, false);
      if (lastSquare.getRow() == 11 && lastSquare.getColumn() == 22){
        uncheckedSquares.add(leftSquareCoordinate);
      } else {
        immediateNeighbors.add(leftSquareCoordinate);
      }
    }
    if (_ghostCoordinate.getColumn() == 22){
      visitedSquares[11][0] = Direction.RIGHT;
      BoardCoordinate rightSquareCoordinate = new BoardCoordinate(11, 0, false);
      if (lastSquare.getRow() == 11 && lastSquare.getColumn() == 0){
        uncheckedSquares.add(rightSquareCoordinate);
      } else {
        immediateNeighbors.add(rightSquareCoordinate);
      }
    }
    /* While the queue isn't empty, we add the neighbors of the immediate neighbors to the
     * arraylist of next neighbors (after setting them to the right direction enum), and then
     * each time, before we repeat, we remove all the immediate neighbors and then add them
     * to the queue. Then we remove all the next neighbors and add them to the immediate
     * neighbors. Then we remove the first element in the queue and then check to see if its
     * the closest coordinate. Finally, we repeat (while the queue isn't empty).
     */
    while(uncheckedSquares.size() != 0){
      Ghost.this.searchNeighbors(visitedSquares, immediateNeighbors, nextNeighbors);
      while (!immediateNeighbors.isEmpty()){
        uncheckedSquares.add(immediateNeighbors.remove(0));
      }
      while (!nextNeighbors.isEmpty()){
        immediateNeighbors.add(nextNeighbors.remove(0));
      }
      BoardCoordinate currentCoordinate = uncheckedSquares.remove();
      Ghost.this.setShortest(currentCoordinate, target);
    }
    //If the current square is the closest square, then a valid direction not towards
    //the last square and not a wall is returned
    if (visitedSquares[_closestCoordinate.getRow()][_closestCoordinate.getColumn()] ==
    Direction.NONE){
      if (!_mapSquares[_ghostCoordinate.getRow()-1][_ghostCoordinate.getColumn()].isWall()){
        if (_ghostCoordinate.getRow()-1 != lastSquare.getRow() || _ghostCoordinate.getColumn()
        != lastSquare.getColumn()){
          return Direction.UP;
        }
      } else if (!_mapSquares[_ghostCoordinate.getRow()+1][_ghostCoordinate.getColumn()].isWall()){
        if (_ghostCoordinate.getRow()+1 != lastSquare.getRow() || _ghostCoordinate.getColumn()
        != lastSquare.getColumn()){
          return Direction.DOWN;
        }
      } else if (!_mapSquares[_ghostCoordinate.getRow()][_ghostCoordinate.getColumn()-1].isWall()){
        if (_ghostCoordinate.getRow() != lastSquare.getRow() || _ghostCoordinate.getColumn()-1
        != lastSquare.getColumn()){
          return Direction.LEFT;
        }
      } else {
        return Direction.RIGHT;
      }
    }
    //If in frightened mode, returns random direction
    if (isFrightened){
      return Ghost.this.getRandomDirection(lastSquare);
    } else if (lastSquare.getRow() == _closestCoordinate.getRow() && lastSquare.getColumn()
      == _closestCoordinate.getColumn()){
      //If the last square is the closest coordinate, we go in the opposite direction
      return visitedSquares[_closestCoordinate.getRow()][_closestCoordinate.getColumn()].getOpposite();
    } else {
      //Returns, the direction enum of the closest coordinate
      return visitedSquares[_closestCoordinate.getRow()][_closestCoordinate.getColumn()];
    }
  }

  /* This method, a helper method to the BFS, searches for the neighbors of the
   * immediate neighbors and adds them to the arraylist of nextneihbors (to become
   * immeidate neighbors later on). We also assign them the appropriate direction
   * enum in the array of direction enums based on which mapsquare of which they
   * are the neighbor. "Searching" for the neighbor means determining whether the
   * square above, below, to the left, and to the right are either walls or null
   * in the visitedSquares array.
   */

  private void searchNeighbors(Direction[][] visitedSquares, ArrayList<BoardCoordinate>
  immediateNeighbors, ArrayList<BoardCoordinate> nextNeighbors){
    //For each element in the arraylist of immediate neighbors-
    for (int i=0; i<immediateNeighbors.size(); i++){
      //We set the indices to usable variables
      int x = immediateNeighbors.get(i).getColumn();
      int y = immediateNeighbors.get(i).getRow();
      //We get the direction from the 2D array of direction enums
      Direction direction = visitedSquares[y][x];
      //If the one above isn't a wall and is hasn't been checked, set it to the
      //right direction and then add it to the arraylist of next neighbors
      if (!_mapSquares[y-1][x].isWall() && visitedSquares[y-1][x] == null){
        visitedSquares[y-1][x] = direction;
        BoardCoordinate upSquareCoordinate = new BoardCoordinate(y-1, x, false);
        nextNeighbors.add(upSquareCoordinate);
      }
      if (!_mapSquares[y+1][x].isWall() && visitedSquares[y+1][x] == null){
        visitedSquares[y+1][x] = direction;
        BoardCoordinate downSquareCoordinate = new BoardCoordinate(y+1, x, false);
        nextNeighbors.add(downSquareCoordinate);
      }
      if (x > 0){
        if (!_mapSquares[y][x-1].isWall() && visitedSquares[y][x-1] == null){
          visitedSquares[y][x-1] = direction;
          BoardCoordinate leftSquareCoordinate = new BoardCoordinate(y, x-1, false);
          nextNeighbors.add(leftSquareCoordinate);
        }
      }
      if (x < 22){
        if (!_mapSquares[y][x+1].isWall() && visitedSquares[y][x+1] == null){
          visitedSquares[y][x+1] = direction;
          BoardCoordinate rightSquareCoordinate = new BoardCoordinate(y, x+1, false);
          nextNeighbors.add(rightSquareCoordinate);
        }
      }
      // If the square being searched is at the edge of the board, it should continue to
      // calculate the path, wrapping around the board
      if (x == 0){
        if (visitedSquares[11][22] == null){
          visitedSquares[11][22] = direction;
          BoardCoordinate leftSquareCoordinate = new BoardCoordinate(11, 22, false);
          nextNeighbors.add(leftSquareCoordinate);
        }
      }
      if (x == 22){
        if (visitedSquares[11][0] == null){
          visitedSquares[11][0] = direction;
          BoardCoordinate rightSquareCoordinate = new BoardCoordinate(11, 0, false);
          nextNeighbors.add(rightSquareCoordinate);
        }
      }
    }
    return;
  }

  //This method justs sets the ghosts location to a BoardCoordinate object
  private void setGhostCoordinate(){
    int x = Ghost.this.getXLoc();
    int y = Ghost.this.getYLoc();
    //Iterating through the array of MapSquares to find the
    //coordinates that match with those of Pacman
    for (int row=0; row<Constants.NUM_Y_SQRS; row++){
      for (int col=0; col<Constants.NUM_X_SQRS; col++){
        if (_mapSquares[row][col].getXLoc() == x &&
        _mapSquares[row][col].getYLoc() == y){
          //Store in BoardCoordinate
          _ghostCoordinate = new BoardCoordinate(row, col, false);
        }
      }
    }
  }

  //This method simply gets the ghosts coordinate
  public BoardCoordinate getGhostCoordinate(){
    Ghost.this.setGhostCoordinate();
    return _ghostCoordinate;
  }

  //This method takes in a boardcoordinate and checks to see if its the
  //closest to the target, updating the _shortestDist if need be
  private void setShortest(BoardCoordinate square, BoardCoordinate target){
    double dist = (double) Math.sqrt((Math.pow(square.getRow()-target.getRow(), 2))+
    (Math.pow(square.getColumn()-target.getColumn(), 2)));
    if (dist < _shortestDist){
      _shortestDist = dist;
      _closestCoordinate = square;
    }
  }

  /* This method gets a random valid direction for when the ghost is in frightened
   * mode. Based on a random double, it returns either up, down, right or left.
   * However, if the square the corresponds to the direction is the lastsquare or
   * is a wall, then it recursively calls the method again- thus, the method is
   * called until a valid direction is returned. 
   */
  private Direction getRandomDirection(BoardCoordinate lastSquare){
    double rand = Math.random()*100;
    Direction direction = Direction.NONE;
    //if random number is less than 25, returns up (unless there's a wall there
    //or it's the last square)
    if (rand < 25){
      if (lastSquare.getRow() == _ghostCoordinate.getRow()-1 && lastSquare.getColumn() ==
      _ghostCoordinate.getColumn()){
        direction = Ghost.this.getRandomDirection(lastSquare);
      } else if (_mapSquares[_ghostCoordinate.getRow()-1][_ghostCoordinate.getColumn()].isWall()){
        direction = Ghost.this.getRandomDirection(lastSquare);
      } else {
        direction = Direction.UP;
      }
    } else if (rand >= 25 && rand < 50){
      if (lastSquare.getRow() == _ghostCoordinate.getRow()+1 && lastSquare.getColumn() ==
      _ghostCoordinate.getColumn()){
        direction = Ghost.this.getRandomDirection(lastSquare);
      } else if (_mapSquares[_ghostCoordinate.getRow()+1][_ghostCoordinate.getColumn()].isWall()){
        direction = Ghost.this.getRandomDirection(lastSquare);
      } else {
        direction = Direction.DOWN;
      }
    } else if (rand >= 50 && rand < 75){
      if (_ghostCoordinate.getColumn() == 22){
        direction = Direction.RIGHT;
      } else if (lastSquare.getRow() == _ghostCoordinate.getRow() && lastSquare.getColumn() ==
      _ghostCoordinate.getColumn()+1){
        direction = Ghost.this.getRandomDirection(lastSquare);
      } else if (_mapSquares[_ghostCoordinate.getRow()][_ghostCoordinate.getColumn()+1].isWall()){
        direction = Ghost.this.getRandomDirection(lastSquare);
      } else {
        direction = Direction.RIGHT;
      }
    } else {
      if (_ghostCoordinate.getColumn() == 0){
        direction = Direction.LEFT;
      } else if (lastSquare.getRow() == _ghostCoordinate.getRow() && lastSquare.getColumn() ==
      _ghostCoordinate.getColumn()-1){
        direction = Ghost.this.getRandomDirection(lastSquare);
      } else if (_mapSquares[_ghostCoordinate.getRow()][_ghostCoordinate.getColumn()-1].isWall()){
        direction = Ghost.this.getRandomDirection(lastSquare);
      } else {
        direction = Direction.LEFT;
      }
    }
    return direction;
  }

  //Sets the location of the ghost
  public void setLocation(int x, int y){
    _rect.setX(x);
    _rect.setY(y);
  }

  //This method returns the x location of the ghost
  public int getXLoc(){
    return (int) _rect.getX();
  }

  //This method returns the y location of the ghost
  public int getYLoc(){
    return (int) _rect.getY();
  }

  //This method sets the color of the ghost
  public void setColor(Color color){
    _rect.setFill(color);
  }

  //This method returns the rectangle that represents the ghost
  public Node getNode(){
    return _rect;
  }

}
