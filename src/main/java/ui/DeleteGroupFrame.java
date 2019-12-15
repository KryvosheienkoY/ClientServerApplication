package ui;

import client.HttpServiceClient;

import javax.swing.*;
import java.util.ArrayList;

public class DeleteGroupFrame extends MyGroupFrame {

	public MyTable groupsTable;
	public MyTable productsTable;
	private JLabel productLabel;
	private HttpServiceClient http;

	public DeleteGroupFrame(HttpServiceClient http, Group group, MyTable groupsTable, MyTable productsTable,JLabel productLabel) {
		super(http);
		this.http=http;
		this.groupsTable = groupsTable;
		this.productsTable = productsTable;
		this.productLabel = productLabel;
		deleteGroupF(group);
	}

	public void deleteGroupF(Group group) {
		int confirmed = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to delete group " + group.getName() + "?", "Delete Group Message Box",
				JOptionPane.YES_NO_OPTION);

		if (confirmed == JOptionPane.YES_OPTION) {

//			DELETE GROUP REQUEST
			try {
				this.http.sendDeleteGroup(group.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}


			try {
				productLabel.setText("Products (total price: " + http.sendGetTotalPrice() + ")");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			productLabel.revalidate();
			productLabel.repaint();

			NotEditableTableModel model = CreateTableModelOfGroups(http);
			groupsTable.setModel(model);

			Group[] gr = null;
			NotEditableTableModel modelP= MyProductFrame.createTableModelOfProducts(gr);
			productsTable.setModel(modelP);
			model.fireTableDataChanged();

		}

	}
}
