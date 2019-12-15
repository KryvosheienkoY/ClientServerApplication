package server;
import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class createDeleteHTTPCommand extends AbstractHTTPCommand {

	public createDeleteHTTPCommand(HttpExchange he) {
		super(he);
	}

	@Override
	public void execute() throws IOException {
		if(!checkToken()) {
			sentFailure(409);
			return;
		}
		int id = goodsId();
		MyHTTPServer.sqlstore.deleteItem(id);
		this.writeResponseBody(204, null);
	}

}
