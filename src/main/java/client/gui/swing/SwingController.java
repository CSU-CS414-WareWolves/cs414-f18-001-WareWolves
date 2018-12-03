package client.gui.swing;

import client.gui.ChadGameDriver;
import client.gui.swing.panels.LoginScreenPanel;
import client.gui.swing.panels.MainMenuPanel;
import client.gui.swing.panels.chadgame.GameJPanel;
import client.gui.swing.panels.testcontrolers.TestSwingController;
import client.presenter.controller.messages.LoginResponseMessage;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.MovePieceResponse;
import client.presenter.controller.messages.RegisterResponseMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.controller.messages.ViewValidMovesResponse;
import client.presenter.network.messages.NetworkMessage;
import java.awt.CardLayout;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SwingController extends Frame implements ChadGameDriver {

  private JPanel mainPanel;
  private MainMenuPanel menuPanel;
  private LoginScreenPanel loginScreenPanel;
  private GameJPanel gameJPanel;
  private JPanel cardPanel;

  private ChadGameDriver controller;
  private CardLayout cardLayout;
  private boolean playingGame = false;

  public SwingController(ChadGameDriver controller) {

    this.controller = controller;

    cardLayout = (CardLayout) cardPanel.getLayout();
    cardLayout.show(cardPanel, "LoginScreen");
  }



  @Override
  public void handleViewMessage(ViewMessage message) {
    System.out.println("handleViewMessage:: " + message.messageType);

    switch (message.messageType){

      case REGISTER:
        controller.handleViewMessage(message);
        break;
      case LOGIN:
        controller.handleViewMessage(message);
        break;
      case UNREGISTER:
        controller.handleViewMessage(message);
        break;
      case SHOW_VALID_MOVES:
        controller.handleViewMessage(message);
        break;
      case MENU:
        handleMenuMessage((MenuMessage) message);
        break;
      case MOVE_PIECE:
        controller.handleViewMessage(message);
        break;
      case REGISTER_RESPONSE:
        RegisterResponseMessage registerResponse = (RegisterResponseMessage) message;
        if(registerResponse.success){
          menuPanel.setNickName(registerResponse.messages[0]);
          cardLayout.show(cardPanel, "MenuScreen");
        } else {
          loginScreenPanel.receiveMessage(message);
        }
        break;
      case LOGIN_RESPONSE:
        LoginResponseMessage loginResponse = (LoginResponseMessage) message;
        if(loginResponse.success){
          menuPanel.setNickName(loginResponse.nickname);
          cardLayout.show(cardPanel, "MenuScreen");
        } else {
          loginScreenPanel.receiveMessage(message);
        }
        break;
      case UNREGISTER_RESPONSE:
        break;
      case SHOW_VALID_MOVES_RESPONSE:
        ViewValidMovesResponse validMoves = (ViewValidMovesResponse) message;
        gameJPanel.setValidMoves(validMoves.locations[0]);
        break;
      case MENU_RESPONSE:
        break;
      case MOVE_PIECE_RESPONSE:
        MovePieceResponse moves = (MovePieceResponse) message;
        JOptionPane.showMessageDialog(gameJPanel, moves.message);
        gameJPanel.setBoardPieces(moves.gameBoard);
        break;
      case PROFILE:
        controller.handleViewMessage(message);
        break;
      case ACTIVE_GAMES:
        controller.handleViewMessage(message);
        break;
      case INBOX:
        break;
      case GAME_REQUEST:
        break;
      case NEW_INVITE:
        controller.handleViewMessage(message);
        break;
    }



  }

  private void handleMenuMessage(MenuMessage message) {

    switch (message.menuType){

      case LOGOUT:
        cardLayout.show(cardPanel, "MenuScreen");
        playingGame = false;
        break;
      case SELECT_GAME:
        controller.handleViewMessage(message);
        //gameJPanel.setVisible(true);
        //this.setSize(gameJPanel.getSize());
        //this.revalidate();
        //this.repaint();
        //this.pack();
        cardLayout.show(cardPanel, "GameScreen");
        playingGame = true;
        break;
      case SEND_INVITE:
        controller.handleViewMessage(message);
        break;
      case RESIGN:
        controller.handleViewMessage(message);
        break;
    }

  }

  @Override
  public void handleNetMessage(NetworkMessage message) {
    System.out.println("handleViewMessage:: " + message.type);
    switch (message.type){

      case LOGIN_RESPONSE:
        break;
      case MOVE:
        break;
      case ACTIVE_GAMES_RESPONSE:
        menuPanel.receiveMessage(message);
        break;
      case INVITE_RESPONSE:
        break;
      case REGISTER_RESPONSE:
        break;
      case INBOX_RESPONSE:
        menuPanel.receiveMessage(message);
        break;
      case PROFILE_RESPONSE:
        menuPanel.receiveMessage(message);
        break;
      case PLAYERS:
        menuPanel.receiveMessage(message);
        break;
      case UNREGISTER_RESPONSE:
        break;
      case SEE_RESULTS:
        break;
    }

  }


  private void createUIComponents() {
    menuPanel = new MainMenuPanel(this);
    loginScreenPanel = new LoginScreenPanel(this);
    gameJPanel =  new GameJPanel(this);

  }



  public static void main(String[] args) {

    SwingController demoController = new SwingController(new TestSwingController());

    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        demoController.createAndShowGUI();
      }
    });
  }
  /**
   * Create the GUI and show it.  For thread safety, this method should be invoked from the
   * event-dispatching thread.
   */
  public void createAndShowGUI() {
    //Create and set up the window.
    JFrame frame = new JFrame("Login Panel Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //Create and set up the content pane.
    SwingController demo = new SwingController(controller);


    frame.add(demo.mainPanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }



}
