package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;

public class PojoGenerator2Test {

	@Test
	public void testGenerate() throws Exception {
		String typeName = "com.x.MyPojo";
		String typeDescription = "Used in PojoGenerator2Test";
		List<Field> properties = List.of(
		  Field.builder().type(Type.LONG).name("id").build(),
		  Field.builder().type(Type.INT).name("score").description("Current score").build(),
		  Field.builder().type("OBJECT_LIST").name("foo").msgField("f").description("The foo")
						 .property(Field.builder().type(Type.TIMESTAMP).name("time").description("Creation time").build())
						 .property(Field.builder().name("bar").description("The bar")
								 				  .property(Field.builder().type(Type.STRING).name("name").build())
								 				  .build())
						 .build(),
		  Field.builder().type("OBJECT_LIST_MAP").name("toto").description("Toto")
						 .property(Field.builder().type(Type.STRING).name("id").description("Toto ID").build())
						 .build()
		);
		
		PojoGenerator2 generator = new PojoGenerator2(typeName, typeDescription, properties, List.of("com.x.common.MyInterface"));
		Assert.assertEquals("package com.x;\n"
				+ "\n"
				+ "import java.util.List;\n"
				+ "import java.util.Map;\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.scz.jxapi.util.CollectionUtil;\n"
				+ "import com.scz.jxapi.util.DeepCloneable;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import com.x.common.MyInterface;\n"
				+ "import com.x.serializers.MyPojoSerializer;\n"
				+ "\n"
				+ "/**\n"
				+ " * Used in PojoGenerator2Test\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyPojoSerializer.class)\n"
				+ "public class MyPojo implements DeepCloneable<MyPojo>, MyInterface {\n"
				+ "  private Long id;\n"
				+ "  private Integer score;\n"
				+ "  private List<MyPojoFoo> foo;\n"
				+ "  private Map<String, List<MyPojoToto>> toto;\n"
				+ "  \n"
				+ "  public Long getId() {\n"
				+ "    return id;\n"
				+ "  }\n"
				+ "  \n"
				+ "  public void setId(Long id) {\n"
				+ "    this.id = id;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Current score\n"
				+ "   */\n"
				+ "  public Integer getScore() {\n"
				+ "    return score;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param score Current score\n"
				+ "   */\n"
				+ "  public void setScore(Integer score) {\n"
				+ "    this.score = score;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return The foo Message field <strong>f</strong>\n"
				+ "   */\n"
				+ "  public List<MyPojoFoo> getFoo() {\n"
				+ "    return foo;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param foo The foo Message field <strong>f</strong>\n"
				+ "   */\n"
				+ "  public void setFoo(List<MyPojoFoo> foo) {\n"
				+ "    this.foo = foo;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Toto\n"
				+ "   */\n"
				+ "  public Map<String, List<MyPojoToto>> getToto() {\n"
				+ "    return toto;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param toto Toto\n"
				+ "   */\n"
				+ "  public void setToto(Map<String, List<MyPojoToto>> toto) {\n"
				+ "    this.toto = toto;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    MyPojo o = (MyPojo) other;\n"
				+ "    return Objects.equals(id, o.id)\n"
				+ "            && Objects.equals(score, o.score)\n"
				+ "            && Objects.equals(foo, o.foo)\n"
				+ "            && Objects.equals(toto, o.toto);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return Objects.hash(id, score, foo, toto);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public MyPojo deepClone() {\n"
				+ "    MyPojo clone = new MyPojo();\n"
				+ "    clone.id = this.id;\n"
				+ "    clone.score = this.score;\n"
				+ "    clone.foo = CollectionUtil.deepCloneList(this.foo, DeepCloneable::deepClone);\n"
				+ "    clone.toto = CollectionUtil.deepCloneMap(this.toto, l0 -> CollectionUtil.deeplCloneList(l0, DeepCloneable::deepClone));\n"
				+ "    return clone;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				generator.generate());
		Assert.assertEquals(properties, generator.getFields());
	}
	
	@Test
	public void testGenerate_ImplicitObjectType() throws Exception {
		String typeName = "com.x.pojo.MyPojoWithNullAdditionnalBody";
		String typeDescription = "Used in PojoGenerator2Test";
		List<Field> properties = List.of(
			Field.builder()
			.name("myObj")
			.description("identifier")
			  .properties(List.of(
					  Field.builder().type(Type.LONG).name("id").description("identifier").build()))
			.build()
		);
		PojoGenerator2 generator = new PojoGenerator2(typeName, typeDescription, properties, null);
		Assert.assertEquals("package com.x.pojo;\n"
				+ "\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.scz.jxapi.util.DeepCloneable;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import com.x.serializers.MyPojoWithNullAdditionnalBodySerializer;\n"
				+ "\n"
				+ "/**\n"
				+ " * Used in PojoGenerator2Test\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyPojoWithNullAdditionnalBodySerializer.class)\n"
				+ "public class MyPojoWithNullAdditionnalBody implements DeepCloneable<MyPojoWithNullAdditionnalBody> {\n"
				+ "  private MyPojoWithNullAdditionnalBodyMyObj myObj;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return identifier\n"
				+ "   */\n"
				+ "  public MyPojoWithNullAdditionnalBodyMyObj getMyObj() {\n"
				+ "    return myObj;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param myObj identifier\n"
				+ "   */\n"
				+ "  public void setMyObj(MyPojoWithNullAdditionnalBodyMyObj myObj) {\n"
				+ "    this.myObj = myObj;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    MyPojoWithNullAdditionnalBody o = (MyPojoWithNullAdditionnalBody) other;\n"
				+ "    return Objects.equals(myObj, o.myObj);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return Objects.hash(myObj);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public MyPojoWithNullAdditionnalBody deepClone() {\n"
				+ "    MyPojoWithNullAdditionnalBody clone = new MyPojoWithNullAdditionnalBody();\n"
				+ "    clone.myObj = this.myObj != null ? this.myObj.deepClone() : null;\n"
				+ "    return clone;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				generator.generate());
		Assert.assertEquals(properties, generator.getFields());
	}
	
	@Test
	public void testGenerate_NullProperties() throws Exception {
		String typeName = "com.x.pojo.MyPojoWithNullProperties";
		String typeDescription = "Used in PojoGenerator2Test";
		PojoGenerator2 generator = new PojoGenerator2(typeName, typeDescription, null, null);
		Assert.assertEquals("package com.x.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.scz.jxapi.util.DeepCloneable;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import com.x.serializers.MyPojoWithNullPropertiesSerializer;\n"
				+ "\n"
				+ "/**\n"
				+ " * Used in PojoGenerator2Test\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyPojoWithNullPropertiesSerializer.class)\n"
				+ "public class MyPojoWithNullProperties implements DeepCloneable<MyPojoWithNullProperties> {\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    return true;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return 31 * getClass().hashCode();\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public MyPojoWithNullProperties deepClone() {\n"
				+ "    return new MyPojoWithNullProperties();\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				generator.generate());
		Assert.assertEquals(List.of(), generator.getFields());
	}
	
	@Test
	public void testGeneratePojoCodeMultipleFields() {
		PojoGenerator2 generator = new PojoGenerator2("x.y.z.pojo.Foo");
		generator.addField(Field.builder().type(Type.STRING).name("name").description("the name").build());
		generator.addField(Field.builder().objectName("Bar").name("bar").msgField("b").build());
		generator.addField(Field.builder().type(Type.INT).name("a").description("lower case 'a'").build());
		generator.addField(Field.builder().type(Type.INT).name("A").description("upper case 'A'").build());
		
		Assert.assertEquals("package x.y.z.pojo;\n"
				+ "\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.scz.jxapi.util.DeepCloneable;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import x.y.z.serializers.FooSerializer;\n"
				+ "\n"
				+ "\n"
				+ "@JsonSerialize(using = FooSerializer.class)\n"
				+ "public class Foo implements DeepCloneable<Foo> {\n"
				+ "  private String name;\n"
				+ "  private Bar bar;\n"
				+ "  private Integer a;\n"
				+ "  private Integer A;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return the name\n"
				+ "   */\n"
				+ "  public String getName() {\n"
				+ "    return name;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param name the name\n"
				+ "   */\n"
				+ "  public void setName(String name) {\n"
				+ "    this.name = name;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Message field <strong>b</strong>\n"
				+ "   */\n"
				+ "  public Bar getBar() {\n"
				+ "    return bar;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param bar Message field <strong>b</strong>\n"
				+ "   */\n"
				+ "  public void setBar(Bar bar) {\n"
				+ "    this.bar = bar;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return lower case 'a'\n"
				+ "   */\n"
				+ "  public Integer geta() {\n"
				+ "    return a;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param a lower case 'a'\n"
				+ "   */\n"
				+ "  public void seta(Integer a) {\n"
				+ "    this.a = a;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return upper case 'A'\n"
				+ "   */\n"
				+ "  public Integer getA() {\n"
				+ "    return A;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param A upper case 'A'\n"
				+ "   */\n"
				+ "  public void setA(Integer A) {\n"
				+ "    this.A = A;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    Foo o = (Foo) other;\n"
				+ "    return Objects.equals(name, o.name)\n"
				+ "            && Objects.equals(bar, o.bar)\n"
				+ "            && Objects.equals(a, o.a)\n"
				+ "            && Objects.equals(A, o.A);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return Objects.hash(name, bar, a, A);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public Foo deepClone() {\n"
				+ "    Foo clone = new Foo();\n"
				+ "    clone.name = this.name;\n"
				+ "    clone.bar = this.bar != null ? this.bar.deepClone() : null;\n"
				+ "    clone.a = this.a;\n"
				+ "    clone.A = this.A;\n"
				+ "    return clone;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				generator.generate());
		Assert.assertEquals(4, generator.getFields().size());
	}

}
