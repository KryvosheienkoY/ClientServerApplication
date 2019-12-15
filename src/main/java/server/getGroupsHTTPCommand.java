package server;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import ui.Group;
import ui.Item;

public class getGroupsHTTPCommand extends AbstractHTTPCommand {

    public getGroupsHTTPCommand(HttpExchange he) {
        super(he);
    }

    @Override
    public void execute() throws IOException {
        if (!checkToken()) {
            sentFailure(409);
            return;
        }
        // return product
        ArrayList<Group> items = MyHTTPServer.sqlstore.getGroups();
        if (items.size()==0)
            sentFailure(404);
        JSONObject js = new JSONObject();
        js.put("size", String.valueOf(items.size()));
        for(int i=0; i<items.size(); i++)
        {
            JSONObject j = new JSONObject();
            j.put("name", items.get(i).getName());
            j.put("description", items.get(i).getDescription());
            js.put("group"+String.valueOf(i),j);
        }
        // if (traceMode)
        // System.out.println("ui.Group - " + group.getName());
        byte[] ar = js.toJSONString().getBytes();
        this.writeResponseBody(200, ar);
    }

}
