package com.scz.jxapi.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.CanonicalEndpointParameterTypes;
import com.scz.jxapi.exchange.descriptor.EndpointParameter;
import com.scz.jxapi.generator.exchange.api.pojo.JsonMessageDeserializerGenerator;


/**
 * Unit test for {@link JsonMessageDeserializerGenerator}
 */
public class JsonMessageDeserializerGeneratorTest {

	@Test
	public void testGenerateDeserializer() {
		String deserialiazedTypeName = "com.x.MyPojo";
		List<EndpointParameter> endpointParameters = new ArrayList<>();
		endpointParameters.add(EndpointParameter.create(CanonicalEndpointParameterTypes.LONG.name(), "id", null, "identifier", "123"));
		endpointParameters.add(EndpointParameter.create(CanonicalEndpointParameterTypes.INT.name(), "score", null, "Current score", "0"));
		endpointParameters.add(EndpointParameter.createObject("OBJECT_LIST", "foo", "f", null,
				Arrays.asList(EndpointParameter.create(CanonicalEndpointParameterTypes.TIMESTAMP.name(), "time", null, "Creation time", "0"),
							  EndpointParameter.createObject(CanonicalEndpointParameterTypes.OBJECT.name(), "bar", "b", "The bar",
									  Arrays.asList(EndpointParameter.create(CanonicalEndpointParameterTypes.STRING.name(), "name", null, "Bar name", "my bar")))
						)
				));
		endpointParameters.add(EndpointParameter.createObject( "OBJECT_LIST_MAP", "toto", "toto", null,
				Arrays.asList(EndpointParameter.create(CanonicalEndpointParameterTypes.STRING.name(), "id", null, "Toto ID", "toto#1"))
				));
		endpointParameters.add(EndpointParameter.createObject( "OBJECT", "titi", "ti", null,
				Arrays.asList(EndpointParameter.create(CanonicalEndpointParameterTypes.STRING.name(), "name", null, "Titi name", "TiName"))
				));
		
		JsonMessageDeserializerGenerator generator = new JsonMessageDeserializerGenerator(deserialiazedTypeName, endpointParameters);
		Assert.assertEquals("package com.x.deserializers;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.core.JsonParser;\n"
				+ "import com.fasterxml.jackson.core.JsonToken;\n"
				+ "import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;\n"
				+ "import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;\n"
				+ "import com.scz.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;\n"
				+ "import com.x.MyPojo;\n"
				+ "import com.x.MyPojoFoo;\n"
				+ "import com.x.MyPojoToto;\n"
				+ "import java.io.IOException;\n"
				+ "import java.util.List;\n"
				+ "import static com.scz.jxapi.util.JsonUtil.readNextInteger;\n"
				+ "import static com.scz.jxapi.util.JsonUtil.readNextLong;\n"
				+ "import static com.scz.jxapi.util.JsonUtil.skipNextValue;\n"
				+ "\n"
				+ "/**\n"
				+ " * Parses incoming JSON messages into com.x.MyPojo instances\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " * @see com.x.MyPojo\n"
				+ " */\n"
				+ "public class MyPojoDeserializer extends AbstractJsonMessageDeserializer<MyPojo> {\n"
				+ "  private final ListJsonFieldDeserializer<MyPojoFoo> fooDeserializer = new ListJsonFieldDeserializer<>(new MyPojoFooDeserializer());\n"
				+ "  private final MapJsonFieldDeserializer<List<MyPojoToto>> totoDeserializer = new MapJsonFieldDeserializer<>(new ListJsonFieldDeserializer<>(new MyPojoTotoDeserializer()));\n"
				+ "  private final MyPojoTitiDeserializer titiDeserializer = new MyPojoTitiDeserializer();\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public MyPojo deserialize(JsonParser parser) throws IOException {\n"
				+ "    MyPojo msg = new MyPojo();\n"
				+ "    while(parser.nextToken() != JsonToken.END_OBJECT) {\n"
				+ "      switch(parser.getCurrentName()) {\n"
				+ "      case \"id\":\n"
				+ "        msg.setId(readNextLong(parser));\n"
				+ "      break;\n"
				+ "      case \"score\":\n"
				+ "        msg.setScore(readNextInteger(parser));\n"
				+ "      break;\n"
				+ "      case \"f\":\n"
				+ "        parser.nextToken();\n"
				+ "        msg.setFoo(fooDeserializer.deserialize(parser));\n"
				+ "      break;\n"
				+ "      case \"toto\":\n"
				+ "        parser.nextToken();\n"
				+ "        msg.setToto(totoDeserializer.deserialize(parser));\n"
				+ "      break;\n"
				+ "      case \"ti\":\n"
				+ "        parser.nextToken();\n"
				+ "        msg.setTiti(titiDeserializer.deserialize(parser));\n"
				+ "      break;\n"
				+ "      default:\n"
				+ "        skipNextValue(parser);\n"
				+ "      }\n"
				+ "    }\n"
				+ "    \n"
				+ "     return msg;\n"
				+ "  }\n"
				+ "}\n", generator.generate());
	}
}
