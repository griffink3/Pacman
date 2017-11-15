package Pacman;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.Node;

//A type of pacmanpiece, free simply allows the mapsquares to remain 
//unchanged if they're of the free type
public class Free implements PacmanPiece{

  private Rectangle _rect;

  public Free(int x, int y){
    _rect = new Rectangle();
    _rect.setWidth(Constants.SQR_SIZE);
    _rect.setHeight(Constants.SQR_SIZE);
    _rect.setFill(Color.BLACK);
    _rect.setX(x*Constants.SQR_SIZE);
    _rect.setY(y*Constants.SQR_SIZE);
  }

  public Node getNode(){
    return _rect;
  }

}
