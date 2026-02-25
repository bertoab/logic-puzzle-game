import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;

import java.util.List;

public class HelloController {

    @FXML private TextArea hintArea;
    @FXML private GridPane grid00;
    @FXML private GridPane grid01;
    @FXML private GridPane grid10;

    private PuzzleGame puzzleGame;

    @FXML
    public void initialize() {
        PuzzleLoader loader = new PuzzleLoader();
        PuzzleDefinition definition = loader.loadDefinition("Puzzle-1.txt");
        Board board = new Board(definition.getCategoryCount());
        puzzleGame = new PuzzleGame(definition, board);

        // Fill each grid with clickable cells
        setupGrid(grid00, new Position(0, 0));
        setupGrid(grid01, new Position(0, 1));
        setupGrid(grid10, new Position(1, 0));
    }

    // Adds a clickable cell pane for every cell in a given grid
    private void setupGrid(GridPane gridPane, Position boardPosition) {
        Grid grid = puzzleGame.getBoard().getGrid(boardPosition);

        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumColumns(); col++) {
                Cell cell = grid.getCell(new Position(row, col));
                gridPane.add(makeCellPane(cell), col, row);
            }
        }
    }

    // Builds a clickable pane for a single cell.
    // The rectangle gives the cell a physical size so clicks register.
    // The label sits on top to show either a checkmark or x
    private StackPane makeCellPane(Cell cell) {
        Rectangle background = new Rectangle(50, 50);
        background.setFill(Color.WHITE);
        background.setOpacity(0.01); // nearly invisible so the grid lines show through

        Label stateLabel = new Label("");

        StackPane pane = new StackPane(background, stateLabel);
        pane.setOnMouseClicked(e -> onCellClicked(cell, stateLabel));

        return pane;
    }

    // Called every time a cell is clicked.
    // Updates the Cell in the game data, then refreshes the label to match.
    private void onCellClicked(Cell cell, Label stateLabel) {
        CellState nextState = nextState(cell.getState());
        cell.setState(nextState);

        stateLabel.setText(labelForState(nextState));
        stateLabel.setTextFill(colorForState(nextState));
    }

    @FXML
    private void onHintClicked() {
        Hint hint = puzzleGame.getNextHint();

        // TODO most likely remove this since it should always show a hint
        // if no hints are available it most likely means you won
        if (hint == null) {
            hintArea.setText("No more hints available.");
            return;
        }
        highlightCellPane(hint.getLocation(), Color.YELLOW);

        hintArea.setText(hint.getText());
    }

    @FXML
    private void onClearErrorsClicked() {
        // get List of all erroneous CellLocations
        List<CellLocation> errors = puzzleGame.clearErrors();
        for (CellLocation location : errors)
            clearErrorCellPane(location);
        hintArea.setText("Cleared " + errors.size() + " error(s).");
    }

    private void highlightCellPane(CellLocation location, Color color) {
        ObservableList<Node> children = getCellPane(location).getChildren();
        for (Node child : children)
            if (child instanceof Rectangle)
                ((Rectangle) child).setFill(color);
    }

    private void clearErrorCellPane(CellLocation location) {
        ObservableList<Node> children = getCellPane(location).getChildren();
        for (Node child : children) {
            if (child instanceof Rectangle) {
                ((Rectangle) child).setFill(Color.RED);
            } if (child instanceof Label) {
                ((Label) child).setText("");
            }
        }
    }

    private StackPane getCellPane(CellLocation location) {
        GridPane targetPane = null;
        Position boardPosition = location.boardPosition();
        if (new Position(0, 0).equals(boardPosition))
            targetPane = grid00;
        else if (new Position(0, 1).equals(boardPosition))
            targetPane = grid01;
        else if (new Position(1, 0).equals(boardPosition))
            targetPane = grid10;
        for (Node child : targetPane.getChildren()) {
            Integer childCol = GridPane.getColumnIndex(child);
            Integer childRow = GridPane.getRowIndex(child);
            if (childCol == null || childRow == null)
                continue;
            int targetCol = location.gridPosition().col();
            int targetRow = location.gridPosition().row();
            if (childCol == targetCol
                && childRow == targetRow
                && child instanceof StackPane)
                return (StackPane) child;
        }
        return null;
    }

    // Blank -> False -> True -> Blank
    private CellState nextState(CellState current) {
        return switch (current) {
            case Blank -> CellState.False;
            case True  -> CellState.Blank;
            case False -> CellState.True;
        };
    }

    private String labelForState(CellState state) {
        return switch (state) {
            case True  -> "✓";
            case False -> "✗";
            case Blank -> "";
        };
    }

    private Color colorForState(CellState state) {
        return switch (state) {
            case True  -> Color.GREEN;
            case False -> Color.RED;
            case Blank -> Color.BLACK;
        };
    }
}