package client.game;

import client.game.pieces.*;

public class GameBoard {
  private Piece[][] board;

  public GameBoard(String pieces){
    board = new Piece[12][12];
    for (int i = 0; i < pieces.length(); i = i + 3){
      String type = pieces.substring(i,i+1);
      Point pos = new Point(pieces.substring(i+1, i+3));
      //System.out.println(type + " " + pos);
      board[pos.getCol()][pos.getRow()] = makePiece(type, pos);
      //System.out.println(board[pos.getCol()][pos.getRow()]);
    }
  }

  /**
   * Creates the correct type of Piece from a Point and a type.
   * @param type [RrQqKk] representing the color and type of the Piece to be created.
   * @param pos Point of where the Piece exists on a GameBoard.
   * @return The new Piece.
   */
  private Piece makePiece(String type, Point pos){
    boolean color = "RQK".contains(type);
    if ("Rr".contains(type)){//Rook
      return new Rook(pos, color);
    }
    if ("Qq".contains(type)){//Queen
      return new Queen(pos, color);
    }
    if ("Kk".contains(type)) {//King
      return new King(pos, color);
    }
    throw new IllegalArgumentException("Piece type must be one of: [RrQqKk]; got: " + type);
  }

  /**
   * Get a piece from (col, row) on this GameBoard.
   * @param col Column to get the Piece from.
   * @param row Row to get the Piece from.
   * @return A Piece at (col, row) on this GameBoard, null if no Piece exists there.
   */
  public Piece getPieceAt(int col, int row){
    return board[col][row];
  }

  /**
   * Get a Piece form the given Point on this GameBoard.
   * @param tile The Point to get a piece from.
   * @return A Piece if there is a piece at the Point, null otherwise.
   */
  public Piece getPieceAt(Point tile){
    return this.getPieceAt(tile.getCol(), tile.getRow());
  }

  //TODO: implement MovePiece
  public boolean MovePiece(Point from, Point to){
    return false;
  }

  /**
   * Get a letter representation of a Piece, for use in toString.
   * @param p The Piece to stringify
   * @return One of [RrQqKk-], based on the Piece.
   */
  private String pieceToLetter(Piece p){
    if (p == null)
      return "-";
    if (p.getClass() == Rook.class){
      return p.getColor() ? "R" : "r";
    }
    if (p.getClass() == King.class){
      return p.getColor() ? "K" : "k";
    }
    if (p.getClass() == Queen.class){
      return p.getColor() ? "Q" : "q";
    }
    throw new IllegalArgumentException("Piece must be a Rook, Queen, King, or null. Was a: " + p.getClass());

  }

  @Override
  public String toString() {
    StringBuilder res = new StringBuilder();
    for (int i = 11; i >=0; --i){
      Piece[] r = board[i];
      for (Piece p : r){
        res.append(pieceToLetter(p)).append(" ");
      }
      res.append("\n");
    }
    return res.toString();
  }
}
