package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.NetworkMessage;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

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

  /**
   * Checks to see if a NetworkMessage received is the one the class expected
   * @param message the message
   * @param expectedClass the expected type of NetworkMessage
   * @param callingClassName the name of calling class
   */
  protected void checkValidMessageType(NetworkMessage message, Class expectedClass, String callingClassName) {
    if (!expectedClass.isInstance(message)) {
      throw new IllegalArgumentException(callingClassName + ":: Received message of type "
              + message.getClass() + " expected" + expectedClass);
    }
  }

  /**
   * Sets the setting for a table and hides the first column if expected to
   * @param table the table to setup
   * @param hideFirstColumn if the user wants the first column hidden
   */
  protected void setTableSettings(JTable table, boolean hideFirstColumn) {
    // Stop the user from reordering the columns
    table.getTableHeader().setReorderingAllowed(false);
    // Allow only row selection
    table.setRowSelectionAllowed(true);
    table.setColumnSelectionAllowed(false);
    // Allow the user to sort the columns
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setAutoCreateRowSorter(true);

    if (hideFirstColumn) {
      // Stop the user from seeing the inviteID
      TableColumnModel columnControl = table.getColumnModel();
      columnControl.removeColumn(columnControl.getColumn(0));
    }

  }
}
