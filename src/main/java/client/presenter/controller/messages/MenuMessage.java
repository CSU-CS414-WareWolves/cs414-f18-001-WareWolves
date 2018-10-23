package client.presenter.controller.messages;

import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.ViewMessageType;

/**
 * This class handles all the messages that involve menu action
 */
public class MenuMessage extends ViewMessage{

  /**
   * The type of menu action
   */
  public final MenuMessageTypes menuType;
  /**
   * The information for the menu action. I can very in size depending on the menu
   */
  public final String[] information;

  /**
   * Sets the menu type and the information for the message
   * @param menuType the menu type
   * @param information the info
   */
  public MenuMessage(MenuMessageTypes menuType, String[] information){
    super(ViewMessageType.MENU);
    this.menuType = menuType;
    this.information = information;

  }

}
