package Pacman;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.Node;

//A type of pacmanpiece, dots are what Pacman eats to raise his score
public class Dot implements PacmanPiece{

  private Circle _circ;

  public Dot(int x, int y){
    _circ = new Circle(Constants.DOT_R);
    _circ.setFill(Color.WHITE);
    int posChange = Constants.SQR_SIZE/2;
    _circ.setCenterX((x*Constants.SQR_SIZE) + posChange);
    _circ.setCenterY((y*Constants.SQR_SIZE) + posChange);
  }

  public Node getNode(){
    return _circ;
  }

}
