# SPEC.md

## Project
Logic Puzzle Game (CS225 Project 2)

## Purpose
This project is a single-player logic puzzle solving game implemented using JavaFX. The user can play a puzzle by marking squares as true or false, viewing clues, revealing hints, clearing erroneous squares, and starting over.

## Scope
- One 3 x 4 puzzle is available to solve.
- The program supports distinct logic puzzles by reading puzzle information from a file.
- A new round begins when a user clicks the “Start Game” button.
- Gameplay is available on-demand.

## Platform / Constraints (FURPS+)
- The system shall be implemented in Java using JavaFX.
- The system shall run as a desktop application.
- The system shall read puzzle data from local file(s) bundled with the project.
- The system shall not require network services or external dependencies.
- The system shall not depend on a database server.
- The system shall not require web hosting or cloud storage.
- The system shall operate without an internet connection.

## User Experience (UI Expectations)
- The program must be intuitive enough that the user can start playing within a short amount of time, by only reading the instructions.
- Instructions appear on the left side of the grid.
- Clue, clear errors & hint buttons are below the grid.
- Error messages appear below the clue, clear errors & hints buttons.
- Buttons must be clear and self-explanatory (example: “Show Hint” instead of “Hint”).
- The grid must visually indicate different states using different colors and symbols for marked true and marked false.
- The user can’t break the game by clicking too fast or invalid areas.
- A menu screen is presented when the user initially starts the program or finishes a round, with options to exit or start a new round.
- Clicking a grid square changes its state and the presentation updates immediately.
- Clicking “Hint” displays the next hint in a text box below all UI elements.
- Clicking “Clear Errors” checks marked grid squares and updates them accordingly, and this change is reflected immediately.
- Clicking “Start Over” resets all grid squares and the hint counter.
- The program can support a timer to track how long it takes to solve.

## Core Features
- Presents a single-player puzzle solving game using JavaFX.
- User will be able to play a puzzle.
- User will be able to mark squares as “true” and “false.”
- User will be able to reveal hints.
- User will be able to view clues.
- User will be able to clear erroneous squares.
- User will be able to start over.

## Game Flow
1. When user initially starts the program, a menu screen is presented with options to exit or start a new round.
2. A new round begins when a user clicks the “Start Game” button.
3. The system loads puzzle data from a local file at the start of a round.
4. The system parses file contents into an in-memory puzzle state.
5. During the round, the user interacts with the board by clicking grid squares and clicking buttons (“Show Hint”, “Clear Errors”, “Start Over”).
6. The game state is stored in memory during a round.
7. The user may start over at any time, which clears grid squares and resets the hint counter.
8. When a round ends, the menu screen is presented again.

## Data Behavior
### Inputs
- The system will accept input primarily through UI interactions.
- The system shall allow the user to click grid squares to change their state.
- The system shall allow the user to click buttons such as “Show Hint”, “Clear Errors”, and “Start Over.”

### Outputs
- The system shall output game information through the UI.
- The system shall display the current grid state visually.
- The system shall display clue text in a dedicated UI area.
- The system shall display hint text in a dedicated UI area when requested.

### Puzzle File
- Reading puzzle information from file.
- System will parse data from a file.
- Comparing game state in memory to file state on storage disk.
- The system shall load clue text from the file.
- The system shall load the puzzle’s correct solution/answer key from the file.
- The system shall load hint text (or hint sequence) from the file.
- The only data that will flow in or out of the system is the puzzle board file.
- The puzzle file is loaded when a round starts.

### In-Memory State
- The system shall store gameplay state in memory during a round.
- The system shall store each grid square state as True, False, or Blank.
- The system shall store the user’s current hint progress

## Reliability / Validation Rules
- The system shall enforce basic rule constraints during play.
- The system shall prevent the user from marking more than one truth marker in the same row and column.
- The system shall detect invalid or conflicting states.
- The system shall identify when the user’s markings contradict the puzzle’s solution state.
- The system shall handle user input safely without crashing.
- The system shall not crash when the user clicks repeatedly or quickly.
- The system shall not crash when the user clicks outside the grid or clicks UI controls in any order.
- The system shall maintain consistent state across core actions.
- The system shall update the visual grid immediately after a square state changes.
- The system shall reset the grid and hint counter when “Start Over” is clicked.
- The system shall remove or correct erroneous markings when “Clear Errors” is clicked.
- The system shall not require backups or persistence across runs.
- The system shall not be required to save progress after closing the application.

## Performance Expectations
- The program must be responsive enough for an enjoyable gameplay experience.
- Reading and parsing the puzzle file into a playable game state must occur within seconds after a user clicks “Start Game” button.
- Interacting with UI elements must feel responsive.
- Very frequent actions, such as clicking a grid square to toggle its state, must not cause visual lag or stuttering.
- This will be achievable with performance optimizations in the JavaFX implementation.

## Maintainability / Supportability Notes
- In future versions, the program may support round timers, leaderboards, flexible puzzle board sizes, and puzzle board generation.
- The system shall support a flexible puzzle board data file to enable users to create their own gameplay experiences.
- The system shall separate the puzzle logic from the JavaFX UI code.
