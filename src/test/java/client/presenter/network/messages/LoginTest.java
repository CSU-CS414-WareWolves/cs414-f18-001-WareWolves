package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class LoginTest {

  private static Login testLogin;

  private final String testUserString = "TestUser:TestPassword:/192.168.1.1:12345";

  @BeforeAll
  static void setup() throws UnknownHostException {
    testLogin = new Login("TestUser", "TestPassword", InetAddress.getByName("192.168.1.1"), 12345);


  }


  @Test
  void getDataString() {
    assertEquals(testUserString, testLogin.getDataString());

  }
}