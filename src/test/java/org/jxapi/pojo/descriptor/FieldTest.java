package org.jxapi.pojo.descriptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link Field}
 */
public class FieldTest {
    @Test
    public void testCreateField() {
        Field field = new Field();
        Assert.assertNotNull(field);
    }

    @Test
    public void testGettersAndSetters() {
        Field field = new Field();
        field.setName("name");
        field.setType("OBJECT_MAP");
        field.setDescription("description");
        field.setSampleValue("sampleValue");
        field.setMsgField("f");
        field.setObjectName("MyObject");
        field.setObjectDescription("The object description");
        field.setProperties(List.of());
        field.setImplementedInterfaces(List.of("com.x.y.MyInterface"));
        field.setIn(UrlParameterType.QUERY);
        Assert.assertEquals("name", field.getName());
        Assert.assertEquals(Type.fromTypeName("OBJECT_MAP"), field.getType());
        Assert.assertEquals("description", field.getDescription());
        Assert.assertEquals("sampleValue", field.getSampleValue());
        Assert.assertEquals("f", field.getMsgField());
        Assert.assertEquals("MyObject", field.getObjectName());
        Assert.assertEquals("The object description", field.getObjectDescription());
        Assert.assertEquals(List.of(), field.getProperties());
        Assert.assertEquals(List.of("com.x.y.MyInterface"), field.getImplementedInterfaces());
        Assert.assertEquals(UrlParameterType.QUERY, field.getIn());
    }

    @Test
    public void testDeepCloneObjectField() {
        Field field = new Field();
        field.setName("name");
        field.setType(Type.fromTypeName("OBJECT_MAP"));
        field.setDescription("description");
        field.setSampleValue("sampleValue");
        field.setMsgField("f");
        field.setObjectName("MyObject");
        field.setObjectDescription("The object description");
        field.setProperties(List.of());
        field.setImplementedInterfaces(List.of("com.x.y.MyInterface"));

        field = field.deepClone();

        Assert.assertEquals("name", field.getName());
        Assert.assertEquals(Type.fromTypeName("OBJECT_MAP") , field.getType());
        Assert.assertEquals("description", field.getDescription());
        Assert.assertEquals("The object description", field.getObjectDescription());
        Assert.assertEquals("sampleValue", field.getSampleValue());
        Assert.assertEquals("f", field.getMsgField());
        Assert.assertEquals("MyObject", field.getObjectName());
        Assert.assertEquals(List.of(), field.getProperties());
        Assert.assertEquals(List.of("com.x.y.MyInterface"), field.getImplementedInterfaces());
    }

    @Test
    public void testDeepCloneSimpleField() {
        Field field = new Field();
        field.setName("name");
        field.setType("STRING");
        field.setDescription("description");
        field.setSampleValue("sampleValue");
        field.setMsgField("f");
        field.setIn(UrlParameterType.PATH);

        field = field.deepClone();

        Assert.assertEquals("name", field.getName());
        Assert.assertEquals(Type.STRING , field.getType());
        Assert.assertEquals("description", field.getDescription());
        Assert.assertEquals("sampleValue", field.getSampleValue());
        Assert.assertEquals("f", field.getMsgField());
        Assert.assertEquals(UrlParameterType.PATH, field.getIn());
    }

    @Test
    public void testToString() {
        Field field = new Field();
        field.setName("name");
        field.setType("STRING");
        field.setDescription("description");
        field.setSampleValue("sampleValue");
        field.setMsgField("f");
        Assert.assertEquals("Field{\"name\":\"name\",\"description\":\"description\",\"type\":{\"canonicalType\":\"STRING\",\"object\":false},\"sampleValue\":\"sampleValue\",\"msgField\":\"f\"}",
                            field.toString());
    }
    
    @Test
    public void testHashCodeIsToStringHashCode() {
      Field field = new Field();
        field.setName("name");
        field.setType("STRING");
        field.setDescription("description");
        field.setSampleValue("sampleValue");
        field.setMsgField("f");
        Assert.assertEquals(field.toString().hashCode(), field.hashCode());
    }
    
    @Test
    public void testEquals_NotEqualsOtherNull() {
      Field field = new Field();
    // Remark: Sonar issue about usage of equals is not relevant, assertNotEquals
    // would skip call to Field.equals() using null as expected value
      Assert.assertFalse(field.equals(null));
    }
    
    @Test
    public void testEquals_NotEqualsOtherNotField() {
      Field field = new Field();
      Assert.assertNotEquals(new Object(), field);
    }
    
    @Test
    public void testEquals_NotEqualsOtherDifferentField() {
        Field f1 = new Field();
        f1.setName("name");
        f1.setType("STRING");
        f1.setDescription("description");
        f1.setSampleValue("sampleValue");
        f1.setMsgField("f");
        
        Field f2 = new Field();
        f2.setName("bar");
        f2.setType("STRING");
        f2.setDescription("description");
        f2.setSampleValue("sampleValue");
        f2.setMsgField("f");
      Assert.assertNotEquals(f1, f2);
    }
    
    @Test
    public void testEquals_SameField() {
        Field f1 = new Field();
        f1.setName("name");
        f1.setType("STRING");
        f1.setDescription("description");
        f1.setSampleValue("sampleValue");
        f1.setMsgField("f");
        
        Field f2 = new Field();
        f2.setName("name");
        f2.setType("STRING");
        f2.setDescription("description");
        f2.setSampleValue("sampleValue");
        f2.setMsgField("f");
      Assert.assertEquals(f1, f2);
    }
    
    @Test
    public void testBuilder() {
      FieldBuilder fb = Field.builder();
      Assert.assertNotNull(fb);
    }
    
    private Field roundTrip(Field f) throws Exception {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bos);
      out.writeObject(f);
      out.close();

      ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
      ObjectInputStream in = new ObjectInputStream(bis);
      Field clone = (Field) in.readObject();
      in.close();

      return clone;
  }

  @Test
  public void testSerializationWithSerializableValues() throws Exception {
      Field f = new Field();
      f.setName("test");
      f.setDescription("desc");
      f.setSampleValue("a string");              // Serializable
      f.setDefaultValue(List.of(1, 2, 3));       // Serializable list

      Field clone = roundTrip(f);

      assertEquals("test", clone.getName());
      assertEquals("desc", clone.getDescription());
      assertEquals("a string", clone.getSampleValue());
      assertEquals(List.of(1, 2, 3), clone.getDefaultValue());
  }

  @Test
  public void testSerializationWithNullValues() throws Exception {
      Field f = new Field();
      f.setName("nullTest");
      f.setSampleValue(null);
      f.setDefaultValue(null);

      Field clone = roundTrip(f);

      assertNull(clone.getSampleValue());
      assertNull(clone.getDefaultValue());
      assertEquals("nullTest", clone.getName());
  }

  @Test(expected = NotSerializableException.class)
  public void testSerializationFailsWhenSampleValueNotSerializable() throws Exception {
      Field f = new Field();
      f.setSampleValue(new Object()); // NOT Serializable

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bos);
      out.writeObject(f); // should throw
  }

  @Test(expected = NotSerializableException.class)
  public void testSerializationFailsWhenDefaultValueNotSerializable() throws Exception {
      Field f = new Field();
      f.setDefaultValue(new Object()); // NOT Serializable

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bos);
      out.writeObject(f); // should throw
  }
    
    
}
