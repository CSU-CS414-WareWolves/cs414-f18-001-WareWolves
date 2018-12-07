package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels;

import edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.SwingGUIController;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.InviteMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.InviteMessageResponse;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.InboxResponse;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.NetworkMessage;
import com.intellij.uiDesigner.core.Spacer;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 * This panel displays all the player current invites. The invites are stored in two different
 * tables, one for all the invites the player received and the other for all the invites the player
 * has sent. From this panel users can accept or reject invites and sent new invites to other
 * players.
 */
public class InvitesPanel extends UpdatableJTableInPanel {

  /**
   * The column names for the Table of received invites
   */
  private final String[] receivedColumns = {"IDs", "Invite From", "Date Sent"};
  /**
   * The column names for the Table of sent invites
   */
  private final String[] sentColumns = {"IDs", "Sent To", "Date Sent"};

  /**
   * The players nickname for filtering sent and received invites
   */
  private String nickname;

  // Swing GUI elements
  /**
   * Main panel of the class
   */
  private JPanel mainPanel;
  /**
   * Panel for displaying the two tables
   */
  private JTabbedPane sentInvites;
  /**
   * Table to display the received invites
   */
  private JTable receivedTable;
  /**
   * Model for the receivedTable, it stores the data in the table
   */
  private DefaultTableModel receivedTableModel;
  /**
   * Table to display the sent invites
   */
  private JTable sentTable;
  /**
   * Model for the sentTable, it stores the data in the table
   */
  private DefaultTableModel sentTableModel;
  /**
   * Button to accept invite
   */
  private JButton acceptInvite;
  /**
   * Button to reject invite
   */
  private JButton rejectInvite;
  /**
   * Button to sent new invite
   */
  private JButton newInvite;
  /**
   * Button to cancel sent invite
   */
  private JButton cancelInvite;

  /**
   * Creates the GUI elements and sets the panels controller for ActionListeners
   *
   * @param controller the controller of the panel
   */
  public InvitesPanel(SwingGUIController controller) {

    $$$setupUI$$$(); // Needed  for GUI creation
    // Setup new invites listener
    newInvite.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Tell controller to run logic for making new invite
        controller.sendMessage(new InviteMessage(null, null));
      }
    });

    // Setup accept invites listener
    acceptInvite.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Find the invite ID from the table
        int inviteId = getHiddenID(receivedTable);
        if (inviteId == -1) {
          return;
        }
        // Sent the accepted invite to the controller
        controller.sendMessage(new InviteMessageResponse(inviteId, true));
      }
    });

    // Setup reject invites listener
    rejectInvite.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Find the invite ID from the table
        int inviteId = getHiddenID(receivedTable);
        if (inviteId == -1) {
          return;
        }
        // Sent the accepted invite to the controller
        controller.sendMessage(new InviteMessageResponse(inviteId, false));
      }
    });

    // Setup cancel sent invites listener
    cancelInvite.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Find the invite ID from the table
        int inviteId = getHiddenID(sentTable);
        if (inviteId == -1) {
          return;
        }
        // Verify the user wants to cancel the invite
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane
                .showConfirmDialog(mainPanel, "Are you sure you want to cancel your invite to "
                                + sentTableModel.getValueAt(
                        sentTable.convertRowIndexToModel(sentTable.getSelectedRow()), 1)
                        , "Warning", dialogButton);
        if (dialogResult == JOptionPane.YES_OPTION) {
          controller.sendMessage(new InviteMessageResponse(inviteId, false));
        }


      }
    });
  }

  /**
   * Takes a InboxResponse and populates the sent and received invites tables
   * @param tableInfo the message with the information
   */
  @Override
  public void updateTable(NetworkMessage tableInfo) {
    // Check for correct type of message
    checkValidMessageType(tableInfo,  InboxResponse.class, "InvitesPanel");

    InboxResponse inboxMessage = (InboxResponse) tableInfo;

    // Reset current data
    receivedTableModel.setNumRows(0);
    sentTableModel.setNumRows(0);

    // -1 represents no invites
    if (inboxMessage.inviteIDs[0] == -1) {
      return;
    }

    // Go through all the invites
    for (int i = 0; i < inboxMessage.inviteIDs.length; i++) {

      // If the sender is the player add the invite to sent table
      if (inboxMessage.senders[i].equals(nickname)) {
        sentTableModel.addRow(new Object[]{inboxMessage.inviteIDs[i],
                inboxMessage.recipients[i], inboxMessage.sendDates[i]});
      } else {
        // Add invite to the received table
        receivedTableModel.addRow(new Object[]{inboxMessage.inviteIDs[i],
                inboxMessage.senders[i], inboxMessage.sendDates[i]});
      }
    }


  }

  /**
   * Creates all the elements that the GUI needed custom constructors for
   */
  private void createUIComponents() {

    /*
     * Attempts to extract shared logic for creating tables to base class failed.
     *
     * I attempted to make a method that took the tableModel, JTable, and the column names
     * as parameters and set the parameters of the tables. However the tables and the models
     * were not initialized when the panel was created
     */

    // Setup the column names for the received table
    receivedTableModel = new DefaultTableModel(new Object[][]{}, receivedColumns);
    // Stop the user from editing the column info
    receivedTable = new JTable(receivedTableModel) {
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    // Stop the user from reordering the columns
    receivedTable.getTableHeader().setReorderingAllowed(false);
    // Allow only row selection
    receivedTable.setRowSelectionAllowed(true);
    receivedTable.setColumnSelectionAllowed(false);
    // Allow the user to sort the columns
    receivedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    receivedTable.setAutoCreateRowSorter(true);
    // Stop the user from seeing the inviteID
    TableColumnModel columnControl = receivedTable.getColumnModel();
    columnControl.removeColumn(columnControl.getColumn(0));

    // Setup the column names for the sent table
    sentTableModel = new DefaultTableModel(new Object[][]{}, sentColumns);
    // Stop the user from editing the column info
    sentTable = new JTable(sentTableModel) {
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    // Stop the user from reordering the columns
    sentTable.getTableHeader().setReorderingAllowed(false);
    // Allow only row selection
    sentTable.setRowSelectionAllowed(true);
    sentTable.setColumnSelectionAllowed(false);
    // Allow the user to sort the columns
    sentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    sentTable.setAutoCreateRowSorter(true);
    // Stop the user from seeing the inviteID
    columnControl = sentTable.getColumnModel();
    columnControl.removeColumn(columnControl.getColumn(0));

  }

  /**
   * Sets the nickname of the current player
   * @param nickname the players nickname
   */
  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout(0, 0));
    sentInvites = new JTabbedPane();
    sentInvites.setName("Recieved Invites");
    mainPanel.add(sentInvites, BorderLayout.NORTH);
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new BorderLayout(0, 0));
    sentInvites.addTab("Recieved Invites", panel1);
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    panel1.add(panel2, BorderLayout.SOUTH);
    final Spacer spacer1 = new Spacer();
    panel2.add(spacer1);
    acceptInvite = new JButton();
    acceptInvite.setActionCommand("acceptInvite");
    acceptInvite.setText("Accept");
    panel2.add(acceptInvite);
    final Spacer spacer2 = new Spacer();
    panel2.add(spacer2);
    rejectInvite = new JButton();
    rejectInvite.setActionCommand("rejectInvite");
    rejectInvite.setText("Reject");
    panel2.add(rejectInvite);
    final Spacer spacer3 = new Spacer();
    panel2.add(spacer3);
    final Spacer spacer4 = new Spacer();
    panel2.add(spacer4);
    final JScrollPane scrollPane1 = new JScrollPane();
    panel1.add(scrollPane1, BorderLayout.NORTH);
    receivedTable.setFillsViewportHeight(true);
    receivedTable.setFocusCycleRoot(false);
    scrollPane1.setViewportView(receivedTable);
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new BorderLayout(0, 0));
    sentInvites.addTab("Sent Invites", panel3);
    final JScrollPane scrollPane2 = new JScrollPane();
    panel3.add(scrollPane2, BorderLayout.NORTH);
    sentTable.setFillsViewportHeight(true);
    scrollPane2.setViewportView(sentTable);
    final JPanel panel4 = new JPanel();
    panel4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    panel3.add(panel4, BorderLayout.CENTER);
    final Spacer spacer5 = new Spacer();
    panel4.add(spacer5);
    newInvite = new JButton();
    newInvite.setActionCommand("newInvite");
    newInvite.setText("New Invite");
    panel4.add(newInvite);
    final Spacer spacer6 = new Spacer();
    panel4.add(spacer6);
    cancelInvite = new JButton();
    cancelInvite.setActionCommand("cancelInvite");
    cancelInvite.setText("Cancel Invite");
    panel4.add(cancelInvite);
    final Spacer spacer7 = new Spacer();
    panel4.add(spacer7);
    final Spacer spacer8 = new Spacer();
    panel4.add(spacer8);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return mainPanel;
  }
}

