package ui;

import client.HttpServiceClient;

import java.util.ArrayList;

public class MyGroupFrame {
    private HttpServiceClient http;

    public MyGroupFrame(HttpServiceClient http) {
        this.http = http;
    }

    public static NotEditableTableModel CreateTableModelOfGroups(HttpServiceClient http) {
        String[] columnNames = {"Groups", "Description", "Price"};

        // SEND GET GROUPS REQUEST
        ArrayList<Group> groups = null;
        try {
            groups = http.sendGetGroups();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[][] rowData = new String[(groups.size())][3];
        int counter = 0;
        for (Group gr : groups) {
            rowData[counter][0] = gr.getName();
            rowData[counter][1] = gr.getDescription();
            try {
                rowData[counter][2] =  String.valueOf(http.sendGetGroupPrice(gr.getName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            counter++;
        }

        NotEditableTableModel model = new NotEditableTableModel(rowData, columnNames);
        return model;
    }

}
