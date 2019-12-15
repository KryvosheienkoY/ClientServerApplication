package server;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.net.httpserver.HttpExchange;
import ui.Group;
import ui.Item;

public class postGroupHTTPCommand extends AbstractHTTPCommand {

    public postGroupHTTPCommand(HttpExchange he) {
        super(he);
    }

    @Override
    public void execute() throws IOException {
        if(!checkToken()) {
            sentFailure(409);
            return;
        }
        String request = MyHTTPServer.getRequestBody(he);
        JSONParser jsonParser = new JSONParser();
        String name = groupName();
        Object titleO = null;
        Object descriptionO = null;
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(request);
            titleO = (jsonObject.get("name"));
            descriptionO = (jsonObject.get("description"));
        } catch (Exception e) {
            sentFailure(500);
            return;
        }

        boolean changed = false;

        if (MyHTTPServer.sqlstore.getGroup(name)!=null&&titleO != null&&descriptionO!=null) {
            MyHTTPServer.sqlstore.editGroup(name, new Group(titleO.toString(), descriptionO.toString()));
            changed = true;
        }
        if (!changed) {
            sentFailure(409);
            return;
        }
        this.writeResponseBody(204, null);

    }

}
