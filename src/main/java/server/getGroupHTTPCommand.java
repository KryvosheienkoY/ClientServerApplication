package server;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import ui.Group;
import ui.Item;

public class getGroupHTTPCommand extends AbstractHTTPCommand {

    public getGroupHTTPCommand(HttpExchange he) {
        super(he);
    }

    @Override
    public void execute() throws IOException {
        if (!checkToken()) {
            sentFailure(409);
            return;
        }
        String name= groupName();
        // return product
        Group group = MyHTTPServer.sqlstore.selectGroup(name);
        ArrayList<Item> items = MyHTTPServer.sqlstore.getAllProductsFromGroup(name);
        if (group == null)
            sentFailure(404);
        JSONObject js = serializeGroup(group, items);
        if (traceMode)
            System.out.println("Group - " + group.getName());
        byte[] ar = js.toJSONString().getBytes();
        this.writeResponseBody(200, ar);
    }

}
