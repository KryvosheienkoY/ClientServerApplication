package server;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.net.httpserver.HttpExchange;
import ui.Group;


public class createGroupHTTPCommand extends AbstractHTTPCommand {

    public createGroupHTTPCommand(HttpExchange he) {
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
        String title = null;
        String description = null;
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(request);
            title = (jsonObject.get("name")).toString();
            description = (jsonObject.get("description")).toString();
        } catch (Exception e) {
            sentFailure(500);
            return;
        }

        if (title == null  || description == null) {
            sentFailure(409);
            return;
        }

        Group item = new Group(title, description);
        System.out.println("Group (PUT) - " + item.getName());
        String answer;
        if (MyHTTPServer.sqlstore.getGroup(item.getName())==null) {
            MyHTTPServer.sqlstore.addGroup(item);
            answer = "Created " + item.getName();
            this.writeResponseBody(201, answer.getBytes("UTF-8"));
        } else {
            answer = "Conflict";
            this.writeResponseBody(409, answer.getBytes("UTF-8"));
        }

    }

}
