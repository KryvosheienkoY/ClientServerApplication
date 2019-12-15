package ui;

import javax.swing.table.AbstractTableModel;

public final class NotEditableTableModel extends AbstractTableModel {
	private final String[][] rowData;
	private final String[] columnNames;

	public NotEditableTableModel(String[][] rowData, String[] columnNames) {
		this.rowData = rowData;
		this.columnNames = columnNames;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
	//	this.fireTableDataChanged();
		return rowData[rowIndex][columnIndex];
	}

	@Override
	public int getRowCount() {
		return rowData.length;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}