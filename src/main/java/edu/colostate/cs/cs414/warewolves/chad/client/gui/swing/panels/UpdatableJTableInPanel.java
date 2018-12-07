package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.NetworkMessage;
import javax.swing.JPanel;
import javax.swing.JTable;

public abstract class UpdatableJTableInPanel extends JPanel {

  public abstract void updateTable(NetworkMessage tableInfo);

  protected int getHiddenID(JTable lookupTable) {
    int gameIndex = lookupTable.getSelectedRow();
    if (gameIndex != -1) {
      return (int) lookupTable.getModel().getValueAt(lookupTable.convertRowIndexToModel(gameIndex), 0);

    }
    return gameIndex;
  }

}
