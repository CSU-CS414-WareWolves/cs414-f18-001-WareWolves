package client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class MenuMessageTest {


  @DisplayName("testMenuTypes")
  @ParameterizedTest(name = "({0}) should be {0}")
  @EnumSource(
      value = MenuMessageTypes.class,
      names = {"LOGOUT", "PLAYER_STATS", "ACTIVE_GAMES", "INVITES", "SELECT_GAME", "SEND_INVITE"})

  public void testMenuTypes(MenuMessageTypes menuMessageTypes) {
    assertEquals(menuMessageTypes, new MenuMessage(menuMessageTypes, new String[0]).menuType);
  }

  @Test
  public void testType() {
    assertEquals(ViewMessageType.MENU,
        new MenuMessage(MenuMessageTypes.LOGOUT, new String[0]).messageType);
  }
}