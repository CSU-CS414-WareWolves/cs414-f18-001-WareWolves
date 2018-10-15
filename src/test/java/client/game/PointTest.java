package client.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PointTest {

  @Test
  void testConstructor1(){
    Point p = new Point(0,0);
    assertEquals(0, p.getCol(), "Column was set wrong");
    assertEquals(0, p.getRow(), "Row was set wrong");
  }

  @Test
  void testConstructor2(){
    Point p = new Point('a',0);
    assertEquals(0, p.getCol(), "Column was set wrong");
    assertEquals(0, p.getRow(), "Row was set wrong");
  }

  @Test
  void testConstructor3(){
    Point p = new Point("a0");
    assertEquals(0, p.getCol(), "Column was set wrong");
    assertEquals(0, p.getRow(), "Row was set wrong");
  }

  @Test
  void testConstructor99(){
    Point p = new Point("j9");
    assertEquals(9, p.getCol(), "Column was set wrong");
    assertEquals(9, p.getRow(), "Row was set wrong");
  }

  @Test
  void testToString() {
    Point p = new Point(0,0);
    assertEquals("a0", p.toString());
  }
}