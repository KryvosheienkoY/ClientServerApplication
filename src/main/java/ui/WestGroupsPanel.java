package ui;

import client.HttpServiceClient;
import lab4.StoreDatabase;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class WestGroupsPanel extends JPanel {
    public MyTable groupsTable;
    public MyTable productTable;
    public JLabel productLabel;
    private MenuProgram menuProgram;
    private HttpServiceClient http;

    public WestGroupsPanel(HttpServiceClient http, MenuProgram menuProgram ) {
        this.menuProgram = menuProgram;
        this.http = http;
        init();
    }

    public void init() {
        // west panel for groupsSelected
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        JLabel groupsLabel = new JLabel("Groups");
        this.add(groupsLabel, BorderLayout.NORTH);
        // names of columns in the table
        String[] columnNames = {"Group", "Description", "Price"};

        formTable(columnNames);
        groupsTable.setFillsViewportHeight(true);

        // Add the scroll pane to this tabel/panel.
        JScrollPane scrollPane = new JScrollPane(groupsTable);
        scrollPane.setPreferredSize(new Dimension(200, 150));
        this.add(scrollPane, BorderLayout.CENTER);

        // panel for icons for groupsSelected
        JPanel iconGroupsPanel = new JPanel();
        this.add(iconGroupsPanel, BorderLayout.SOUTH);

        // 3 icons in 1 row
        iconGroupsPanel.setLayout(new GridLayout(1, 3, 0, 20));
        JButton buttonAdd = new JButton("ADD");
        JButton buttonEdit = new JButton("EDIT");
        JButton buttonDelete = new JButton("DELETE");
        iconGroupsPanel.add(buttonAdd);
        iconGroupsPanel.add(buttonEdit);
        iconGroupsPanel.add(buttonDelete);

        buttonAdd.addActionListener((new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                AddGroupFrame addGroup = new AddGroupFrame(http, getGroupTable());


            }
        }));
        buttonEdit.addActionListener((new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                int selectedRow = groupsTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Select a group", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Object value = groupsTable.getValueAt(selectedRow, 0);

                //GET GROUP REQUEST
                Group group = null;
                try {
                    group = http.sendGetGroup((String) value);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                new EditGroupFrame(http, group, getGroupTable());
            }
        }));
        buttonDelete.addActionListener((new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = groupsTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Select a group", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Object value = groupsTable.getValueAt(selectedRow, 0);

                //GET GROUP REQUEST
                Group group = null;
                try {
                    group = http.sendGetGroup((String) value);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                DeleteGroupFrame deleteGroup = new DeleteGroupFrame(http, group, groupsTable,productTable,productLabel);
            }
        }));

    }

    public void formTable(String[] columnNames) {
        ArrayList<Group> groups = null;
        try {
            groups = http.sendGetGroups();
            int size = groups.size();
            int counter = 0;

            String[][] rowData = new String[size][3];
            for (Group group : groups) {
                rowData[counter][0] = group.getName();
                rowData[counter][1] = group.getDescription();
                double price = http.sendGetGroupPrice(group.getName());
                rowData[counter][2] =  String.valueOf(price);
                counter++;
            }

            groupsTable = new MyTable(rowData, columnNames);
            groupsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    int selectedRow = groupsTable.getSelectedRow();
                    if (selectedRow == -1) {
                        return;
                    }
                    Object value = groupsTable.getValueAt(selectedRow, 0);
                    menuProgram.changeGroupSelection((String) value);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JPanel getWestGroupsPanel() {
        return this;
    }

    public MyTable getGroupTable() {
        return (MyTable) this.groupsTable;
    }
}