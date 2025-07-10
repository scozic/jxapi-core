package org.jxapi.exchange.descriptor;

import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link Constant}
 */
public class ConstantTest {

    @Test
    public void testCreate() {
        Constant c = Constant.create("name", Type.LONG, "description", "12345678901");
        Assert.assertEquals("name", c.getName());
        Assert.assertEquals(Type.LONG, c.getType());
        Assert.assertEquals("description", c.getDescription());
        Assert.assertEquals("12345678901", c.getValue());
    }
    
    @Test
    public void testCreateGroup() {
      Constant foo = Constant.create("foo", Type.STRING, "foo desc", "Foo!");
      Constant bar = Constant.create("bar", Type.INT, "bar desc", 123);
      List<Constant> constants = List.of(foo, bar);
      Constant g = Constant.createGroup("myGroup", "A constant group", constants);
      Assert.assertEquals("myGroup", g.getName());
      Assert.assertEquals("A constant group", g.getDescription());
      Assert.assertEquals(constants, g.getConstants());
    }

    @Test
    public void testSettersAndGetters() {
        Constant c = new Constant();
        Assert.assertEquals(Type.STRING, c.getType());
        c.setName("name");
        c.setType(Type.INT);
        c.setDescription("description");
        c.setValue(456);
        Assert.assertEquals("name", c.getName());
        Assert.assertEquals(Type.INT, c.getType());
        Assert.assertEquals("description", c.getDescription());
        Assert.assertEquals(456, c.getValue());
        c.setType("LONG_LIST");
        Assert.assertEquals(Type.fromTypeName("LONG_LIST"), c.getType());
    }
    
    @Test
    public void testSettersAndGetters_ConstantGroup() {
        Constant group = new Constant();
        group.setName("myGroup");
        group.setDescription("A constant group");
        Constant foo = Constant.create("foo", Type.STRING, "foo desc", "Foo!");
        Constant bar = Constant.create("bar", Type.INT, "bar desc", 123);
        List<Constant> constants = List.of(foo, bar);
        group.setConstants(constants);
        Assert.assertEquals("myGroup", group.getName());
        Assert.assertEquals("A constant group", group.getDescription());
        Assert.assertEquals(constants, group.getConstants());
    }
    
    @Test
    public void testIsGroup() {
      Constant group = new Constant();
      group.setName("myGroup");
      group.setDescription("A constant group");
      Assert.assertFalse(group.isGroup());
      group.setConstants(List.of());
      Assert.assertFalse(group.isGroup());
      Constant foo = Constant.create("foo", Type.STRING, "foo desc", "Foo!");
      Constant bar = Constant.create("bar", Type.INT, "bar desc", 123);
      List<Constant> constants = List.of(foo, bar);
      group.setConstants(constants);
      Assert.assertTrue(group.isGroup());
    }

    @Test
    public void testToString() {
        Constant c = Constant.create("name", Type.STRING, "description", "value");
        Assert.assertEquals("Constant{\"name\":\"name\",\"description\":\"description\",\"value\":\"value\",\"type\":{\"canonicalType\":\"STRING\",\"object\":false},\"group\":false}",
                            c.toString());
        
    }
    
    @Test
    public void testEquals() {
      Constant c1 = Constant.create("name", Type.STRING, "description", "value");
      Constant c2 = Constant.create("name", Type.STRING, "description", "value");
      Assert.assertEquals(c1, c2);
      Assert.assertFalse(c1.equals(null));
      Assert.assertFalse(c1.equals(new Object()));
      Assert.assertNotEquals(c1, new Object());
      Assert.assertEquals(c1, c1);
      Assert.assertEquals(c1.hashCode(), c2.hashCode());

      c2.setValue("new value");
      Assert.assertNotEquals(c1, c2);
      Assert.assertNotEquals(c1.hashCode(), c2.hashCode());

      c2.setValue("value");
      c2.setName("new name");
      Assert.assertNotEquals(c1, c2);
      
      c2.setName("name");
      c2.setDescription("new description");
      Assert.assertNotEquals(c1, c2);
      
      c2.setDescription("description");
      c2.setType(Type.INT);
      Assert.assertNotEquals(c1, c2);
      
      c2.setType(Type.STRING);
      c2.setConstants(List.of(Constant.create("nested", Type.STRING, "nested desc", "nested value")));
      Assert.assertNotEquals(c1, c2);
    }
    
    @Test
    public void testHashCode() {
      Constant c1 = Constant.create("name", Type.STRING, "description", "value");
      Assert.assertEquals(Objects.hash("name", Type.STRING, "value", "description", null), c1.hashCode());
      Constant c2 = Constant.createGroup("myGroup", "A group", List.of(c1));
      Assert.assertEquals(Objects.hash("myGroup", Type.STRING,  null, "A group", List.of(c1)), c2.hashCode());
    }
}
