package client.gui.cl;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.ChadPresenter;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CLDriverTest {

  private CLDriver driver;

  @BeforeEach
  void setUp(){
    driver = new CLDriver(new ChadPresenter());
  }

  @Test
  void clearScreen() {
    driver.clearScreen();
  }

  @Test
  void createAndShowGUI() {
    try {
      driver.setKeyboard("3");
      driver.createAndShowGUI();
    } catch(Exception e) {
      e.printStackTrace();
      fail("");
    }
  }

  @Test
  void handleNetMessage() {

  }

  @Test
  void handleViewMessage() {

  }

  @Test
  void handleLogin() {
    try {
      driver.setKeyboard("test1\ntest1");
      driver.handleLogin();
    } catch(NoSuchAlgorithmException e){
      e.printStackTrace();
      fail("");
    }
  }

  @Test
  void handleRegister() {
    try {
      driver.setKeyboard("reg\ntest\nreg");
      driver.handleRegister();
    } catch(NoSuchAlgorithmException e){
      e.printStackTrace();
      fail("");
    }
  }

  @Test
  void handleUnregister() {
    try {
      driver.setKeyboard("test1\ntest1");
      driver.handleUnregister();
    } catch(Exception e){
      e.printStackTrace();
      fail("");
    }
  }

  @Test
  void showActiveGames() {
    int[] ids = {23,12};
    String[] nicks = {"theGameMaster", "AI"};
    boolean[] ts = {true, false};
    boolean[] color = {false, true};

    driver.setKeyboard("23");
    driver.showActiveGames(ids, nicks, ts, color);
    Assertions.assertEquals(23, driver.getGameid());
  }

  @Test
  void setKeyboard() {
    try{
      driver.setKeyboard("12");
    }catch(Exception e) {
      e.printStackTrace();
      fail("");
    }
  }

}