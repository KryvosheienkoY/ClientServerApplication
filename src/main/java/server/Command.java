package server;
import java.io.IOException;

public interface Command {

	public void execute() throws IOException;

	public void setTraceMode(boolean traceMode);

}
