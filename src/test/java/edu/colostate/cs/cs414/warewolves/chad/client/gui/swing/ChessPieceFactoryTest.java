package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing;

import static org.junit.jupiter.api.Assertions.*;

import edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels.chadgame.ChessPieceFactory;
import javax.swing.JLabel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ChessPieceFactoryTest {

  private static final ChessPieceFactory factory = ChessPieceFactory.getInstance();

  @DisplayName("CreateIcon(int, int)")
  @ParameterizedTest(name = "{1} expected at ({0})")
  @CsvSource({"K", "R", "Q", "k", "r", "q"})
  public void getPiece(char piece){
    JLabel pieceLabel = factory.getPiece(piece);
    assertEquals("Piece", pieceLabel.getName());
  }

  @Test

  public void getInvalidPiece(){
    assertThrows(IllegalArgumentException.class, () -> factory.getPiece('g'));

  }



}