package client.gui.swing;

import client.Point;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Set;
import java.util.TreeSet;
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
      19, 20, 21, 30, 34, 42, 46, 54, 58, 67, 68, 69, 74, 75, 76, 85,
      89, 97, 101, 109, 113, 122, 123, 124).collect(Collectors.toSet());


  private static final Set<Integer> INDEX_OF_CASTLES = Stream.of(
      31, 32, 33, 43, 44, 45, 55, 56, 57, 86, 87, 88,
      98, 99, 100, 110, 111, 112).collect(Collectors.toSet());


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


  private static final Set<Point> WHITE_CASTLE = Stream.of(
      new Point(2, 2), new Point(2, 3), new Point(2, 4),
      new Point(3, 2), new Point(3, 3), new Point(3, 4),
      new Point(4, 2), new Point(4, 3), new Point(4, 4)
  ).collect(Collectors.toSet());

  private static final Set<Point> BLACK_CASTLE = Stream.of(
      new Point(7, 7), new Point(7, 8), new Point(7, 9),
      new Point(8, 7), new Point(8, 8), new Point(8, 9),
      new Point(9, 7), new Point(9, 8), new Point(9, 9)
  ).collect(Collectors.toSet());


  private GameSquareFactory() {
    // Make default constructor private
  }

  public static GameSquareFactory getInstance() {
    return ourInstance;
  }


}
