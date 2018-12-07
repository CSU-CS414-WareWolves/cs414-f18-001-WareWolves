package edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages;

import static edu.colostate.cs.cs414.warewolves.chad.client.presenter.SharedTestAttributes.*;
import static org.junit.jupiter.api.Assertions.*;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.ViewMessageType;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnregisterMessageTest {

  private static UnregisterMessage testMessage;

  @BeforeEach
  public void setup() throws NoSuchAlgorithmException {
    testMessage = new UnregisterMessage(TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD, TEST_NICKNAME);
  }

  @Test
  public void testType(){
    assertEquals(ViewMessageType.UNREGISTER, testMessage.messageType);
  }

  @Test
  public void testPassword(){
    assertEquals(TEST_HASHED_PASSWORD, testMessage.password);
  }

  @Test
  public void testEmail(){
    assertEquals(TEST_LOGIN_EMAIL, testMessage.email);
  }

  @Test
  public void testNickname(){ assertEquals(TEST_NICKNAME, testMessage.nickname);
  }

  @Test
  public void testNotEqualNull(){
    assertNotEquals( testMessage, null);
  }

  @Test
  public void testHashCode() throws NoSuchAlgorithmException {
    UnregisterMessage testHash =
        new UnregisterMessage(TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD, TEST_NICKNAME);
    assertEquals(testMessage.hashCode(), testHash.hashCode());
  }


}