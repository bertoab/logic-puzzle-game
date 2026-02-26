/** Written by: Roberto Baez
 * Board is the class that represents a logic puzzle
 * game board. It stores a two-dimensional array of
 * Grids, and the number of categories. The number
 * of categories defines the shape of its arrays of Grids.
 *
 * @author Roberto Baez
 */
public class Board {
  // default number of categories based on 3x4 logic puzzle
  private static final int DEFAULT_NUM_CATEGORIES = 3;
  private final int numberOfCategories;
  private Grid[][] grids;
  /**
   * Constructs a Board with the default number of categories.
   */
  public Board() {
    this(DEFAULT_NUM_CATEGORIES);
  }
  /**
   * Constructs a Board with numCategories categories.
   * @param numCategories the number of categories in the puzzle game
   */
  public Board(int numCategories) {
    this.numberOfCategories = numCategories;
    // calculate number of grids
    // math formula: `numGrids` = combinations of `numCategories` taken 2 at a time
    int numGrids = ( numCategories * (numCategories - 1) ) / 2;
    // calculate number of rows
    int gridCount = numGrids;
    int numRows = 0;
    int i = 1;
    while (gridCount > 0) {
      gridCount -= i++;
      numRows++;
    }
    // initialize array that holds rows
    this.grids = new Grid[numRows][];
    // initialize each array
    int numGridsInRow = 1;
    for (int row = this.grids.length - 1; row >= 0; row--) {
      this.grids[row] = new Grid[numGridsInRow++];
      for (int col = 0; col < this.grids[row].length; col++)
        this.grids[row][col] = new Grid();
    }
  }
  /**
   * Returns the number of categories for the puzzle
   * represented by this board.
   * @return the number of categories this board supports
   */
  public int getNumCategories() {
    return this.numberOfCategories;
  }
  /**
   * Returns the Grid object located at gridPosition within
   * this board's grids attribute.
   * @param gridPosition  the Position object of the grid to be located
   * @return              the Grid located at gridPosition
   */
  public Grid getGrid(Position gridPosition) {
    return this.grids[gridPosition.row()][gridPosition.col()];
  }
  /**
   * Returns a string representation of this object.
   * @return a String object representing this object
   */
  @Override
  public String toString() {
    // count number of Grids
    int numGrids = 0;
    for (int row = 0; row < this.grids.length; row++)
      numGrids += this.grids[row].length;
    return String.format("Board with %d Grids\n", numGrids);
  }
  /**
   * Indicates whether some other object is "equal to" this one.
   * @param obj    the reference object with which to compare
   * @return       true if this object is the same as the obj argument;
   *               false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Board)) return false; // `obj` not same class
    Board b = (Board) obj;
    if (this.getNumCategories() != b.getNumCategories())
      return false; // `obj` not same shape (# of categories)
    for (int row = 0; row < this.grids.length; row++)
      for (int col = 0; col < this.grids[row].length; col++) {
        Position target = new Position(row, col);
        if (!(this.getGrid(target).equals(b.getGrid(target))))
          return false; // `obj` has a Grid that is not equal
      }
    return true;
  }
}
