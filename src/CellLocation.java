/** Written by: Roberto Baez
 * CellLocation is a record class which is used
 * to create immutable objects that represent an
 * absolute location to a particular Cell. It
 * describes a Position within a Board, and
 * another Position within a Grid contained by
 * that Board.
 * @param boardPosition   the Position object describing
                          the location of the Cell's parent
                          Grid within a Board
 * @param gridPosition    the Position object describing
 *                        the location of the Cell within its
 *                        parent Grid
 *
 * @author Roberto Baez
 */
public record CellLocation(Position boardPosition, Position gridPosition) { }
