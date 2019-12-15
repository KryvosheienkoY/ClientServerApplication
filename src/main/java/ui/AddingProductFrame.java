package ui;

import client.HttpServiceClient;
import lab4.StoreDatabase;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AddingProductFrame extends MyProductFrame {
    private MyTable productsTable;
    private MyTable groupTable;
    private JLabel productLabel;
    private HttpServiceClient http;

    public AddingProductFrame(HttpServiceClient http, Group group, MyTable productsTable, MyTable groupTable, JLabel productLabel) {

        super(http, new Group[]{group});
        this.http = http;
        this.productsTable = productsTable;
        this.groupTable = groupTable;
        this.productLabel = productLabel;
        init();
    }

    public void init() {
        /// check if a group is chosen
        JFrame frame = new JFrame("Add Product");
        JPanel panel = new JPanel();
        JLabel name = new JLabel("Product name");
        name.setSize(100, 100);
        JTextField pName = new JTextField();
        JTextField manufact = new JTextField();
        JLabel man = new JLabel("Manufacturer");
        JButton ok = new JButton("OK");

        JLabel price = new JLabel("Price");
        JLabel desc = new JLabel("Description");
        JLabel quantity = new JLabel("Quantity");
        JSlider quan = new JSlider(0, 100, 0);
        JTextField pr = new JTextField();
        JTextArea des = new JTextArea();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        pr.setSize(300, 40);
        panel.add(name);
        pName.setSize(100, 20);
        panel.add(pName);
        panel.add(price);

        panel.add(pr);
        panel.add(man);
        panel.add(manufact);
        panel.add(quantity);
        panel.add(quan);
        quan.setMajorTickSpacing(10);
        quan.setMinorTickSpacing(5);
        quan.setPaintTicks(true);
        quan.setPaintLabels(true);
        panel.add(desc);
        panel.add(des);
        des.setWrapStyleWord(true);
        panel.add(ok);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String prodName = pName.getText();
                String manufacturer = manufact.getText();
                String descript = des.getText();
                int quantity = quan.getValue();
                double price = -1;

                try {
                    price = Double.parseDouble(pr.getText());
                } catch (NumberFormatException event) {
                    JOptionPane.showMessageDialog(null, "Wrong input", "ERROR!", JOptionPane.ERROR_MESSAGE);
                    // handle the error
                    return;
                }

                if (price <= 0) {
                    JOptionPane.showMessageDialog(null, "Negative or zero price", "ERROR!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (prodName.equals("")) {
                    JOptionPane.showMessageDialog(null, "Empty name field", "ERROR!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Item item = new Item(prodName, descript, manufacturer, price, quantity);
                // add a item in a group register

                //SEND PUT PRODUCT REQUEST
                try {
                    http.sendPutProduct(item, groups[0]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                NotEditableTableModel model = createTableModelOfProducts(new Group[]{groups[0]});
                productsTable.setModel(model);
                model.fireTableDataChanged();

                //SEND PRICE REQUEST
                double priceC = 0;
                try {
                    priceC = http.sendGetTotalPrice();
                    productLabel.setText("Products (total price: " + priceC + ")");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                productLabel.revalidate();
                productLabel.repaint();
                refreshGroupTable();


                frame.dispose();

            }

        });

        frame.add(panel);
        frame.setSize(300, 400);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setBackground(Color.WHITE);

    }

    public void refreshGroupTable() {
        NotEditableTableModel modelG = MyGroupFrame.CreateTableModelOfGroups(http);
        groupTable.setModel(modelG);
        modelG.fireTableDataChanged();
    }
}
