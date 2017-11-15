package Pacman;

/**
 * This class is an immutable representation of some coordinate within the
 * Pacman board world. As the board consists of square blocks arranged in a
 * 23x23 grid, all elements/blocks in the game must exist within this coordinate
 * space. *However*, when creating targets in Chase mode, the target may be out
 * of the bounds of the board -- that is okay for this scenario only. Therefore,
 * I can explicitly override the bounds-checking functionality by setting
 * isTarget to true in the constructor's third parameter. I should *only*
 * set this parameter to true if, because of the game logic, I specifically
 * want to allow out of bounds squares to be created (essentially, only when
 * creating targets).
 */
public class BoardCoordinate {

    private final int _row;
    private final int _column;
    private static final int _ROW_MAX = 22;
    private static final int _COL_MAX = 22;

    /**
     * The constructor. it takes in a row and a column whose location this
     * instance will model, and a boolean of whether the square is a target
     * square (see header comments for more on this).
     */
    public BoardCoordinate(int row, int column, boolean isTarget) {
        if (!isTarget) {
            this.checkValidity(row, column);
        }
        _row = row;
        _column = column;
    }

    /** Returns the row index that this BoardCoordinate represents. */
    public int getRow() {
        return _row;
    }

    /** Returns the column index that this BoardCoordinate represents. */
    public int getColumn() {
        return _column;
    }

    /**
     * Checks that the row and index passed into this class' constructor are
     * bounded by 0 and the _ROW_MAX for the row and the _COL_MAX for the column,
     * respectively.
     */
    private void checkValidity(int row, int column) {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException(
                      "Board Coordinates must not be negative: " +
                      " Given row = " + row + " col = " + column);
        } else if (row > _ROW_MAX || column > _COL_MAX) {
            throw new IllegalArgumentException(
                      "Board Coordinates must not exceed board dimensions: " +
                      " Given row = " + row + " col = " + column);
        }
    }
}
