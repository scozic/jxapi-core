package com.scz.jxapi.generator.exchange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link EndpointPojoGenerator}
 * @author Sylvestre COZIC
 */
public class EndpointPojoGeneratorTest {

	@Test
	public void testGenerate() throws Exception {
		String typeName = "com.x.MyPojo";
		String typeDescription = "Used in EndpointPojoGeneratorTest";
		List<EndpointParameter> endpointParameters = new ArrayList<>();
		endpointParameters.add(EndpointParameter.create(CanonicalEndpointParameterTypes.LONG.name(), "id", null, "identifier", "123"));
		endpointParameters.add(EndpointParameter.create(CanonicalEndpointParameterTypes.INT.name(), "score", null, "Current score", "0"));
		endpointParameters.add(EndpointParameter.create("OBJECT_LIST", "foo", "f", null,
				Arrays.asList(EndpointParameter.create(CanonicalEndpointParameterTypes.TIMESTAMP.name(), "time", null, "Creation time", "0"),
							  EndpointParameter.create(CanonicalEndpointParameterTypes.OBJECT.name(), "bar", "b", "The bar",
									  Arrays.asList(EndpointParameter.create(CanonicalEndpointParameterTypes.STRING.name(), "name", null, "Bar name", "my bar")))
						)
				));
		endpointParameters.add(EndpointParameter.create( "OBJECT_LIST_MAP", "toto", "toto", null,
				Arrays.asList(EndpointParameter.create(CanonicalEndpointParameterTypes.STRING.name(), "id", null, "Toto ID", "toto#1"))
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
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", generator.generate());
	}
}
