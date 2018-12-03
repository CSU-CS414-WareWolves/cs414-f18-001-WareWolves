package client.gui.swing;

import client.gui.ChadGameDriver;
import client.gui.swing.panels.chadgame.ChadGameBoard;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.network.messages.NetworkMessage;
import java.awt.event.MouseEvent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import org.junit.jupiter.api.Test;

class ChadGameBoardTest {


  /**
   * Class to save messages from the swing gui
   */
  private static class ChadGameBoardTestDriver implements ChadGameDriver {

    public ChadGameBoard gamePanel;

    public ViewMessage lastMessage = null;


    public ChadGameBoardTestDriver(){

      gamePanel = new ChadGameBoard(this, DEFAULT_GAME_BOARD);

    }

    @Override
    public void handleViewMessage(ViewMessage message) {
      lastMessage = message;

    }

    @Override
    public void handleNetMessage(NetworkMessage message) {

      // not used for these tests

    }

    @Override
    public void createAndShowGUI() {

    }

    public void setBoard(String board){
      gamePanel.setBoardPieces(board);
    }
  }

  private static ChadGameBoardTestDriver driver = new ChadGameBoardTestDriver();
  private static ChadGameBoard gamePanel = driver.gamePanel;


  @Test
  void setBoardPieces() {

  }

  /**
   * Should find a piece Label but it is not.
   * However, the piece is there
   */
  @Test
  void mousePressed() {


    MouseEvent action = new MouseEvent(gamePanel, MouseEvent.MOUSE_PRESSED, 10, 0, 279, 563, 1, false);
    driver.gamePanel.mousePressed(action);
    JLayeredPane mainBoard = (JLayeredPane) driver.gamePanel.getComponents()[0];
    JPanel gameBoard = (JPanel) mainBoard.getComponents()[0];
    JPanel square = (JPanel) gameBoard.getComponent(112);
    System.out.println(square.getComponents()[1].getName());

  }

  @Test
  void mouseDragged() {
  }

  @Test
  void mouseReleased() {
  }

  @Test
  void setValidMoves() {
  }


  @Test
  void mouseClicked() {
  }

  @Test
  void mouseMoved() {
  }

  @Test
  void mouseEntered() {
  }

  @Test
  void mouseExited() {
  }

}