package client.gui.swing.panels;

import client.gui.swing.SwingGUIController;
import client.presenter.controller.messages.InviteMessage;
import client.presenter.controller.messages.InviteMessageResponse;
import client.presenter.network.messages.InboxResponse;
import client.presenter.network.messages.NetworkMessage;
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

public class InvitesPanel extends UpdatableJTableInPanel {

  private final String[] receivedColumns = {"IDs", "Invite From", "Date Sent"};
  private final String[] sentColumns = {"IDs", "Sent To", "Date Sent"};

  private String nickname;


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

    $$$setupUI$$$();
    newInvite.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.sendMessage(new InviteMessage(null, null));
      }
    });
    acceptInvite.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int inviteId = getHiddenID(receivedTable);
        if (inviteId == -1) {
          return;
        }
        controller.sendMessage(new InviteMessageResponse(inviteId, true));
      }
    });
    rejectInvite.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        int inviteId = getHiddenID(receivedTable);
        if (inviteId == -1) {
          return;
        }
        controller.sendMessage(new InviteMessageResponse(inviteId, false));
      }
    });
    cancelInvite.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        int inviteId = getHiddenID(sentTable);
        if (inviteId == -1) {
          return;
        }
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

    if (inboxMessage.inviteIDs[0] == -1) {
      return;
    }

    for (int i = 0; i < inboxMessage.inviteIDs.length; i++) {

      if (inboxMessage.senders[i].equals(nickname)) {
        sentTableModel.addRow(new Object[]{inboxMessage.inviteIDs[i],
                inboxMessage.recipients[i], inboxMessage.sendDates[i]});
      } else {
        receivedTableModel.addRow(new Object[]{inboxMessage.inviteIDs[i],
                inboxMessage.senders[i], inboxMessage.sendDates[i]});
      }
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

