package client.presenter.controller.util;

import client.presenter.controller.ViewMessageType;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.MovePieceMessage;
import client.presenter.controller.messages.RegisterMessage;
import client.presenter.controller.messages.UnregisterMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.controller.messages.ViewValidMoves;
import java.security.NoSuchAlgorithmException;

public class ViewMessageFactory {

  private static ViewMessageFactory ourInstance = new ViewMessageFactory();

  public static ViewMessageFactory getInstance() {
    return ourInstance;
  }

  private ViewMessageFactory() {
  }

  public ViewMessage createViewMessage(ViewMessageType type, String[] info)
      throws NoSuchAlgorithmException {
    switch (type){
      case REGISTER:
        return new RegisterMessage(info[0], info[1], info[2]);
      case LOGIN:
        return new LoginMessage(info[0], info[1]);
      case UNREGISTER:
        return new UnregisterMessage(info[0], info[1], info[2]);
      case SHOW_VALID_MOVES:
        return new ViewValidMoves(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
      case MENU:
        //not implemented;
      case MOVE_PIECE:
        return new MovePieceMessage(Integer.parseInt(info[0]), Integer.parseInt(info[1]),
            Integer.parseInt(info[2]), Integer.parseInt(info[3]));
      case REGISTER_RESPONSE:
        //not implemented;
      case LOGIN_RESPONSE:
        //not implemented;
      case UNREGISTER_RESPONSE:
        //not implemented;
      case SHOW_VALID_MOVES_RESPONSE:
        //not implemented;
      case MENU_RESPONSE:
        //not implemented;
      case MOVE_PIECE_RESPONSE:
        //not implemented;
      default:
        throw new IllegalArgumentException("The messageType of " + type.name() + " is not valid");
    }

  }
}
