public class Grid {
  // default rows and columns based on 3x4 logic puzzle
  private static final int DEFAULT_NUM_COLUMNS = 4;
  private static final int DEFAULT_NUM_ROWS = 4;
  private Cell[][] cells;
  public Grid() {
    this(DEFAULT_NUM_ROWS, DEFAULT_NUM_COLUMNS);
  }
  public Grid(int numRows, int numColumns) {
    this.cells = new Cell[numRows][numColumns];
    // initialize all Cell objects
    for (int i = 0; i < numRows; i++)
      for (int k = 0; k < numColumns; k++)
        this.cells[i][k] = new Cell(new Position(i, k));
  }
  public Cell getCell(Position pos) {
    return cells[pos.col()][pos.row()];
  }
  public int getNumRows() {
    return this.cells.length;
  }
  public int getNumColumns() {
  /*
    all inner arrays are guaranteed to be the same length.
    (see initialization of `cells` attribute in constructor)
  */
    return this.cells[0].length; 
  }
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
