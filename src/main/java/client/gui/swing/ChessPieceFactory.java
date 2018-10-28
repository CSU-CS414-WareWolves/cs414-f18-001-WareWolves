package client.gui.swing;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChessPieceFactory {

  private static ImageIcon BLACK_KING;
  private static ImageIcon BLACK_QUEEN;
  private static ImageIcon BLACK_ROOK;

  private static final String BLACK_KING_FILE = "Chess_kdt60.png";
  private static final String BLACK_QUEEN_FILE = "Chess_qdt60.png";
  private static final String BLACK_ROOK_FILE = "Chess_rdt60.png";

  private static final char BLACK_KING_LABEL = 'K';
  private static final char BLACK_QUEEN_LABEL = 'Q';
  private static final char BLACK_ROOK_LABEL = 'R';

  private static ImageIcon WHITE_KING;
  private static ImageIcon WHITE_QUEEN;
  private static ImageIcon WHITE_ROOK;

  private static final String WHITE_KING_FILE = "Chess_klt60.png";
  private static final String WHITE_QUEEN_FILE = "Chess_qlt60.png";
  private static final String WHITE_ROOK_FILE = "Chess_rlt60.png";

  private static final char WHITE_KING_LABEL = 'k';
  private static final char WHITE_QUEEN_LABEL = 'q';
  private static final char WHITE_ROOK_LABEL = 'r';

  private static ChessPieceFactory ourInstance = new ChessPieceFactory();

  public static ChessPieceFactory getInstance() {
    return ourInstance;
  }

  private ChessPieceFactory() {

    BLACK_KING = new ImageIcon(getClass().getClassLoader().getResource(BLACK_KING_FILE).getFile());
    BLACK_QUEEN = new ImageIcon(getClass().getClassLoader().getResource(BLACK_QUEEN_FILE).getFile());
    BLACK_ROOK = new ImageIcon(getClass().getClassLoader().getResource(BLACK_ROOK_FILE).getFile());

    WHITE_KING = new ImageIcon(getClass().getClassLoader().getResource(WHITE_KING_FILE).getFile());
    WHITE_QUEEN = new ImageIcon(getClass().getClassLoader().getResource(WHITE_QUEEN_FILE).getFile());
    WHITE_ROOK = new ImageIcon(getClass().getClassLoader().getResource(WHITE_ROOK_FILE).getFile());

  }

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

  private JLabel createPiece(ImageIcon pieceIcon){
    JLabel piece = new JLabel(pieceIcon);
    piece.setName("Piece");
    return piece;
  }

}
