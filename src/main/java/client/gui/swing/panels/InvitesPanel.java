package client.gui.swing.panels;

import client.gui.swing.SwingGUIController;
import client.gui.swing.info.ActiveGameInfo;
import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.network.messages.InboxResponse;
import client.presenter.network.messages.NetworkMessage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class InvitesPanel extends UpdatableJTableList {

  private final String[] receivedColumns = {"IDs", "Invite From", "Date Sent"};
  private final String[] sentColumns = {"IDs", "Sent To", "Date Sent"};


  private JPanel mainPanel;
  private JTabbedPane sentInvites;
  private JTable receivedTable;
  private JTable sentTable;
  private JButton acceptInvite;
  private JButton rejectInvite;
  private JButton newInvite;
  private JButton cancelInvite;
  private DefaultTableModel receivedTableModel;
  private DefaultTableModel sentTableModel;

  public InvitesPanel(SwingGUIController controller) {

    newInvite.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.sendMessage(new MenuMessage(MenuMessageTypes.INVITES, new String[]{}));
      }
    });
    acceptInvite.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int rowIndex = getGameId(receivedTable);
        if (rowIndex == -1) {
          return;
        }
        String inviteId = (String) receivedTableModel.getValueAt(rowIndex, 0);
        controller.sendMessage(new MenuMessage(MenuMessageTypes.INVITES,
            new String[] {inviteId, String.valueOf(true)}));
      }
    });
    rejectInvite.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        int rowIndex = getGameId(receivedTable);
        if (rowIndex == -1) {
          return;
        }
        String inviteId = (String) receivedTableModel.getValueAt(rowIndex, 0);
        controller.sendMessage(new MenuMessage(MenuMessageTypes.INVITES,
            new String[] {inviteId, String.valueOf(false)}));
      }
    });
    cancelInvite.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Send cancel Message


      }
    });
  }

  @Override
  public void updateTable(NetworkMessage tableInfo) {

    if (!(tableInfo instanceof InboxResponse)) {
      throw new IllegalArgumentException("ActiveGamePanel:: Received message of type "
          + tableInfo.getClass() + " expected" + InboxResponse.class);
    }

    InboxResponse inboxMessage = (InboxResponse) tableInfo;

    // Reset current data
    receivedTableModel.setNumRows(0);
    sentTableModel.setNumRows(0);

    for (int i = 0; i < inboxMessage.inviteIDs.length; i++) {

    }


  }

  private void createUIComponents() {


    receivedTableModel = new DefaultTableModel(new Object[][]{}, receivedColumns);
    receivedTable = new JTable(receivedTableModel) {
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    receivedTable.setRowSelectionAllowed(true);
    receivedTable.setColumnSelectionAllowed(false);
    receivedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    receivedTable.setAutoCreateRowSorter(true);
    receivedTable.getTableHeader().setReorderingAllowed(false);
    TableColumnModel columnControl = receivedTable.getColumnModel();
    columnControl.removeColumn(columnControl.getColumn(0));

    sentTableModel = new DefaultTableModel(new Object[][]{}, sentColumns);
    sentTable = new JTable(sentTableModel) {
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    sentTable.setRowSelectionAllowed(true);
    sentTable.setColumnSelectionAllowed(false);
    sentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    sentTable.setAutoCreateRowSorter(true);
    sentTable.getTableHeader().setReorderingAllowed(false);
    columnControl = sentTable.getColumnModel();
    columnControl.removeColumn(columnControl.getColumn(0));

  }



  public static void main(String[] args) {

    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }

  /**
   * Create the GUI and show it.  For thread safety, this method should be invoked from the
   * event-dispatching thread.
   */
  private static void createAndShowGUI() {

    //Create and set up the window.
    JFrame frame = new JFrame("Login Panel Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Create and set up the content pane.
    InvitesPanel demo = new InvitesPanel(new TestGameMenuController());
    frame.add(demo.mainPanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }


}

