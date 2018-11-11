package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UnregisterResponseTest {
	private static UnregisterResponse testUnregisterResponse1, testUnregisterResponse2;

	public final String testRegisterResponseString = "20:true";

	@BeforeAll
	public static void setup() {
		testUnregisterResponse1 = new UnregisterResponse(true);
		testUnregisterResponse2 = new UnregisterResponse("20:true");
	}

	@Test
	public void getDataString() {
		assertEquals(testRegisterResponseString, testUnregisterResponse1.getDataString());
	}

	@Test
	public void getDataString2() {
		assertEquals(testRegisterResponseString, testUnregisterResponse2.getDataString());
	}
}
