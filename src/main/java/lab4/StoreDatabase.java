package lab4;

import org.sqlite.SQLiteException;
import ui.Group;
import ui.Item;

import java.sql.*;
import java.util.ArrayList;

public class StoreDatabase {
    private Connection connection;
    private static int gID;

    public static void main(String[] args) {
        StoreDatabase store = new StoreDatabase();
        store.start("goods");
        Group fr = store.getGroup("Fruits");
//        System.out.println("Price of fruit - "+store.get);


//        ArrayList<Item> arr = store.getAllProducts();
//        for (Item it : arr) {
//            System.out.println(it.toString());
//        }
    }

    public void start(String url) {
        try {
            String driverName = "org.sqlite.JDBC";
            Class.forName(driverName);
            connection = DriverManager.getConnection("jdbc:sqlite:" + url);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM 'group'");
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                gID = Math.max(gID, res.getInt(1) + 1);
            }
            res.close();
            statement.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean addGroup(Group g) {
        try (PreparedStatement st = connection.prepareStatement("INSERT into 'group'(GROUPID, NAME, DESCRIPTION) values(?,?,?)")) {
            st.setInt(1, gID++);
            st.setString(2, g.getName());
            st.setString(3, g.getDescription());
            st.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void editGroup(String name, Group group) {
        try (PreparedStatement st = connection.prepareStatement("UPDATE 'group' SET NAME =?, DESCRIPTION=? WHERE NAME=?")) {
            st.setString(3, name);
            st.setString(1, group.getName());
            st.setString(2, group.getDescription());
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Group selectGroup(String name) {
        try (PreparedStatement st = connection.prepareStatement("SELECT NAME, DESCRIPTION from 'group'  WHERE NAME=?")) {
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return new Group(rs.getString("NAME"), rs.getString("DESCRIPTION"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public ArrayList<Group> getGroups() {
        ArrayList<Group> groups = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement("SELECT * from 'group'")) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                groups.add(new Group(rs.getString("NAME"), rs.getString("DESCRIPTION")));
            }
            return groups;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteGroup(String name) {
        try (PreparedStatement st = connection.prepareStatement("DELETE FROM 'group' WHERE NAME=?")) {
            st.setString(1, name);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getGroupID(String name) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT GROUPID from 'group' where NAME=?")) {
            ps.setString(1, name);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                return res.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Group getGroup(String name) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT DESCRIPTION from 'group' where NAME=?")) {
            ps.setString(1, name);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                return new Group(name, res.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean addItem(String group, Item item) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO 'item'(ID, NAME, DESCRIPTION,MANUFACTURER, AMOUNT, PRICE, VALUE, GROUPID) VALUES (?,?,?,?,?,?,?,?)")) {
            statement.setInt(1, item.getName().hashCode());
            statement.setString(2, item.getName());
            statement.setString(3, item.getDescription());
            statement.setString(4, item.getManufacturer());
            statement.setInt(5, item.getAmount());
            statement.setDouble(6, item.getPrice());
            statement.setDouble(7, item.getPrice() * item.getAmount());
            statement.setInt(8, getGroupID(group));
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLiteException e) {
            System.out.println("Товар вже існує");

            return false;
        } catch (SQLException e) {
            System.out.println("Не вірний SQL запит на вставку");

            e.printStackTrace();
            return false;
        }
    }


    public Item selectItem(int item) {
        Item pr = null;
        try (PreparedStatement st = connection.prepareStatement("SELECT NAME, DESCRIPTION, MANUFACTURER, PRICE, AMOUNT FROM 'item' WHERE ID = ?")) {
            st.setInt(1, item);
            ResultSet res = st.executeQuery();
            //   printlnPrResults(res);
            while (res.next()) {
                pr = new Item(res.getString("NAME"), res.getString("DESCRIPTION"), res.getString("MANUFACTURER"), res.getDouble("PRICE"), res.getInt("AMOUNT"));
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pr;
    }

    public void deleteItem(int item) {

        try (PreparedStatement st = connection.prepareStatement("DELETE FROM item WHERE ID = ? ")) {
            st.setInt(1, item);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean changeAmount(int item, int amount) {
        try {
            Item it = selectItem(item);
            PreparedStatement statement = connection.prepareStatement("UPDATE item set AMOUNT = ?, VALUE=? where ID = ?;");
            int total = it.getAmount() + amount;
            if (total < 0)
                return false;
            statement.setInt(1, it.getAmount() + amount);
            statement.setDouble(2, it.getPrice() * total);
            statement.setInt(3, item);
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateItem(int name, Item item) {
        try (PreparedStatement st = connection.prepareStatement("UPDATE 'item' SET NAME=?, DESCRIPTION=?, MANUFACTURER=?, PRICE=?, ID=?, AMOUNT=?, VALUE=?  WHERE ID=?")) {
            st.setString(1, item.getName());
            st.setString(2, item.getDescription());
            st.setString(3, item.getManufacturer());
            st.setDouble(4, item.getPrice());
            st.setInt(5, item.getName().hashCode());
            st.setDouble(6, item.getAmount());
            st.setDouble(7, item.getPrice() * item.getAmount());
            st.setInt(8, name);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItemGroup(int item, String group) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE item set GROUPID = ? where ID = ?")) {
            statement.setInt(1, getGroupID(group));
            System.out.println("New group id " + getGroupID(group));
            statement.setInt(2, item);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public double getItemValue(int id) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT VALUE from 'item' where ID=?")) {
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                return res.getDouble("VALUE");
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getGroupValue(int groupId) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT SUM(VALUE) from 'item' WHERE GROUPID=? ")) {
            ps.setInt(1, groupId);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                double d = res.getDouble(1);
                return d;
            }
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public double getTotalValue() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT SUM(VALUE) from 'item'")) {
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                return res.getDouble(1);
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public ArrayList<Item> getAllProducts() {
        ArrayList<Item> products = new ArrayList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet res = st.executeQuery("SELECT * FROM item");
            while (res.next()) {
                Item p = new Item(res.getString("NAME"), res.getString("DESCRIPTION"), res.getString("MANUFACTURER"), res.getDouble("PRICE"), res.getInt("AMOUNT"));
                products.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public ArrayList<Item> getAllProductsFromGroup(String group) {
        ArrayList<Item> products = new ArrayList<>();
        try (PreparedStatement pr = connection.prepareStatement("SELECT * FROM item WHERE GROUPID=?")) {
            pr.setInt(1, getGroupID(group));
            ResultSet res = pr.executeQuery();
            while (res.next()) {
                Item p = new Item(res.getString("NAME"), res.getString("DESCRIPTION"), res.getString("MANUFACTURER"), res.getDouble("PRICE"), res.getInt("AMOUNT"));
                products.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }


    public void clear() {
        String sql = "DELETE FROM 'group'";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
