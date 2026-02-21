import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores parsed puzzle data in memory.
 * Holds unique categories, clues, hints, and answer-key true cells.
 * Derives basic dimensions from category data.
 */
public class PuzzleDefinition {
    // TODO toString()/equals()
    private final ArrayList<Category> categories;
    private final ArrayList<Clue> clues;
    private final ArrayList<Hint> hints;
    private final ArrayList<CellLocation> answerKeyTrueCells;

    public PuzzleDefinition(List<Category> categories,
                            List<Clue> clues,
                            List<Hint> hints,
                            List<CellLocation> answerKeyTrueCells) {
        this.categories = new ArrayList<>(categories);
        this.clues = new ArrayList<>(clues);
        this.hints = new ArrayList<>(hints);
        this.answerKeyTrueCells = new ArrayList<>(answerKeyTrueCells);

    }

    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public List<Clue> getClues() {
        return Collections.unmodifiableList(clues);
    }

    public List<Hint> getHints() {
        return Collections.unmodifiableList(hints);
    }

    public List<CellLocation> getAnswerKeyTrueCells() {
        return Collections.unmodifiableList(answerKeyTrueCells);
    }

    public int getCategoryCount() {
    return categories.size();
    }

    public int getLabelCount() {
        return categories.isEmpty() ? 0 : categories.get(0).size();
    }

}

