package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProfileResponseTest {
	private static ProfileResponse testResponse1, testResponse2;
	
	public final String testResponseString = "18:testUser2:testUser:01-01-18:01-01-18:true#testUser:testUser2:02-14-18:02-14-18:false";
	
	  @BeforeAll
	  public static void setup() {
		  String[] whitePlayer = {"testUser2", "testUser"};
		  String[] blackPlayer = {"testUser", "testUser2"};
		  String[] dates = {"01-01-18", "02-14-18"};
		  boolean[] results = {true, false};
		  testResponse1 = new ProfileResponse(whitePlayer, blackPlayer, dates, dates, results);
		  testResponse2 = new ProfileResponse("18:testUser2:testUser:01-01-18:01-01-18:true#testUser:testUser2:02-14-18:02-14-18:false");
	  }

	  @Test
	  public void getDataString() {
	    assertEquals(testResponseString, testResponse1.getDataString());
	  }
	  
	  @Test
	  public void getDataString2() {
	    assertEquals(testResponseString, testResponse2.getDataString());
	  }
}
