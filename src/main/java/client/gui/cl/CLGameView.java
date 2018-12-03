package client.gui.cl;

import client.game.GameBoard;
import client.game.pieces.King;
import client.game.pieces.Piece;
import client.game.pieces.Queen;
import client.game.pieces.Rook;
import client.gui.GameView;
import java.util.ArrayList;

public class CLGameView implements GameView {

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
  public void showCurrentGames(int[] ids, String[] players){
    //Print the list in a nice fashion
    StringBuilder res = new StringBuilder();

    res.append("+++ Select the Game you wish to continue +++\n\n");
    for(int i=0; i<players.length; i++){
      res.append("[").append(ids[i]).append("]: ");
      res.append(players[i]);
      res.append("\n");
    }

    System.out.println(res);
  }

  /**
   * Prints the current state of the board
   * @param board a String representation from a GameBoard instance
   */
  public void showGameBoard(String board) {
    GameBoard gb = new GameBoard(board);
    char row = 'a';
    StringBuilder res = new StringBuilder();
    for(int i=11; i>=0; --i){
      res.append(row++).append(" {");
        for (int j=11; j>=0; --j) {
          //check for walls, represent with ▣ = "\u25A3"
          if(checkWall(i,j)){
            res.append(" ").append("\u25A3");
          }
          else {
            res.append(" ").append(pieceToCharacter(gb.getPieceAt(j, i)));
          }
          if(j==0){
            res.append(" }\n");
          }
        }
    }
    res.append("  { 1  2 3  4 5 6  7 8 9 10 11 12}");
    System.out.println(res);
  }

  public boolean checkWall(int row, int col){
    return (row==1 &&(col==2 || col==3 || col==4))
        || (row==5 &&(col==2 || col==3 || col==4))
        || (col==1 &&(row==2 || row==3 || row==4))
        || (col==5 &&(row==2 || row==3 || row==4))

        || (row==6 &&(col==7 || col==8 || col==9))
        || (row==10 &&(col==7 || col==8 || col==9))
        || (col==6 &&(row==7 || row==8 || row==9))
        || (col==10 &&(row==7 || row==8 || row==9));
  }

  /**
   * Returns the valid ASCII character for the chess piece
   * @param p An instance of a Piece
   * @return ASCII representation of Piece
   */
  public String pieceToCharacter(Piece p){
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
   * @return void
   */
  public void showValidMoves(String[] list){
    //Print the list in a nice fashion
    StringBuilder res = new StringBuilder();

    for(int i=0; i<list.length; i++){
      res.append(list[i].toString()).append(", ");
    }
    res.append("\n");

    System.out.println(res);
  }

  /**
   * Print in-game menu for player's benefit
   */
  public void showInGameMenu(){
    System.out.println("~[ Type QUIT to leave ]~[ Type RESIGN to forfeit ]~\n");
  }
}