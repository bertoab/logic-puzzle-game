import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the puzzle game screen.
 * Connects the JavaFX UI to the game logic.
 *
 * @author Cesar Pimentel & Roberto Baez
 */
public class GameController {
    @FXML private TextArea hintArea;
    @FXML private GridPane grid00;
    @FXML private GridPane grid01;
    @FXML private GridPane grid10;

    private PuzzleApplication app;
    private PuzzleGame puzzleGame;
    private List<Rectangle> highlightedCells;

    /**
     * Initializes this controller's attributes and populates the grids with cells.
     */
    @FXML
    public void initialize() {
        this.app = null;

        PuzzleLoader loader = new PuzzleLoader();
        PuzzleDefinition definition = loader.loadDefinition("Puzzle-1.txt");
        Board board = new Board(definition.getCategoryCount());
        puzzleGame = new PuzzleGame(definition, board);

        highlightedCells = new ArrayList<Rectangle>();

        // Fill each grid with clickable cells
        setupGrid(grid00, new Position(0, 0));
        setupGrid(grid01, new Position(0, 1));
        setupGrid(grid10, new Position(1, 0));
    }

    /**
     * Populates a GridPane with one clickable pane per cell.
     *
     * @param gridPane the UI grid to fill
     * @param boardPosition identifies which board grid to use
     */
    private void setupGrid(GridPane gridPane, Position boardPosition) {
        Grid grid = puzzleGame.getBoard().getGrid(boardPosition);

        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumColumns(); col++) {
                Cell cell = grid.getCell(new Position(row, col));
                gridPane.add(makeCellPane(cell, grid), col, row);
            }
        }
    }

    /**
     * Sets up the app so this controller can trigger scene changes.
     *
     * @param app the running application instance
     */
    public void setApp(PuzzleApplication app) {
        this.app = app;
    }

    /**
     * Builds a clickable pane for a single cell.
     * Clicking cycles the state: Blank -> False -> True -> Blank.
     *
     * @param cell the game cell this pane represents
     * @param grid the grid that contains the game cell represented by this pane
     * @return a StackPane ready to be added to a GridPane
     */
    private StackPane makeCellPane(Cell cell, Grid grid) {
        Rectangle background = new Rectangle(50, 50);
        background.setFill(Color.WHITE);
        background.setOpacity(0.3); // nearly invisible so the grid lines show through

        Label stateLabel = new Label("");

        StackPane pane = new StackPane(background, stateLabel);
        pane.setOnMouseClicked(e -> onCellClicked(cell, grid, stateLabel));

        return pane;
    }

    /**
     * Clears highlighted cells, validates and updates the clicked cell's state,
     * and checks if the puzzle is solved.
     *
     * @param cell the cell that was clicked
     * @param grid the Grid object containing the game cell that was clicked
     * @param stateLabel the label inside the cell pane
     */
    private void onCellClicked(Cell cell, Grid grid, Label stateLabel) {
        clearHighlightedCells();

        // ensure the next state is a valid move
        if (cell.getState() == CellState.False
            && !puzzleGame.getPuzzleValidator().isMoveValid(grid, cell)) {
            hintArea.setText("Cannot set a checkmark there. \nYou may only have one checkmark in each row and each column.");
            return;
        }

        // advance the state
        CellState nextState = nextState(cell.getState());
        cell.setState(nextState);

        // update label
        stateLabel.setText(labelForState(nextState));
        stateLabel.setTextFill(colorForState(nextState));

        checkPuzzleWon();
    }

    /**
     * Gets the next hint, highlights its cell yellow, and shows the hint text.
     */
    @FXML
    private void onHintClicked() {
        Hint hint = puzzleGame.getNextHint();

        if (hint == null) {
            hintArea.setText("No more hints available.");
            return;
        }
        highlightCellPane(hint.getLocation(), Color.YELLOW);

        hintArea.setText(hint.getText());
    }

    /**
     * Clears all incorrect cells and highlights them red.
     */
    @FXML
    private void onClearErrorsClicked() {
        // get List of all erroneous CellLocations
        List<CellLocation> errors = puzzleGame.clearErrors();
        for (CellLocation location : errors)
            clearErrorCellPane(location);
        hintArea.setText("Cleared " + errors.size() + " error(s).");
    }

    /**
     * Resets the game by reloading the game scene from scratch.
     */
    @FXML
    private void onStartOverClicked() {
        app.showGameScene();
    }

    /**
     * Checks if the puzzle is solved and switches to the menu scene if so.
     */
    private void checkPuzzleWon() {
        if (puzzleGame.getPuzzleValidator().isSolved(puzzleGame.getBoard())) {
            String winnerMessage = String.format("Congratulations, you solved the puzzle!\nHints Used: %d\n", puzzleGame.getHintCount());
            app.showMenuScene(winnerMessage);
        }
    }

    /**
     * Removes the highlight color from all previously highlighted cells.
     */
    private void clearHighlightedCells() {
        for (Rectangle rectangle : highlightedCells)
            rectangle.setFill(Color.WHITE);
        highlightedCells.clear();
    }

    /**
     * Highlights the cell at the given location with the specified color.
     *
     * @param location the cell to highlight
     * @param color the color to apply
     */
    private void highlightCellPane(CellLocation location, Color color) {
        ObservableList<Node> children = getCellPane(location).getChildren();
        for (Node child : children)
            if (child instanceof Rectangle) {
                highlightedCells.add((Rectangle) child);
                ((Rectangle) child).setFill(color);
            }
    }

    /**
     * Marks a cell red and clears its label to indicate it was an error.
     *
     * @param location the location of the erroneous cell
     */
    private void clearErrorCellPane(CellLocation location) {
        ObservableList<Node> children = getCellPane(location).getChildren();
        for (Node child : children) {
            if (child instanceof Rectangle) {
                highlightedCells.add((Rectangle) child);
                ((Rectangle) child).setFill(Color.RED);
            } if (child instanceof Label) {
                ((Label) child).setText("");
            }
        }
    }

    /**
     * Returns the StackPane in the UI matching the given cell location, or null if not found.
     *
     * @param location the cell location to look up
     * @return the matching StackPane
     */
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

    /**
     * Returns the next state in the click cycle: Blank -> False -> True -> Blank.
     *
     * @param current the current cell state
     * @return the next CellState
     */
    private CellState nextState(CellState current) {
        return switch (current) {
            case Blank -> CellState.False;
            case True  -> CellState.Blank;
            case False -> CellState.True;
        };
    }

    /**
     * Returns the display text for a given cell state.
     *
     * @param state the cell state
     * @return "✓" for True, "✗" for False, "" for Blank
     */
    private String labelForState(CellState state) {
        return switch (state) {
            case True  -> "✓";
            case False -> "✗";
            case Blank -> "";
        };
    }

    /**
     * Returns the display color for a given cell state.
     *
     * @param state the cell state
     * @return green for True, red for False, black for Blank
     */
    private Color colorForState(CellState state) {
        return switch (state) {
            case True  -> Color.GREEN;
            case False -> Color.RED;
            case Blank -> Color.BLACK;
        };
    }
}