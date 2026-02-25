import java.util.ArrayList;
import java.util.List;

public class PuzzleGame {

    private final PuzzleDefinition puzzleDefinition;
    private final Board board;
    private final PuzzleValidator puzzleValidator;
    private int errorCount;
    private int nextHintIndex;

    public PuzzleGame(PuzzleDefinition puzzleDefinition, Board board) {
        this.puzzleDefinition = puzzleDefinition;
        this.board = board;
        this.puzzleValidator = new PuzzleValidator(puzzleDefinition);
        this.errorCount = 0;
        this.nextHintIndex = 0;
    }

    public Hint getNextHint() {
        List<Hint> hints = puzzleDefinition.getHints();
        if (nextHintIndex >= hints.size()) return null;

        Hint currentHint = hints.get(nextHintIndex);

        Cell cell = board.getGrid(currentHint.getLocation().boardPosition())
                .getCell(currentHint.getLocation().gridPosition());

        // If this hint is solved, move the index forward and return the next hint
        if (cell.getState() == currentHint.getState()) {
            nextHintIndex++;
            if (nextHintIndex >= hints.size()) return null;
            return hints.get(nextHintIndex);
        }

        return currentHint;
    }

    public List<CellLocation> clearErrors() {
        List<CellLocation> errors = new ArrayList<CellLocation>();
        Board solvedBoard = puzzleValidator.getSolvedBoard();

        // The grids we have on the board
        List<Position> gridPositions = List.of(
                new Position(0, 0),
                new Position(0, 1),
                new Position(1, 0)
        );

        for (Position boardPos : gridPositions) {
            Grid grid = board.getGrid(boardPos);

            for (int row = 0; row < grid.getNumRows(); row++) {
                for (int col = 0; col < grid.getNumColumns(); col++) {
                    Cell cell = grid.getCell(new Position(row, col));
                    CellState state = cell.getState();
                    CellState solvedState = solvedBoard.getGrid(boardPos)
                                                       .getCell(cell.getPosition())
                                                       .getState();
                    if (state != CellState.Blank
                        && state != solvedState) {
                        cell.setState(CellState.Blank);
                        errors.add(new CellLocation(boardPos, cell.getPosition()));
                    }
                }
            }
        }

        this.errorCount = 0;
        return errors;
    }

    public int getErrorCount() {
        return this.errorCount;
    }

    public PuzzleDefinition getPuzzleDefinition() {
        return puzzleDefinition;
    }

    public Board getBoard() {
        return board;
    }

    public PuzzleValidator getPuzzleValidator() {
        return puzzleValidator;
    }

    @Override
    public String toString() {
        return String.format("PuzzleGame { categories: %d, errors: %d, hintsUsed: %d/%d }",
                puzzleDefinition.getCategoryCount(),
                errorCount,
                nextHintIndex,
                puzzleDefinition.getHints().size()
        );
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PuzzleGame other)) return false;
        return this.board.equals(other.board)
                && this.puzzleDefinition.equals(other.puzzleDefinition);
    }
}