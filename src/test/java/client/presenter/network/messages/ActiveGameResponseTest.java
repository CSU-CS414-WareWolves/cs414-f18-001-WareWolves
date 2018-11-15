package client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ActiveGameResponseTest {
	private static ActiveGameResponse testResponse1, testResponse2;
	
	public final String testResponseString = "10:123:RaaQllrbb:testUser:01-01-18:true:true:false#1234:QbbqccRdd:testUser2:02-14-18:false:false:false";
	
	  @BeforeAll
	  public static void setup() {
		  int[] gameIDs = {123, 1234};
		  String[] gameBoard = {"RaaQllrbb","QbbqccRdd"};
		  String[] opponents = {"testUser", "testUser2"};
		  String[] dates = {"01-01-18", "02-14-18"};
		  boolean[] turns = {true, false};
		  boolean[] colors = {true, false};
		  boolean[] ended = {false, false};
		  testResponse1 = new ActiveGameResponse(gameIDs, gameBoard, opponents, dates, turns, colors, ended);
		  testResponse2 = new ActiveGameResponse("10:123:RaaQllrbb:testUser:01-01-18:true:true:false#1234:QbbqccRdd:testUser2:02-14-18:false:false:false");
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
