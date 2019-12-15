package server;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import ui.Group;
import ui.Item;

public class getGoodsHTTPCommand extends AbstractHTTPCommand {

    public getGoodsHTTPCommand(HttpExchange he) {
        super(he);
    }

    @Override
    public void execute() throws IOException {
        if (!checkToken()) {
            sentFailure(409);
            return;
        }
        // return product
        ArrayList<Item> items = MyHTTPServer.sqlstore.getAllProducts();
        if (items.size()==0)
            sentFailure(404);
        JSONObject js = new JSONObject();
        js.put("size", String.valueOf(items.size()));
        for(int i=0; i<items.size(); i++)
        {
            js.put("item"+String.valueOf(i),serializeProduct(items.get(i)));
        }
        // if (traceMode)
        // System.out.println("ui.Group - " + group.getName());
        byte[] ar = js.toJSONString().getBytes();
        this.writeResponseBody(200, ar);
    }

}
