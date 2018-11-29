package client.gui.swing.panels;

import client.gui.swing.SwingGUIController;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.network.messages.InboxResponse;
import client.presenter.network.messages.NetworkMessage;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class InvitesPanel extends JFrame implements UpdateJTableList {

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

  public InvitesPanel() {
    newInvite.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

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
    receivedTable = new JTable();
    receivedTableModel = new DefaultTableModel();
    sentTable = new JTable();
    sentTableModel = new DefaultTableModel();
    setupTables(receivedTable, receivedTableModel, receivedColumns);
    setupTables(sentTable, sentTableModel, sentColumns);

  }

  private void setupTables(JTable table, DefaultTableModel tableModel, String[] columns) {
    tableModel = new DefaultTableModel(new Object[][]{}, columns);
    table = new JTable(tableModel) {
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    table.setRowSelectionAllowed(true);
    table.setColumnSelectionAllowed(false);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setAutoCreateRowSorter(true);
    table.getTableHeader().setReorderingAllowed(false);
    TableColumnModel columnControl = table.getColumnModel();
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
    InvitesPanel demo = new InvitesPanel();
    frame.add(demo.mainPanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  private static class TestMainInviteGameController extends SwingGUIController {

    @Override
    public void sendMessage(ViewMessage message) {
      if (message instanceof MenuMessage) {
        MenuMessage loginMessage = (MenuMessage) message;

        switch (loginMessage.menuType) {
          case SELECT_GAME:
            System.out.println("Select Game: " + loginMessage.information[0] + " Opponent: "
                + loginMessage.information[2]);
            break;
          case RESIGN_GAME:
            System.out.println("Resign Game: " + loginMessage.information[0] + " Opponent: "
                + loginMessage.information[2]);
        }

      } else {
        throw new IllegalArgumentException("ActiveGame:: Did not sent a menu message - "
            + message.messageType);
      }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

      throw new IllegalArgumentException("ActiveGame:: Tried to send a action - "
          + e.getActionCommand());

    }
  }


}

