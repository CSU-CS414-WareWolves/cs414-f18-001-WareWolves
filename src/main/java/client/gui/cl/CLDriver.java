package client.gui.cl;

import client.Point;
import client.game.Game;
import client.gui.ChadGameDriver;
import client.gui.swing.info.ActiveGameInfo;
import client.presenter.controller.messages.*;
import client.presenter.network.messages.ActiveGameResponse;
import client.presenter.network.messages.GameInfo;
import client.presenter.network.messages.InboxResponse;
import client.presenter.network.messages.NetworkMessage;
import client.presenter.network.messages.Players;
import client.presenter.network.messages.ProfileResponse;
import java.security.NoSuchAlgorithmException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class CLDriver implements ChadGameDriver {

  private ChadGameDriver controller;

  private CLLogin login;
  private CLMenu menu;
  private CLGameView game;
  private KeyboardThread keyboard;

  // Active player nickname
  private String nickname;
  private ActiveGameInfo gameInfo;

  private Game chadGame;
  private int gameid;
  private String requestedName;
  private String[] activePlayers;

  public CLDriver(ChadGameDriver _controller){
    controller = _controller;
    login = new CLLogin();
    menu = new CLMenu();
    game = new CLGameView();

    activePlayers = new String[]{};
    requestedName = "";
    keyboard = new KeyboardThread();
  }

  /**
   * Creates space for readability of the command-line
   * (returns nothing, but prints a long line and some space for readability)
   */
  void clearScreen() {
    System.out.println("\n-----------------------------------------------------------\n");
  }

  /**
   * Prints the splash and title screen menu
   */
  public void createAndShowGUI(){
    login.showSplash();
    chadGame = new Game();
    warningBugs();
    delay();
    handleTitleScreen();
  }

  /**
   *
   */
  private void handleTitleScreen() {
    clearScreen();
    int option;
    try {
      while(true) {
        login.showLogin();
        option = requestInt();
        switch (option) {
          case 1:
            controller.handleViewMessage(handleLogin());
            return;
          case 2:
            controller.handleViewMessage(handleRegister());
            return;
          case 3:
            System.out.println("[!] Hope to see you soon!");
            System.exit(0);
          default:
            warningValidOption();
            delay();
            clearScreen();
        }
      }
    } catch(NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles a given NetworkMessage, acts according to the its type
   * @param message a NetworkMessage with a type and data dependent on its type
   */
  public void handleNetMessage(NetworkMessage message){
    clearScreen();
    switch(message.type){
      case ACTIVE_GAMES_RESPONSE:
        ActiveGameResponse agr = (ActiveGameResponse) message;
        int index = showActiveGames(agr.gameIDs, agr.opponents, agr.turns, agr.color);
        if(index == 0) {
          controller.handleViewMessage(handleMenu());
        }
        else if(index == -1) {
          warningValidOption();
          controller.handleViewMessage(handleMenu());
        }
        else {
          gameInfo = new ActiveGameInfo(gameid, agr.gameBoards[index],
              agr.opponents[index], agr.startDates[index],
              agr.turns[index], agr.color[index], agr.ended[index]);
          controller.handleViewMessage(new GameRequestMessage(gameInfo.getInfoArray()));
        }
        break;
      case GAME_INFO:
        GameInfo gi = (GameInfo) message;
        chadGame = new Game(gi.gameBoard, gi.turn);
        gameid = gi.gameID;
        break;
      case INBOX_RESPONSE:
        InboxResponse ir = (InboxResponse) message;
        ViewMessage im = handleInbox(ir.inviteIDs, ir.sendDates, ir.senders);
        //send an invite
        controller.handleViewMessage(im);
        //jump back to menu
        controller.handleViewMessage(handleMenu());
        break;
      case LOGOUT:
        login.showLogout();
        break;
      case PLAYERS:
        Players p = (Players) message;
        activePlayers = p.players;
        break;
      case PROFILE_RESPONSE:
        ProfileResponse pr = (ProfileResponse) message;
        menu.showStats(requestedName, pr.whitePlayers, pr.blackPlayers, pr.results);
        clearScreen();

        controller.handleViewMessage(handleProfile());
        break;
    }
  }

  /**
   * Handles a given ViewMessage, acts according to the its type
   * @param message a ViewMessage with a type and data dependent on its type
   */
  public void handleViewMessage(ViewMessage message){
    clearScreen();
    switch (message.messageType){
      case LOGIN_RESPONSE:
        LoginResponseMessage lrm = (LoginResponseMessage) message;
        if(lrm.success) {
          this.nickname = lrm.nickname;
          ViewMessage vm = handleMenu();
          controller.handleViewMessage(vm);
        }
        else {
          login.failedCreds(0);
          handleTitleScreen();
        }
        break;
      case MOVE_PIECE_RESPONSE:
        MovePieceResponse mpr = (MovePieceResponse) message;

        System.out.println("[!] "+mpr.message);
        if(mpr.message.contains("'s turn. Playing: ")) {
          chadGame = new Game(mpr.gameBoard, gameInfo.getTurn());
          showGame();
          if(gameInfo.getColor() == gameInfo.getTurn()) {
            controller.handleViewMessage(handleMovePiece(true));
          }
          else{
            controller.handleViewMessage(handleMovePiece(false));
          }
        }
        else {
          game.showGameBoard(mpr.gameBoard);
          controller.handleViewMessage(handleMenu());
        }
        break;
      case REGISTER:
        try {
          RegisterMessage rm = handleRegister();
          controller.handleViewMessage(rm);
          nickname = rm.nickname;
        } catch (NoSuchAlgorithmException e) {
          //handle error
          e.printStackTrace();
        }
        break;
      case REGISTER_RESPONSE:
        RegisterResponseMessage rrm = (RegisterResponseMessage) message;
        if(rrm.success){
          System.out.println("[!] "+arrayToString(rrm.messages));
          ViewMessage vmm = handleMenu();
          controller.handleViewMessage(vmm);
        }
        else{
          System.out.println(arrayToString(rrm.messages));
          login.failedCreds(1);
          handleTitleScreen();
        }
        break;
      case SHOW_VALID_MOVES_RESPONSE:
        ViewValidMovesResponse vvmr = (ViewValidMovesResponse) message;
        String[] validMovesArray1 = vvmr.locations;
        game.showValidMoves(validMovesArray1);
        break;
      case UNREGISTER:
        menu.unregisterUser();
        UnregisterMessage um = handleUnregister();
        controller.handleViewMessage(um);
        break;
      case UNREGISTER_RESPONSE:
        UnregisterResponseMessage urm = (UnregisterResponseMessage) message;
        if(urm.success) {
          //return to title screen on success
          System.out.println("[!] "+arrayToString(urm.messages));
          handleTitleScreen();
        } else {
          //return to main menu on fail
          System.out.println("[!] "+arrayToString(urm.messages));
          ViewMessage vm2 = handleMenu();
          controller.handleViewMessage(vm2);
        }
        break;
    }
  }

  /**
   * Handles login for an existing user
   * @return a LoginMessage with the user's input
   */
  private LoginMessage handleLogin() throws NoSuchAlgorithmException {
    String email;
    String pass;

    System.out.println("Enter your e-mail:");
    email = requestLine();
    System.out.println("Enter your password:");
    pass = requestLine();

    return new LoginMessage(email, pass);
  }

  /**
   * Handles registration for a new user
   * @return a RegisterMessage with the new user's input
   * @throws NoSuchAlgorithmException in case of Hash fail
   */
  private RegisterMessage handleRegister() throws NoSuchAlgorithmException {
    String email = "";
    String pass;
    String nick = "";

    System.out.println("Please enter a valid e-mail:");
    while(email.equals("")) {
      email = requestLine();
    }
    System.out.println("Enter a unique nickname:");
    while(nick.equals("")) {
      nick = requestLine();
    }
    System.out.println("Enter a strong password:");
    pass = requestLine();

    nickname = nick;
    return new RegisterMessage(email, pass, nick);
  }

  /**
   * Handles main menu interactions
   * @return a ViewMessage corresponding to the option chosen
   */
  private ViewMessage handleMenu(){
    int option;
    while(true) {
      menu.showMenu(nickname);
      option = requestInt();
      switch (option) {
        case 1:
          //View Active Games
          return new ActiveGameMessage();
        case 2:
          //View Inbox
          return new InboxMessage();
        case 3:
          //Send Outbox
          return handleOutbox();
        case 4:
          //View Stats
          menu.showPlayers(activePlayers);
          return handleProfile();
        case 5:
          //Unregister
          return handleUnregister();
        case 6:
          //Logout
          System.out.println("[!] Hope to see you again soon, " + nickname + "!");
          controller.handleViewMessage(new LogoutMessage());
          System.exit(0);
        default:
          warningValidOption();
          delay();
          clearScreen();
          break;
      }
    }
  }

  /**
   * Handles unregister confirmation for current user
   * @return an Unregister message
   */
  private UnregisterMessage handleUnregister() {
    try {
      String email;
      String pass;
      menu.unregisterUser();
      System.out.println("[!] E-mail:");
      email = requestLine();
      System.out.println("[!] Password:");
      pass = requestLine();
      return new UnregisterMessage(email, pass, nickname);

    } catch(NoSuchAlgorithmException e) {
      return null;
    }
  }

  /**
   * Shows the user's active games
   * @param gameIDs game ids
   * @param opponents String array of opponent nicknames
   * @param turns whose turn is it in game
   * @param color current player's color in game
   * @return int of selected game id
   */
  private int showActiveGames(int[] gameIDs, String[] opponents, boolean[] turns, boolean[] color){
    game.showCurrentGames(gameIDs, opponents, turns, color);
    //get next input, returns -1 if not correct input
    int temp = requestInt();
    if(temp == -1) {
      return temp;
    }
    for (int i = 0; i < gameIDs.length; i++) {
      if (temp == gameIDs[i]) {
        gameid = temp;
        return i;
      }
    }
    return -1;
  }

  /**
   * Helper method to show in-game view.
   */
  private void showGame(){
    clearScreen();
    game.showGameBoard(chadGame.getBoard());
  }

  /**
   * Handle in-game interactions with the board
   * @param turn true=current players turn
   * @return a ViewMessage corresponding to the user's actions
   */
  private ViewMessage handleMovePiece(boolean turn) {
    String from = "";

    while(turn) {
      System.out.println("~ Select a piece (e.g. \"cE\"): ");
      while(from.equals("")) {
        from = requestLine();
        //check input
        if(from.length() != 2) {
          //check for exit
          if(from.toUpperCase().equals("EXIT")) {
            return handleMenu();
          }
          //check for resign
          else if(from.toUpperCase().equals("RESIGN")) {
            return new ResignMessage(gameid);
          }
          //incorrect format, try again
          else {
            from = "";
          }
        }
      }
      //check for exit or resignation

      //display valid moves for selected piece
      String[] moves = stringToArray(chadGame.validMoves(from));
      game.showValidMoves(moves);

      System.out.println("[!] Type \"C\" to cancel piece selection");
      System.out.println("~ Select space to move to (e.g. \"bE\"): ");
      String to = requestLine();

      if (!to.toUpperCase().equals("C")) {
        return new MovePieceMessage(new Point(from), new Point(to));
      }
      clearScreen();
      showGame();
      from = "";
    }

    clearScreen();
    return handleMenu();
  }

  /**
   * Handle inbox interactions
   * @param ids array with ids for games
   * @param dates array with invite received dates
   * @param senders array with challenger nicknames
   * @return an AcceptInvite message with chosen id/nickname
   */
  private ViewMessage handleInbox(int[] ids, String[] dates, String[] senders){
    boolean check = menu.viewInvites(ids, dates, senders);
    if(check) {
      String temp = requestLine();
      String[] info = temp.split(" ");
      if(temp.toUpperCase().contains("EXIT")) {
        return handleMenu();
      }
      int id = Integer.parseInt(info[0]);
      boolean response;
      if(info[1].toUpperCase().equals("ACCEPT")) {
        response = true;
      }
      else if(info[1].toUpperCase().equals("REJECT")){
        response = false;
      }
      else {
        warningValidOption();
        delay();
        return handleMenu();
      }

      return new InviteMessageResponse(id, response);
    }
    else {
      return handleMenu();
    }
  }

  /**
   * Takes input from the user to send a number of invites according to their selection
   * @return a MenuMessage of type SEND_INVITE with a String array of nicknames||emails(?)
   */
  private InviteMessage handleOutbox() {
    String info;
    menu.requestUsername();
    info = requestLine();
    System.out.println("Invite will be sent to: " + info);
    return new InviteMessage(nickname, info);
  }

  private ViewMessage handleProfile() {
    menu.showPlayers(activePlayers);
    menu.requestUsername();
    String nick = requestLine();
    if(nick.toUpperCase().equals("EXIT")){
      return handleMenu();
    }
    requestedName = nick;
    return new ProfileMessage(nick);
  }

  /**
   * Prints a warning if incorrect input is entered
   */
  private void warningValidOption() {
    System.err.println("[!] Please input a valid option\n");
  }

  /**
   * Prints title screen warning about bugs
   */
  private void warningBugs() {
    System.err.println("[!!!]  ~  WARNING  ~  [!!!]");
    delay();
    System.err.println("- Due to some unresolved bugs, the CL interface is unable to resume games");
    System.err.println("- All other menu functions should be normal");
    delay();
    System.err.println("\n- Thank you for your cooporation, and enjoy Chad Chess!");
    clearScreen();
  }

  /**
   * Parses valid moves String to array, splits at every two characters
   * @param moves String value
   */
  private static String[] stringToArray(String moves) {
    String[] temps = new String[(moves.length()/2)];
    StringBuilder temp = new StringBuilder();
    for(int i = 0, index = 0; i < moves.length(); i = i+2) {
      temp.append(moves.charAt(i));
      temp.append(moves.charAt(i + 1));
      temps[index] = temp.toString();
      index++;
      temp = new StringBuilder();
    }
    return temps;
  }

  /**
   * Prints array nicely with commas, no comma added at the end.
   * @param S String[] array
   */
  private static String arrayToString(String[] S) {
    StringBuilder res = new StringBuilder();
    for(int i = 0; i < S.length; i++) {
      if(i+1 != S.length) {
        res.append(S[i]).append(", ");
      }
      else {
        res.append(S[i]);
      }
    }
    return res.toString();
  }

  private String requestLine() {
    keyboard.keys = new Scanner(System.in);
    return keyboard.keys.nextLine();
  }

  private int requestInt() {
    keyboard.keys = new Scanner(System.in);
    int ret;
    try {
      ret = keyboard.keys.nextInt();
    } catch (InputMismatchException e) {
      return -1;
    }
    return ret;
  }

  private void delay() {
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public class KeyboardThread extends Thread {
    Scanner keys = new Scanner(System.in);

    private KeyboardThread() {
    }

    public void run() {
    }
  }

  public static void main(String[] args) {
  }
}
