package ui;

import client.HttpServiceClient;
import lab4.StoreDatabase;

import java.util.ArrayList;

public class MyProductFrame {
    public Group[] groups;
    private static HttpServiceClient http;

    public MyProductFrame(HttpServiceClient http, Group[] groups) {
        this.groups = groups;
        this.http = http;
    }


    public  static NotEditableTableModel createTableModelOfProducts(Group[] groupsSelected) {
        String[] columnNames = {"Product", "Amount", "Price", "Producer", "Description"};
        int size = 0;
        ArrayList<Item> items = null;
        // GET ALL PRODUCTS  REQUEST
        if (groupsSelected == null) {
            try {
                ArrayList<Group> gr = http.sendGetGroups();
                groupsSelected = gr.toArray(new Group[gr.size()]);
                items = http.sendGetProducts();
            } catch (Exception e) {
                e.printStackTrace();
            }
            size = items.size();
        } else {
            try {
                for (Group group : groupsSelected) {
                    http.sendGetGroup(group.getName());
                    size += HttpServiceClient.items.size();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int counter = 0;
        String[][] rowData = new String[size][5];

        for (Group group : groupsSelected) {
            // Send GROUP REQUEST
            try {
                http.sendGetGroup(group.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayList<Item> products = HttpServiceClient.items;

            for (Item item : products) {
                rowData[counter][0] = item.getName();
                rowData[counter][1] = Integer.toString(item.getAmount());
                rowData[counter][2] = Double.toString(item.getPrice());
                rowData[counter][3] = item.getManufacturer();
                rowData[counter][4] = item.getDescription();
                counter++;
            }
        }
        NotEditableTableModel model = new NotEditableTableModel(rowData, columnNames);
        return model;
    }

    public NotEditableTableModel createTableModelOfProducts(Item item) {
        String[] columnNames = {"Product", "Amount", "Price", "Producer", "Description"};
        String[][] rowData = new String[1][5];

        rowData[0][0] = item.getName();
        rowData[0][1] = Integer.toString(item.getAmount());
        rowData[0][2] = Double.toString(item.getPrice());
        rowData[0][3] = item.getManufacturer();
        rowData[0][4] = item.getDescription();

        NotEditableTableModel model = new NotEditableTableModel(rowData, columnNames);
        return model;
    }

}
