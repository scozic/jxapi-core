package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.Field;

/**
 * Unit test for {@link EndpointPojoGenerator}
 */
public class EndpointPojoGeneratorTest {

	@Test
	public void testGenerate() throws Exception {
		String typeName = "com.x.MyPojo";
		String typeDescription = "Used in EndpointPojoGeneratorTest";
		List<Field> endpointParameters = new ArrayList<>();
		endpointParameters.add(Field.create(CanonicalType.LONG.name(), "id", null, "identifier", "123"));
		endpointParameters.add(Field.create(CanonicalType.INT.name(), "score", null, "Current score", "0"));
		endpointParameters.add(Field.createObject("OBJECT_LIST", "foo", "f", null,
				Arrays.asList(Field.create(CanonicalType.TIMESTAMP.name(), "time", null, "Creation time", "0"),
							  Field.createObject(CanonicalType.OBJECT.name(), "bar", "b", "The bar",
									  Arrays.asList(Field.create(CanonicalType.STRING.name(), "name", null, "Bar name", "my bar")), null)
						), null
				));
		endpointParameters.add(Field.createObject( "OBJECT_LIST_MAP", "toto", "toto", null,
				Arrays.asList(Field.create(CanonicalType.STRING.name(), "id", null, "Toto ID", "toto#1")), null
				));
		
		EndpointPojoGenerator generator = new EndpointPojoGenerator(typeName, typeDescription, endpointParameters, List.of("com.x.common.MyInterface"), "// Additionnal body here\n\n");
		Assert.assertEquals("package com.x;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import com.x.common.MyInterface;\n"
				+ "import com.x.serializers.MyPojoSerializer;\n"
				+ "import java.util.List;\n"
				+ "import java.util.Map;\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "/**\n"
				+ " * Used in EndpointPojoGeneratorTest\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyPojoSerializer.class)\n"
				+ "public class MyPojo implements MyInterface {\n"
				+ "  private List<MyPojoFoo> foo;\n"
				+ "  private Long id;\n"
				+ "  private Integer score;\n"
				+ "  private Map<String, List<MyPojoToto>> toto;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return  Message field <strong>f</strong>\n"
				+ "   */\n"
				+ "  public List<MyPojoFoo> getFoo() {\n"
				+ "    return foo;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param foo  Message field <strong>f</strong>\n"
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
				+ "   * @return  Message field <strong>toto</strong>\n"
				+ "   */\n"
				+ "  public Map<String, List<MyPojoToto>> getToto() {\n"
				+ "    return toto;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param toto  Message field <strong>toto</strong>\n"
				+ "   */\n"
				+ "  public void setToto(Map<String, List<MyPojoToto>> toto) {\n"
				+ "    this.toto = toto;\n"
				+ "  }\n"
				+ "  \n"
				+ "  // Additionnal body here\n"
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
				+ "}\n", 
				generator.generate());
	}
	
	@Test
	public void testGenerate_NullAdditionnalBody() throws Exception {
		String typeName = "com.x.MyPojoWithNullAdditionnalBody";
		String typeDescription = "Used in EndpointPojoGeneratorTest";
		List<Field> endpointParameters = new ArrayList<>();
		endpointParameters.add(Field.create(CanonicalType.LONG.name(), "id", null, "identifier", "123"));
		
		EndpointPojoGenerator generator = new EndpointPojoGenerator(typeName, typeDescription, endpointParameters, null, null);
		Assert.assertEquals("package com.x;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import com.x.serializers.MyPojoWithNullAdditionnalBodySerializer;\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "/**\n"
				+ " * Used in EndpointPojoGeneratorTest\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyPojoWithNullAdditionnalBodySerializer.class)\n"
				+ "public class MyPojoWithNullAdditionnalBody {\n"
				+ "  private Long id;\n"
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
				+ "  @Override\n"
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    MyPojoWithNullAdditionnalBody o = (MyPojoWithNullAdditionnalBody) other;\n"
				+ "    return Objects.equals(id, o.id);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return Objects.hash(id);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				generator.generate());
	}
	
	@Test
	public void testGenerate_NullProperties() throws Exception {
		String typeName = "com.x.MyPojoWithNullProperties";
		String typeDescription = "Used in EndpointPojoGeneratorTest";
		
		EndpointPojoGenerator generator = new EndpointPojoGenerator(typeName, typeDescription, null, null, null);
		Assert.assertEquals("package com.x;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import com.x.serializers.MyPojoWithNullPropertiesSerializer;\n"
				+ "\n"
				+ "/**\n"
				+ " * Used in EndpointPojoGeneratorTest\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyPojoWithNullPropertiesSerializer.class)\n"
				+ "public class MyPojoWithNullProperties {\n"
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
				+ "}\n", 
				generator.generate());
	}
}
