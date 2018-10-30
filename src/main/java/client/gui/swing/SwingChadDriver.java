package client.gui.swing;

import client.game.Game;
import client.game.GameBoard;
import client.presenter.controller.ViewMessageType;
import client.presenter.controller.messages.MovePieceMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.controller.messages.ViewValidMoves;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SwingChadDriver {

  private final ArrayList<ViewMessage> viewMessages = new ArrayList<>();

  private ChadGameBoard gameBoard;
  private Game chadGame;


  private static final String DEFAULT_GAME_BOARD =
      "rdCreDRiHRjIrcCkdDreERhHKiIRjJrcDrdERhIRiJrcERhJreCRjH";


  public void handleViewMessage(ViewMessage message){

    if(message.messageType == ViewMessageType.SHOW_VALID_MOVES){
      ViewValidMoves validMoves = (ViewValidMoves) message;
      String valid = chadGame.validMoves(validMoves.location.toString());

      gameBoard.setValidMoves(valid, true);
    } else {
      MovePieceMessage moves = (MovePieceMessage) message;
      chadGame.move(moves.fromLocation.toString(), moves.toLocation.toString());

      gameBoard.setBoardPieces(chadGame.getBoard());

    }



  }




  public SwingChadDriver(){



  }

  public void start(){
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          createAndShowGUI();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
  }

  private void createAndShowGUI() throws InterruptedException {
    //Create and set up the window.
    JFrame frame = new JFrame("Team Warewolves Chad");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    chadGame = new Game();


    //Create and set up the content pane.
    gameBoard = new ChadGameBoard(this, chadGame.getBoard());
    gameBoard.setOpaque(true); //content panes must be opaque
    frame.setContentPane(gameBoard);

    //Display the window.
    frame.pack();
    frame.setVisible(true);

  }

  public static void main(String[] args) {

    SwingChadDriver app = new SwingChadDriver();
    app.start();

    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    /**
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    }); */
  }
}
