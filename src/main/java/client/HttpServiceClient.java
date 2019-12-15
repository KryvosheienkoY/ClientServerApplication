package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ui.Group;
import ui.Item;
import ui.LabaRegister;

public class HttpServiceClient {
    public static ArrayList<Item> items=new ArrayList<>();
    private String token;
    private static LabaRegister reg;

    public static void main(String[] args) throws Exception {

        HttpServiceClient http = new HttpServiceClient();
          reg = new LabaRegister(http);
//        http.sendGetLogin("admin", "1234");
//        ArrayList<Group> it = http.sendGetGroups();
//        for(Group i: it)
//        {
//            System.out.println(i.getName());
//        }
//        http.sendGetGroup("Dairy");
//        http.sendGetProduct("Bread");
//        http.sendPutProduct(new Item("Mandarin", "sweet mandarins", "Turkey", 25.99, 20), new Group("Fruits","Sweet fruits"));
//        http.sendGetProduct("Mandarin");
//        http.sendDeleteProduct("Mandarin");
//        http.sendPostProduct("Orange", 300);
//        http.sendPostProduct("Bread", 10);

    }

    // HTTP GET request
    public void sendGetLogin(String login, String password) throws Exception {
        String url = "http://localhost:6666/login";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        // add request header
        con.setRequestProperty("login", login);
        con.setRequestProperty("password", encodePassword(password));
        int responseCode = con.getResponseCode();



        System.out.println("\nSending 'GET' request to URL : " + url);
        token = con.getHeaderField("token");
        System.out.println("Token (cl) - " + token);
        getResponse(con, responseCode);
        if (responseCode == 200) {
            this.reg.logIn.openMenu();
        } else {
            this.reg.logIn.addErrorWindow();
        }
        con.disconnect();
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

    // HTTP GET request
    public Item sendGetProduct(String name) throws Exception {

        long id = name.hashCode();
        String url = "http://localhost:6666/api/good/" + id;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // add request header
        con.setRequestProperty("token", token);
        // GET
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        String res= getResponse(con, responseCode);
        JSONParser pars = new JSONParser();
        JSONObject it = null;
        Item i=null;
        try {
            it = (JSONObject) pars.parse(res);

            i = new Item((String) it.get("name"), (String) it.get("description"), (String) it.get("manufacturer"), Double.parseDouble(it.get("price").toString()), Integer.parseInt(it.get("amount").toString()));
        }
        catch(Exception e)
        {
        }
        con.disconnect();
        return i;
    }
    public double sendGetTotalPrice() throws Exception {

        String url = "http://localhost:6666/api/price";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // add request header
        con.setRequestProperty("token", token);
        // GET
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        String res= getResponse(con, responseCode);
        JSONParser pars = new JSONParser();
        JSONObject it = (JSONObject) pars.parse(res);
        double value = Double.parseDouble(it.get("value").toString());
        con.disconnect();
        return value;
    }
    public double sendGetGroupPrice(String group) throws Exception {

        String url = "http://localhost:6666/api/price/"+group;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // add request header
        con.setRequestProperty("token", token);
        // GET
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        String res= getResponse(con, responseCode);
        JSONParser pars = new JSONParser();
        JSONObject it = (JSONObject) pars.parse(res);
        double value = Double.parseDouble(it.get("value").toString());
        con.disconnect();
        return value;
    }
    public int sendGetItemPrice(int id) throws Exception {

        String url = "http://localhost:6666/api/price/"+id;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // add request header
        con.setRequestProperty("token", token);
        // GET
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        String res= getResponse(con, responseCode);
        JSONParser pars = new JSONParser();
        JSONObject it = (JSONObject) pars.parse(res);
        int value = Integer.parseInt(it.get("value").toString());
        con.disconnect();
        return value;
    }
    // HTTP GET request
    public ArrayList<Item> sendGetProducts() throws Exception {

        String url = "http://localhost:6666/api/goods";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // add request header
        con.setRequestProperty("token", token);

        // GET
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        String str = getResponse(con, responseCode);
        JSONParser js= new JSONParser();
        JSONObject goods = (JSONObject) js.parse(str);
        int size = Integer.parseInt((String) goods.get("size"));
        ArrayList<Item> res = new ArrayList<>();
        for(int i=0; i<size; i++)
        {
            JSONObject it = (JSONObject) goods.get("item"+String.valueOf(i));
            res.add(new Item((String)it.get("name"), (String) it.get("description"), (String)it.get("manufacturer"), Double.parseDouble(it.get("price").toString()), Integer.parseInt(it.get("amount").toString())));
        }
        con.disconnect();
        return res;
    }
    public ArrayList<Group> sendGetGroups() throws Exception {

        String url = "http://localhost:6666/api/groups";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // add request header
        con.setRequestProperty("token", token);
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        String str = getResponse(con, responseCode);
        JSONParser js= new JSONParser();
        JSONObject goods = (JSONObject) js.parse(str);
        int size = Integer.parseInt((String) goods.get("size"));
        ArrayList<Group> res = new ArrayList<>();
        for(int i=0; i<size; i++)
        {
            JSONObject it = (JSONObject) goods.get("group"+String.valueOf(i));
            res.add(new Group((String)it.get("name"), (String) it.get("description")));
        }
        con.disconnect();
        return res;
    }


    public Group sendGetGroup(String name) throws Exception {

        String url = "http://localhost:6666/api/group/" + name;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // add request header
        con.setRequestProperty("token", token);

        // GET
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        String res= getResponse(con, responseCode);
        JSONParser js = new JSONParser();
        JSONObject parsed = null;
        Group gottenGroup = null;
        try {
            parsed = (JSONObject) js.parse(res);

            String n = (String) parsed.get("name");
            String d = (String) parsed.get("description");

            int length = Integer.parseInt(parsed.get("size").toString());
            gottenGroup = new Group(n, d);
            System.out.println(n + "  " + d + "  " + length);
            ArrayList<Item> ite = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                JSONObject it = (JSONObject) parsed.get("item" + String.valueOf(i));
                JSONParser ob = new JSONParser();
                //   JSONObject it = (JSONObject) ob.parse(curr);
                ite.add(new Item((String) it.get("name"), (String) it.get("description"), (String) it.get("manufacturer"), Double.parseDouble(it.get("price").toString()), Integer.parseInt(it.get("amount").toString())));
            }
            items = ite;
        }
        catch (Exception e)
        {

        }
        con.disconnect();
        return gottenGroup;
    }

    public void sendDeleteProduct(String name) throws Exception {
        long id = name.hashCode();
        String url = "http://localhost:6666/api/good/" + id;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // add request header
        con.setRequestProperty("token", token);
        // con.setRequestProperty("token", "t");
        con.setRequestMethod("DELETE");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'DELETE' request to URL : " + url);
        getResponse(con, responseCode);
        con.disconnect();
    }
    public void sendDeleteGroup(String name) throws Exception {
        String url = "http://localhost:6666/api/group/" + name;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // add request header
        con.setRequestProperty("token", token);
        // con.setRequestProperty("token", "t");
        con.setRequestMethod("DELETE");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'DELETE' request to URL : " + url);
        getResponse(con, responseCode);
        con.disconnect();
    }

    private String getResponse(HttpURLConnection con, int responseCode) {
        System.out.print("Response Code : " + responseCode + " ");
        BufferedReader in = null;
        StringBuffer response = new StringBuffer();
        try {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (IOException e) {
            if (responseCode == 404) {
                response.append("Not found");
            } else if (responseCode == 409) {
                response.append("Conflict");
            } else if (responseCode == 500) {
                response.append("Server error");
            }
            System.out.println(response);
            return "";
        }
        //print result
        return response.toString();
    }

    // HTTP PUT request
    public void sendPutProduct(Item product, Group group) throws Exception {

        String url = "http://localhost:6666/api/good";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // add request header
        con.setRequestProperty("token", token);
        con.setRequestMethod("PUT");
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        String name = product.getName();
        int amount = product.getAmount();
        double price = product.getPrice();
//        String group = product.getGroup();
        JSONObject js = new JSONObject();
        js.put("name", product.getName());
        js.put("description", product.getDescription());
        js.put("manufacturer", product.getManufacturer());
        js.put("amount", product.getAmount());
        js.put("price", product.getPrice());
        js.put("id", name.hashCode());
        js.put("group", group.getName());
        osw.write(js.toJSONString());
        osw.flush();
        osw.close();
        os.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'PUT' request to URL : " + url);
        getResponse(con, responseCode);
        con.disconnect();
    }

    public void sendPutGroup( Group group) throws Exception {

        String url = "http://localhost:6666/api/group";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // add request header
        con.setRequestProperty("token", token);
        con.setRequestMethod("PUT");
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        String name = group.getName();
        String descr = group.getDescription();
        JSONObject js = new JSONObject();
        js.put("name", name);
        js.put("description", descr);
        osw.write(js.toJSONString());
        osw.flush();
        osw.close();
        os.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'PUT' request to URL : " + url);
        getResponse(con, responseCode);
        con.disconnect();
    }
    // HTTP POST request
    public void sendPostProduct(String name, Item item) throws Exception {

        String url = "http://localhost:6666/api/good";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // add request header
        con.setRequestProperty("token", token);
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        int id = name.hashCode();
        JSONObject js = new JSONObject();
        js.put("name", name);
        js.put("description", item.getDescription());
        js.put("manufacturer", item.getManufacturer());
        js.put("price", item.getPrice());
        js.put("amount", item.getAmount());
        js.put("id", name.hashCode());
        osw.write(js.toJSONString());
        osw.flush();
        osw.close();
        os.close();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        getResponse(con, responseCode);
        con.disconnect();
    }


    public void sendPostGroup(String name, Group group) throws Exception {

        String url = "http://localhost:6666/api/group/"+name;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // add request header
        con.setRequestProperty("token", token);
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        JSONObject js = new JSONObject();
        js.put("name", group.getName());
        js.put("description", group.getDescription());
        osw.write(js.toJSONString());
        osw.flush();
        osw.close();
        os.close();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        getResponse(con, responseCode);
        con.disconnect();
    }
    public static void init(HttpServiceClient h)
    {
        reg=new LabaRegister(h);
    }
}
