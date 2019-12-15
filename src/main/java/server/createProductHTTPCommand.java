package server;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.net.httpserver.HttpExchange;
import ui.Item;


public class createProductHTTPCommand extends AbstractHTTPCommand {

    public createProductHTTPCommand(HttpExchange he) {
        super(he);
    }

    @Override
    public void execute() throws IOException {
        if (!checkToken()) {
            sentFailure(409);
            return;
        }
        String request = MyHTTPServer.getRequestBody(he);
        if (traceMode)
            System.out.println("request - " + request);
        JSONParser jsonParser = new JSONParser();
        int amount = -1;
        String title = null;
        double price = -1;
        String description = null;
        String manufacturer = null;
        String group = null;
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(request);
            title = (jsonObject.get("name")).toString();
            amount = Integer.parseInt((jsonObject.get("amount")).toString());
            price = Double.parseDouble((jsonObject.get("price")).toString());
            description = (jsonObject.get("description")).toString();
            manufacturer = (jsonObject.get("manufacturer")).toString();
            group = (jsonObject.get("group")).toString();
        } catch (Exception e) {
            sentFailure(500);
            return;
        }

        if (title == null || group == null || manufacturer == null || description == null || price <= 0 || amount < 0) {
            sentFailure(409);
            return;
        }

        Item item = new Item(title, description, manufacturer, price, amount);
        System.out.println("Item (PUT) - " + item);
        String answer;
        if ((MyHTTPServer.sqlstore.getGroupID(group)) !=-1 && (MyHTTPServer.sqlstore.addItem(group, item))) {
            answer = "Created " + item.getName();
            this.writeResponseBody(201, answer.getBytes("UTF-8"));
        } else {
            answer = "Conflict";
            this.writeResponseBody(409, answer.getBytes("UTF-8"));
        }

    }

}
