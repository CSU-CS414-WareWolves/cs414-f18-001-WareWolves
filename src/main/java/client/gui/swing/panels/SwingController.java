package client.gui.swing.panels;

import client.gui.ChadGameDriver;
import client.gui.swing.GameJPanel;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.LoginResponseMessage;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.RegisterResponseMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.network.messages.LoginResponse;
import client.presenter.network.messages.NetworkMessage;
import java.awt.CardLayout;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SwingController extends Frame implements ChadGameDriver {

  private JPanel mainPanel;
  private MainMenuPanel menuPanel;
  private LoginScreenPanel loginScreenPanel;
  private GameJPanel gameJPanel;

  private ChadGameDriver controller;
  private CardLayout cardLayout;
  private boolean playingGame = false;

  public SwingController(ChadGameDriver controller) {

    this.controller = controller;

    cardLayout = (CardLayout) mainPanel.getLayout();
    cardLayout.show(mainPanel, "LoginScreen");
  }


  @Override
  public void handleViewMessage(ViewMessage message) {

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
        break;
      case MENU:
        handleMenuMessage((MenuMessage) message);
        break;
      case MOVE_PIECE:
        break;
      case REGISTER_RESPONSE:
        RegisterResponseMessage registerResponse = (RegisterResponseMessage) message;
        if(registerResponse.success){
          menuPanel.setNickName(registerResponse.messages[0]);
          cardLayout.show(mainPanel, "MenuScreen");
        } else {
          loginScreenPanel.receiveMessage(message);
        }
        break;
      case LOGIN_RESPONSE:
        LoginResponseMessage loginResponse = (LoginResponseMessage) message;
        if(loginResponse.success){
          menuPanel.setNickName(loginResponse.nickname);
          cardLayout.show(mainPanel, "MenuScreen");
        } else {
          loginScreenPanel.receiveMessage(message);
        }
        break;
      case UNREGISTER_RESPONSE:
        break;
      case SHOW_VALID_MOVES_RESPONSE:
        break;
      case MENU_RESPONSE:
        break;
      case MOVE_PIECE_RESPONSE:
        break;
    }



  }

  private void handleMenuMessage(MenuMessage message) {

    switch (message.menuType){

      case LOGOUT:
        cardLayout.show(mainPanel, "MenuScreen");
        playingGame = false;
        break;
      case PLAYER_STATS:
        controller.handleViewMessage(message);
        break;
      case ACTIVE_GAMES:
        controller.handleViewMessage(message);
        break;
      case INVITES:
        controller.handleViewMessage(message);
        break;
      case SELECT_GAME:
        controller.handleViewMessage(message);
        //gameJPanel.setVisible(true);
        //this.setSize(gameJPanel.getSize());
        //this.revalidate();
        //this.repaint();
        //this.pack();
        cardLayout.show(mainPanel, "GameScreen");
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
    //gameJPanel.setVisible(false);
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
    TestGameDriver control = new TestGameDriver();
    //Create and set up the content pane.
    SwingController demo = new SwingController(control);
    control.setGui(demo);


    frame.add(demo.mainPanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }



}
