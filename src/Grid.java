/** Written by: Roberto Baez
 * Grid is the class that represents a single game grid.
 * It holds a two-dimensional array of Cells and supports
 * various lengths and widths.
 *
 * @author Roberto Baez
 */
public class Grid {
  // default rows and columns based on 3x4 logic puzzle
  private static final int DEFAULT_NUM_COLUMNS = 4;
  private static final int DEFAULT_NUM_ROWS = 4;
  private Cell[][] cells;
  /**
   * Constructs a Grid with the default number of rows and columns.
   */
  public Grid() {
    this(DEFAULT_NUM_ROWS, DEFAULT_NUM_COLUMNS);
  }
  /**
   * Constructs a Grid with the specified number of rows
   * and columns.
   * @param numRows the number of rows for this Grid
   * @param numColumns the number of columns for this Grid
   */
  public Grid(int numRows, int numColumns) {
    this.cells = new Cell[numRows][numColumns];
    // initialize all Cell objects
    for (int i = 0; i < numRows; i++)
      for (int k = 0; k < numColumns; k++)
        this.cells[i][k] = new Cell(new Position(i, k));
  }
  /**
   * Returns the Cell object located at pos within
   * this grid's cells attribute.
   * @param pos   the Position object of the cell to be located
   * @return      the Cell located at pos
   */
  public Cell getCell(Position pos) {
    return cells[pos.row()][pos.col()];
  }
  /**
   * Returns the number of rows in this grid.
   * @return the number of rows in this grid
   */
  public int getNumRows() {
    return this.cells.length;
  }
  /**
   * Returns the number of columns in this grid.
   * @return the number of columns in this grid
   */
  public int getNumColumns() {
  /*
    all inner arrays are guaranteed to be the same length.
    (see initialization of `cells` attribute in constructor)
  */
    return this.cells[0].length; 
  }
  /**
   * Returns a string representation of this object.
   * @return a String object representing this object
   */
  @Override
  public String toString() {
    // count number of Cells that have each CellState value
    int numTrue = 0, numFalse = 0, numBlank = 0;
    for (int row = 0; row < this.getNumRows(); row++)
      for (int col = 0; col < this.getNumColumns(); col++) {
        CellState state = this.getCell(new Position(row, col)).getState();
        if (state == CellState.True)
          numTrue++;
        else if (state == CellState.False)
          numFalse++;
        else
          numBlank++;
      }
    // format counted values
    String trueStr = String.format("%d \"True\" Cells", numTrue);
    String falseStr = String.format("%d \"False\" Cells", numFalse);
    String blankStr = String.format("%d \"Blank\" Cells", numBlank);
    return String.format("Grid with %s, %s, and %s\n", trueStr, falseStr, blankStr);
  }
  /**
   * Indicates whether some other object is "equal to" this one.
   * @param obj    the reference object with which to compare
   * @return       true if this object is the same as the obj argument;
   *               false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Grid)) return false; // `obj` not same class
    Grid g = (Grid) obj;
    if ( this.getNumRows() != g.getNumRows() ||
         this.getNumColumns() != g.getNumColumns() )
      return false; // `obj` not same shape (# of rows and columns)
    for (int row = 0; row < this.getNumRows(); row++)
      for (int col = 0; col < this.getNumColumns(); col++) {
        Position target = new Position(row, col);
        if (this.getCell(target).getState() != g.getCell(target).getState())
          return false; // `obj` doesn't have identical CellState for every Cell
      }
    return true;
  }
}
