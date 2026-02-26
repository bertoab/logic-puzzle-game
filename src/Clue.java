// Gabriel Ferreira
import java.util.Objects;

/**
 * Represents a numbered clue shown to the player.
 *
 * @author Gabriel Ferreira
 */
public class Clue {
    private final int number;
    private final String text;

    /**
     * Creates an empty clue.
     */
    public Clue() {
        this(0, "");
    }

    /**
     * Creates a clue with a clue number and text.
     *
     * @param number clue number
     * @param text clue text
     */
    public Clue(int number, String text) {
        this.number = number;
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Clue{number=" + number + ", text='" + text + "'}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Clue other)) return false;
        return number == other.number && Objects.equals(text, other.text);
    }
}
