public class Clue {
    // REVIEW NOTE: CS225 "complete class" checklist may also expect toString/equals/hashCode.
    private final int number;
    private final String text;

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
}
