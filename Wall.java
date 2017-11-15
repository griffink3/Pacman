package Pacman;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.Node;

//A type of pacmanpiece, the wall serves to block movement
public class Wall implements PacmanPiece{

  private Rectangle _rect;

  public Wall(int x, int y){
    _rect = new Rectangle();
    _rect.setWidth(Constants.SQR_SIZE);
    _rect.setHeight(Constants.SQR_SIZE);
    _rect.setFill(Color.BLUE);
    _rect.setX(x*Constants.SQR_SIZE);
    _rect.setY(y*Constants.SQR_SIZE);
  }

  public Node getNode(){
    return _rect;
  }

}
