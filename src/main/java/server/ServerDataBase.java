package server;

import java.sql.*;

public class ServerDataBase {
    private Connection connection;

    public void start(String url)
    {
        try {
            String driverName = "org.sqlite.JDBC";
            Class.forName(driverName);
            connection= DriverManager.getConnection("jdbc:sqlite:" + url);

            PreparedStatement st = connection.prepareStatement("create table if not exists users " +
                    "('LOGIN' text UNIQUE , 'PASSWORD' text);");


            st.executeUpdate();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public boolean addUser(String l, String p) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users(LOGIN, PASSWORD) VALUES (?,?)");
            statement.setString(1, l);
            statement.setString(2, p);


            if (selectUser(l)==null)
                statement.executeUpdate();
            else {
                statement.close();
                return false;
            }
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }





    public String selectUser(String item) {
        String pw = null;
        try {
            PreparedStatement st = connection.prepareStatement("SELECT password FROM users WHERE login = ?");
            st.setString(1, item);
            ResultSet res = st.executeQuery();
            //   printlnPrResults(res);
            while (res.next()) {
                pw = res.getString("password");
            }
            res.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pw;
    }

    public void deleteUser(String id) {

        try {
            PreparedStatement st = connection.prepareStatement("DELETE FROM users WHERE login = ? ");
            st.setString(1, id);
            st.executeUpdate();

            st.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void clear(){
        String sql = "DELETE FROM users";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public boolean setNewPassword(String l, String p)
    {

        String query = "UPDATE users SET PASSWORD = ? WHERE LOGIN = ?";

        try {
            PreparedStatement pr = connection.prepareStatement(query);
            pr.setString(2, l);
            pr.setString(1, p);
            pr.executeUpdate();
            pr.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}