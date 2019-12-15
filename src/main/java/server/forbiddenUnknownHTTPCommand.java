package server;
import com.sun.net.httpserver.HttpExchange;

public class forbiddenUnknownHTTPCommand extends AbstractHTTPCommand {

	public forbiddenUnknownHTTPCommand(HttpExchange he) {
		super(he);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
	}

}
