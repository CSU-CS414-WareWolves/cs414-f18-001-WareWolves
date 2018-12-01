package client.gui.swing.panels;

import client.gui.swing.SwingGUIController;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.RegisterMessage;
import client.presenter.controller.messages.ViewMessage;
import java.awt.event.ActionEvent;

public class TestGameMenuController extends SwingGUIController {

  @Override
  public void sendMessage(ViewMessage message) {
    if (message instanceof MenuMessage) {
      MenuMessage menuMessage = (MenuMessage) message;

      switch (menuMessage.menuType) {
        case SELECT_GAME:
          System.out.println("Select Game: " + menuMessage.information[0] + " Opponent: "
              + menuMessage.information[2]);
          break;
        case RESIGN_GAME:
          System.out.println("Resign Game: " + menuMessage.information[0] + " Opponent: "
              + menuMessage.information[2]);
          break;
        case INVITES:

          if ( menuMessage.information.length == 0){
            System.out.println("User attempting to send new invite");
          } else {
            System.out.println("Invite ID: " + menuMessage.information[0] + " Accepting: "
                + menuMessage.information[1]);
          }
          break;


      }

    }

    if (message instanceof LoginMessage) {
      LoginMessage loginMessage = (LoginMessage) message;

      System.out.println("Email: " + loginMessage.email + " Password: " + loginMessage.password);

    }

    if (message instanceof RegisterMessage) {
      RegisterMessage registerMessage = (RegisterMessage) message;

      System.out.println("Email: " + registerMessage.email +
          " Password: " + registerMessage.password +
          " Nickname: " + registerMessage.nickname);

    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    throw new IllegalArgumentException("ActiveGame:: Tried to send a action - "
        + e.getActionCommand());

  }
}
