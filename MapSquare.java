package Pacman;

import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import java.util.ArrayList;

/* This class creates the object mapsquare which is essentially a smart
 * square. Filling up the pacmanboard, these squares are initially black,
 * but they're passed a pacmanpiece. They're also passed the corresponding
 * piecetype enum that they add to an arraylist, _pieceTypes, filled with
 * piecetype enums that allows them to keep track of what pieces they
 * contain. The MapSquares are also passed a pane so they can add the
 * black background to the scene graph and passed integers to set the
 * location of the square.
 */

public class MapSquare {

  private Rectangle _rect;
  private PacmanPiece _initialPiece;
  private boolean _wallTracker;
  private PieceType _pieceType;
  private ArrayList<PieceType> _pieceTypes;

  public MapSquare(int x, int y, Pane pane, PacmanPiece piece, PieceType pieceType){
    _rect = new Rectangle();
    _rect.setWidth(Constants.SQR_SIZE);
    _rect.setHeight(Constants.SQR_SIZE);
    _rect.setFill(Color.BLACK);
    _rect.setX(x*Constants.SQR_SIZE);
    _rect.setY(y*Constants.SQR_SIZE);
    _pieceType = pieceType;
    _pieceTypes = new ArrayList<PieceType>();
    _pieceTypes.add(pieceType);
    _initialPiece = piece;
    //This int tracks if the mapSquare is a wall or not
    _wallTracker = false;
    pane.getChildren().addAll(_rect, piece.getNode());
  }

  //When this class is instantiated and passed a wall, this method is
  //called to set the mapsquare as a wall, by setting the walltracker to true
  public void setWall(){
    _wallTracker = true;
  }

  //This method tells if this mapsquare is a wall or not
  public boolean isWall(){
    return _wallTracker;
  }

  //This method adds new piecetypes to the arraylist
  public void addToSquare(PieceType newPieceType){
    _pieceTypes.add(newPieceType);
  }

  //This method allows the arraylist keeping track of the pieceTypes
  //to be accessed elsewhere
  public ArrayList<PieceType> getPieceTypes(){
    return _pieceTypes;
  }

  //This method gets the x location of the mapsquare
  public int getXLoc(){
    return (int) _rect.getX();
  }

  //This method gets the y location of the mapsquare
  public int getYLoc(){
    return (int) _rect.getY();
  }

  //This method allows the original pacman piece to be accessed
  public PacmanPiece getInitialPiece(){
    return _initialPiece;
  }

}
