package ui;

import client.HttpServiceClient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddGroupFrame extends MyGroupFrame{
	
	public MyTable groupsTable;
	private HttpServiceClient http;

	public AddGroupFrame(HttpServiceClient http, MyTable groupsTable) {
		super(http);
		this.http=http;
		this.groupsTable = groupsTable;
		addGroupF();
	}

	public void addGroupF() {
		JFrame frame = new JFrame("Add Group");

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		JLabel name = new JLabel("Group name");
		JLabel descriptionL = new JLabel("Group description");
		panel.setLayout(new GridLayout(5,1));
		JTextField gname = new JTextField();
		JTextField gDescription = new JTextField();

		JButton okk = new JButton("OK");
		panel.add(name);
		panel.add(gname);
		panel.add(descriptionL);
		panel.add(gDescription);
		okk.setSize(50, 20);
		panel.add(okk, BorderLayout.SOUTH);
		frame.add(panel);
		okk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String grN = gname.getText();
				String grD = gDescription.getText();
				Group group = new Group(grN, grD);

				//SEND PUT GROUP REQUEST
				try {
					http.sendPutGroup(group);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				//				// refresh data in a table
				NotEditableTableModel model = CreateTableModelOfGroups(http);
				groupsTable.setModel(model);
				frame.dispose();
			}
		});
		frame.setVisible(true);
		frame.setSize(350, 200);
	}

	
}
