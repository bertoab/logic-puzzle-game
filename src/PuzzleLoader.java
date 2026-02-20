import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Loads puzzle definition data from a text file.
 * Keeps parsing responsibilities separate from UI and game logic.
 * Uses simple fail-fast RuntimeExceptions for malformed data.
 */
public class PuzzleLoader {

    public PuzzleDefinition loadDefinition(String path) {
        ArrayList<Category> categories = new ArrayList<>();
        ArrayList<Clue> clues = new ArrayList<>();
        ArrayList<Hint> hints = new ArrayList<>();
        ArrayList<CellLocation> answerKey = new ArrayList<>();

        List<String> lines = readAllLines(path);

        int i = 0;
        while (i < lines.size()) {
            String line = lines.get(i).trim();

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

            i++;
        }

        // Normalize categories to your intended 3-category meaning (optional but recommended)
        categories = normalizeCategories(categories);

        validatePuzzleDimensions(categories);
        int n = categories.size();
        int l = categories.get(0).size();
        validateLocations(hints, answerKey, n, l);

        return new PuzzleDefinition(categories, clues, hints, answerKey);
    }

    // Reads the file into a list of strings.
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

    // Parses repeating blocks like:
    // categoryName
    // - label1
    // - label2
    // (blank line)
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
            i++;

            ArrayList<String> labels = new ArrayList<>();
            while (i < lines.size()) {
                String l = lines.get(i).trim();
                if (l.isEmpty()) { i++; break; }
                if (l.startsWith("-")) {
                    labels.add(l.substring(1).trim());
                    i++;
                    continue;
                }
                // REVIEW NOTE: Non-dash lines here are treated as a new category.
                // Discuss whether this should throw immediately as malformed input.
                // next category block begins
                break;
            }

            if (!labels.isEmpty()) {
                categories.add(new Category(categoryName, labels));
            } else {
                // REVIEW NOTE: Empty category blocks are currently ignored.
                // Discuss whether this should fail fast with a clear error message.
            }
        }
        return i;
    }

    private int parseClues(List<String> lines, int start, List<Clue> clues) {
        int i = start;
        while (i < lines.size()) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) { i++; continue; }
            if (isSectionHeader(line)) return i;

            // "1. blah blah"
            int dot = line.indexOf('.');
            if (dot > 0) {
                try {
                    int num = Integer.parseInt(line.substring(0, dot).trim());
                    String text = line.substring(dot + 1).trim();
                    clues.add(new Clue(num, text));
                } catch (Exception ignored) {
                    // REVIEW NOTE: Malformed clue lines are currently ignored.
                }
            }
            i++;
        }
        return i;
    }

    // Each hint block looks like:
    // <text line>
    // <a,b - c,d>
    // <True/False>
    // (blank)
    private int parseHints(List<String> lines, int start, List<Hint> hints) {
        int i = start;
        while (i < lines.size()) {
            String textLine = lines.get(i).trim();
            if (textLine.isEmpty()) { i++; continue; }
            if (isSectionHeader(textLine)) return i;

            String locationLine = safeGetTrim(lines, i + 1);
            String stateLine = safeGetTrim(lines, i + 2);

            CellLocation loc = parseCellLocation(locationLine);
            CellState state = parseCellState(stateLine);

            hints.add(new Hint(textLine, loc, state));
            i += 3;
        }
        return i;
    }

    private int parseAnswerKey(List<String> lines, int start, List<CellLocation> answerKey) {
        int i = start;
        while (i < lines.size()) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) { i++; continue; }
            if (isSectionHeader(line)) return i;

            answerKey.add(parseCellLocation(line));
            i++;
        }
        return i;
    }

    private String safeGetTrim(List<String> lines, int index) {
        if (index >= lines.size()) return "";
        return lines.get(index).trim();
    }

    private boolean isSectionHeader(String line) {
        String s = line.trim();
        return s.equalsIgnoreCase("Horizontal Label:")
                || s.equalsIgnoreCase("Vertical Label:")
                || s.equalsIgnoreCase("Clues:")
                || s.equalsIgnoreCase("Hints:")
                || s.equalsIgnoreCase("Answer Key:");
    }

    private CellLocation parseCellLocation(String s) {
        // format
        // "0,0 - 1,2"
        String[] parts = s.split("-");
        if (parts.length != 2) {
            throw new RuntimeException("Bad cell location: " + s);
        }

        Position boardPos = parsePosition(parts[0].trim());
        Position gridPos = parsePosition(parts[1].trim());

        return new CellLocation(boardPos, gridPos);
    }

    private Position parsePosition(String s) {
        // accepts "1, 2" or "1,2"
        String[] parts = s.split(",");
        if (parts.length != 2) {
            throw new RuntimeException("Bad position: " + s);
        }
        int r = Integer.parseInt(parts[0].trim());
        int c = Integer.parseInt(parts[1].trim());
        return new Position(r, c);
    }

    private CellState parseCellState(String s) {
        if (s.equalsIgnoreCase("true")) return CellState.True;
        if (s.equalsIgnoreCase("false")) return CellState.False;
        // REVIEW NOTE: Invalid state text currently defaults to Blank.
        return CellState.Blank;
    }

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


    private int getLabelCount(List<Category> categories) {
        if (categories.isEmpty()) throw new RuntimeException("No categories found.");
        return categories.get(0).size();
    }

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


    private void validateLocations(List<Hint> hints, List<CellLocation> answerKey, int n, int l) {
        for (Hint h : hints) validateLocation(h.getLocation(), n, l);
        for (CellLocation loc : answerKey) validateLocation(loc, n, l);
    }

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

}
