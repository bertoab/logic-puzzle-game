import java.util.List;

public class PuzzleGame {

    private final PuzzleDefinition puzzleDefinition;
    private final Board board;
    //private final PuzzleValidator puzzleValidator;
    private int errorCount;
    private int nextHintIndex;

    //No validator yet.
    public PuzzleGame(PuzzleDefinition puzzleDefinition, Board board) {
        this.puzzleDefinition = puzzleDefinition;
        this.board = board;
        //this.puzzleValidator = puzzleValidator;
        this.errorCount = 0;
        this.nextHintIndex = 0;
    }

    // Returns the next hint's cell location, or null if no hints remain
    public CellLocation getNextHint() {
        List<Hint> hints = puzzleDefinition.getHints();
        if (nextHintIndex >= hints.size()) return null;
        return hints.get(nextHintIndex++).getLocation();
    }

    public int clearErrors() {
        int cleared = this.errorCount;
        this.errorCount = 0;
        return cleared;
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
                && this.puzzleDefinition.equals(other.puzzleDefinition)
                && this.errorCount == other.errorCount;
    }
}