package edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PlayersTest {
	
	private static Players testPlayers1, testPlayers2;

	private final String testUserString = "19:testUser:testUser2:testUser3";

	@BeforeAll
	public static void setup() {
		String[] players = {"testUser", "testUser2", "testUser3"};
		testPlayers1 = new Players(players);
		testPlayers2 = new Players("19:testUser:testUser2:testUser3");
	}

	@Test
	public void getDataString() {
		assertEquals(testUserString, testPlayers1.getDataString());
	}

	@Test
	public void getDataString2() {
		assertEquals(testUserString, testPlayers2.getDataString());
	}
}
