package client.gui.swing.panels.testcontrolers;

import client.gui.swing.SwingGUIController;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.ProfileMessage;
import client.presenter.controller.messages.RegisterMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.network.messages.NetworkMessage;
import java.awt.event.ActionEvent;

public class TestGameMenuController extends SwingGUIController {

  @Override
  public void sendMessage(ViewMessage message) {

    switch (message.messageType){

      case REGISTER:
        RegisterMessage registerMessage = (RegisterMessage) message;
        System.out.println("Email: " + registerMessage.email +
            " Password: " + registerMessage.password +
            " Nickname: " + registerMessage.nickname);
        break;
      case LOGIN:
        LoginMessage loginMessage = (LoginMessage) message;
        System.out.println("Email: " + loginMessage.email + " Password: " + loginMessage.password);
        break;
      case UNREGISTER:
        break;
      case SHOW_VALID_MOVES:
        break;
      case MENU:
        break;
      case MOVE_PIECE:
        break;
      case REGISTER_RESPONSE:
        break;
      case LOGIN_RESPONSE:
        break;
      case UNREGISTER_RESPONSE:
        break;
      case SHOW_VALID_MOVES_RESPONSE:
        break;
      case MENU_RESPONSE:
        break;
      case MOVE_PIECE_RESPONSE:
        break;
      case PROFILE:
        ProfileMessage profileMessage = (ProfileMessage) message;
        System.out.println("Request Player Stats: " + profileMessage.nickname);
        break;
      case ACTIVE_GAMES:
        break;
      case INBOX:
        break;
      case GAME_REQUEST:
        break;
      case NEW_INVITE:
        break;
      case LOGOUT:
        break;
    }

    if (message instanceof MenuMessage) {
      MenuMessage menuMessage = (MenuMessage) message;

      switch (menuMessage.menuType) {
        case SELECT_GAME:
          System.out.println("Select Game: " + menuMessage.information[0] + " Opponent: "
              + menuMessage.information[2]);
          break;
        case RESIGN:
          System.out.println("Resign Game: " + menuMessage.information[0] + " Opponent: "
              + menuMessage.information[2]);
          break;
        case INVITES:

          if ( menuMessage.information.length == 0){
            System.out.println("User attempting to send new invite");
          } else if ( menuMessage.information.length == 1){
            System.out.println("Requesting invites for " + menuMessage.information[0]);
          } else {
            System.out.println("Invite ID: " + menuMessage.information[0] + " Accepting: "
                + menuMessage.information[1]);
          }
          break;
      }

    }
  }

  @Override
  public void receiveMessage(ViewMessage message) {

  }

  @Override
  public void receiveMessage(NetworkMessage message) {

  }

  @Override
  public void actionPerformed(ActionEvent e) {

    throw new IllegalArgumentException("ActiveGame:: Tried to send a action - "
        + e.getActionCommand());

  }
}
