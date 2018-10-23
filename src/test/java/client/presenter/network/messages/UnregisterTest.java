package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UnregisterTest {
	private static Unregister testRegister1, testRegister2;

	public final String testRegisterString = "5:me@example.com:testUser:231314";

	@BeforeAll
	public static void setup() {
		testRegister1 = new Unregister("me@example.com", "testUser", "231314");
		testRegister2 = new Unregister("5:me@example.com:testUser:231314");
	}

	@Test
	public void getDataString() {
		assertEquals(testRegisterString, testRegister1.getDataString());
	}

	@Test
	public void getDataString2() {
		assertEquals(testRegisterString, testRegister2.getDataString());
	}
}
