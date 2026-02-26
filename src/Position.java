/** Written by: Roberto Baez
 * Position is a record class which is used
 * to create immutable objects that represent a
 * two-dimensional position. It is used to
 * describe both the location of a Cell within a Grid,
 * and the location of a Grid within a Board.
 * @param row     the zero-indexed number representing the
 *                position's row (vertical axis)
 * @param col     the zero-indexed number representing the
 *                position's column (horizontal axis)
 *
 * @author Roberto Baez
 */
public record Position(int row, int col) { }
