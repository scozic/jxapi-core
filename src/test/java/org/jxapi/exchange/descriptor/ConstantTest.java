package org.jxapi.exchange.descriptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.gen.ConstantDescriptor;
import org.jxapi.pojo.descriptor.Type;

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
    
    @Test
    public void testDeepClone() {
        // Create a Constant instance
        Constant original = new Constant();
        original.setName("original");
        original.setDescription("Original constant");
        original.setType(Type.STRING);
        original.setValue("value");

        // Create a nested constant group
        Constant nested = new Constant();
        nested.setName("nested");
        nested.setDescription("Nested constant");
        nested.setType(Type.INT);
        nested.setValue(42);

        original.setConstants(Arrays.asList(nested));

        // Perform deep clone
        Constant clone = original.deepClone();

        // Verify the cloned object is not the same instance
        Assert.assertNotSame(original, clone);

        // Verify the cloned object has the same properties
        Assert.assertEquals(original, clone);

        // Verify the nested constants are deep cloned
        Assert.assertNotSame(original.getConstants(), clone.getConstants());
        Assert.assertEquals(original.getConstants(), clone.getConstants());
        Assert.assertNotSame(original.getConstants().get(0), clone.getConstants().get(0));
        Assert.assertEquals(original.getConstants().get(0), clone.getConstants().get(0));
    }
    
    @Test
    public void testCompareTo() {
        // Case 1: Both names are null
        Constant constant1 = new Constant();
        constant1.setName(null);

        Constant constant2 = new Constant();
        constant2.setName(null);

        Assert.assertEquals(0, constant1.compareTo(constant2));

        // Case 2: This name is null, other is not
        constant2.setName("name");
        Assert.assertTrue(constant1.compareTo(constant2) < 0);

        // Case 3: Other name is null, this is not
        constant1.setName("name");
        constant2.setName(null);
        Assert.assertTrue(constant1.compareTo(constant2) > 0);

        // Case 4: Both names are non-null and equal
        constant2.setName("name");
        Assert.assertEquals(0, constant1.compareTo(constant2));

        // Case 5: This name is lexicographically less than other
        constant1.setName("alpha");
        constant2.setName("beta");
        Assert.assertTrue(constant1.compareTo(constant2) < 0);

        // Case 6: This name is lexicographically greater than other
        constant1.setName("gamma");
        constant2.setName("beta");
        Assert.assertTrue(constant1.compareTo(constant2) > 0);

        // Case 7: Comparing with null object
        Assert.assertTrue(constant1.compareTo(null) > 0);
    }
    
    @Test
    public void testFromConstantDescriptor() {
        // Create a ConstantDescriptor
        ConstantDescriptor descriptor = new ConstantDescriptor();
        descriptor.setName("myGroupConstant");
        descriptor.setDescription("This is a test group constant");
        descriptor.setValue("TestValue");

        // Create a nested ConstantDescriptor
        ConstantDescriptor nestedDescriptor = new ConstantDescriptor();
        nestedDescriptor.setName("myNestedConstant");
        nestedDescriptor.setDescription("This is a nested constant");
        nestedDescriptor.setType(Type.INT.toString());
        nestedDescriptor.setValue(42);

        descriptor.setConstants(Arrays.asList(nestedDescriptor));

        // Convert using fromConstantDescriptor
        Constant constant = Constant.fromConstantDescriptor(descriptor);

        // Verify the converted Constant
        Assert.assertNotNull(constant);
        Assert.assertEquals("myGroupConstant", constant.getName());
        Assert.assertEquals("This is a test group constant", constant.getDescription());
        Assert.assertNull(constant.getType());
        Assert.assertEquals("TestValue", constant.getValue());

        // Verify the nested Constant
        Assert.assertNotNull(constant.getConstants());
        Assert.assertEquals(1, constant.getConstants().size());
        Constant nestedConstant = constant.getConstants().get(0);
        Assert.assertEquals("myNestedConstant", nestedConstant.getName());
        Assert.assertEquals("This is a nested constant", nestedConstant.getDescription());
        Assert.assertEquals(Type.INT, nestedConstant.getType());
        Assert.assertEquals(42, nestedConstant.getValue());
    }
    
    @Test
    public void testFromDescriptors() {
      // Create multiple ConstantDescriptors
      ConstantDescriptor descriptor1 = new ConstantDescriptor();
      descriptor1.setName("firstConstant");
      descriptor1.setType(Type.STRING.toString());
      descriptor1.setDescription("First constant");
      descriptor1.setValue("ValueOne");

      ConstantDescriptor descriptor2 = new ConstantDescriptor();
      descriptor2.setName("secondConstant");
      descriptor2.setDescription("Second constant");
      descriptor2.setType(Type.INT.toString());
      descriptor2.setValue(2);

      List<ConstantDescriptor> descriptors = Arrays.asList(descriptor1, descriptor2);

      // Convert using fromDescriptors
      List<Constant> constants = Constant.fromDescriptors(descriptors);

      // Verify the converted Constants
      Assert.assertNotNull(constants);
      Assert.assertEquals(2, constants.size());

      Constant constant1 = constants.get(0);
      Assert.assertEquals("firstConstant", constant1.getName());
      Assert.assertEquals(Type.STRING, constant1.getType());
      Assert.assertEquals("First constant", constant1.getDescription());
      Assert.assertEquals("ValueOne", constant1.getValue());
      Constant constant2 = constants.get(1);
      Assert.assertEquals("secondConstant", constant2.getName());
      Assert.assertEquals(Type.INT, constant2.getType());
      Assert.assertEquals("Second constant", constant2.getDescription());
      Assert.assertEquals(2, constant2.getValue());
    }
    
    private Constant roundTrip(Constant c) throws Exception {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bos);
      out.writeObject(c);
      out.close();

      ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
      ObjectInputStream in = new ObjectInputStream(bis);
      Constant clone = (Constant) in.readObject();
      in.close();

      return clone;
  }

  @Test
  public void testSerializationWithSerializableValue() throws Exception {
      Constant c = new Constant();
      c.setName("PI");
      c.setDescription("Math constant");
      c.setType(Type.BIGDECIMAL);
      c.setValue("3.14159"); // Serializable

      Constant clone = roundTrip(c);

      assertEquals("PI", clone.getName());
      assertEquals("Math constant", clone.getDescription());
      assertEquals(Type.BIGDECIMAL, clone.getType());
      assertEquals("3.14159", clone.getValue());
  }

  @Test
  public void testSerializationWithNullValue() throws Exception {
      Constant c = new Constant();
      c.setName("NULL_TEST");
      c.setValue(null);

      Constant clone = roundTrip(c);

      assertNull(clone.getValue());
      assertEquals("NULL_TEST", clone.getName());
  }

  @Test(expected = NotSerializableException.class)
  public void testSerializationFailsWhenValueNotSerializable() throws Exception {
      Constant c = new Constant();
      c.setValue(new Object()); // NOT Serializable

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bos);
      out.writeObject(c); // should throw
  }

  @Test
  public void testSerializationOfGroupConstant() throws Exception {
      Constant child = Constant.create("CHILD", Type.STRING, "child desc", "value");
      Constant parent = Constant.createGroup("PARENT", "parent desc", List.of(child));

      Constant clone = roundTrip(parent);

      assertEquals("PARENT", clone.getName());
      assertEquals("parent desc", clone.getDescription());
      assertNotNull(clone.getConstants());
      assertEquals(1, clone.getConstants().size());
      assertEquals("CHILD", clone.getConstants().get(0).getName());
  }
}
