package client.gui.cl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CLLoginTest {

  private CLLogin login;

  @BeforeEach
  void setUp() {
    login = new CLLogin();
  }

  @Test
  void showSplash() {
    try {
      login.showSplash();
    } catch(Exception e) {
      e.printStackTrace();
      fail("");
    }
  }

  @Test
  void showLogin() {
    try {
      login.showLogin();
    } catch(Exception e) {
      e.printStackTrace();
      fail("");
    }
  }

  @ParameterizedTest
  @CsvSource({"1","0"})
  void failedCreds(int option) {
    try {
      login.failedCreds(option);
    } catch(Exception e) {
      e.printStackTrace();
      fail("");
    }
  }

  @Test
  void showLogout() {
    try {
      login.showLogout();
    } catch(Exception e) {
      e.printStackTrace();
      fail("");
    }
  }
}