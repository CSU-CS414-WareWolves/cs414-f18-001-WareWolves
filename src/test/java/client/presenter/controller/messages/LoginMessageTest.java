package client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.ViewMessageType;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class LoginMessageTest {

  private static final String loginEmail = "test@cs.csu.edu";
  private static final String loginPassword = "IamABadpassword";
  private static final String hashedPassword = "5f4f8b99d5cd5ccd665ae5d57296b16cad7aaadc";

  private static LoginMessage testMessage;

  @BeforeEach
  public void setup() throws NoSuchAlgorithmException {
    testMessage = new LoginMessage(loginEmail, loginPassword);
  }

  @Test
  public void testType(){
    assertEquals(ViewMessageType.LOGIN, testMessage.messageType);
  }

  @Test
  public void testPassword(){
    assertEquals(hashedPassword, testMessage.password);
  }

  @Test
  public void testEmail(){
    assertEquals(loginEmail, testMessage.email);
  }

}