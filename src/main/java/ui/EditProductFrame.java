package ui;

import client.HttpServiceClient;
import lab4.StoreDatabase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EditProductFrame extends MyProductFrame {
    public Group group;
    private MyTable productsTable;
    private MyTable groupTable;
    private JLabel productLabel;
    private HttpServiceClient http;

    public EditProductFrame(HttpServiceClient http, Group group, Item item, MyTable productsTable, MyTable groupTableTable, JLabel productLabel) {

        super(http, new Group[]{group});
        this.productsTable = productsTable;
        this.groupTable = groupTable;
        this.productLabel = productLabel;
        this.http = http;
        editProduct(item);
    }

    public void editProduct(Item prod) {
        JFrame frame = new JFrame("Product");
        JPanel panel = new JPanel();
        JLabel name = new JLabel("Productname");
        name.setSize(150, 150);
        JTextField pName = new JTextField(prod.getName());
        JTextField manufact = new JTextField(prod.getManufacturer());
        JLabel man = new JLabel("Manufacturer");
        JButton ok = new JButton("OK");
        JLabel price = new JLabel("Price");
        JLabel desc = new JLabel("Description");
        JLabel quantity = new JLabel("Quantity");
        JSlider quan = new JSlider(0, prod.getAmount() + 100, prod.getAmount());
        String ppr = new String();
        ppr += prod.getPrice();
        JTextField pr = new JTextField(ppr);
        JTextArea des = new JTextArea(prod.getDescription());

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(name);
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
        panel.add(ok);
        des.setWrapStyleWord(true);
        frame.add(panel);
        frame.setSize(300, 6666);
        frame.setVisible(true);
        frame.setResizable(false);
        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String prodName = pName.getText();
                String manufacturer = manufact.getText();
                double price = -1;
                String descript = des.getText();
                int quantity = quan.getValue();
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


                // SEND POST PRODUCT REQUEST
                try {
                    http.sendPostProduct(prod.getName(), new Item(prodName, descript, manufacturer, price,quantity));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

				NotEditableTableModel model = createTableModelOfProducts(new Group[]{groups[0]});
                productsTable.setModel(model);
                model.fireTableDataChanged();

                try {
                    productLabel.setText("Products (total price: " + http.sendGetTotalPrice() + ")");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                productLabel.revalidate();
                productLabel.repaint();
                refreshGroupTable();
                frame.dispose();
            }

        });
    }
    public void refreshGroupTable() {
        NotEditableTableModel modelG = MyGroupFrame.CreateTableModelOfGroups(http);
        groupTable.setModel(modelG);
        modelG.fireTableDataChanged();
    }
}
