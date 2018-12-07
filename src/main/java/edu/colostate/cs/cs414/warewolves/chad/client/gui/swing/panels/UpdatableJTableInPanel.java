package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.NetworkMessage;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 * Any panel that has a JTable in it extends this class to allow the panels controller to update
 * table
 */
public abstract class UpdatableJTableInPanel extends JPanel {

  /**
   * Uses the information in the Network Message to update its table
   * @param tableInfo the message with the information
   */
  public abstract void updateTable(NetworkMessage tableInfo);

  /**
   * Many of the tables have a hidden ID in the first column, this method gets the value of
   * the ID or -1 if not row is selected in the table
   * @param lookupTable the table to look up the ID from
   * @return the value of the hidden ID or -1 if no row was selected
   */
  protected int getHiddenID(JTable lookupTable) {
    int selectedTableRow = lookupTable.getSelectedRow();
    // If a row was selected
    if (selectedTableRow != -1) {
      return (int) lookupTable.getModel().getValueAt(lookupTable.convertRowIndexToModel(selectedTableRow), 0);

    }
    return selectedTableRow; // -1
  }

}
