package client.gui.cl;

import static java.lang.Thread.sleep;

import java.io.IOException;

public class CLDriver {
  /**
   * Receives a packet
   * @return [?]
   */
  public void recvPacket() {
    return;
  }

  /**
   * Sends a packet
   * @param
   * @return [?]
   */
  public void sendPacket(){
    return;
  }

  public static void main(String[] args) throws IOException {
    CLLogin login = new CLLogin();
    CLMenu menu = new CLMenu();
    CLGameView game = new CLGameView();
    login.showSplash();
    menu.showMenu();
  }
}
