package ui;

import client.HttpServiceClient;
import lab4.StoreDatabase;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditGroupFrame extends MyGroupFrame {
    public Group group;
    private MyTable groupsTable;
    private HttpServiceClient http;

    public EditGroupFrame(HttpServiceClient http, Group group, MyTable groupsTable) {
        super(http);
        this.groupsTable = groupsTable;
        this.http = http;
        editGroupF(group);
    }

    public void editGroupF(Group group) {
        JFrame frame = new JFrame("Edit Group Name");

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setLayout(new GridLayout(4,1));
        JLabel name1 = new JLabel("Group name");
        JTextField gname = new JTextField(group.getName());
        JLabel descript1 = new JLabel("Group name");
        JTextField gdescript = new JTextField(group.getDescription());

        JButton okk = new JButton("OK");
        panel.add(name1);
        panel.add(gname );

        panel.add(descript1);
        panel.add(gdescript );
        okk.setSize(50, 20);
        panel.add(okk);
        frame.add(panel);
        okk.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String oldName = group.getName();
                String grN = gname.getText();
                String grD = gdescript.getText();

                // POST GROUP REQUEST
                try {
                    http.sendPostGroup(oldName,new Group(grN, grD));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                NotEditableTableModel model = CreateTableModelOfGroups(http);
                groupsTable.setModel(model);

                frame.dispose();
            }
        });
        frame.setVisible(true);
        frame.setSize(300, 200);
    }
}
