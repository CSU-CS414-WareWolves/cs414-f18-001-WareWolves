# WareWolves Chad - Development Manual

A simple and elegant game of tactical and strategic depth



### Technologies needed

For development this project requires the latest versions of:

- [Java SDK 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/#installation)
- [Maven](https://maven.apache.org/install.html)
- [IntelliJ](https://www.jetbrains.com/idea/)

To run the AI this project requires the latest versions of:

- [Python 3](https://www.python.org/downloads/)
- [Pytorch](https://pytorch.org/)


### Running instructions

1. Clone this repository into a directory of your choice.
2. To launch a server run the main method of `server/ChadServer` with the argument of the port number that you want to server to run on. 
3. Run the main method of the `client/presenter/ChadPresenter` class with the arguments ["gui", "cli"], ip address of the server, post of the server.
4. To run an AI client run the main method of `client/presenter/ai/AiDriver` with arguments of the ip address of the server, post of the server. There should only be one AI client logged into a server at a time.


### Using IntelliJ
#### Cloning the Repository
1. Open IntelliJ
2. Select Check out from version control
3. Select GitHub
4. Enter the Git Repository URL of https://github.com/CSU-CS414-WareWolves/cs414-f18-001-WareWolves.git
5. Delect the directory of your choice for Parent directory and Directory name
6. Select Clone
7. Create a new branch using the IntelliJ menu.


#### After making changes
1. Run all tests using the Maven Projects window.
2. Use the menu or ctr-k to open the commit window. 
3. Make sure that the files that you changed are selected.
4. Enter a discriptive commit message.
5. Continue making changes and commiting until satisifed with your work.
6. Use the menu or ctr-shift-k to open the push window.
7. Push your changes to your remote branch.
8. In the [Github.com](https://github.com/CSU-CS414-WareWolves/cs414-f18-001-WareWolves) page for this project make a pull request for your branch. Attach it to any issues that is is connected to and add yourself as the assignee. 
9. Fix any problems raised by CodeClimate or Travis CI for your branch.
10. Once your branch is approved, merge it and delete the merged branch.


### Instructions for development

1. Clone this repository into a directory of your choice.
2. Create a new branch.
3. Pick an issue to tackle.
4. Commit your solution to your branch, with complete comments and reasoning for your changes.
5. Create pull request.



### Packages

This project has 2 packages: `server` and `client`. The `client` package contains three primary sub-packages: `game`, `gui`, and `presenter`. `game` contains all code for game logic, `gui` contains packages for both the Swing GUI and the command line interface, and `presenter` contains the driver and network code.



### Running the Game

__If a server is running__

Run `client/presenter/ChadPresenter` with the arguments ["gui", "cli"], ip address of the server, post of the server. This will open a JFrame window where the game can be played if the "gui" argument is passed and run a command line interface if "cli" is passed. Once a game is launched you can log in or create an account then start a game with another player or resume a game you are already a part of.

__If a server is not running__

Run `server/ChadServer` with the argument of the port number that you want to server to run on. The output of this command will show the IP address of the computer running the game, which you can use to launch a game client. 


### Testing

- Run the command `mvn test`
- Alternatively, you can run individual JUnit test cases from src/test/java/client
- See [testing document](./P3/WareWolvesTestingDocument11-04-18.pdf) for non-junit test cases 

