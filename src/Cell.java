/** Written by: Roberto Baez
 * Cell is the class that represents a single game tile.
 * It holds an immutable `position` within a Grid along
 * with its mutable `state`.
 *
 * @author Roberto Baez
 */
public class Cell {
  private CellState state;
  private Position position;
  /**
   * Constructs a Cell with "blank" state, located at position.
   * @param position the immutable location of this Cell
   */
  public Cell(Position position) {
    this.position = position;
    this.state = CellState.Blank;
  }
  /**
   * Returns the current state of this cell.
   * @return the current CellState object
   */
  public CellState getState() {
    return this.state;
  }
  /**
   * Returns the position of this cell.
   * @return the immutable Position object
   */
  public Position getPosition() {
    return this.position;
  }
  /**
   * Sets the state of this cell.
   * @param newState the new CellState for this Cell
   */
  public void setState(CellState newState) {
    this.state = newState;
  }
  public void setPosition(Position newPosition) {
    this.position = newPosition;
  }
  /**
   * Returns a string representation of this object.
   * @return a String object representing this object
   */
  @Override
  public String toString() {
    return String.format("Cell positioned at (%d, %d) with state %s\n",
                          this.position.row(), this.position.col(), this.state);
  }
  /**
   * Indicates whether some other object is "equal to" this one.
   * @param obj    the reference object with which to compare
   * @return       true if this object is the same as the obj argument;
   *               false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    return true; //TODO
  }
}
