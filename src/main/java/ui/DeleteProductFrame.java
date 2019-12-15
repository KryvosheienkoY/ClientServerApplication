package ui;

import client.HttpServiceClient;
import lab4.StoreDatabase;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class DeleteProductFrame extends MyProductFrame {
    private MyTable productsTable;
    private MyTable groupTable;
    private JLabel productLabel;
    private HttpServiceClient http;

    public DeleteProductFrame(HttpServiceClient http, Group group, Item item, MyTable productsTable, MyTable groupTable,
                              JLabel productLabel) {

        super(http, new Group[]{group});
        this.http = http;
        this.productsTable = productsTable;
        this.groupTable = groupTable;
        this.productLabel = productLabel;
        deleteProduct(item);
    }

    public void deleteProduct(Item item) {
        int confirmed = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete item " + item.getName() + "?", "Delete Item Message Box",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {

//			SEND DELETE PRODUCT REQUEST
            try {
                this.http.sendDeleteProduct(item.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            NotEditableTableModel model = createTableModelOfProducts(new Group[]{groups[0]});
            productsTable.setModel(model);
            model.fireTableDataChanged();

            // SEND PRICE REQUEST
            double price= 0;
            try {
                price = http.sendGetGroupPrice(groups[0].getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            productLabel.setText("Products (total price: " + price+ ")");
            refreshGroupTable();
            productLabel.revalidate();
            productLabel.repaint();

        }

    }
    public void refreshGroupTable() {
        NotEditableTableModel modelG = MyGroupFrame.CreateTableModelOfGroups(http);
        groupTable.setModel(modelG);
        modelG.fireTableDataChanged();
    }
}
