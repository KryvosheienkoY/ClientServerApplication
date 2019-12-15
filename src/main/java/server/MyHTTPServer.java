package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import com.sun.net.httpserver.*;

import lab4.StoreDatabase;


public class MyHTTPServer {

    public static StoreDatabase sqlstore;
    public static ServerDataBase udb;

    public static void main(String[] args) throws Exception {
        sqlstore = new StoreDatabase();
        sqlstore.start("goods");

        udb = new ServerDataBase();
        udb.start("users");

        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(6666), 0);

        HttpContext context1 = server.createContext("/api/good", new EchoProductHandler());
        HttpContext context6 = server.createContext("/api/price", new EchoPriceHandler());
        HttpContext context4 = server.createContext("/api/goods", new EchoGoodsHandler());
        HttpContext context3 = server.createContext("/api/group", new EchoGroupHandler());
        HttpContext context5 = server.createContext("/api/groups", new EchoGroupsHandler());
        HttpContext context2 = server.createContext("/login", new EchoLoginHandler());

        server.setExecutor(null);
        server.start();
    }

    public static String getRequestBody(HttpExchange he) {
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(he.getRequestBody(), "utf-8");
            br = new BufferedReader(isr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int b;
        StringBuilder buf = new StringBuilder();
        while (true) {
            try {
                if ((b = br.read()) == -1)
                    break;
                buf.append((char) b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            br.close();
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    public static class EchoProductHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            String method = he.getRequestMethod();

            Command httpCommand = null;
            if (method.equals("GET")) {
                httpCommand = new getProductHTTPCommand(he);
            } else if (method.equals("PUT")) {
                httpCommand = new createProductHTTPCommand(he);
            } else if (method.equals("DELETE")) {
                httpCommand = new createDeleteHTTPCommand(he);
            } else if (method.equals("POST")) {
                httpCommand = new createPostHTTPCommand(he);
            } else {
                httpCommand = new forbiddenUnknownHTTPCommand(he);
            }
            httpCommand.setTraceMode(true);
            httpCommand.execute();
        }
    }

    public static class EchoPriceHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            String method = he.getRequestMethod();

            Command httpCommand = null;
            if (method.equals("GET")) {
                httpCommand = new getPriceHTTPCommand(he);
            }  else {
                httpCommand = new forbiddenUnknownHTTPCommand(he);
            }
            httpCommand.setTraceMode(true);
            httpCommand.execute();
        }
    }

    public static class EchoGoodsHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            String method = he.getRequestMethod();
            Command httpCommand  = new getGoodsHTTPCommand(he);
            httpCommand.setTraceMode(true);
            httpCommand.execute();
        }
    }

    public static class EchoGroupHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            String method = he.getRequestMethod();

            Command httpCommand = null;
            if (method.equals("GET")) {
                httpCommand = new getGroupHTTPCommand(he);
            } else if (method.equals("PUT")) {
                httpCommand = new createGroupHTTPCommand(he);
            } else if (method.equals("DELETE")) {
                httpCommand = new deleteGroupHTTPCommand(he);
            } else if (method.equals("POST")) {
                httpCommand = new postGroupHTTPCommand(he);
            } else {
                httpCommand = new forbiddenUnknownHTTPCommand(he);
            }
            httpCommand.setTraceMode(true);
            httpCommand.execute();
//            System.out.println(sqlstore.showAllData());
        }
    }

    public static class EchoLoginHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {

            String method = he.getRequestMethod();
            if (method.equals("GET")) {

                Headers headers = he.getRequestHeaders();
                List<String> login = headers.get("login");
                List<String> password = headers.get("password");
                String str = "OK";
                int code = 200;
                String pass = udb.selectUser(login.get(0));
                System.out.println(pass);
                System.out.println(password.get(0));
                if (login.isEmpty() || password.isEmpty() || udb.selectUser(login.get(0)) == null || !udb.selectUser(login.get(0)).equals(password.get(0))) {
                    str = "Unauthorized";
                    code = 401;
                } else {
                    String token = JWT.createJWT("auth",login.get(0), pass,1000000);
                    he.getResponseHeaders().set("token", token);
//                    userMap.add(token);
                    System.out.println("Token (server) - " + token);
                }
                byte[] ar = str.getBytes();
                he.sendResponseHeaders(code, ar.length);
                OutputStream os = he.getResponseBody();
                os.write(ar);
                os.close();
            }
        }

    }

    private static String encodePassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    static class Auth extends Authenticator {
        @Override
        public Result authenticate(HttpExchange httpExchange) {
            if ("/forbidden".equals(httpExchange.getRequestURI().toString()))
                return new Failure(409);
            else
                return new Success(new HttpPrincipal("c0nst", "realm"));
        }
    }

    private static class EchoGroupsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException {
            String method = he.getRequestMethod();
            Command httpCommand  = new getGroupsHTTPCommand(he);
            httpCommand.setTraceMode(true);
            httpCommand.execute();
        }
    }
}
