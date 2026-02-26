/** Written by: Roberto Baez
 * CellState is an enum class that is used to represent
 * an enumeration where each value represents 1 of 3
 * distinct states for a Cell to be in:
 * - `Blank` represents a neutral, indeterminate state; visually
 *   depicted as a blank game square
 * - `True` represents a true state; visually depicted as a green
 *   checkmark
 * - `False` represents a false state; visually depicted as a red
 *   "x" symbol
 *
 * @author Roberto Baez
 */
public enum CellState {
  Blank, True, False
}
