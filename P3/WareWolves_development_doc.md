# WareWolves Chad - Development Manual

A simple and elegant game of tactical and strategic depth



### Technologies needed

This project requires the latest versions of:
- [Java SDK 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/#installation)
- [Maven](https://maven.apache.org/install.html)



### Running instructions

1. Clone this repository into a directory of your choice.
2. Run SwingChadDriver or CLDriver to launch the interface of your choice.



### Instructions for development

1. Clone this repository into a directory of your choice.
2. Create a new branch.
3. Pick an issue to tackle.
4. Commit your solution to your branch, with complete comments and reasoning for your changes.
5. Create pull request.



### Packages

This project has 3 primary packages: `game`, `gui`, and `presenter`. `game` contains all code for game logic, `gui` contains packages for both the Swing GUI and the command line interface, and `presenter` contains the driver and network code.



### Running the Game

To run the current version of the game GUI run the main method of `SwingChadDriver` or `CLDriver`. This will open a JFrame window where the game can be played. Once starting a game, click a piece (on the current player's side) then drag it to one of the indicated valid moves.

### Testing

- Run the command `mvn test`
- Alternatively, you can run individual JUnit test cases from src/test/java/client

