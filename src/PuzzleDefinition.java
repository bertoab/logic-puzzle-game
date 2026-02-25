// Gabriel Ferreira
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Stores parsed puzzle data in memory.
 * Holds unique categories, clues, hints, and answer-key true cells.
 * Derives basic dimensions from category data.
 *
 * @author Gabriel Ferreira
 */
public class PuzzleDefinition {
    private final ArrayList<Category> categories;
    private final ArrayList<Clue> clues;
    private final ArrayList<Hint> hints;
    private final ArrayList<CellLocation> answerKeyTrueCells;

    /**
     * Creates an empty puzzle definition.
     */
    public PuzzleDefinition() {
        this(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Creates a puzzle definition from parsed puzzle data.
     *
     * @param categories puzzle categories
     * @param clues puzzle clues
     * @param hints puzzle hints
     * @param answerKeyTrueCells answer-key true cell locations
     */
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

    @Override
    public String toString() {
        return "PuzzleDefinition{categories=" + categories
                + ", clues=" + clues
                + ", hints=" + hints
                + ", answerKeyTrueCells=" + answerKeyTrueCells + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PuzzleDefinition other)) return false;
        return Objects.equals(categories, other.categories)
                && Objects.equals(clues, other.clues)
                && Objects.equals(hints, other.hints)
                && Objects.equals(answerKeyTrueCells, other.answerKeyTrueCells);
    }
}

