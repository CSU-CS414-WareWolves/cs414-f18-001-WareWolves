package client.gui.cl;

import client.game.GameBoard;
import client.game.pieces.King;
import client.game.pieces.Piece;
import client.game.pieces.Queen;
import client.game.pieces.Rook;
import client.gui.GameView;
import java.util.ArrayList;

public class CLGameView {

  // Piece representations
  private static final String WKING = "\u2654";
  private static final String WQUEEN = "\u2655";
  private static final String WROOK = "\u2656";
  private static final String BKING = "\u265A";
  private static final String BQUEEN = "\u265B";
  private static final String BROOK = "\u265C";

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
    char row = 'a';
    StringBuilder res = new StringBuilder();
    for(int i=0; i<=11; ++i){
      res.append(row++).append(" {");
        for (int j=0; j<12; ++j) {
          //check for walls, represent with ▣ = "\u25A3"
          if(checkWall(i,j)){
            res.append(" ").append("\u25A3");
          }
          else {
            res.append(" ").append(pieceToCharacter(gb.getPieceAt(i, j)));
          }
          if(j==11){
            res.append(" }\n");
          }
        }
    }
    res.append("  { L K J I H G F E D C B A}");
    System.out.println(res);
  }

  boolean checkWall(int row, int col){
    return (row==6 &&(col==2 || col==3 || col==4))
        || (row==10 &&(col==2 || col==3 || col==4))
        || (col==6 &&(row==2 || row==3 || row==4))
        || (col==10 &&(row==2 || row==3 || row==4))

        || (row==1 &&(col==7 || col==8 || col==9))
        || (row==5 &&(col==7 || col==8 || col==9))
        || (col==1 &&(row==7 || row==8 || row==9))
        || (col==5 &&(row==7 || row==8 || row==9));
  }

  /**
   * Returns the valid ASCII character for the chess piece
   * @param p An instance of a Piece
   * @return ASCII representation of Piece
   */
  String pieceToCharacter(Piece p){
    if (p == null)
      return "\u2610";
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

    for(int i=0; i<list.length; i++){
      res.append(list[i].toString()).append(", ");
    }
    res.append("\n");

    System.out.println(res);
  }

  private String playerFromBool(boolean p) {
    return p ? "white" : "black";
  }

  /**
   * Print in-game menu for player's benefit
   */
  void showInGameMenu(){
    System.out.println("~[ Type EXIT to leave ]~[ Type RESIGN to forfeit ]~\n");
  }

  public void showGameover(boolean p) {
    System.out.println(playerFromBool(p)+" player won the game!");
  }
}