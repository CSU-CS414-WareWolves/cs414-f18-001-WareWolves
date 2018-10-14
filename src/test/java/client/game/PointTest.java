package client.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PointTest {

  @Test
  void TestConstructor1(){
    Point p = new Point(0,0);
    assertEquals(0, p.getCol(), "Column was set wrong");
    assertEquals(0, p.getRow(), "Row was set wrong");
  }

  @Test
  void TestConstructor2(){
    Point p = new Point('a',0);
    assertEquals(0, p.getCol(), "Column was set wrong");
    assertEquals(0, p.getRow(), "Row was set wrong");
  }

  @Test
  void TestConstructor3(){
    Point p = new Point("a0");
    assertEquals(0, p.getCol(), "Column was set wrong");
    assertEquals(0, p.getRow(), "Row was set wrong");
  }

  @Test
  void TestToString() {
    Point p = new Point(0,0);
    assertEquals("a0", p.toString());
  }
}