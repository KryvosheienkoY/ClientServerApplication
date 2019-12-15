package server;

import java.io.IOException;
import org.json.simple.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import ui.Item;

public class getProductHTTPCommand extends AbstractHTTPCommand {

    public getProductHTTPCommand(HttpExchange he) {
        super(he);
    }

    @Override
    public void execute() throws IOException {
        if (!checkToken()) {
            sentFailure(409);
            return;
        }

        int id = goodsId();
        // return product
        Item product = MyHTTPServer.sqlstore.selectItem(id);
        if (product == null)
            sentFailure(404);
        JSONObject js = serializeProduct(product);
        if (traceMode)
            System.out.println("Item - " + product);
        byte[] ar = js.toJSONString().getBytes();
        this.writeResponseBody(200, ar);

    }

}
