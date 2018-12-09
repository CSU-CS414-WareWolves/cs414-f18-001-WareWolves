package client.gui.cl;

import edu.colostate.cs.cs414.warewolves.chad.client.Point;
import edu.colostate.cs.cs414.warewolves.chad.client.game.GameBoard;
import edu.colostate.cs.cs414.warewolves.chad.client.game.pieces.*;

public class CLGameView {

  // Piece representations
  private static final String WKING = "\u2654";
  private static final String WQUEEN = "\u2655";
  private static final String WROOK = "\u2656";
  private static final String BKING = "\u265A";
  private static final String BQUEEN = "\u265B";
  private static final String BROOK = "\u265C";
  private static final String TILE = "\u2610";
  private static final String WALL = "\u25A3";

  /**
   * Prints all current games of the user
   * @param ids list of ids of the active games
   * @param players list of nicknames the player has an active game with
   */
  void showCurrentGames(int[] ids, String[] players, boolean[] turns, boolean[] color){
    //Print the list in a nice fashion
    StringBuilder res = new StringBuilder();

    res.append("+++ Select the Game you wish to continue +++\n");
    res.append("Enter 0 to return to previous menu\n");
    for(int i=0; i<players.length; i++){
      res.append("[").append(ids[i]).append("]: ");
      res.append(players[i]);
      res.append(" - Current turn: ").append(playerFromBool(turns[i]));
      res.append(" - Your color : ").append(playerFromBool(color[i]));
      res.append("\n");
    }

    System.out.println(res);
  }

  /**
   * Prints the current state of the board
   * @param board a String representation from a GameBoard instance
   */
  void showGameBoard(String board) {
    GameBoard gb = new GameBoard(board);
    System.out.println(gb);

    String[][] newBoard = new String[12][12];
    for(int row = 11; row >= 0; row--) {
      for(int col = 11; col >= 0; col--) {
        if(isWall(row, col)){
          newBoard[row][col] = WALL + " ";
        }
        else {
          Piece pc = gb.getPieceAt(row,col);
          if(pc == null) {
            newBoard[row][col] = TILE + " ";
          }
          else {
            newBoard[row][col] = pieceToCharacter(pc) + " ";
          }
        }
      }
    }
    printGameBoard(newBoard);
  }

  private void printGameBoard(String[][] array) {
    StringBuilder res = new StringBuilder();
    char rowLetter = 'l';

    res.append("\n~[Type EXIT to leave]~[Type RESIGN to forfeit]~\n");
    int i=0;
    for(String[] row : array) {
      res.append(i++).append("       ").append(rowLetter--).append(" {");
      for(String p : row) {
        res.append(p);
      }
      res.append("}\n");
    }
    res.append("         { A B C D  E F  G H I  J K  L}\n");
    System.out.println(res);
  }

  private boolean isWall(int row, int col) {
    return (row==6 &&(col==2 || col==3 || col==4))
        || (row==10 &&(col==2 || col==3 || col==4))
        || (col==6 &&(row==2 || row==3 || row==4))
        || (col==10 &&(row==2 || row==3 || row==4))

        || (row==1 &&(col==7 || col==8 || col==9))
        || (row==5 &&(col==7 || col==8 || col==9))
        || (col==1 &&(row==7 || row==8 || row==9))
        || (col==5 &&(row==7 || row==8 || row==9));
  }

  Point convertLocation(Point p) {

    return new Point(0,0);
  }

  /**
   * Returns the valid ASCII character for the chess piece
   * @param p An instance of a Piece
   * @return ASCII representation of Piece
   */
  String pieceToCharacter(Piece p){
    if (p.getClass() == Rook.class){
      return p.getColor() ? BROOK : WROOK;
    }
    if (p.getClass() == King.class){
      return p.getColor() ? BKING : WKING;
    }
    if (p.getClass() == Queen.class){
      return p.getColor() ? BQUEEN : WQUEEN;
    }
    throw new IllegalArgumentException("Piece must be a Rook, Queen, King, or null. Was a: " + p.getClass());
  }

  /**
   * Prints the valid moves for the current player
   * @param list A collection of Points
   */
  void showValidMoves(String[] list){
    //Print the list in a nice fashion
    StringBuilder res = new StringBuilder();

    res.append("~ Valid moves for piece: ");
    for (String move : list) {
      res.append(move).append(", ");
    }
    res.append("\n");

    System.out.println(res);
  }

  private String playerFromBool(boolean p) {
    return p ? "white" : "black";
  }

  public static void main(String[] args) {
    CLGameView g = new CLGameView();
    g.showGameBoard("rdCreDRiHRjIrcCkdDreERhHKiIRjJrcDrdERhIRiJrcERhJreCRjH");
  }
}
