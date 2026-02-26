// Written by: Roberto Baez
import java.util.List;

/**
 * PuzzleValidator is the class that is used to validate
 * a Board's current or potential state, according to a
 * PuzzleDefinition and the rules of the logic puzzle game.
 * It uses the PuzzleDefinition to determine whether a
 * Board is solved. A PuzzleValidator is also used to
 * indicate whether a potential move would invalidate any
 * of a Board's cells.
 *
 * @author Roberto Baez
 */
public class PuzzleValidator {
  private final PuzzleDefinition puzzleDefinition;
  /*
   * NOTE: this class does not have a no-argument constructor
   * because it has a hard dependency on a PuzzleDefinition object.
   */
  /**
   * Constructs a PuzzleValidator for sourcePuzzleDefinition.
   * @param sourcePuzzleDefinition   the PuzzleDefinition object
   *                                 containing the solution to
   *                                 the puzzle
   */
  public PuzzleValidator(PuzzleDefinition sourcePuzzleDefinition) {
    this.puzzleDefinition = sourcePuzzleDefinition;
  }
  /**
   * Indicates whether a change in state for target
   * will be valid, in the context of its grid.
   * A move is invalid when another cell in the same
   * row or column already has its state set to
   * CellState.True.
   * @param grid     the Grid object containing target
   * @param target   the Cell object being validated
   */
  public boolean isMoveValid(Grid grid, Cell target) {
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
  /**
   * Indicates whether board is in a solved state. A solved state
   * means that all answers are correctly set on this board,
   * and that all cells are valid.
   * @param board the board to indicate the solved state of
   * @see #isMoveValid(Grid, Cell)
   * @return true if board is in a solved state; false otherwise
   */
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
  /**
   * Returns a solved board for this validator's puzzleDefinition.
   * @return a Board object that is in a solved state with no blank cells
   */
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
    // set answer cells to CellState.True
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
