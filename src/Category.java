// Gabriel Ferreira
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a puzzle category and the labels that belong to it.
 *
 * @author Gabriel Ferreira
 */
public class Category {
    private final String name;
    private final ArrayList<String> labels;

    /**
     * Creates an empty category.
     */
    public Category() {
        this("", Collections.emptyList());
    }

    /**
     * Creates a category with a name and labels.
     *
     * @param name category name
     * @param labels category labels
     */
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

    @Override
    public String toString() {
        return "Category{name='" + name + "', labels=" + labels + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Category other)) return false;
        return Objects.equals(name, other.name) && Objects.equals(labels, other.labels);
    }

}
