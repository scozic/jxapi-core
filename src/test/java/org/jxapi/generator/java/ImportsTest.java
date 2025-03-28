package org.jxapi.generator.java;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link Imports}
 */
public class ImportsTest {

  @Test
  public void testAddAndGetAllImports() {
    Imports imports = new Imports();
    Assert.assertEquals(0, imports.size());
    imports.add(Integer.class);
    Assert.assertEquals(0, imports.size());
    imports.add(BigDecimal.class);
    Assert.assertEquals(1, imports.size());
    imports.add("com.x.y.z.MyOtherClass");
    Assert.assertEquals(2, imports.size());
    imports.add("com.x.y.MyClass");
    Assert.assertEquals(3, imports.size());
    imports.add("com.x.y.MyGenericClass<com.x.y.MyItem>");
    
    
    List<String> allImports = imports.getAllImports();
    Assert.assertEquals(4, allImports.size());
    Assert.assertEquals(BigDecimal.class.getName(), allImports.get(0));
    Assert.assertEquals("com.x.y.MyClass", allImports.get(1));
    Assert.assertEquals("com.x.y.MyGenericClass", allImports.get(2));
    Assert.assertEquals("com.x.y.z.MyOtherClass", allImports.get(3));
  }
  
  @Test
  public void testIterator() {
    Imports im = new Imports();
    im.add(Integer.class);
    im.add(BigDecimal.class);
    im.add("com.x.y.z.MyOtherClass");
    im.add("com.x.y.MyClass");
    
    Iterator<String> it = im.iterator();
    
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(BigDecimal.class.getName(), it.next());
    Assert.assertEquals("com.x.y.MyClass", it.next());
    Assert.assertEquals("com.x.y.z.MyOtherClass", it.next());
    Assert.assertFalse(it.hasNext());
  }
  
  @Test
  public void testAddAll() {
    Imports im = new Imports();
    im.add(Integer.class);
    im.add(BigDecimal.class);
    im.add("com.x.y.MyClass");
    
    Imports im2 = new Imports();
    im2.add("com.x.y.z.MyOtherClass");
    im2.add(List.class);
    
    im.addAll(im2);
    
    List<String> allImports = im.getAllImports();
    Assert.assertEquals(4, allImports.size());
    Assert.assertEquals(BigDecimal.class.getName(), allImports.get(0));
    Assert.assertEquals(List.class.getName(), allImports.get(1));
    Assert.assertEquals("com.x.y.MyClass", allImports.get(2));
    Assert.assertEquals("com.x.y.z.MyOtherClass", allImports.get(3));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testAddNullImport() {
    Imports im = new Imports();
    im.add((String) null);
  }
  
  @Test
  public void testGenerate_EmptyImports() {
    Imports im = new Imports();
    Assert.assertEquals("", im.generate(null));
  }
  
  @Test
  public void testGenerate_NullDefaultPackage() {
    Imports im = new Imports();
    im.add(Integer.class);
    im.add(BigDecimal.class);
    im.add("com.x.y.z.MyOtherClass");
    im.add("com.x.y.MyClass");
    
    Assert.assertEquals("import java.math.BigDecimal;\n"
              + "\n"
              + "import com.x.y.MyClass;\n"
              + "import com.x.y.z.MyOtherClass;\n", 
              im.generate(null));
  }
  
  @Test
  public void testGenerate_WithCurrentPackage() {
    Imports im = new Imports();
    im.add(Integer.class);
    im.add(BigDecimal.class);
    im.add("com.x.y.z.MyOtherClass");
    im.add("com.x.y.MyClass");
    
    Assert.assertEquals("import java.math.BigDecimal;\n"
              + "\n"
              + "import com.x.y.z.MyOtherClass;\n", 
              im.generate("com.x.y"));
  }
  
  @Test
  public void testGenerate_WithCurrentPackage_OnlyJavaImports() {
    Imports im = new Imports();
    im.add(Integer.class);
    im.add(BigDecimal.class);
    im.add(List.class);
    
    Assert.assertEquals("import java.math.BigDecimal;\n"
              + "import java.util.List;\n", 
              im.generate("com.x.y"));
  }
  
  @Test
  public void testGenerate_WithCurrentPackage_OnlyJavaImportsAndCurrentPackageClass() {
    Imports im = new Imports();
    im.add(Integer.class);
    im.add(BigDecimal.class);
    im.add(List.class);
    im.add("com.x.y.MyClass");
    
    Assert.assertEquals("import java.math.BigDecimal;\n"
              + "import java.util.List;\n", 
              im.generate("com.x.y"));
  }

}
