package server;

import java.io.IOException;
import org.json.simple.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import ui.Item;

public class getPriceHTTPCommand extends AbstractHTTPCommand {

    public getPriceHTTPCommand(HttpExchange he) {
        super(he);
    }

    @Override
    public void execute() throws IOException {
        if (!checkToken()) {
            sentFailure(409);
            return;
        }


        String req = String.valueOf(he.getRequestURI());
        int last = req.lastIndexOf("/");
        String idStr = req.substring(last + 1);
        JSONObject js = new JSONObject();
        try
        {
            int id= Integer.parseInt(idStr);
            js.put("value", MyHTTPServer.sqlstore.getItemValue(id));
        }
        catch (Exception e)
        {
            if(idStr.equals("price"))
            {
                js.put("value", MyHTTPServer.sqlstore.getTotalValue());
            }
            else
            {
                int id = MyHTTPServer.sqlstore.getGroupID(idStr);
                js.put("value", MyHTTPServer.sqlstore.getGroupValue(id));
            }
        }
        // return product
        byte[] ar = js.toJSONString().getBytes();
        this.writeResponseBody(200, ar);

    }

}
