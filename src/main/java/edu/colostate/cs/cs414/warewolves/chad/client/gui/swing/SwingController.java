package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing;

import edu.colostate.cs.cs414.warewolves.chad.client.gui.ChadGameDriver;
import edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels.LoginScreenPanel;
import edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels.MainMenuPanel;
import edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels.UnregisterAccountPanel;
import edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels.chadgame.GameJPanel;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ActiveGameMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.LoginResponseMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.LogoutMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.MovePieceResponse;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.RegisterResponseMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.UnregisterMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.UnregisterResponseMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ViewMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ViewValidMovesResponse;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.NetworkMessage;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
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

/**
 * This class is the main controller for the swing GUI it acts as a facade passing information and
 * commands to the various swing panels and switching between those panels.
 */
public class SwingController extends JFrame implements ChadGameDriver {

  /**
   * Main panel for the class
   */
  private JPanel mainPanel;
  /**
   * Main Menu panel
   */
  private MainMenuPanel menuPanel;
  /**
   * Login screen panel
   */
  private LoginScreenPanel loginScreenPanel;
  /**
   * Game panel to show the game
   */
  private GameJPanel gameJPanel;
  /**
   * Panel for unregistering Account
   */
  private UnregisterAccountPanel unregisterAccountPanel;
  /**
   * Panel that holds all the other panels
   */
  private JPanel cardPanel;
  /**
   * The menu bar with logout and unregister options
   */
  JMenuBar menuBar;
  /**
   * Controller of the class
   */
  private ChadGameDriver controller;
  /**
   * Card layout manager for changing panels
   */
  private CardLayout cardLayout;
  /**
   * Is there a game being played currently
   */
  private boolean playingGame = false;

  /**
   * Creates the GUI elements and sets the panels controller for ActionListeners
   *
   * @param controller the controller of the panel
   */
  public SwingController(ChadGameDriver controller) {

    this.controller = controller;

    $$$setupUI$$$(); // Needed for the GUI
    cardLayout = (CardLayout) cardPanel.getLayout();
    this.add(menuBar, BorderLayout.NORTH);
    cardLayout.show(cardPanel, "LoginScreen"); // Set default panel to login
  }


  /**
   * Handles the logic for the message type it is given
   * @param message the message to process
   */
  @Override
  public void handleViewMessage(ViewMessage message) {
    System.out.println("SwingController::handleViewMessage " + message.messageType);
    switch (message.messageType) {
      case UNREGISTER:
        handleUnregisterMessage((UnregisterMessage) message);
        break;
      case REGISTER_RESPONSE:
        handleRegisterResponse((RegisterResponseMessage) message);
        break;
      case LOGIN_RESPONSE:
        handleLoginResponse((LoginResponseMessage) message);
        break;
      case UNREGISTER_RESPONSE:
        handleUnregisterResponse((UnregisterResponseMessage) message);
        break;
      case SHOW_VALID_MOVES_RESPONSE:
        handleValidMoveResponse((ViewValidMovesResponse) message);
        break;
      case MOVE_PIECE_RESPONSE:
        handleMovePieceResponse((MovePieceResponse) message);
        break;
      case LOGOUT:
        handleLogoutFromGameScreen();
        break;
      // Pass other message types to controller
      default:
        handlePassThroughMessage(message);
        break;
    }
  }

  /**
   * Handles all the messages that only need to be sent to the controller
   * @param message the message to pass to the controller
   */
  private void handlePassThroughMessage(ViewMessage message) {
    System.out.println("SwingController::handlePassThroughMessage " + message.messageType);
    switch (message.messageType) {
      case PROFILE:
      case ACTIVE_GAMES:
      case INBOX:
      case GAME_REQUEST:
      case NEW_INVITE:
      case INVITE_RESPONSE:
      case RESIGN:
      case SHOW_VALID_MOVES:
      case MOVE_PIECE:
      case REGISTER:
      case LOGIN:
        controller.handleViewMessage(message);
        break;
      default:
        System.err.println("SwingController::handlePassThroughMessage sent invalid message type "
            + message.messageType);
    }
  }

  /**
   * Handles a users attempt to login. If they were successful, the active panel is changed to the
   * main menu. If they were not successful, a pop up displays why
   *
   * @param message the results of the login attempt attempt
   */
  private void handleLoginResponse(LoginResponseMessage message) {
    if (message.success) {
      menuPanel.setNickName(message.nickname);
      cardLayout.show(cardPanel, "MenuScreen");
      menuBar.setVisible(true);
    } else {
      loginScreenPanel.receiveMessage(message);
    }
  }

  /**
   * Handles a users attempt to register. If they were successful, the active panel is changed to the
   * main menu. If they were not successful, a pop up displays why
   *
   * @param message the results of the registration attempt
   */
  private void handleRegisterResponse(RegisterResponseMessage message) {
    // Successful registration
    if (message.success) {
      menuPanel.setNickName(message.messages[0]);
      cardLayout.show(cardPanel, "MenuScreen");
      menuBar.setVisible(true);
    } else {
      // Unsuccessful registration
      loginScreenPanel.receiveMessage(message);
    }
  }

  /**
   * Handles a message for the user trying to unregister. If they canceled the attempt, the active
   * panel is changed to the menu screen. If they did not cancel the message is passed to the
   * controller
   *
   * @param message a users attempt to unregister
   */
  private void handleUnregisterMessage(UnregisterMessage message) {
    // player canceled the unregister attempt
    if (message.email == null) {
      cardLayout.show(cardPanel, "MenuScreen");
      menuBar.setVisible(true);
      return;
    }
    controller.handleViewMessage(message);
  }

  /**
   * Changes the active panel to the login screen if the user was unregistered, or displays why the
   * unregister attempt was not successful
   *
   * @param message the results of unregister a user
   */
  private void handleUnregisterResponse(UnregisterResponseMessage message) {
    JOptionPane.showMessageDialog(gameJPanel, message.messages[0]);
    if (message.success) {
      cardLayout.show(cardPanel, "LoginScreen");
      menuBar.setVisible(false);
    }
  }

  /**
   * Shows the valid moves on a game board
   *
   * @param message the valid moves
   */
  private void handleValidMoveResponse(ViewValidMovesResponse message) {
    gameJPanel.setValidMoves(message.locations[0]);
  }

  /**
   * Activates the game screen and print out the game board of a game
   *
   * @param message the game board
   */
  private void handleMovePieceResponse(MovePieceResponse message) {
    // Change panel to game
    if (!playingGame) {
      cardLayout.show(cardPanel, "GameScreen");
      playingGame = true;
      menuBar.setVisible(false);
    }
    // Display game board
    gameJPanel.clearValidMoves();
    gameJPanel.setSetGameStatus(message.message);
    gameJPanel.setBoardPieces(message.gameBoard);
  }

  /**
   * Sets the active panel to the game menu and updates the active game list
   */
  private void handleLogoutFromGameScreen() {
    if (playingGame) {
      cardLayout.show(cardPanel, "MenuScreen");
      controller.handleViewMessage(new ActiveGameMessage());
      gameJPanel.setBoardPieces("");
      playingGame = false;
      menuBar.setVisible(true);
    }
  }


  /**
   * Passes a NetworkMessage to the menuPanel these message are used to update the tables in the menu.
   * Valid NetworkMessage types: ACTIVE_GAMES_RESPONSE INBOX_RESPONSE PROFILE_RESPONSE PLAYERS
   *
   * @param message the message to process
   */
  @Override
  public void handleNetMessage(NetworkMessage message) {
    System.out.println("SwingController::handleNetMessage " + message.type);
    switch (message.type) {
      // Pass message to menuController
      case ACTIVE_GAMES_RESPONSE:
      case INBOX_RESPONSE:
      case PROFILE_RESPONSE:
      case PLAYERS:
        menuPanel.receiveMessage(message);
        break;
      default:
        System.err.println("SwingController::handleNetMessage sent invalid message type "
            + message.type);
    }

  }

  /**
   * Creates all the elements that the GUI needed custom constructors for
   */
  private void createUIComponents() {
    menuPanel = new MainMenuPanel(this);
    loginScreenPanel = new LoginScreenPanel(this);
    gameJPanel = new GameJPanel(this);
    unregisterAccountPanel = new UnregisterAccountPanel(this);
    createMenuBar();

  }


  /**
   * Creates the menu for controller
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
   * @noinspection ALL
   */
  private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
    if (currentFont == null) {
      return null;
    }
    String resultName;
    if (fontName == null) {
      resultName = currentFont.getName();
    } else {
      Font testFont = new Font(fontName, Font.PLAIN, 10);
      if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
        resultName = fontName;
      } else {
        resultName = currentFont.getName();
      }
    }
    return new Font(resultName, style >= 0 ? style : currentFont.getStyle(),
        size >= 0 ? size : currentFont.getSize());
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
