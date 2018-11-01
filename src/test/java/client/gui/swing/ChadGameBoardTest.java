package client.gui.swing;

import static org.junit.jupiter.api.Assertions.*;

import client.game.Game;
import client.gui.ChadGameDriver;
import client.presenter.Driver;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.MovePieceMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.controller.messages.ViewValidMoves;
import client.presenter.network.messages.NetworkMessage;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import org.junit.jupiter.api.Test;

class ChadGameBoardTest {


  private static class ChadGameBoardTestDriver implements ChadGameDriver {

    public ChadGameBoard gamePanel;

    public ViewMessage lastMessage = null;


    public ChadGameBoardTestDriver(){

      gamePanel = new ChadGameBoard(this, DEFAULT_GAME_BOARD);
      //gamePanel.revalidate();
      //gamePanel.repaint();


    }

    @Override
    public void handleViewMessage(ViewMessage message) {
      lastMessage = message;

    }

    @Override
    public void handleNetMessage(NetworkMessage message) {

      // not used for these tests

    }

    public void setBoard(String board){

    }
  }

  private static ChadGameBoardTestDriver driver = new ChadGameBoardTestDriver();
  private static ChadGameBoard gamePanel = driver.gamePanel;


  @Test
  void setBoardPieces() {

  }

  @Test
  void mousePressed() {


    MouseEvent action = new MouseEvent(gamePanel, MouseEvent.MOUSE_PRESSED, 10, 0, 155, 515, 1, false);
    driver.gamePanel.mousePressed(action);
    System.out.println(driver.gamePanel);
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