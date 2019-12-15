package ui;

import client.HttpServiceClient;
import lab4.StoreDatabase;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class SellProduct extends MyProductFrame {

    private MyTable productsTable;
    private MyTable groupTable;
    private HttpServiceClient http;
    private JLabel productLabel;

    public SellProduct(HttpServiceClient http, Group group, Item item, MyTable productsTable, MyTable groupTable, JLabel productLabel) {

        super(http, new Group[]{group});
        this.http = http;
        this.productLabel=productLabel;
        this.productsTable = productsTable;
        this.groupTable = groupTable;
        changeproductQuantity(item);
    }

    public void changeproductQuantity(Item item) {
        int q;
        JFrame frame = new JFrame("Change Quantity");
        JLabel choose = new JLabel("         Choose action");
        JSlider slide = new JSlider();
        JPanel panel = new JPanel();
        frame.setVisible(true);
        q = item.getAmount();
        frame.setResizable(false);

        panel.setLayout(new BorderLayout());
        JButton sell = new JButton("Sell");
        JButton buy = new JButton("Buy");
        frame.setSize(250, 100);
        panel.add(sell, BorderLayout.EAST);
        panel.add(buy, BorderLayout.WEST);
        panel.add(choose, BorderLayout.CENTER);
        frame.add(panel);
        sell.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame se = new JFrame("Sell");
                JPanel pan = new JPanel();
                se.setSize(250, 150);
                se.setVisible(true);
                se.setResizable(false);
                slide.setMajorTickSpacing(10);
                slide.setMinorTickSpacing(5);
                slide.setPaintTicks(true);
                slide.setPaintLabels(true);
                JButton ok = new JButton("OK");
                ok.setSize(50, 20);
                pan.add(slide);
                pan.add(ok, BorderLayout.EAST);
                pan.add(slide, BorderLayout.BEFORE_FIRST_LINE);
                se.add(pan);
                ok.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int n = slide.getValue();
                        while (n > item.getAmount()) {
                            try {
                                n = Integer.parseInt(JOptionPane.showInputDialog(frame,
                                        "There are only " + item.getAmount() + " left. Enter quantity: "));

                            } catch (Exception e2) {

                            }
                        }
                        /// SEND POST PRODUCT REQUEST
                        try {
                            http.sendPostProduct(item.getName(), new Item(item.getName(), item.getDescription(), item.getManufacturer(), item.getPrice(), (item.getAmount() - n)));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        JOptionPane.showMessageDialog(frame,
                                n + " items of item " + item.getName() + " were sold");
                        NotEditableTableModel model = createTableModelOfProducts(new Group[]{groups[0]});
                        productsTable.setModel(model);
                        model.fireTableDataChanged();
                        se.dispose();
                        frame.dispose();

                    }
                });

            }

        });
        buy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame se = new JFrame("Buy");
                JPanel pan = new JPanel();
                se.setSize(250, 150);
                se.setVisible(true);
                se.setResizable(false);
                slide.setMajorTickSpacing(10);
                slide.setMinorTickSpacing(5);
                slide.setPaintTicks(true);
                slide.setPaintLabels(true);
                JButton ok = new JButton("OK");
                ok.setSize(50, 20);
                pan.add(slide);
                pan.add(ok, BorderLayout.EAST);
                pan.add(slide, BorderLayout.BEFORE_FIRST_LINE);
                se.add(pan);
                ok.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int n = slide.getValue();

                        int amountN=0;
                        amountN= (item.getAmount() + n);

                        /// SEND POST PRODUCT REQUEST
                        try {
                            http.sendPostProduct(item.getName(), new Item(item.getName(), item.getDescription(), item.getManufacturer(), item.getPrice(), amountN));
                        } catch (
                                Exception ex) {
                            ex.printStackTrace();
                        }

                        JOptionPane.showMessageDialog(frame,
                                n + " items of item " + item.getName() + " were bought");
                        NotEditableTableModel model = createTableModelOfProducts(new Group[]{groups[0]});

                        try {
                            productLabel.setText("Products (total price: " + http.sendGetTotalPrice() + ")");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        productLabel.revalidate();
                        productLabel.repaint();

                        productsTable.setModel(model);
                        model.fireTableDataChanged();

                        refreshGroupTable();
                        se.dispose();
                        frame.dispose();
                    }
                });

            }
        });
    }
    public void refreshGroupTable() {
        NotEditableTableModel modelG = MyGroupFrame.CreateTableModelOfGroups(http);
        groupTable.setModel(modelG);
        modelG.fireTableDataChanged();
    }
}
