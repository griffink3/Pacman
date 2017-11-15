package Pacman;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.Node;

/* This class represents the Pacman object. Pacman consists of a yellow
 * circle with radius defined in the constants class.
 */

public class Pacman{

  private Circle _circ;
  private PacmanBoard _pacmanBoard;

  public Pacman(int x, int y){
    _circ = new Circle(Constants.HALF_SQR);
    _circ.setFill(Color.YELLOW);
    _circ.setCenterX((x*Constants.SQR_SIZE) + Constants.HALF_SQR);
    _circ.setCenterY((y*Constants.SQR_SIZE) + Constants.HALF_SQR);
  }

  //This method sets the location of Pacman
  public void setLocation(int x, int y){
    _circ.setCenterX(x + Constants.HALF_SQR);
    _circ.setCenterY(y + Constants.HALF_SQR);
  }


  //This method gets the x location of Pacman
  public int getXLoc(){
    return (int) (_circ.getCenterX() - Constants.HALF_SQR);
  }

  //This method gets the y location of Pacman
  public int getYLoc(){
    return (int) (_circ.getCenterY() - Constants.HALF_SQR);
  }

  //This method allows the node, _circ, to be accessed elsewhere
  public Node getNode(){
    return _circ;
  }

}
