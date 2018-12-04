package client.presenter.ai;

import client.game.Game;
import client.gui.ChadGameDriver;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.network.NetworkManager;
import client.presenter.network.messages.ActiveGameRequest;
import client.presenter.network.messages.ActiveGameResponse;
import client.presenter.network.messages.InboxRequest;
import client.presenter.network.messages.InboxResponse;
import client.presenter.network.messages.InviteResponse;
import client.presenter.network.messages.Login;
import client.presenter.network.messages.LoginResponse;
import client.presenter.network.messages.Move;
import client.presenter.network.messages.NetworkMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

public class AiDriver implements ChadGameDriver {

  private NetworkManager network;

  public AiDriver(InetAddress addr, int port) throws IOException {
    System.out.println("Starting AI at " + addr.toString() + ":" + port);
    this.network = new NetworkManager(addr, port, this);
    network.startThread();
    if (network.sendMessage(new Login(
        "ai@ai.ai", "easymode"))) {
      System.out.println("Sent login request");
    } else {
      System.out.println("AI login failed to send");
    }
    InboxPing inboxChecker = new InboxPing();
    inboxChecker.run();
    network.sendMessage(new ActiveGameRequest("AI"));
  }

  @Override
  public void createAndShowGUI() {

  }

  @Override
  public void handleViewMessage(ViewMessage message) {

  }

  @Override
  public void handleNetMessage(NetworkMessage message) {
    switch (message.type) {
      case LOGIN_RESPONSE:
        System.out.println("Login response received...");
        LoginResponse login = (LoginResponse) message;
        if (login.success) {
          System.out.println("AI logged in");
        } else {
          System.out.println("AI login failed.");
        }
        break;
      case MOVE:
        System.out.println("Move received...");
        Move move = (Move) message;
        boolean turn = (!Character.isUpperCase(move.move.charAt(0)));

        this.move(move.gameID, move.board, turn);

        break;
      case INBOX_RESPONSE:
        System.out.println("Received Inbox response:");
        InboxResponse response = (InboxResponse) message;
        System.out.println(response);
        for (int i = 0; i < response.inviteIDs.length; ++i) {
          if (!response.senders[i].equals("AI") && !response.senders[i].equals("-1")) {
            System.out.println("Received invite from " + response.senders[i]);
            network.sendMessage(new InviteResponse(response.inviteIDs[i], true));
          }
        }
        break;
      case ACTIVE_GAMES_RESPONSE:
        System.out.println("Received Active Games response:");
        ActiveGameResponse games = (ActiveGameResponse) message;
        for (int i = 0; i < games.color.length; ++i) {
          if (games.color[i] == games.turns[i]) {
            this.move(games.gameIDs[i], games.gameBoards[i], games.turns[i]);
          }
        }

        break;
      default:
        break;
    }
  }

  private void move(int gameID, String gameBoard, boolean turn) {
    Game chadGame = new Game(gameBoard, turn);
    if (chadGame.gameover()) {
      return;
    }
    String args = "\"" + gameBoard + "\", " + (turn ? "True" : "False");
    String command = "python3 -c \'import ChadML as gm; gm.bestMove(" + args + ");\'";
    try {
      System.out.println("Calling python script with command:");
      System.out.println(command);

      Process p = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", command});
      p.waitFor();

      String aiMove = null;

      try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
        String line;
        while ((line = br.readLine()) != null) {
          aiMove = line;
        }
      }
      if (aiMove == null) {
        throw new InvalidValue("No move received from AI");
      }
      System.out.println("Move calculated, making move '" + aiMove + "'");
      String boardString = chadGame.getBoard();
      int pIndex = boardString.indexOf(aiMove.substring(0, 2));
      String moveFrom = boardString.substring(pIndex - 1, pIndex + 2);
      chadGame.move(aiMove.substring(0, 2), aiMove.substring(2, 4));
      System.out.println("Sending move to server");
      network.sendMessage(new Move(
          gameID,
          moveFrom + aiMove.substring(2, 4),
          chadGame.getBoard(),
          chadGame.gameover(), false));
    } catch (InterruptedException | IOException | InvalidValue e) {
      e.printStackTrace();
    }
  }

  public class InboxPing extends Thread {

    public void run() {
      while (true) {
        try {
          sleep(10000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("Check for invites...");
        if (!network.sendMessage(new InboxRequest("AI"))) {
          System.exit(1);
        }

      }
    }
  }

  public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
    AiDriver driver = new AiDriver(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
  }

}
