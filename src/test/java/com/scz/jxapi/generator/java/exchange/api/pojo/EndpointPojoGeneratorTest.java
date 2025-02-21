package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;

/**
 * Unit test for {@link EndpointPojoGenerator}
 */
public class EndpointPojoGeneratorTest {

	@Test
	public void testGenerate() throws Exception {
		String typeName = "com.x.MyPojo";
		String typeDescription = "Used in EndpointPojoGeneratorTest";
		List<Field> endpointParameters = List.of(
		  Field.builder().type(Type.LONG).name("id").description("identifier").build(),
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
		
		EndpointPojoGenerator generator = new EndpointPojoGenerator(typeName, typeDescription, endpointParameters, List.of("com.x.common.MyInterface"));
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
				+ " * Used in EndpointPojoGeneratorTest\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyPojoSerializer.class)\n"
				+ "public class MyPojo implements DeepCloneable<MyPojo>, MyInterface {\n"
				+ "  private List<MyPojoFoo> foo;\n"
				+ "  private Long id;\n"
				+ "  private Integer score;\n"
				+ "  private Map<String, List<MyPojoToto>> toto;\n"
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
				+ "   * @return identifier\n"
				+ "   */\n"
				+ "  public Long getId() {\n"
				+ "    return id;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param id identifier\n"
				+ "   */\n"
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
				+ "    return Objects.equals(foo, o.foo)\n"
				+ "            && Objects.equals(id, o.id)\n"
				+ "            && Objects.equals(score, o.score)\n"
				+ "            && Objects.equals(toto, o.toto);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return Objects.hash(foo, id, score, toto);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
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
				+ "}\n"
				+ "", 
				generator.generate());
	}
	
	@Test
	public void testGenerate_ImplicitObjectType() throws Exception {
		String typeName = "com.x.MyPojoWithNullAdditionnalBody";
		String typeDescription = "Used in EndpointPojoGeneratorTest";
		List<Field> endpointParameters = List.of(
			Field.builder()
			.name("myObj")
			.description("identifier")
			  .properties(List.of(
					  Field.builder().type(Type.LONG).name("id").description("identifier").build()))
			.build()
		);
		EndpointPojoGenerator generator = new EndpointPojoGenerator(typeName, typeDescription, endpointParameters, null);
		Assert.assertEquals("package com.x;\n"
				+ "\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.scz.jxapi.util.DeepCloneable;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import com.x.serializers.MyPojoWithNullAdditionnalBodySerializer;\n"
				+ "\n"
				+ "/**\n"
				+ " * Used in EndpointPojoGeneratorTest\n"
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
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public MyPojoWithNullAdditionnalBody deepClone() {\n"
				+ "    MyPojoWithNullAdditionnalBody clone = new MyPojoWithNullAdditionnalBody();\n"
				+ "    clone.myObj = this.myObj != null ? this.myObj.deepClone() : null;\n"
				+ "    return clone;\n"
				+ "  }\n"
				+ "}\n", 
				generator.generate());
	}
	
	@Test
	public void testGenerate_NullProperties() throws Exception {
		String typeName = "com.x.MyPojoWithNullProperties";
		String typeDescription = "Used in EndpointPojoGeneratorTest";
		EndpointPojoGenerator generator = new EndpointPojoGenerator(typeName, typeDescription, null, null);
		Assert.assertEquals("package com.x;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.scz.jxapi.util.DeepCloneable;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import com.x.serializers.MyPojoWithNullPropertiesSerializer;\n"
				+ "\n"
				+ "/**\n"
				+ " * Used in EndpointPojoGeneratorTest\n"
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
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public MyPojoWithNullProperties deepClone() {\n"
				+ "    return new MyPojoWithNullProperties();\n"
				+ "  }\n"
				+ "}\n", 
				generator.generate());
	}
}
