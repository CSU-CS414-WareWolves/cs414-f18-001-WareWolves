package client.gui;

import client.presenter.controller.messages.ViewMessage;
import client.presenter.network.messages.NetworkMessage;

public interface ChadGameDriver {

  String DEFAULT_GAME_BOARD = "rdCreDRiHRjIrcCkdDreERhHKiIRjJrcDrdERhIRiJrcERhJreCRjH";


  void handleViewMessage(ViewMessage message);
  void handleNetMessage(NetworkMessage message);

}
