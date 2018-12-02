package client.gui.cl;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.ViewMessage;
import java.security.NoSuchAlgorithmException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CLDriverTest {

  private CLDriver driver;

  private CLLogin login;
  private CLMenu menu;
  private CLGameView game;

  @BeforeEach
  void setUp(){
    login = new CLLogin();
    menu = new CLMenu();
    game = new CLGameView();
    driver = new CLDriver(login, menu, game);
  }

  @Test
  void getLogin() {
    CLLogin temp = driver.getLogin();
    Assert.assertTrue(temp.equals(login));
  }

  @Test
  void getMenu() {
    CLMenu temp = driver.getMenu();
    Assert.assertTrue(temp.equals(menu));
  }

  @Test
  void getGame() {
    CLGameView temp = driver.getGame();
    Assert.assertTrue(temp.equals(game));
  }

  @Test
  void clearScreen() {
    driver.clearScreen();
  }

  @Test
  void createAndShowGUI() {
    fail("");
  }

  @Test
  void handleNetworkMessage() {
    fail("");
  }

  @Test
  void handleViewMessage() {
    fail("");
  }

  @Test
  void handleLogin() {
    fail("");
  }

  @Test
  void handleRegister() {
    fail("");
  }

  @Test
  void handleUnregister() {
    fail("");
  }

  @Test
  void handleActiveGames() {
    fail("");
  }

  @Test
  void handleInGame() {
    fail("");
  }

  @Test
  void handleMovePiece() {
    fail("");
  }

  @Test
  void handleGameQuit() {
    fail("");
  }

  @Test
  void handleGameResign() {
    fail("");
  }

  @Test
  void showGame() {
    fail("");
  }

  @Test
  void handleInbox() {
    fail("");
  }

  @Test
  void handleOutbox() {
    fail("");
  }

}