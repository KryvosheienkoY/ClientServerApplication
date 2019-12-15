import client.HttpServiceClient;
import org.junit.Assert;
import org.junit.Test;
import ui.Group;
import ui.Item;
import ui.LabaRegister;

import java.util.ArrayList;

public class ProjectTest
{

    @Test
    public void testGet()
    {
        HttpServiceClient http = new HttpServiceClient();
        http.init(http);
        try {
            http.sendGetLogin("admin", "1234");
            Item exists = http.sendGetProduct("Milk");
            Assert.assertTrue(exists!=null);
            Item notexists = http.sendGetProduct("sdfghjkl");
            Assert.assertTrue(notexists==null);
            Group g = http.sendGetGroup("Dairy");
            Assert.assertTrue(g!=null);
            Group ng= http.sendGetGroup("ertyui");
            Assert.assertTrue(ng==null);
            ArrayList<Group> groups = http.sendGetGroups();
            Assert.assertTrue(groups!=null);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }

    }
    @Test
    public void testAdd()
    {
        HttpServiceClient http = new HttpServiceClient();
        http.init(http);
        try {
            http.sendGetLogin("admin", "1234");
            Item exists = http.sendGetProduct("Buttermilk");
            if(exists==null)
            {
                http.sendPutProduct(new Item("Buttermilk", "Fat cow smoothie", "cow", 14.55, 40),http.sendGetGroup("Dairy"));
            }

//            Assert.assertTrue(http.sendGetProduct("Buttermilk")!=null);
//            Item notexists = http.sendGetProduct("sdfghjkl");
//            Assert.assertTrue(notexists==null);
            Group g = http.sendGetGroup("Tools");
            if(g==null)
            {
                http.sendPutGroup(new Group("Tools", "Useful in household"));
            }
//            Assert.assertTrue(g!=null);
            Group ng= http.sendGetGroup("Tools");
            Assert.assertTrue(ng!=null);
//            ArrayList<Group> groups = http.sendGetGroups();
//            Assert.assertTrue(groups!=null);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }

    }

    @Test
    public void testDelete() {
        HttpServiceClient http = new HttpServiceClient();
        http.init(http);
        try {
            http.sendGetLogin("admin", "1234");
            Item exists = http.sendGetProduct("Buttermilk");
            if (exists == null) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }
}
