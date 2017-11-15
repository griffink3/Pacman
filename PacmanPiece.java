package Pacman;

import javafx.scene.Node;

//This class simply creates an interface so that all the pacmanpieces
//except pacman can be accessed under the same type
public interface PacmanPiece {

  //Each pacmanpiece is a node, and thus should be able to be accessed as one
  public Node getNode();

}
