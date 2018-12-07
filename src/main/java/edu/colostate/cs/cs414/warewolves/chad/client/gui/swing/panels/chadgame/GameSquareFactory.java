package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels.chadgame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Factory to create game squares for the Swing GUI board
 */
public class GameSquareFactory {

  /**
   * The only instance of the factory
   */
  private final static GameSquareFactory ourInstance = new GameSquareFactory();

  /**
   * Color for valid move squares
   */
  private static final Color VALID_MOVE_COLOR = new Color(0, 0, 255, 100);
  /**
   * Color for the squares outside the castle
   */
  private static final Color TILE_COLOR = new Color(246, 184, 49, 210);
  /**
   * Color for the walls
   */
  private static final Color WALL_COLOR = new Color(116, 116, 116, 255);
  /**
   * Color for castle floor
   */
  private static final Color CASTLE_COLOR = new Color(196, 145, 46, 255);
  /**
   * Index in the grid for all the walls
   */
  private static final Set<Integer> INDEX_OF_WALLS = Stream.of(
      19, 20, 21, 30, 34, 42, 46, 54, 58, 67, 68, 69, 74, 75, 76, 85,
      89, 97, 101, 109, 113, 122, 123, 124).collect(Collectors.toSet());
  /**
   * Index in the grid for all the castle floors
   */
  private static final Set<Integer> INDEX_OF_CASTLES = Stream.of(
      31, 32, 33, 43, 44, 45, 55, 56, 57, 86, 87, 88,
      98, 99, 100, 110, 111, 112).collect(Collectors.toSet());

  /**
   * Returns the correct type of game square given an index
   * @param index the index to create a square for
   * @param squareSize the size of the square to made
   * @return the created square
   */
  public JPanel createdBoardSquare(int index, int squareSize){
    JPanel square = new JPanel( new BorderLayout());
    square.setName("Squire");
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

  /**
   * Creates a label that is used to show the valid moves
   * this label is not visible by default
   * @param squareSize the size of the label to make
   * @return the valid move label
   */
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

  /**
   * The constructor is private
   */
  private GameSquareFactory() {
    // Make default constructor private
  }

  /**
   * Returns a singleton instance of the factory.
   * @return the instance of the factory
   */
  public static GameSquareFactory getInstance() {
    return ourInstance;
  }


}
