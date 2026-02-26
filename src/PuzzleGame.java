//Cesar Pimentel

import java.util.ArrayList;
import java.util.List;

/**
 * Manages an active puzzle session.
 * Holds the board, puzzle data, and tracks hint and error counts.
 *
 * @author Cesar Pimentel
 */
public class PuzzleGame {

    private final PuzzleDefinition puzzleDefinition;
    private final Board board;
    private final PuzzleValidator puzzleValidator;
    private int errorCount;
    private int nextHintIndex;

    /**
     * Creates a new game with the given puzzle data and blank board.
     *
     * @param puzzleDefinition the loaded puzzle data
     * @param board the board the player will fill in
     */
    public PuzzleGame(PuzzleDefinition puzzleDefinition, Board board) {
        this.puzzleDefinition = puzzleDefinition;
        this.board = board;
        this.puzzleValidator = new PuzzleValidator(puzzleDefinition);
        this.errorCount = 0;
        this.nextHintIndex = 0;
    }

    /**
     * Returns the next unsolved hint in order.
     * Skips the current hint if its cell is already in the correct state.
     *
     * @return the next hint, or null if all hints are exhausted
     */
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

    /**
     * Resets all incorrect cells to Blank and returns their locations.
     *
     * @return list of locations for every cell that was cleared
     */
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

    /**
     * Returns how many hints the player has used.
     *
     * @return hint count
     */
    public int getHintCount() {
        return this.nextHintIndex;
    }

    /**
     * Returns the puzzle definition for this game.
     *
     * @return the puzzle definition
     */
    public PuzzleDefinition getPuzzleDefinition() {
        return puzzleDefinition;
    }

    /**
     * Returns the board the player is filling out.
     *
     * @return the active board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the validator used to check if the puzzle is solved.
     *
     * @return the puzzle validator
     */
    public PuzzleValidator getPuzzleValidator() {
        return puzzleValidator;
    }

    /**
     * Returns a summary of the current game state.
     *
     * @return formatted string with category count, errors, and hints used
     */
    @Override
    public String toString() {
        return String.format("PuzzleGame { categories: %d, errors: %d, hintsUsed: %d/%d }",
                puzzleDefinition.getCategoryCount(),
                errorCount,
                nextHintIndex,
                puzzleDefinition.getHints().size()
        );
    }

    /**
     * Two games are equal if they have the same board and puzzle definition.
     *
     * @param obj the object to compare
     * @return true if board and definition match
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PuzzleGame other)) return false;
        return this.board.equals(other.board)
                && this.puzzleDefinition.equals(other.puzzleDefinition);
    }
}