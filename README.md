Tested on macOS, Java 12, JavaFX 11.0.2, Gradle Build

## Author

Yuyang LIN

## How To Compile

1. Download the whole package from git.
2. Import to IntelliJ.
3. Choose the file as a gradle one.
4. Set the Java version of 12, and import lib of JavaFX 11.0.2.
5. Set the main class as `com.sokoban.game.Main`.
6. In gradle menu, click `Tasks -> application -> Run` to run the project.

## Basic Maintenance

- Added auto-playing music and enable the "toggle music" button
- Added undo and reset level function
- Added dialog message after each level of the game with current / whole time and moves count on it, also the top 10 scores of current level will be printed (The ranking is based on time, the shorter, the higher rank. If times are the same, then check the smaller moves count)
- Closed some input streams to avoid memory leakage
- Fixed the bug that the game cannot go to next level properly
- Added a START page for user to choose wall colours (ChoiceBox) and asked users to input their names (TextField) to record in txt file
- Implemented the saveFile function

## Refactoring 

### 1. Design Pattern

- Used **Singleton Pattern** for `GameLogger.java` and `StartMeUp.java`, which are only initialized once. Make the constructors private to make sure they will not initialize again.
- Implemented **MVC Pattern** to separate models and GUI. Models in model files and GUI in view files, and controller files for handling events.

### 2. Package

- There are 5 packages in total, including controller, model, view, game, logger. 

  ```
  logger:
  		Gamelogger
  controller:
  		MainPageController
  		StartPageController
  view:
  		MainPageView
  		StartPageView
  model:
  		MusicModel
  		StageModel
  game:
  		Main
  		Dialog
  		GameGrid
  		GameObject
  		GraphicObject
  		Level
  		StartMeUp
  ```

### 3. Other Refactoring

- Refactored the code and produce a new class MusicModel with music functionality
- Refactored the code and produce a new class Dialog to make dialog message window
- Used Gradle for build tool
- Used symbolic content instead of magic numbers
- Set instance variables to private and use setter and getter for outside to call

## Extension

- Changed the game interface
- Added functions to record the scores of each player each level
- Added features to make keepers have different direction changes, and if there are invalid keyboard events, there will be an alert window
- Added a new menu item to enable the player to see the top 10 during the playing time
- Removed the blurring effect on text, and blur the gameGrid while showing victory message
- In the start page, if the user do not enter a name, there will be an alert popup for name request after the "Start" button is clicked.
- Added a button in start page for users to check the top 10 list for each level in the game
- Fixed the bug while loading new files, the music for the default example game will not stop
- Added a permanent status display for steps count, time and level in the game grid
- Added a new level at the first
- Added JUnit test classes
