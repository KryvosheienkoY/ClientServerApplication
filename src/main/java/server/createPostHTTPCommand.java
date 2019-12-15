package server;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.net.httpserver.HttpExchange;
import ui.Item;

public class createPostHTTPCommand extends AbstractHTTPCommand {

	public createPostHTTPCommand(HttpExchange he) {
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
		int amount = -1;
		double price = -1;
		int id = -1;
		Object titleO = null;
		Object amountO = null;
		Object descriptionO = null;
		Object manufacturerO = null;
		Object priceO = null;
		Object idO = null;
		try {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(request);
			titleO = (jsonObject.get("name"));
			amountO = (jsonObject.get("amount"));
			priceO = (jsonObject.get("price"));
			descriptionO = (jsonObject.get("description"));
			manufacturerO = (jsonObject.get("manufacturer"));
			idO = (jsonObject.get("id"));
		} catch (Exception e) {
			sentFailure(500);
			return;
		}

		boolean changed = false;
		if(titleO != null && descriptionO != null&& manufacturerO != null&& priceO != null &&amountO != null &&  idO != null){
			amount = Integer.parseInt(amountO.toString());
			if (amount >= 0) {
				price = Double.parseDouble(priceO.toString());
				if (price > 0) {
					id = Integer.parseInt(idO.toString());
					Item item = new Item(titleO.toString(), descriptionO.toString(), manufacturerO.toString(), price,amount);
					MyHTTPServer.sqlstore.updateItem(titleO.toString().hashCode(), item);
					changed = true;
				}
			}
		}
		if(!changed) {
			sentFailure(409);
			return;
		}
		this.writeResponseBody(204, null);
	}

}
