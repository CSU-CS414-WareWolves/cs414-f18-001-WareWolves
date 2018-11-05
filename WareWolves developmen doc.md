# WareWolves Chad

A simple and elegant game of tactical and strategic depth



### Technologies needed

* Java 1.8
* Junit 5
* maven



### Running instructions

1. Clone this repository into a directory of your choice.
2. Run SwingChadDriver or CLDriver to launch the 



### Running a server

1. Clone this repository into a directory of your choice.
2. Run server package.



### Instructions for development

1. Clone this repository into a directory of your choice.
2. Create a new branch.
3. Do your thing.
4. Create pull request.





## WareWolves Chad - Development Manual

### Setup
This project requires the latest versions of:

- [IntelliJ](https://www.jetbrains.com/idea/)
- [Java SDK 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/#installation)



### Packages

This project has 3 primary packages: `game`, `gui`, and `presenter`. `game` contains all code for game logic, `gui` contains packages for both the Swing GUI and the command line interface, and `presenter` contains the driver and network code.



### Running the Game

To run the current version of the game GUI run the main method of `SwingChadDriver` or `CLDriver`. This will open a JFrame window where the game can be played. Once starting a game, click a piece (on the current player's side) then drag it to one of the indicated valid moves.

### Testing

Run the command `mvn test`
