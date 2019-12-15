package server;
import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class deleteGroupHTTPCommand extends AbstractHTTPCommand {

    public deleteGroupHTTPCommand(HttpExchange he) {
        super(he);
    }

    @Override
    public void execute() throws IOException {
        if(!checkToken()) {
            sentFailure(409);
            return;
        }
        String id = groupName();
//        if(MyHTTPServer.sqlstore.getGroup(id)==null) {
//            this.writeResponseBody(404, null);
//            return;
//        }
        if(MyHTTPServer.sqlstore.getGroup(id)!=null) {
            MyHTTPServer.sqlstore.deleteGroup(id);
            this.writeResponseBody(204, null);
        }
        else
        {
            this.writeResponseBody(404, null);
        }
    }

}
