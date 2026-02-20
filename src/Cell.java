public class Cell {
  // REVIEW NOTE: state starts as null; discuss defaulting to CellState.Blank.
  private CellState state;
  private Position position;
  public Cell(Position position) {
    this.position = position;
  }
  public CellState getState() {
    return this.state;
  }
  public Position getPosition() {
    return this.position;
  }
  public void setState(CellState newState) {
    this.state = newState;
  }
  public void setPosition(Position newPosition) {
    this.position = newPosition;
  }
  @Override
  public String toString() {
    return String.format("Cell positioned at (%d, %d) with state %s\n",
                          this.position.row(), this.position.col(), this.state);
  }
  @Override
  public boolean equals(Object obj) {
    return true; //TODO
  }
}
