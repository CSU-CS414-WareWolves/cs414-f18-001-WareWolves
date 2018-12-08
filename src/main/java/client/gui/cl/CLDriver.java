package client.gui.cl;

import client.Point;
import client.game.Game;
import client.gui.ChadGameDriver;
import client.gui.swing.info.ActiveGameInfo;
import client.presenter.ChadPresenter;
import client.presenter.controller.messages.*;
import client.presenter.network.messages.ActiveGameResponse;
import client.presenter.network.messages.GameInfo;
import client.presenter.network.messages.InboxResponse;
import client.presenter.network.messages.NetworkMessage;
import client.presenter.network.messages.Players;
import client.presenter.network.messages.ProfileResponse;
import java.security.NoSuchAlgorithmException;
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
  public void clearScreen() {
    System.out.println("\n-----------------------------------------------------------\n");
  }

  /**
   * Prints the splash and title screen menu
   */
  public void createAndShowGUI(){
    login.showSplash();
    chadGame = new Game();
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    handleTitleScreen();
  }

  /**
   *
   */
  public void handleTitleScreen() {
    clearScreen();
    int option = 0;
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
            return;
          default:
            warningValidOption();
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
  public LoginMessage handleLogin() throws NoSuchAlgorithmException {
    String email = "";
    String pass = "";

    System.out.println("Enter your e-mail:");
    email = requestLine();
    System.out.println("Enter your password:");
    pass = requestLine();

    return new LoginMessage(email, pass);
  }

  /**
   * Handles registration for a new user
   * @return a RegisterMessage with the new user's input
   * @throws NoSuchAlgorithmException
   */
  public RegisterMessage handleRegister() throws NoSuchAlgorithmException {
    String email = "";
    String pass = "";
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
  public ViewMessage handleMenu(){
    clearScreen();
    menu.showMenu(nickname);
    int option = 0;
    while(true) {
      option = Integer.parseInt(requestLine());
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
          clearScreen();
          break;
      }
    }
  }

  /**
   * Handles unregister confirmation for current user
   * @return an Unregister message
   */
  public UnregisterMessage handleUnregister() {
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
  public int showActiveGames(int[] gameIDs, String[] opponents, boolean[] turns, boolean[] color){
    game.showCurrentGames(gameIDs, opponents, turns, color);
    gameid = requestInt();
    for(int i = 0; i < gameIDs.length; i++) {
      if(gameid == gameIDs[i]) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Helper method to show in-game view.
   * (returns nothing but prints a nice view)
   */
  public void showGame(){
    clearScreen();
    game.showGameBoard(chadGame.getBoard());
    game.showInGameMenu();
  }

  /**
   * Handle in-game interactions with the board
   * @param turn true=current players turn
   * @return a ViewMessage corresponding to the user's actions
   */
  public ViewMessage handleMovePiece(boolean turn) {
    String from = "";
    String to = "";

    while(turn) {
      System.out.println("~ Select a piece (e.g. \"1a\"): ");
      while(from.equals("")) {
        from = requestLine();
      }
      //check for exit or resignation
      if(from.toUpperCase().equals("EXIT")) {
        return handleMenu();
      }
      else if(from.toUpperCase().equals("RESIGN")) {
        return new ResignMessage(gameid);
      }

      //display valid moves for selected piece
      String[] moves = stringToArray(chadGame.validMoves(from));
      game.showValidMoves(moves);

      System.out.println("[!] Type \"c\" to cancel piece selection");
      System.out.println("~ Select space to move to (e.g. \"1a\"): ");
      to = requestLine();
      if (!to.equals("c")) {
        return new MovePieceMessage(new Point(from), new Point(to));
      }
      showGame();
    }

    return handleMenu();
  }

  /**
   * Handle inbox interactions
   * @param ids array with ids for games
   * @param dates array with invite received dates
   * @param senders array with challenger nicknames
   * @return an AcceptInvite message with chosen id/nickname
   */
  public ViewMessage handleInbox(int[] ids, String[] dates, String[] senders){
    boolean check = menu.viewInvites(ids, dates, senders);
    if(check) {
      String temp = requestLine();
      String[] info = temp.split(" ");

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
  public InviteMessage handleOutbox() {
    String info = "";
    menu.requestUsername();
    info = requestLine();
    System.out.println("Invite will be sent to: " + info);
    return new InviteMessage(nickname, info);
  }

  public ViewMessage handleProfile() {
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
   * >>May not be needed in this class<<
   */
  private void warningValidOption() {
    System.err.println("[!] Please input a valid option\n");
  }

  /**
   * Parses valid moves String to array
   * @param s
   */
  public String[] stringToArray(String s) {
    String[] res = s.split("");
    return res;
  }

  /**
   * Prints array nicely with commas, no comma added at the end.
   * @param S String[] array
   */
  public String arrayToString(String[] S) {
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

  public String requestLine() {
    keyboard.keys = new Scanner(System.in);
    return keyboard.keys.nextLine();
  }

  public int requestInt() {
    keyboard.keys = new Scanner(System.in);
    return keyboard.keys.nextInt();
  }

  public class KeyboardThread extends Thread {
    Scanner keys = new Scanner(System.in);

    public KeyboardThread() {
    }

    public void run() {
    }
  }

  public static void main(String[] args) {

  }
}
