package Pacman;

//This enum class simply contains directions- up, down, left, and right
public enum Direction{

  UP, DOWN, LEFT, RIGHT, NONE;

  private Direction opposite;

  static {
    UP.opposite = DOWN;
    DOWN.opposite = UP;
    RIGHT.opposite = LEFT;
    LEFT.opposite = RIGHT;
    NONE.opposite = NONE;
  }

  //This method returns the opposite direction
  public Direction getOpposite() {
    return opposite;
  }
}
