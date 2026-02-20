import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Category {
    // TODO toString()/equals()
    private final String name;
    private final ArrayList<String> labels;

    public Category(String name, List<String> labels) {
        this.name = name;
        this.labels = new ArrayList<>(labels);
    }

    public String getName() {
        return name;
    }

    public List<String> getLabels() {
        return Collections.unmodifiableList(labels);
    }

    public int size() {
        return labels.size();
    }
}
