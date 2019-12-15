package ui;

import client.HttpServiceClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

public class EastProductPanel extends JPanel {

    public Group[] groupsSelected;
    public MyTable productsTable;
    public MyTable groupTable;
    public JFrame frame;
    public JLabel productLabel;
    private HttpServiceClient http;


    public EastProductPanel(HttpServiceClient http, JFrame frame, MyTable groupTable) {
        this.frame = frame;
        this.http = http;
        this.groupTable = groupTable;
        init();
    }

    public MyTable getProductsTable() {
        return this.productsTable;
    }

    private void init() {
        // east panel for groupsSelected
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        productLabel = new JLabel("Products (total price: ");
        this.add(productLabel, BorderLayout.NORTH);
        calculateGroupProductTotalPrice();
        // tabel
        this.productsTable = new MyTable(new String[][]{},
                new String[]{"Product", "Amount", "Price", "Producer", "Description"});
        // names of columns in the table
        formProductTable();
        this.add(productsTable);

        // Add the scroll pane to this tabel /panel.
        JScrollPane scrollPane = new JScrollPane(productsTable);
        scrollPane.setPreferredSize(new Dimension(200, 150));
        this.add(scrollPane, BorderLayout.CENTER);
        // panel for icons for groupsSelected
        JPanel iconGroupsPanel = new JPanel();
        this.add(iconGroupsPanel, BorderLayout.SOUTH);

        // 4 icons in 1 row
        iconGroupsPanel.setLayout(new GridLayout(1, 4, 0, 20));

        JButton buttonAdd = new JButton("ADD");
        buttonAdd.addActionListener(new AddingListener());
        JButton buttonEdit = new JButton("EDIT");
        buttonEdit.addActionListener(new EditingListener());
        JButton buttonDelete = new JButton("DELETE");
        buttonDelete.addActionListener(new DeletingListener());
        JButton buttonNumber = new JButton("CHANGE AMOUNT");
        buttonNumber.addActionListener(new ChangingListener());
        iconGroupsPanel.add(buttonAdd);
        iconGroupsPanel.add(buttonEdit);
        iconGroupsPanel.add(buttonDelete);
        iconGroupsPanel.add(buttonNumber);

    }

    public void formProductTable() {
        MyProductFrame frame = new MyProductFrame(http, groupsSelected);
        NotEditableTableModel model = frame.createTableModelOfProducts(groupsSelected);
        productsTable.setModel(model);
        productsTable.setFillsViewportHeight(true);
        calculateGroupProductTotalPrice();
    }

    public void formProductTable(Item item) {
        MyProductFrame frame = new MyProductFrame(http, groupsSelected);
        NotEditableTableModel model = frame.createTableModelOfProducts(item);
        productsTable.setModel(model);
        productsTable.setFillsViewportHeight(true);
        calculateGroupProductTotalPrice();
    }


    public void calculateGroupProductTotalPrice() {
        double price = 0;

        //SEND PRICE REQUEST
        try {
            price = this.http.sendGetTotalPrice();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.productLabel.setText("Products (total price: " + price + ")");
        this.productLabel.revalidate();
        this.productLabel.repaint();
    }

    class AddingListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (groupsSelected == null) {
                JOptionPane.showMessageDialog(null, "Select a group!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AddingProductFrame addProduct = new AddingProductFrame(http, groupsSelected[0], productsTable,groupTable, productLabel);
        }


    }

    class EditingListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            int selectedRow = productsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Select a item", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Object value = productsTable.getValueAt(selectedRow, 0);

            // GET ITEM REQUEST
            Item item = null;
            try {
                item = http.sendGetProduct((String) value);
                EditProductFrame editProduct = new EditProductFrame(http, groupsSelected[0], item, getProductsTable(),
                       groupTable, productLabel);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            refreshGroupTable();
        }

    }

    class DeletingListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            int selectedRow = productsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Select a item", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Object value = productsTable.getValueAt(selectedRow, 0);

            // GET ITEM REQUEST
            Item item = null;
            try {
                item = http.sendGetProduct((String) value);
                DeleteProductFrame deleteProduct = new DeleteProductFrame(http, groupsSelected[0], item, productsTable,groupTable
                        , productLabel);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    private void refreshGroupTable() {
        NotEditableTableModel modelG = MyGroupFrame.CreateTableModelOfGroups(http);
        groupTable.setModel(modelG);
        modelG.fireTableDataChanged();
    }

    class ChangingListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = productsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Select a item", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Object value = productsTable.getValueAt(selectedRow, 0);

            // GET ITEM REQUEST
            Item item = null;
            try {
                item = http.sendGetProduct((String) value);
                SellProduct sellProduct = new SellProduct(http, groupsSelected[0], item, productsTable,groupTable, productLabel);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void setCurrentGroup(Group[] g) {
        this.groupsSelected = g;
        formProductTable();
    }
}
