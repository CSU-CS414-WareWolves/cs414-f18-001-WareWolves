package client.gui.swing;

import client.gui.ChadGameDriver;
import client.gui.swing.panels.LoginScreenPanel;
import client.gui.swing.panels.MainMenuPanel;
import client.gui.swing.panels.UnregisterAccountPanel;
import client.gui.swing.panels.chadgame.GameJPanel;
import client.gui.swing.panels.testcontrolers.TestGameDriver;
import client.gui.swing.panels.testcontrolers.TestSwingController;
import client.presenter.ChadPresenter;
import client.presenter.controller.messages.ActiveGameMessage;
import client.presenter.controller.messages.LoginResponseMessage;
import client.presenter.controller.messages.LogoutMessage;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.MovePieceResponse;
import client.presenter.controller.messages.RegisterResponseMessage;
import client.presenter.controller.messages.UnregisterMessage;
import client.presenter.controller.messages.UnregisterResponseMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.controller.messages.ViewValidMovesResponse;
import client.presenter.network.messages.NetworkMessage;
import client.presenter.network.messages.UnregisterResponse;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class SwingController extends JFrame implements ChadGameDriver {

  private JPanel mainPanel;
  private MainMenuPanel menuPanel;
  private LoginScreenPanel loginScreenPanel;
  private GameJPanel gameJPanel;
  private JPanel cardPanel;
  private UnregisterAccountPanel unregisterAccountPanel;

  /**
   * The menu bar
   */
  JMenuBar menuBar;


  private ChadGameDriver controller;
  private CardLayout cardLayout;
  private boolean playingGame = false;


  public SwingController(ChadGameDriver controller) {

    this.controller = controller;

    $$$setupUI$$$();
    cardLayout = (CardLayout) cardPanel.getLayout();
    this.add(menuBar, BorderLayout.NORTH);
    cardLayout.show(cardPanel, "LoginScreen");
  }


  @Override
  public void handleViewMessage(ViewMessage message) {
    System.out.println("SwingController::handleViewMessage " + message.messageType);

    switch (message.messageType) {

      case REGISTER:
        controller.handleViewMessage(message);
        break;
      case LOGIN:
        controller.handleViewMessage(message);
        break;
      case UNREGISTER:
        UnregisterMessage unregisterMessage = (UnregisterMessage) message;
        if (unregisterMessage.email == null) {
          cardLayout.show(cardPanel, "MenuScreen");
          menuBar.setVisible(true);
          return;
        }
        controller.handleViewMessage(message);
        break;
      case SHOW_VALID_MOVES:
        controller.handleViewMessage(message);
        break;
      case MOVE_PIECE:
        controller.handleViewMessage(message);
        break;
      case REGISTER_RESPONSE:
        RegisterResponseMessage registerResponse = (RegisterResponseMessage) message;
        if (registerResponse.success) {
          menuPanel.setNickName(registerResponse.messages[0]);
          cardLayout.show(cardPanel, "MenuScreen");
          menuBar.setVisible(true);
        } else {
          loginScreenPanel.receiveMessage(message);
        }
        break;
      case LOGIN_RESPONSE:
        LoginResponseMessage loginResponse = (LoginResponseMessage) message;
        if (loginResponse.success) {
          menuPanel.setNickName(loginResponse.nickname);
          cardLayout.show(cardPanel, "MenuScreen");
          menuBar.setVisible(true);
        } else {
          loginScreenPanel.receiveMessage(message);
        }
        break;
      case UNREGISTER_RESPONSE:
        UnregisterResponseMessage unregisterResponse = (UnregisterResponseMessage) message;
        JOptionPane.showMessageDialog(gameJPanel, unregisterResponse.messages[0]);
        if (unregisterResponse.success) {
          cardLayout.show(cardPanel, "LoginScreen");
          menuBar.setVisible(false);
        }
        break;
      case SHOW_VALID_MOVES_RESPONSE:
        ViewValidMovesResponse validMoves = (ViewValidMovesResponse) message;
        gameJPanel.setValidMoves(validMoves.locations[0]);
        break;
      case MENU_RESPONSE:
        break;
      case MOVE_PIECE_RESPONSE:
        if (!playingGame) {
          cardLayout.show(cardPanel, "GameScreen");
          playingGame = true;
          menuBar.setVisible(false);
        }
        MovePieceResponse moves = (MovePieceResponse) message;
        gameJPanel.clearValidMoves();
        gameJPanel.setSetGameStatus(moves.message);
        gameJPanel.setBoardPieces(moves.gameBoard);
        break;
      case PROFILE:
        controller.handleViewMessage(message);
        break;
      case ACTIVE_GAMES:
        controller.handleViewMessage(message);
        break;
      case INBOX:
        controller.handleViewMessage(message);
        break;
      case GAME_REQUEST:
        controller.handleViewMessage(message);
        break;
      case NEW_INVITE:
        controller.handleViewMessage(message);
        break;
      case INVITE_RESPONSE:
        controller.handleViewMessage(message);
        break;
      case LOGOUT:
        if (playingGame) {
          cardLayout.show(cardPanel, "MenuScreen");
          controller.handleViewMessage(new ActiveGameMessage());
          gameJPanel.setBoardPieces("");
          playingGame = false;
          menuBar.setVisible(true);
        }
        break;
      case RESIGN:
        controller.handleViewMessage(message);
        break;
    }


  }


  @Override
  public void handleNetMessage(NetworkMessage message) {
    System.out.println("SwingController::handleViewMessage " + message.type);
    switch (message.type) {

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
    gameJPanel = new GameJPanel(this);
    unregisterAccountPanel = new UnregisterAccountPanel(this);
    createMenuBar();

  }


  /**
   * Creates the menu items
   */
  private void createMenuBar() {

    //Create the menu bar.
    menuBar = new JMenuBar();

    //Build the first menu.
    JMenu menu = new JMenu("Game Menu");
    menuBar.add(menu);

    //a group of JMenuItems
    JMenuItem logout = new JMenuItem("Logout");
    logout.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(cardPanel, "LoginScreen");
        controller.handleViewMessage(new LogoutMessage());
        menuBar.setVisible(false);
      }
    });

    JMenuItem unregister = new JMenuItem("Unregister");
    unregister.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(cardPanel, "UnregisterScreen");
        menuBar.setVisible(false);
      }
    });

    menuBar.setVisible(false);
    menu.add(logout);
    menu.add(unregister);

  }

  public static void main(String[] args) {

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

    frame.setJMenuBar(menuBar);
    frame.add(this.mainPanel);
    frame.setResizable(false);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }


  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout(0, 0));
    cardPanel = new JPanel();
    cardPanel.setLayout(new CardLayout(0, 0));
    mainPanel.add(cardPanel, BorderLayout.CENTER);
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridBagLayout());
    cardPanel.add(panel1, "LoginScreen");
    GridBagConstraints gbc;
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    panel1.add(loginScreenPanel.$$$getRootComponent$$$(), gbc);
    final JPanel spacer1 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(spacer1, gbc);
    final JPanel spacer2 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer2, gbc);
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new BorderLayout(0, 0));
    cardPanel.add(panel2, "MenuScreen");
    panel2.add(menuPanel.$$$getRootComponent$$$(), BorderLayout.CENTER);
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new BorderLayout(0, 0));
    cardPanel.add(panel3, "GameScreen");
    panel3.add(gameJPanel, BorderLayout.CENTER);
    final JPanel panel4 = new JPanel();
    panel4.setLayout(new BorderLayout(0, 0));
    cardPanel.add(panel4, "UnregisterScreen");
    panel4.add(unregisterAccountPanel.$$$getRootComponent$$$(), BorderLayout.CENTER);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return mainPanel;
  }
}
