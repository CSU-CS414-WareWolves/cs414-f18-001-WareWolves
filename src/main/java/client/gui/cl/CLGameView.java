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
  public static final String WKING = "\u2654";
  public static final String WQUEEN = "\u2655";
  public static final String WROOK = "\u2656";
  public static final String BKING = "\u265A";
  public static final String BQUEEN = "\u265B";
  public static final String BROOK = "\u265C";


  /**
   * Prints all current games of the user
   * @param games list of usernames the player has an active game with
   */
  public void showCurrentGames(ArrayList<String> games){
    //Print the list in a nice fashion
    StringBuilder res = new StringBuilder();

    res.append("+++ Select the Game you wish to continue +++\n\n");
    for(int i=0; i<games.size(); i++){
      res.append("[").append(i+1).append("]: ");
      res.append(games.get(i).toString());
      res.append("\n");
    }

    System.out.println(res);
  }

  /**
   * Prints the current state of the board
   * @param gb An instance of a GameBoard that will be printed with pieces
   * @return void
   */
  public void showGameBoard(GameBoard gb) {
    char row = 'a';
    StringBuilder res = new StringBuilder();
    for(int i=11; i>=0; --i){
      res.append(row++).append(" {");
        for (int j=11; j>=0; --j) {
          res.append(" ").append(pieceToCharacter(gb.getPieceAt(j, i)));
          if(j==0){
            res.append(" }\n");
          }
        }
    }
    res.append("  { 1  2 3  4 5 6  7 8 9 10 11 12}");
    System.out.println(res);
  }

  /**
   * Returns the valid ASCII character for the chess piece
   * @param p An instance of a Piece
   * @return ASCII representation of Piece
   */
  public static String pieceToCharacter(Piece p){
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
  public void showValidMoves(ArrayList<String> list){
    //Print the list in a nice fashion
    StringBuilder res = new StringBuilder();

    for(int i=0; i<list.size(); i++){
      res.append(list.get(i).toString()).append(", ");
    }
    res.append("\n");

    System.out.println(res);
  }

  /**
   * Print in-game menu for player's benefit
   */
  public void showIngameMenu(){
    StringBuilder res = new StringBuilder();
    res.append("(you can type the below in-game options at any time)\n");
    res.append("~[    Exit    ]~~~~~[   Forfeit  ]~\n");
    System.out.println(res);
  }
}