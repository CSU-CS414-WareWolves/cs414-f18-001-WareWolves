package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProfileRequestTest {
	private static ProfileRequest testProfileRequest1, testProfileRequest2;

	public final String testGameRequestString = "17:testUser";

	@BeforeAll
	public static void setup() {
		testProfileRequest1 = new ProfileRequest("testUser");
		testProfileRequest2 = new ProfileRequest("17:testUser", 0);
	}

	@Test
	public void getDataString() {
		assertEquals(testGameRequestString, testProfileRequest1.getDataString());
	}

	@Test
	public void getDataString2() {
		assertEquals(testGameRequestString, testProfileRequest2.getDataString());
	}
}
