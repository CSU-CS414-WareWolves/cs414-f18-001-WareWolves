package client.gui.swing.panels;

import client.presenter.network.messages.NetworkMessage;
import javax.swing.JPanel;
import javax.swing.JTable;

public abstract class UpdatableJTableList extends JPanel {

  public abstract void updateTable(NetworkMessage tableInfo);

  protected int getGameId(JTable lookupTable) {
    int gameIndex = lookupTable.getSelectedRow();
    if (gameIndex != -1) {
      return (int) lookupTable.getModel().getValueAt(lookupTable.convertRowIndexToModel(gameIndex), 0);

    }
    return gameIndex;
  }

}
