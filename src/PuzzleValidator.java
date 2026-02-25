import java.util.List;

public class PuzzleValidator {
  private final PuzzleDefinition puzzleDefinition;
  // TODO explain lack of no-argument constructor
  public PuzzleValidator(PuzzleDefinition sourcePuzzleDefinition) {
    this.puzzleDefinition = sourcePuzzleDefinition;
  }
  private boolean isMoveValid(Grid grid, Cell target) {
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
      Grid grid = board.getGrid(answer.boardPosition());
      Cell cell = grid.getCell(answer.gridPosition());
      if (cell.getState() != CellState.True && isMoveValid(grid, cell))
        return false;
    }
    return true;
  }
  public Board getSolvedBoard() {
    Board board = new Board();
    // set all Cells to CellState.False
    List<Position> gridPositions = List.of(
            new Position(0, 0),
            new Position(0, 1),
            new Position(1, 0)
    );
    for (Position boardPos : gridPositions) {
      Grid grid = board.getGrid(boardPos);
      for (int col = 0; col < grid.getNumColumns(); col++)
        for (int row = 0; row < grid.getNumRows(); row++)
          grid.getCell(new Position(row, col)).setState(CellState.False);
    }


    for (CellLocation answer : puzzleDefinition.getAnswerKeyTrueCells())
      board.getGrid(answer.boardPosition())
           .getCell(answer.gridPosition())
           .setState(CellState.True);
    return board;
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
