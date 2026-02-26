// Gabriel Ferreira
import java.util.Objects;

/**
 * Represents a hint with text, a target cell, and a suggested state.
 *
 * @author Gabriel Ferreira
 */
public class Hint {
    private final String text;
    private final CellLocation location;
    private final CellState state;

    /**
     * Creates an empty hint.
     */
    public Hint() {
        this("", new CellLocation(new Position(0, 0), new Position(0, 0)), CellState.Blank);
    }

    /**
     * Creates a hint with text, location, and state.
     *
     * @param text hint text
     * @param location target cell location
     * @param state suggested cell state
     */
    public Hint(String text, CellLocation location, CellState state) {
        this.text = text;
        this.location = location;
        this.state = state;
    }

    public String getText() {
        return text;
    }

    public CellLocation getLocation() {
        return location;
    }

    public CellState getState() {
        return state;
    }

    @Override
    public String toString() {
        return "Hint{text='" + text + "', location=" + location + ", state=" + state + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Hint other)) return false;
        return Objects.equals(text, other.text)
                && Objects.equals(location, other.location)
                && state == other.state;
    }
}
