import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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
        // TODO: replace with PuzzleLoader once file loading is ready
        PuzzleDefinition definition = buildHardcodedDefinition();
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

        hintArea.setText(hint.getText());
    }

    @FXML
    private void onClearErrorsClicked() {
        // TODO visually highlight and remove error cells once PuzzleValidator exists
        int clearedCount = puzzleGame.clearErrors();
        hintArea.setText("Cleared " + clearedCount + " error(s).");
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

    // Temporary hardcoded to match what's already shown
    private PuzzleDefinition buildHardcodedDefinition() {
        List<Category> categories = List.of(
                new Category("employees", List.of("Eduardo", "Jeremy", "Kurt", "Marc")),
                new Category("riders",    List.of("50", "75", "100", "125")),
                new Category("section",   List.of("blue", "pink", "purple", "red"))
        );

        List<Clue> clues = List.of(
                new Clue(1, "Marc served 125 riders."),
                new Clue(2, "The person who works in the blue section served 50 more riders than Eduardo."),
                new Clue(3, "Jeremy served 50 more riders than the worker who works in the purple section."),
                new Clue(4, "The employee in the red section served 50 more riders than the one in the pink section.")
        );

        List<Hint> hints = List.of(

                new Hint("Clue #1: 125 riders and Marc are explicitly true. Mark the highlighted cell as TRUE.",
                        new CellLocation(new Position(0, 0), new Position(3, 3)), CellState.True),

                new Hint("Clue #3: If purple is less than Jeremy by some specific amount, then purple does not equal Jeremy. Mark the highlighted cell as FALSE.",
                        new CellLocation(new Position(0, 1), new Position(1, 2)), CellState.False),

                new Hint("Clue #3: If Jeremy is 2 steps greater than purple, then purple cannot equal 100 riders. Otherwise, Jeremy would have to be larger than the largest item in the set (125 riders). Mark the highlighted cell as FALSE.",
                        new CellLocation(new Position(1, 0), new Position(2, 2)), CellState.False),

                new Hint("Clue #3: If Jeremy is 2 steps greater than purple, then purple cannot equal 125 riders. Otherwise, Jeremy would have to be larger than the largest item in the set (125 riders). Mark the highlighted cell as FALSE.",
                        new CellLocation(new Position(1, 0), new Position(2, 3)), CellState.False),

                new Hint("Clue #3: If Jeremy is 2 steps greater than purple, then Jeremy cannot equal 50 riders. Otherwise, purple would have to be smaller than the smallest item in the set (50 riders). Mark the highlighted cell as FALSE.",
                        new CellLocation(new Position(0, 0), new Position(1, 0)), CellState.False),

                new Hint("Clue #3: If Jeremy is 2 steps greater than purple, then Jeremy cannot equal 75 riders. Otherwise, purple would have to be smaller than the smallest item in the set (50 riders). Mark the highlighted cell as FALSE.",
                        new CellLocation(new Position(0, 0), new Position(1, 1)), CellState.False),

                new Hint("100 riders must equal Jeremy, since all other possibilities have been eliminated. Mark the highlighted cell as TRUE.",
                        new CellLocation(new Position(0, 0), new Position(1, 2)), CellState.True),

                new Hint("Clue #3: If Jeremy is 2 steps greater than purple, and Jeremy is equal to 100 riders, then purple must be 2 steps less than Jeremy, and purple must equal 50 riders. Mark the highlighted cell as TRUE.",
                        new CellLocation(new Position(1, 0), new Position(2, 0)), CellState.True),

                new Hint("Clue #2: If Eduardo is less than blue by some specific amount, then Eduardo does not equal blue. Mark the highlighted cell as FALSE.",
                        new CellLocation(new Position(0, 1), new Position(0, 0)), CellState.False),

                new Hint("Clue #2: If blue is 2 steps greater than Eduardo, then blue cannot equal 75 riders. Otherwise, Eduardo would have to be smaller than the smallest item in the set (50 riders). Mark the highlighted cell as FALSE.",
                        new CellLocation(new Position(1, 0), new Position(0, 1)), CellState.False),

                new Hint("Clue #4: If red is 2 steps greater than pink, then pink cannot equal 100 riders. Otherwise, red would have to be larger than the largest item in the set (125 riders). Mark the highlighted cell as FALSE.",
                        new CellLocation(new Position(1, 0), new Position(1, 2)), CellState.False),

                new Hint("Clue #4: If red is 2 steps greater than pink, then pink cannot equal 125 riders. Otherwise, red would have to be larger than the largest item in the set (125 riders). Mark the highlighted cell as FALSE.",
                        new CellLocation(new Position(1, 0), new Position(1, 3)), CellState.False),

                new Hint("75 riders must equal pink, since all other possibilities have been eliminated. Mark the highlighted cell as TRUE.",
                        new CellLocation(new Position(1, 0), new Position(1, 1)), CellState.True),

                new Hint("Clue #4: If red is 2 steps greater than pink, and pink is equal to 75 riders, then red must be 2 steps greater than 75 riders, and red must equal 125 riders. Mark the highlighted cell as TRUE.",
                        new CellLocation(new Position(1, 0), new Position(3, 3)), CellState.True),

                new Hint("100 riders must equal blue, since all other possibilities have been eliminated. Mark the highlighted cell as TRUE.",
                        new CellLocation(new Position(1, 0), new Position(0, 2)), CellState.True),

                new Hint("Clue #2: If blue is 2 steps greater than Eduardo, and blue is equal to 100 riders, then Eduardo must be 2 steps less than blue, and Eduardo must equal 50 riders. Mark the highlighted cell as TRUE.",
                        new CellLocation(new Position(0, 0), new Position(0, 0)), CellState.True),

                new Hint("75 riders must equal Kurt, since all other possibilities have been eliminated. Mark the highlighted cell as TRUE.",
                        new CellLocation(new Position(0, 0), new Position(2, 1)), CellState.True),

                new Hint("50 riders is equal to Eduardo, and 50 riders is equal to purple, therefore Eduardo is equal to purple. Mark the highlighted cell as TRUE.",
                        new CellLocation(new Position(0, 1), new Position(0, 2)), CellState.True),

                new Hint("75 riders is equal to Kurt, and 75 riders is equal to pink, therefore Kurt is equal to pink. Mark the highlighted cell as TRUE.",
                        new CellLocation(new Position(0, 1), new Position(2, 1)), CellState.True),

                new Hint("100 riders is equal to Jeremy, and 100 riders is equal to blue, therefore Jeremy is equal to blue. Mark the highlighted cell as TRUE.",
                        new CellLocation(new Position(0, 1), new Position(1, 0)), CellState.True),

                new Hint("Red must equal Marc, since all other possibilities have been eliminated. Mark the highlighted cell as TRUE.",
                        new CellLocation(new Position(0, 1), new Position(3, 3)), CellState.True)
        );

        List<CellLocation> answerKey = List.of(
                new CellLocation(new Position(0, 0), new Position(3, 3)),
                new CellLocation(new Position(0, 0), new Position(1, 2)),
                new CellLocation(new Position(1, 0), new Position(2, 0)),
                new CellLocation(new Position(1, 0), new Position(1, 1)),
                new CellLocation(new Position(1, 0), new Position(3, 3)),
                new CellLocation(new Position(1, 0), new Position(0, 2)),
                new CellLocation(new Position(0, 0), new Position(0, 0)),
                new CellLocation(new Position(0, 0), new Position(2, 1)),
                new CellLocation(new Position(0, 1), new Position(0, 2)),
                new CellLocation(new Position(0, 1), new Position(2, 1)),
                new CellLocation(new Position(0, 1), new Position(1, 0)),
                new CellLocation(new Position(0, 1), new Position(3, 3))
        );

        return new PuzzleDefinition(categories, clues, hints, answerKey);
    }
}