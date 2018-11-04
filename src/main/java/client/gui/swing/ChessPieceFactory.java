package client.gui.swing;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Factory for making game pieces for the Swing GUI
 */
public class ChessPieceFactory {

  /**
   * The icon for the Black King
   */
  private static ImageIcon BLACK_KING;
  /**
   * The icon for the Black Queen
   */
  private static ImageIcon BLACK_QUEEN;
  /**
   * The icon for the Black Rook
   */
  private static ImageIcon BLACK_ROOK;
  /**
   * The file name for the Black King
   */
  private static final String BLACK_KING_FILE = "Chess_kdt60.png";
  /**
   * The file name for the Black Queen
   */
  private static final String BLACK_QUEEN_FILE = "Chess_qdt60.png";
  /**
   * The file name for the Black Rook
   */
  private static final String BLACK_ROOK_FILE = "Chess_rdt60.png";
  /**
   * The char used to represent the Black King
   */
  private static final char BLACK_KING_LABEL = 'K';
  /**
   * The char used to represent the Black Queen
   */
  private static final char BLACK_QUEEN_LABEL = 'Q';
  /**
   * The char used to represent the Black Rook
   */
  private static final char BLACK_ROOK_LABEL = 'R';

  /**
   * The icon for the White King
   */
  private static ImageIcon WHITE_KING;
  /**
   * The icon for the White Queen
   */
  private static ImageIcon WHITE_QUEEN;
  /**
   * The icon for the White Rook
   */
  private static ImageIcon WHITE_ROOK;
  /**
   * The file name for the White King
   */
  private static final String WHITE_KING_FILE = "Chess_klt60.png";
  /**
   * The file name for the White Rook
   */
  private static final String WHITE_QUEEN_FILE = "Chess_qlt60.png";
  /**
   * The file name for the White Rook
   */
  private static final String WHITE_ROOK_FILE = "Chess_rlt60.png";

  /**
   * The char used to represent the White King
   */
  private static final char WHITE_KING_LABEL = 'k';
  /**
   * The char used to represent the White Rook
   */
  private static final char WHITE_QUEEN_LABEL = 'q';
  /**
   * The char used to represent the White Rook
   */
  private static final char WHITE_ROOK_LABEL = 'r';

  /**
   * The only instance of the factory
   */
  private static ChessPieceFactory ourInstance = new ChessPieceFactory();

  /**
   * Returns a singleton instance of the factory.
   * @return the instance of the factory
   */
  public static ChessPieceFactory getInstance() {
    return ourInstance;
  }

  /**
   * Reads all the piece icons from the resources folder
   */
  private ChessPieceFactory() {

    BLACK_KING = new ImageIcon(getClass().getClassLoader().getResource(BLACK_KING_FILE).getFile());
    BLACK_QUEEN = new ImageIcon(getClass().getClassLoader().getResource(BLACK_QUEEN_FILE).getFile());
    BLACK_ROOK = new ImageIcon(getClass().getClassLoader().getResource(BLACK_ROOK_FILE).getFile());

    WHITE_KING = new ImageIcon(getClass().getClassLoader().getResource(WHITE_KING_FILE).getFile());
    WHITE_QUEEN = new ImageIcon(getClass().getClassLoader().getResource(WHITE_QUEEN_FILE).getFile());
    WHITE_ROOK = new ImageIcon(getClass().getClassLoader().getResource(WHITE_ROOK_FILE).getFile());

  }

  /**
   * Reads a char that represents the piece and returns the icon for the piece
   * @param piece the piece to create
   * @return the piece icon
   */
  public JLabel getPiece(char piece){
    switch (piece){
      case (BLACK_KING_LABEL):
        return createPiece(BLACK_KING);
      case (BLACK_QUEEN_LABEL):
        return createPiece(BLACK_QUEEN);
      case (BLACK_ROOK_LABEL):
        return createPiece(BLACK_ROOK);
      case (WHITE_KING_LABEL):
        return createPiece(WHITE_KING);
      case (WHITE_QUEEN_LABEL):
        return createPiece(WHITE_QUEEN);
      case (WHITE_ROOK_LABEL):
        return createPiece(WHITE_ROOK);
      default:
        throw new IllegalArgumentException("Piece code:" + piece + " is not a valid piece type");
    }
  }

  /**
   * Creates a label for the piece
   * @param pieceIcon the Icon to use
   * @return the piece label
   */
  private JLabel createPiece(ImageIcon pieceIcon){
    JLabel piece = new JLabel(pieceIcon);
    piece.setName("Piece");
    return piece;
  }
}
