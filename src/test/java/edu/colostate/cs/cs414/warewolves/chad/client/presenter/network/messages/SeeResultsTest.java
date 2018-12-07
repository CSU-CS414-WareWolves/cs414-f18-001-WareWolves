package edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SeeResultsTest {
	  private static SeeResults testSeeResults1, testSeeResults2;
	  private final String testString = "21:1:false";

		@BeforeAll
		public static void setup() {
			testSeeResults1 = new SeeResults(1, false);
			testSeeResults2 = new SeeResults("21:1:false");
		}

		@Test
		public void getDataString() {
			assertEquals(testString, testSeeResults1.getDataString());
		}

		@Test
		public void getDataString2() {
			assertEquals(testString, testSeeResults2.getDataString());
		}
}
