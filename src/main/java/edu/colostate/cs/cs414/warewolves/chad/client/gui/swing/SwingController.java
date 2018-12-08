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


  @Override
  public void handleViewMessage(ViewMessage message) {
    System.out.println("SwingController::handleViewMessage " + message.messageType);

    switch (message.messageType) {


      case UNREGISTER:
        UnregisterMessage unregisterMessage = (UnregisterMessage) message;
        if (unregisterMessage.email == null) {
          cardLayout.show(cardPanel, "MenuScreen");
          menuBar.setVisible(true);
          return;
        }
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

      case LOGOUT:
        if (playingGame) {
          cardLayout.show(cardPanel, "MenuScreen");
          controller.handleViewMessage(new ActiveGameMessage());
          gameJPanel.setBoardPieces("");
          playingGame = false;
          menuBar.setVisible(true);
        }
        break;
        // Pass all other message types to controller
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
        System.err.println("SwingController::handleViewMessage sent invalid message type "
            + message.messageType);
    }


  }


  /**
   * Passes a NetworkMessage to the menuPanel these message are used to update the tables
   * in the menu.
   * Valid NetworkMessage types:
   * ACTIVE_GAMES_RESPONSE
   * INBOX_RESPONSE
   * PROFILE_RESPONSE
   * PLAYERS
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
