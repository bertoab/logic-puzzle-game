public class Clue {
    // TODO toString()/equals()
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
