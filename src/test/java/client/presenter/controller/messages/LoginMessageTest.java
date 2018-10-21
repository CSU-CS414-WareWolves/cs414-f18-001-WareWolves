package client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class LoginMessageTest {

  private static final String loginEmail = "test@cs.csu.edu";
  private static final String loginPassword = "IamABadpassword";

  private static LoginMessage testMessage;

  @BeforeEach
  public void setup(){
    testMessage = new LoginMessage(loginEmail, loginPassword);
  }

  @Test
  public void testType(){
    assertEquals(ViewMessageType.LOGIN, testMessage.messageType);
  }

  @Test
  public void testPassword(){
    assertEquals(loginPassword, testMessage.password);
  }

  @Test
  public void testEmail(){
    assertEquals(loginEmail, testMessage.email);
  }

}