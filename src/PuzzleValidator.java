import java.util.List;

public class PuzzleValidator {
  private final PuzzleDefinition puzzleDefinition;
  // TODO explain lack of no-argument constructor
  public PuzzleValidator(PuzzleDefinition sourcePuzzleDefinition) {
    this.puzzleDefinition = sourcePuzzleDefinition;
  }
  public boolean isMoveValid(Grid grid, Cell target, CellState newState) {
    // REVIEW NOTE: assumes `newState` == CellState.True
    // check if there is a Cell with CellState.True in row
    for (int i = 0; i < grid.getNumColumns(); i++)
      if (grid.getCell(new Position(target.getPosition().row(), i))
              .getState() == CellState.True)
        return false;
    // check if there is a Cell with CellState.True in column
    for (int j = 0; j < grid.getNumRows(); j++)
      if (grid.getCell(new Position(j, target.getPosition().col()))
              .getState() == CellState.True)
        return false;
    return true;
  }
  public boolean isSolved(Board board) {
    List<CellLocation> answers = this.puzzleDefinition.getAnswerKeyTrueCells();
    for (CellLocation answer : answers) {
      // assumes the Grid specificed by `answer.boardPosition()` exists
      // ensure the Cell within `board` specified by `answer` is "true"
      if (board.getGrid(answer.boardPosition())
          .getCell(answer.gridPosition())
          .getState() != CellState.True)
        return false;
    }
    return true;
  }
  /* TODO
  @Override
  public String toString() {

  }
  @Override
  public boolean equals(Object obj) {

  }
  */
}
