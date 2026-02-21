public class Hint {
    // TODO toString()/equals()
    private final String text;
    private final CellLocation location;
    private final CellState state;

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
}
