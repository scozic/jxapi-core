package org.jxapi.pojo.descriptor.parser;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.PojosDescriptor;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.pojo.descriptor.UrlParameterType;
import org.jxapi.pojo.parser.PojosDescriptorParseUtil;

/**
 * Unit test for {@link PojosDescriptorParseUtil}
 */
public class PojosDescriptorParseUtilTest {
  
  private final Path resourcesPath = Paths.get(".", "src", "test", "resources");
  private final Path testPojosPath = resourcesPath.resolve(Paths.get("testPojos"));
  private final Path pojos1YamlPath = testPojosPath.resolve(Paths.get("pojos1.yaml"));
  private final Path pojos1JsonPath = resourcesPath.resolve(Paths.get("pojos1.json"));
  private final Path testInvalidPojosPath = resourcesPath.resolve(Paths.get("testInvalidPojos"));

  private Path srcFolder;
  
  @After
  public void tearDown() throws IOException {
    if (srcFolder != null) {
      JavaCodeGenUtil.deletePath(srcFolder);
      srcFolder = null;
    }
  }
  
  @Test
  public void testParsePojos1DescriptorFromYaml() throws Exception {
    PojosDescriptor pd = PojosDescriptorParseUtil.fromYaml(pojos1YamlPath);
    checkPojos1Descriptor(pd);
  }
  
  @Test
  public void testParsePojos1DescriptorFromJson() throws Exception {
    PojosDescriptor pd = PojosDescriptorParseUtil.fromJson(pojos1JsonPath);
    checkPojos1Descriptor(pd);
  }
  
  @Test
  public void testCollectAndMergePojosDescriptors() throws IOException {
    List<PojosDescriptor> pd = PojosDescriptorParseUtil.collectAndMergePojosDescriptors(testPojosPath);
    Assert.assertEquals(2, pd.size());
    PojosDescriptor pd1 = pd.get(0);
    checkPojos1Descriptor(pd1);
    PojosDescriptor pd2 = pd.get(1);
    checkPojos2Descriptor(pd2);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCollectAndMergePojosDescriptors_InvalidPojosPath() throws IOException {
    PojosDescriptorParseUtil.collectAndMergePojosDescriptors(testInvalidPojosPath);
  }

  private void checkPojos1Descriptor(PojosDescriptor pd) {
    // Check that the parsed descriptor contains expected pojos
    Assert.assertEquals("org.jxapi.pojos1.gen", pd.getBasePackage());
    Assert.assertEquals(2, pd.getPojos().size());
    
    Field fooPojo = pd.getPojos().get(0);
    Assert.assertEquals("foo", fooPojo.getName());
    Assert.assertEquals("Foo object", fooPojo.getDescription());
    Assert.assertEquals(3, fooPojo.getProperties().size());
    
    Field fooIdField = fooPojo.getProperties().get(0);
    Assert.assertEquals("id", fooIdField.getName());
    Assert.assertEquals("Identifier", fooIdField.getDescription());
    Assert.assertEquals(Type.STRING, fooIdField.getType());
    
    Field fooValueField = fooPojo.getProperties().get(1);
    Assert.assertEquals("value", fooValueField.getName());
    Assert.assertEquals("Value of the foo", fooValueField.getDescription());
    Assert.assertEquals(Type.INT, fooValueField.getType());
    Field fooBarField = fooPojo.getProperties().get(2);
    Assert.assertEquals("bar", fooBarField.getName());
    Assert.assertEquals("Bar object", fooBarField.getDescription());
    Assert.assertEquals(2, fooBarField.getProperties().size());
    Field fooBarNameField = fooBarField.getProperties().get(0);
    Assert.assertEquals("name", fooBarNameField.getName());
    Assert.assertEquals("Name of the bar", fooBarNameField.getDescription());
    Field fooBarAmountField = fooBarField.getProperties().get(1);
    Assert.assertEquals("amount", fooBarAmountField.getName());
    Assert.assertEquals("Amount in the bar", fooBarAmountField.getDescription());
    Assert.assertEquals(Type.BIGDECIMAL, fooBarAmountField.getType());
    
    Field bazPojo = pd.getPojos().get(1);
    Assert.assertEquals("baz", bazPojo.getName());
    Assert.assertEquals("Baz object", bazPojo.getDescription());
    Assert.assertEquals(2, bazPojo.getProperties().size());
    Assert.assertEquals(1, bazPojo.getImplementedInterfaces().size());
    Assert.assertEquals("org.jxapi.pojos1.BazInterface", bazPojo.getImplementedInterfaces().get(0));
    
    Field bazTimestampField = bazPojo.getProperties().get(0);
    Assert.assertEquals("timestamp", bazTimestampField.getName());
    Assert.assertEquals("Timestamp of the baz", bazTimestampField.getDescription());
    Assert.assertEquals(Type.LONG, bazTimestampField.getType());
    Field bazActiveField = bazPojo.getProperties().get(1);
    Assert.assertEquals("active", bazActiveField.getName());
    Assert.assertEquals("Is the baz active", bazActiveField.getDescription());
    Assert.assertEquals(Type.BOOLEAN, bazActiveField.getType());
    Assert.assertEquals(UrlParameterType.PATH, bazActiveField.getIn());
  }
  
  private void checkPojos2Descriptor(PojosDescriptor pd) {
    Assert.assertEquals("org.jxapi.pojos2.gen", pd.getBasePackage());
    Assert.assertEquals(2, pd.getPojos().size());
    Field fooPojo = pd.getPojos().get(0);
    Assert.assertEquals("foo", fooPojo.getName());
    Assert.assertEquals("Foo object", fooPojo.getDescription());
    Assert.assertEquals(2, fooPojo.getProperties().size());
    Field fooIdField = fooPojo.getProperties().get(0);
    Assert.assertEquals("id", fooIdField.getName());
    Assert.assertEquals("Identifier", fooIdField.getDescription());
    Assert.assertEquals(Type.STRING, fooIdField.getType());
    
    Field barListPojo = fooPojo.getProperties().get(1);
    Assert.assertEquals("barList", barListPojo.getName());
    Assert.assertEquals("List of Bar objects", barListPojo.getDescription());
    Assert.assertEquals("Bar", barListPojo.getObjectName());
    Assert.assertEquals(Type.fromTypeName("OBJECT_LIST"), barListPojo.getType());
    
    Field barPojo = pd.getPojos().get(1);
    Assert.assertEquals("bar", barPojo.getName());
    Assert.assertEquals("Bar object", barPojo.getDescription());
    Assert.assertEquals(2, barPojo.getProperties().size());
    Field barNameField = barPojo.getProperties().get(0);
    Assert.assertEquals("name", barNameField.getName());
    Assert.assertEquals("Name of the bar", barNameField.getDescription());
    Field barAmountField = barPojo.getProperties().get(1);
    Assert.assertEquals("amount", barAmountField.getName());
    Assert.assertEquals("Amount in the bar", barAmountField.getDescription());
    Assert.assertEquals(Type.BIGDECIMAL, barAmountField.getType());
  }
  
}
