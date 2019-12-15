package ui;

import javax.swing.JTable;

public class MyTable extends JTable {
	/**
	 * A table which cant be edited by user
	 * 
	 * @param rowData
	 *            information in rows
	 * @param columnNames
	 *            names of columnes
	 */
	public MyTable(String[][] rowData, String[] columnNames) {
		super(rowData, columnNames);
		this.setModel(new NotEditableTableModel(rowData, columnNames));
	}

	public MyTable(NotEditableTableModel model) {
		super(model);
	}

}