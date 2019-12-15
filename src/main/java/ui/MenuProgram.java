package ui;

import client.HttpServiceClient;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class MenuProgram {
    public final static int W_HEIGHT = 900;
    public final static int W_WIDTH = 1110;

    public WestGroupsPanel westPanel;
    public EastProductPanel eastPanel;
    public JFrame frame;
    public JTextField searchField;
    private HttpServiceClient http;

    public MenuProgram(HttpServiceClient http) {
        this.http = http;
        init();
    }

    private void init() {
        // register for actions with the store
        // frame
        this.frame = new JFrame("MENU");
        frame.setLayout(new BorderLayout());

        // north panel for search and store
        JPanel northPanel = new JPanel();
        frame.add(northPanel, BorderLayout.NORTH);
        searchField = new JTextField();
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(new SearchButtonListener());
        JButton storeButton = new JButton("Store");
        storeButton.addActionListener(new StoreButtonListener());

        northPanel.setLayout(new GridLayout(1, 4, 0, 20));
        northPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        northPanel.add(storeButton);
        northPanel.add(searchField);
        northPanel.add(searchButton);

        // west panel for groupsSelected
        this.westPanel = new WestGroupsPanel(http, this);
        // east panel for products
        this.eastPanel = new EastProductPanel(http, frame, westPanel.groupsTable);
        this.westPanel.productLabel=this.eastPanel.productLabel;
        this.westPanel.productTable=this.eastPanel.getProductsTable();


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPanel, eastPanel);
        frame.add(splitPane, BorderLayout.CENTER);
        frame.setSize(W_WIDTH, W_HEIGHT);
        frame.setVisible(true);

    }

    class StoreButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //GET ALL GROUPS REQUEST

            Collection<Group> values = null;
            try {
                values = http.sendGetGroups();
                Group[] groups = new Group[values.size()];
                values.toArray(groups);
                eastPanel.setCurrentGroup(groups);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = searchField.getText();

            //SEND GET PRODUCT REQUEST
            Item p = null;
            try {
                p = http.sendGetProduct(str);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (p == null) {
                JOptionPane.showMessageDialog(null, "The product wasn`t found", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                eastPanel.formProductTable(p);
            }

        }
    }

    public void changeGroupSelection(String gName) {
        ///GET GROUP REQUEST
        Group g = null;
        try {
            g = http.sendGetGroup(gName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        eastPanel.setCurrentGroup(new Group[]{g});

    }

}
