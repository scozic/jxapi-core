package org.jxapi.pojo.descriptor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link FieldBuilder}
 */
public class FieldBuilderTest {

    @Test
    public void testBuild() {
        FieldBuilder builder = new FieldBuilder();
        Field field = builder.build();
        Assert.assertNotNull(field);
    }

    @Test
    public void testSetName() {
        FieldBuilder builder = new FieldBuilder();
        Field field = builder.name("name").build();
        Assert.assertEquals("name", field.getName());
    }

    @Test
    public void testSetType() {
        FieldBuilder builder = new FieldBuilder();
        Field field = builder.type(Type.STRING).build();
        Assert.assertEquals(Type.STRING, field.getType());
    }

    @Test
    public void testSetTypeStr() {
        FieldBuilder builder = new FieldBuilder();
        Type type = Type.fromTypeName("INT_LIST_MAP");
        Field field = builder.type(type.toString()).build();
        Assert.assertEquals(type, field.getType());
    }
    
    @Test
    public void testSetDescription() {
        FieldBuilder builder = new FieldBuilder();
        Field field = builder.description("description").build();
        Assert.assertEquals("description", field.getDescription());
    }

    @Test
    public void testSetSampleValue() {
        FieldBuilder builder = new FieldBuilder();
        Field field = builder.sampleValue("foo").build();
        Assert.assertEquals("foo", field.getSampleValue());
    }

    @Test
    public void testSetMsgField() {
        FieldBuilder builder = new FieldBuilder();
        Field field = builder.msgField("f").build();
        Assert.assertEquals("f", field.getMsgField());
    }

    @Test
    public void testSetProperties() {
        FieldBuilder builder = new FieldBuilder();
        Field f1 = new Field();
        f1.setName("foo");
        Field f2 = new Field();
        f2.setName("bar");
        List<Field> properties = List.of(f1, f2);
        Field field = builder.properties(properties).build();
        Assert.assertEquals(properties, field.getProperties());
    }

    @Test
    public void testSetProperty() {
        FieldBuilder builder = new FieldBuilder();
        Field f1 = new Field();
        f1.setName("foo");
        Field f2 = new Field();
        f2.setName("bar");
        Field field = builder.property(f1).property(f2).build();
        Assert.assertEquals(List.of(f1, f2), field.getProperties());
    }

    @Test
    public void testSetObjectName() {
        FieldBuilder builder = new FieldBuilder();
        Field field = builder.objectName("Foo").build();
        Assert.assertEquals("Foo", field.getObjectName());
    }
    
    @Test
    public void testSetObjectDescription() {
        FieldBuilder builder = new FieldBuilder();
        Field field = builder.objectDescription("Foo").build();
        Assert.assertEquals("Foo", field.getObjectDescription());
    }

    @Test
    public void testSetImplmentedInterfaces() {
        FieldBuilder builder = new FieldBuilder();
        Field field = builder.implementedInterfaces(List.of("com.x.y.Foo", "com.x.z.Bar")).build();
        Assert.assertEquals(List.of("com.x.y.Foo", "com.x.z.Bar"), field.getImplementedInterfaces());
    }

    @Test
    public void testSetImplementedInterface() {
        FieldBuilder builder = new FieldBuilder();
        Field field = builder.implementedInterface("com.x.y.Foo")
                             .implementedInterface("com.x.z.Bar").build();
        Assert.assertEquals(List.of("com.x.y.Foo", "com.x.z.Bar"), field.getImplementedInterfaces());
    }   
    
    @Test
    public void testSetIn() {
      FieldBuilder builder = new FieldBuilder();
      Field field = builder.in(UrlParameterType.QUERY).build();
      Assert.assertEquals(UrlParameterType.QUERY, field.getIn());
    }
    
    @Test
    public void testSetDefaultValue() {
      FieldBuilder builder = new FieldBuilder();
      Field field = builder.defaultValue(123).build();
      Assert.assertEquals(Integer.valueOf(123), field.getDefaultValue());
    }
}
