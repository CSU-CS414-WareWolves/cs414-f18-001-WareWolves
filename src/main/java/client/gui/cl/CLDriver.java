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
import client.presenter.network.messages.Move;
import client.presenter.network.messages.NetworkMessage;
import client.presenter.network.messages.Players;
import client.presenter.network.messages.ProfileResponse;
import client.presenter.network.messages.RegisterResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class CLDriver implements ChadGameDriver {

  private ChadPresenter controller;

  private CLLogin login;
  private CLMenu menu;
  private CLGameView game;
  private KeyboardThread keyboard;

  // Active player nickname
  private String nickname;

  private Game chadGame;
  private int gameid;
  private String requestedName;
  private String[] activePlayers;

  public CLDriver(ChadPresenter _controller){
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
    login.showLogin();
    chadGame = new Game();

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
        option = requestInt();
        switch (option) {
          case 1:
            controller.handleViewMessage(handleLogin());
            return;
          case 2:
            controller.handleViewMessage(handleRegister());
            return;
          case 3:
            //handleLogout();
            return;
          default:
            warningValidOption();
            login.showSplash();
            login.showLogin();
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
    switch(message.type){
      case ACTIVE_GAMES_RESPONSE:
        ActiveGameResponse agr = (ActiveGameResponse) message;
        showActiveGames(agr.gameIDs, agr.opponents, agr.turns, agr.color);

        ActiveGameInfo agi = new ActiveGameInfo(gameid, agr.gameBoards[gameid], agr.opponents[gameid],
            agr.startDates[gameid], agr.turns[gameid], agr.color[gameid], agr.ended[gameid]);

        controller.handleViewMessage(new GameRequestMessage(agi.getInfoArray()));
        break;
      case GAME_INFO:
        GameInfo gi = (GameInfo) message;
        chadGame = new Game(gi.gameBoard, gi.turn);
        gameid = gi.gameID;
        showGame();
        break;
      case INBOX_RESPONSE:
        InboxResponse ir = (InboxResponse) message;
        ViewMessage im = handleInbox(ir.inviteIDs, ir.sendDates, ir.senders);
        controller.handleViewMessage(im);
        break;
      case LOGOUT:
        login.showLogout();
        break;
      case MOVE:
        break;
      case PLAYERS:
        Players p = (Players) message;
        activePlayers = p.players;
        break;
      case PROFILE_RESPONSE:
        ProfileResponse pr = (ProfileResponse) message;
        menu.showPlayers(activePlayers);
        menu.showStats(requestedName, pr.whitePlayers, pr.blackPlayers, pr.results);
        controller.handleViewMessage(handleProfile());
        break;
      case REGISTER_RESPONSE:
        RegisterResponse rr = (RegisterResponse) message;
        handleViewMessage(new RegisterResponseMessage(rr.success, new String[]{}));
        break;
    }
  }

  /**
   * Handles a given ViewMessage, acts according to the its type
   * @param message a ViewMessage with a type and data dependent on its type
   */
  public void handleViewMessage(ViewMessage message){
    switch (message.messageType){
      case LOGIN_RESPONSE:
        LoginResponseMessage lrm = (LoginResponseMessage) message;
        this.nickname = lrm.nickname;
        ViewMessage vm = handleMenu();
        controller.handleViewMessage(vm);
        break;
      case MOVE_PIECE:
        MovePieceMessage mpr = (MovePieceMessage) message;
        chadGame.move(mpr.fromLocation.toString(), mpr.toLocation.toString());

        // Show the winner if the game is over
        if(chadGame.gameover()){
          game.showGameover(!chadGame.getTurn());
        }

        // Send Move to Server
        controller.handleViewMessage(mpr);
        break;
      case REGISTER:
        try {
          RegisterMessage rm = handleRegister();
          controller.handleViewMessage(rm);
        } catch (NoSuchAlgorithmException e) {
          //handle error
          e.printStackTrace();
        }
        break;
      case REGISTER_RESPONSE:
        RegisterResponseMessage rrm = (RegisterResponseMessage) message;
        if(rrm.success){
          System.out.println(arrayToString(rrm.messages));
          menu.showMenu(nickname);
          ViewMessage vmm = handleMenu();
          controller.handleViewMessage(vmm);
        }
        else{
          System.out.println(arrayToString(rrm.messages));
          login.failedCreds(1);
          handleTitleScreen();
        }
        break;
      case SHOW_VALID_MOVES:
        String temp = chadGame.validMoves(((ViewValidMoves)message).location.toString());
        String[] moves = stringToArray(temp);
        game.showValidMoves(moves);
        break;
      case SHOW_VALID_MOVES_RESPONSE:
        ViewValidMovesResponse vvmr = (ViewValidMovesResponse) message;
        String[] validMovesArray1 = vvmr.locations;
        game.showValidMoves(validMovesArray1);
        break;
      case UNREGISTER:
        clearScreen();
        menu.unregisterUser();
        UnregisterMessage urm = handleUnregister();
        controller.handleViewMessage(urm);
        break;
      case UNREGISTER_RESPONSE:
        UnregisterResponseMessage urrm = (UnregisterResponseMessage) message;
        if(urrm.success) {
          //return to title screen on success
          System.out.println(urrm.messages);
          handleTitleScreen();
        } else {
          //return to main menu on fail
          System.out.println(urrm.messages);
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
    clearScreen();
    String email = "";
    String pass = "";

    System.out.println("Enter your e-mail:");
    while(email.equals("")) {
      email = requestLine();
    }
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
    clearScreen();
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
    String email;
    String pass;

    menu.unregisterUser();
    System.out.println("E-mail:");
    email = requestLine();
    System.out.println("Password:");
    pass = requestLine();

    try {
      return new UnregisterMessage(email, pass, nickname);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Shows the user's active games
   * @param ids list of ids of invitations
   * @param opponents list of nicknames the player has an invite from
   */
  public void showActiveGames(int[] ids, String[] opponents, boolean[] turns, boolean[] color){
    clearScreen();
    game.showCurrentGames(ids, opponents, turns, color);
  }

  /**
   * Handle the game selection screen
   * @return gameid int
   */
  public int handleSelectGame() {
    return requestInt();
  }

  /**
   * Handle in-game interactions with the board
   * @return a ViewMessage corresponding to the user's actions
   */
  public ViewMessage handleMovePiece() {
    String from = "";
    String to = "";
    while(true) {
      System.out.println("~ Select a piece (e.g. \"1a\"): ");
      from = requestLine();
      //check for exit or resignation
      if(from.toUpperCase().equals("EXIT")) {
        return handleMenu();
      }
      else if(from.toUpperCase().equals("RESIGN")) {
//        return handleMenu();
        return new ResignMessage(gameid);
      }

      //Display valid moves for selected piece
      //low TODO: helper to convert validMoves String into String[]
      String[] moves = {chadGame.validMoves(from)};
      game.showValidMoves(moves);

      System.out.println("[!] Type \"c\" to cancel piece selection");
      System.out.println("~ Select space to move to (e.g. \"1a\"): ");
      to = requestLine();
      if (!to.equals("c")) {
        break;
      }
    }
    return new MovePieceMessage(new Point(from), new Point(to));
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
   * Handle inbox interactions
   * @param ids array with ids for games
   * @param dates array with invite received dates
   * @param senders array with challenger nicknames
   * @return an AcceptInvite message with chosen id/nickname
   */
  public ViewMessage handleInbox(int[] ids, String[] dates, String[] senders){
    clearScreen();
    boolean check = menu.viewInvites(ids, dates, senders);
    if(check) {
      String info = "";
      int option = requestInt();

      info = senders[option];
      //high TODO: what do I return here?
      // probably not an invite lol, needs an accept_invite
      return new InviteMessage(info, nickname);
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
    clearScreen();
    String info = "";
    menu.requestUsername();
    info = requestLine();
    System.out.println("Invite will be sent to: " + info);
    return new InviteMessage(nickname, info);
  }

  public ViewMessage handleProfile() {
    clearScreen();
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
