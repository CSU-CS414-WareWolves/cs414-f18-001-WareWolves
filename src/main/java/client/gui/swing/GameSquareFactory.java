package client.gui.swing;

import client.game.Point;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameSquareFactory {

  private final static GameSquareFactory ourInstance = new GameSquareFactory();

  private static final Color VALID_MOVE_COLOR = new Color(0, 0, 255, 100);
  private static final Color TILE_COLOR = new Color(246, 184, 49, 210);
  private static final Color WALL_COLOR = new Color(116, 116, 116, 255);
  private static final Color CASTLE_COLOR = new Color(196, 145, 46, 255);


  private static final Set<Integer> INDEX_OF_WALLS = Stream.of(
      14, 15, 16, 25, 29, 37, 41, 49, 53,
      62, 63, 64, 79, 80, 81, 90, 94, 102,
      106, 114, 118, 127, 128, 129).collect(Collectors.toSet());

  private static final Set<Integer> INDEX_OF_CASTLES = Stream.of(
      26, 27, 28, 38, 39, 40, 50, 51, 52,
      91, 92, 93, 103, 104, 105, 115, 116,
      117).collect(Collectors.toSet());


  public JPanel createdBoardSquare(int index, int squareSize){
    JPanel square = new JPanel( new BorderLayout());
    square.setBorder(BorderFactory.createLineBorder(Color.black));

    // Color the tile
    if(INDEX_OF_WALLS.contains(index)){
      square.setBackground(WALL_COLOR);
    } else if(INDEX_OF_CASTLES.contains(index)){
      square.setBackground(CASTLE_COLOR);
    } else {
      square.setBackground(TILE_COLOR);
    }

    // Add the valid move component
    square.add(createValidMoveLabel(squareSize));
    return square;
  }


  private JLabel createValidMoveLabel(int squareSize) {
    JLabel label = new JLabel();
    label.setName("ValidMove");
    label.setVerticalAlignment(JLabel.TOP);
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setOpaque(true);
    label.setBackground(VALID_MOVE_COLOR);
    label.setBounds(0, 0, squareSize, squareSize);
    label.setBorder(BorderFactory.createLineBorder(Color.black));
    label.setVisible(false);
    return label;
  }


  private static final Set<Point> WALLS = Stream.of(
      new Point(1, 2), new Point(1, 3), new Point(1, 4),
      new Point(2, 1), new Point(3, 1), new Point(4, 1),
      new Point(5, 2), new Point(5, 3), new Point(5, 4),
      new Point(2, 5), new Point(3, 5), new Point(4, 5),

      new Point(6, 7), new Point(6, 8), new Point(6, 9),
      new Point(10, 7), new Point(10, 8), new Point(10, 9),
      new Point(7, 6), new Point(8, 6), new Point(9, 6),
      new Point(7, 10), new Point(8, 10), new Point(9, 10)
  ).collect(Collectors.toSet());


  private GameSquareFactory() {
    // Make default constructor private
  }

  public static GameSquareFactory getInstance() {
    return ourInstance;
  }

  public static void main(String[] args){

    for (Point p : WALLS) {
      System.out.println((p.getArrayCol() + p.getArrayRow()*12));
    }

  }

}
