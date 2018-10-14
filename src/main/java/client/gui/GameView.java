package client.gui;

public interface GameView {
  //
  public void getGameState();

  //
  void getValidMove(Point p);

  //
  void makeMove(Point from, Point to);
}
