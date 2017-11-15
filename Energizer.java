package Pacman;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.Node;

//A type of pacmanpiece, the energizer is a circle that allows Pacman
//to eat the ghosts
public class Energizer implements PacmanPiece{

  private Circle _circ;

  public Energizer(int x, int y){
    _circ = new Circle(Constants.ENERGIZER_R);
    _circ.setFill(Color.WHITE);
    int posChange = Constants.SQR_SIZE/2;
    _circ.setCenterX((x*Constants.SQR_SIZE) + posChange);
    _circ.setCenterY((y*Constants.SQR_SIZE) + posChange);
  }

  public Node getNode(){
    return _circ;
  }

}
