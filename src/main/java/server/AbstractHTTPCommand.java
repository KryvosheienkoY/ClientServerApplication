package server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.Headers;
import org.json.simple.JSONObject;

import com.sun.net.httpserver.HttpExchange;

import ui.Group;
import ui.Item;

public abstract class AbstractHTTPCommand implements Command {

    protected HttpExchange he;
    protected boolean traceMode;

    public AbstractHTTPCommand(HttpExchange he) {
        this.he = he;
    }

    public void setTraceMode(boolean traceMode) {
        this.traceMode = traceMode;
    }


    protected int goodsId() {
        String req = String.valueOf(he.getRequestURI());
        int last = req.lastIndexOf("/");
        String idStr = req.substring(last + 1);

        if (traceMode)
            System.out.println("id: " + idStr);

        return Integer.parseInt(idStr);

    }

    protected String groupName() {
        String req = String.valueOf(he.getRequestURI());
        int last = req.lastIndexOf("/");
        String idStr = req.substring(last + 1);

        if (traceMode)
            System.out.println("id: " + idStr);

        return idStr;

    }

    protected boolean checkToken() {
        Headers headers = he.getRequestHeaders();
        List<String> tokenL = headers.get("token");
        if (!JWT.parseJWT(tokenL.get(0))) {
            return false;
        }
        return true;
    }

    public static JSONObject serializeProduct(Item item) {
        JSONObject js = new JSONObject();
        js.put("name", item.getName());
        js.put("amount", item.getAmount());
        js.put("price", item.getPrice());
        js.put("description", item.getDescription());
        js.put("manufacturer", item.getManufacturer());
        return js;
    }
    public static JSONObject serializeGroup(Group group, ArrayList<Item> item) {
        JSONObject js = new JSONObject();
        js.put("name", group.getName());
        js.put("description", group.getDescription());
        js.put("size", item.size());
        for(int i=0; i<item.size(); i++)
        {
            js.put("item"+i, serializeProduct(item.get(i)));
        }
        return js;
    }

    public static void sentFailure(int code, HttpExchange he) throws IOException {
        byte[] ar = "Failed".getBytes("UTF-8");
        he.sendResponseHeaders(code, ar.length);
        OutputStream os = he.getResponseBody();
        os.write(ar);
        os.flush();
        os.close();

    }

    protected void sentFailure(int code) throws IOException {
        sentFailure(code, he);
    }

    protected void writeResponseBody(int code, byte[] ar) throws IOException {
        if (ar == null) {
            he.sendResponseHeaders(code, -1);
        } else {
            he.sendResponseHeaders(code, ar.length);
            OutputStream os = he.getResponseBody();
            os.write(ar);
            os.flush();
            os.close();
        }
    }

}
