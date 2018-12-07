package edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MoveTest {
	private static Move testMove1, testMove2;

	public final String testMoveString = "8:123:raAdA:rcCrcDrcErdCkdDRhHRhIRhJRiHKiIRiJRjHRjIRjJ:false:false";

	@BeforeAll
	public static void setup() {
		testMove1 = new Move(123, "raAdA", "rcCrcDrcErdCkdDRhHRhIRhJRiHKiIRiJRjHRjIRjJ", false, false);
		testMove2 = new Move("8:123:raAdA:rcCrcDrcErdCkdDRhHRhIRhJRiHKiIRiJRjHRjIRjJ:false:false");
	}

	@Test
	public void getDataString() {
		assertEquals(testMoveString, testMove1.getDataString());
	}

	@Test
	public void getDataString2() {
		assertEquals(testMoveString, testMove2.getDataString());
	}
}
