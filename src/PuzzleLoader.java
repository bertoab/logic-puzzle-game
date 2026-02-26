// Gabriel Ferreira
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Loads puzzle definition data from a text file.
 * Keeps parsing responsibilities separate from UI and game logic.
 * Uses simple fail-fast RuntimeExceptions for malformed data.
 *
 * @author Gabriel Ferreira
 */
public class PuzzleLoader {
    // Optional stored path for callers that want to keep loader configuration with the object.
    private final String defaultPath;

    /**
     * Creates a loader with no default file path.
     */
    public PuzzleLoader() {
        this("");
    }

    /**
     * Creates a loader with an optional default file path.
     *
     * @param defaultPath default file path for this loader
     */
    public PuzzleLoader(String defaultPath) {
        this.defaultPath = defaultPath == null ? "" : defaultPath;
    }

    /**
     * Loads, parses, normalizes, and validates a puzzle definition file.
     *
     * @param path path to the puzzle file to read
     * @return parsed puzzle definition
     */
    public PuzzleDefinition loadDefinition(String path) {
        ArrayList<Category> categories = new ArrayList<>();
        ArrayList<Clue> clues = new ArrayList<>();
        ArrayList<Hint> hints = new ArrayList<>();
        ArrayList<CellLocation> answerKey = new ArrayList<>();

        List<String> lines = readAllLines(path);

        int i = 0;
        while (i < lines.size()) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) { i++; continue; }

            // Dispatch parsing based on the current section header.
            if (line.equalsIgnoreCase("Horizontal Label:") || line.equalsIgnoreCase("Vertical Label:")) {
                i++;
                i = parseCategoryBlocks(lines, i, categories);
                continue;
            }

            if (line.equalsIgnoreCase("Clues:")) {
                i++;
                i = parseClues(lines, i, clues);
                continue;
            }

            if (line.equalsIgnoreCase("Hints:")) {
                i++;
                i = parseHints(lines, i, hints);
                continue;
            }

            if (line.equalsIgnoreCase("Answer Key:")) {
                i++;
                i = parseAnswerKey(lines, i, answerKey);
                continue;
            }

            throw new RuntimeException("Unexpected line outside a section at line " + (i + 1) + ": " + line);
        }

        // Merge duplicate category headers (case-insensitive) and ensure duplicates agree on labels.
        categories = normalizeCategories(categories);

        // Validate parsed data before building the immutable-ish definition object.
        validatePuzzleDimensions(categories);
        int n = categories.size();
        int l = categories.get(0).size();
        validateLocations(hints, answerKey, n, l);

        return new PuzzleDefinition(categories, clues, hints, answerKey);
    }

    /**
     * Reads every line from a puzzle file.
     *
     * @param path path to the file
     * @return file contents as a list of lines
     */
    private List<String> readAllLines(String path) {
        ArrayList<String> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String s;
            while ((s = br.readLine()) != null) {
                out.add(s);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read puzzle file: " + path);
        }
        return out;
    }

    /**
     * Parses repeated category blocks under a labels section.
     *
     * Expected format:
     * 
     * Category Name
     * - Label 1
     * - Label 2
     * 
     *
     * @param lines all puzzle file lines
     * @param start index to begin parsing
     * @param categories destination list for parsed categories
     * @return index of the next unread line (often a new section header)
     */
    private int parseCategoryBlocks(List<String> lines, int start, List<Category> categories) {
        int i = start;

        while (i < lines.size()) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) { i++; continue; }

            // stop if we hit next major section
            if (isSectionHeader(line)) {
                return i;
            }

            String categoryName = line;
            int categoryLineNumber = i + 1;
            i++;

            ArrayList<String> labels = new ArrayList<>();
            while (i < lines.size()) {
                String l = lines.get(i).trim();
                if (l.isEmpty()) { i++; break; }
                if (isSectionHeader(l)) {
                    break;
                }
                if (l.startsWith("-")) {
                    // Strip the leading dash and keep only the label text.
                    String labelText = l.substring(1).trim();
                    if (labelText.isEmpty()) {
                        throw new RuntimeException("Empty category label for '" + categoryName
                                + "' at line " + (i + 1));
                    }
                    labels.add(labelText);
                    i++;
                    continue;
                }
                // Non-dash lines here are treated as a new category header (blank line optional).
                break;
            }

            if (!labels.isEmpty()) {
                categories.add(new Category(categoryName, labels));
            } else {
                throw new RuntimeException("Category '" + categoryName
                        + "' has no labels (line " + categoryLineNumber + ")");
            }
        }
        return i;
    }

    /**
     * Parses clue lines in the form {@code number.text}.
     *
     * @param lines all puzzle file lines
     * @param start index to begin parsing
     * @param clues destination list for parsed clues
     * @return index of the next unread line
     */
    private int parseClues(List<String> lines, int start, List<Clue> clues) {
        int i = start;
        while (i < lines.size()) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) { i++; continue; }
            if (isSectionHeader(line)) return i;

            // 1. Marc served 125 riders.
            int dot = line.indexOf('.');
            if (dot <= 0) {
                throw new RuntimeException("Malformed clue at line " + (i + 1)
                        + ": expected '<number>. <text>' but got '" + line + "'");
            }
            try {
                int num = Integer.parseInt(line.substring(0, dot).trim());
                String text = line.substring(dot + 1).trim();
                if (text.isEmpty()) {
                    throw new RuntimeException("Malformed clue at line " + (i + 1) + ": clue text is empty");
                }
                clues.add(new Clue(num, text));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Malformed clue number at line " + (i + 1) + ": " + line, e);
            }
            i++;
        }
        return i;
    }

    /**
     * Parses hint blocks made of three lines: text, location, and state.
     *
     * @param lines all puzzle file lines
     * @param start index to begin parsing
     * @param hints destination list for parsed hints
     * @return index of the next unread line
     */
    private int parseHints(List<String> lines, int start, List<Hint> hints) {
        int i = start;
        while (i < lines.size()) {
            String textLine = lines.get(i).trim();
            if (textLine.isEmpty()) { i++; continue; }
            if (isSectionHeader(textLine)) return i;

            if (i + 2 >= lines.size()) {
                throw new RuntimeException("Incomplete hint block starting at line " + (i + 1));
            }

            // Hint format is three consecutive lines.
            String locationLine = safeGetTrim(lines, i + 1);
            String stateLine = safeGetTrim(lines, i + 2);
            if (locationLine.isEmpty()) {
                throw new RuntimeException("Missing hint location at line " + (i + 2));
            }
            if (stateLine.isEmpty()) {
                throw new RuntimeException("Missing hint state at line " + (i + 3));
            }

            CellLocation loc;
            CellState state;
            try {
                loc = parseCellLocation(locationLine);
            } catch (RuntimeException e) {
                throw new RuntimeException("Bad hint location at line " + (i + 2) + ": " + locationLine, e);
            }
            try {
                state = parseCellState(stateLine);
            } catch (RuntimeException e) {
                throw new RuntimeException("Bad hint state at line " + (i + 3) + ": " + stateLine, e);
            }

            hints.add(new Hint(textLine, loc, state));
            // Advance past the text/location/state lines we just consumed.
            i += 3;
        }
        return i;
    }

    /**
     * Parses answer-key entries, one cell location per line.
     *
     * @param lines all puzzle file lines
     * @param start index to begin parsing
     * @param answerKey destination list for true-cell locations
     * @return index of the next unread line
     */
    private int parseAnswerKey(List<String> lines, int start, List<CellLocation> answerKey) {
        int i = start;
        while (i < lines.size()) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) { i++; continue; }
            if (isSectionHeader(line)) return i;

            try {
                answerKey.add(parseCellLocation(line));
            } catch (RuntimeException e) {
                throw new RuntimeException("Bad answer key location at line " + (i + 1) + ": " + line, e);
            }
            i++;
        }
        return i;
    }

    /**
     * Returns a trimmed line or an empty string when the index is out of range.
     *
     * @param lines source lines
     * @param index line index to read
     * @return trimmed line text or {@code ""}
     */
    private String safeGetTrim(List<String> lines, int index) {
        if (index >= lines.size()) return "";
        return lines.get(index).trim();
    }

    /**
     * Checks whether a line starts a known top-level section.
     *
     * @param line line text to inspect
     * @return {@code true} if the line is a section header
     */
    private boolean isSectionHeader(String line) {
        String s = line.trim();
        return s.equalsIgnoreCase("Horizontal Label:")
                || s.equalsIgnoreCase("Vertical Label:")
                || s.equalsIgnoreCase("Clues:")
                || s.equalsIgnoreCase("Hints:")
                || s.equalsIgnoreCase("Answer Key:");
    }

    /**
     * Parses a cell location in the form {@code boardRow,boardCol - gridRow,gridCol}.
     *
     * @param s raw cell-location text
     * @return parsed cell location
     */
    private CellLocation parseCellLocation(String s) {
        // Expected format: "0,0 - 1,2"
        if (s == null || s.trim().isEmpty()) {
            throw new RuntimeException("Cell location is blank");
        }
        String[] parts = s.split("-");
        if (parts.length != 2) {
            throw new RuntimeException("Bad cell location: " + s);
        }

        Position boardPos = parsePosition(parts[0].trim());
        Position gridPos = parsePosition(parts[1].trim());

        return new CellLocation(boardPos, gridPos);
    }

    /**
     * Parses a row/column pair in the form {@code row,col}.
     *
     * @param s raw position text
     * @return parsed position
     */
    private Position parsePosition(String s) {
        // Accepts "1, 2" or "1,2".
        String[] parts = s.split(",");
        if (parts.length != 2) {
            throw new RuntimeException("Bad position: " + s);
        }
        int r;
        int c;
        try {
            r = Integer.parseInt(parts[0].trim());
            c = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Position contains non-integer values: " + s, e);
        }
        return new Position(r, c);
    }

    /**
     * Parses a hint state string.
     *
     * @param s raw state text
     * @return parsed state
     */
    private CellState parseCellState(String s) {
        if (s.equalsIgnoreCase("true")) return CellState.True;
        if (s.equalsIgnoreCase("false")) return CellState.False;
        throw new RuntimeException("Bad cell state: " + s + " (expected True or False)");
    }

    /**
     * Removes duplicate categories by name (case-insensitive) while preserving first-seen order.
     *
     * @param raw parsed categories before normalization
     * @return normalized category list
     */
    private ArrayList<Category> normalizeCategories(ArrayList<Category> raw) {
        java.util.LinkedHashMap<String, Category> map = new java.util.LinkedHashMap<>();

        for (Category c : raw) {
            // category names are case-insensitive ("Section" == "section").
            String key = c.getName().trim().toLowerCase(java.util.Locale.ROOT);
            Category existing = map.get(key);
            if (existing == null) {
                map.put(key, c);
            } else {
                // Must match label list exactly
                if (!existing.getLabels().equals(c.getLabels())) {
                    throw new RuntimeException("Category '" + c.getName()
                            + "' is defined multiple times with different labels.");
                }
            }
        }

        return new ArrayList<>(map.values());
    }

    /**
     * Gets the expected label count shared by all categories.
     *
     * @param categories parsed categories
     * @return label count from the first category
     */
    private int getLabelCount(List<Category> categories) {
        if (categories.isEmpty()) throw new RuntimeException("No categories found.");
        return categories.get(0).size();
    }

    /**
     * Validates core puzzle dimensions (minimum sizes and matching label counts).
     *
     * @param categories parsed categories
     */
    private void validatePuzzleDimensions(List<Category> categories) {
        int n = categories.size();
        if (n < 2) throw new RuntimeException("Puzzle must have at least 2 categories.");

        int l = getLabelCount(categories);
        if (l < 2) throw new RuntimeException("Each category must have at least 2 labels.");

        for (Category c : categories) {
            if (c.size() != l) {
                throw new RuntimeException("All categories must have the same label count L. Category "
                        + c.getName() + " has " + c.size() + " labels, expected " + l);
            }
        }
    }

    /**
     * Validates all hint and answer-key locations against the derived puzzle dimensions.
     *
     * @param hints parsed hints
     * @param answerKey parsed answer-key locations
     * @param n category count
     * @param l labels per category
     */
    private void validateLocations(List<Hint> hints, List<CellLocation> answerKey, int n, int l) {
        for (Hint h : hints) validateLocation(h.getLocation(), n, l);
        for (CellLocation loc : answerKey) validateLocation(loc, n, l);
    }

    /**
     * Validates one cell location against board-space and grid-space bounds.
     *
     * @param loc location to validate
     * @param n category count
     * @param l labels per category
     */
    private void validateLocation(CellLocation loc, int n, int l) {
        Position bp = loc.boardPosition();
        Position gp = loc.gridPosition();

        // boardPosition depends on N (categories)
        if (bp.row() < 0 || bp.row() > n - 2) throw new RuntimeException("boardPosition row out of range: " + bp);
        if (bp.col() < 0 || bp.col() > (n - 2 - bp.row())) throw new RuntimeException("boardPosition col out of range: " + bp);

        // gridPosition depends on L (labels)
        if (gp.row() < 0 || gp.row() > l - 1) throw new RuntimeException("gridPosition row out of range: " + gp);
        if (gp.col() < 0 || gp.col() > l - 1) throw new RuntimeException("gridPosition col out of range: " + gp);
    }

    @Override
    public String toString() {
        return "PuzzleLoader{defaultPath='" + defaultPath + "'}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PuzzleLoader other)) return false;
        return Objects.equals(defaultPath, other.defaultPath);
    }
}
