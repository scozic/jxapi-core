package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.Field;

public class JsonPojoSerializerGeneratorTest {
	
	@Test
	public void testGenerateSerializer() {
		String deserializedTypeName = "com.x.MyPojo";
		List<Field> endpointParameters = new ArrayList<>();
		endpointParameters.add(Field.create(CanonicalType.LONG.name(), "id", null, "identifier", "123"));
		endpointParameters.add(Field.create(CanonicalType.INT.name(), "score", null, "Current score", "0"));
		endpointParameters.add(Field.createObject("OBJECT_LIST", "foo", "f", null,
				Arrays.asList(Field.create(CanonicalType.TIMESTAMP.name(), "time", null, "Creation time", "0"),
							  Field.createObject(CanonicalType.OBJECT.name(), "bar", "b", "The bar",
									  Arrays.asList(Field.create(CanonicalType.STRING.name(), "name", null, "Bar name", "my bar")))
						)
				));
		endpointParameters.add(Field.createObject( "OBJECT_LIST_MAP", "toto", "toto", null,
				Arrays.asList(Field.create(CanonicalType.STRING.name(), "id", null, "Toto ID", "toto#1"))
				));
		
		JsonPojoSerializerGenerator generator = new JsonPojoSerializerGenerator(deserializedTypeName, endpointParameters);
		Assert.assertEquals("package com.x.serializers;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.core.JsonGenerator;\n"
				+ "import com.fasterxml.jackson.databind.SerializerProvider;\n"
				+ "import com.fasterxml.jackson.databind.ser.std.StdSerializer;\n"
				+ "import com.x.MyPojo;\n"
				+ "import java.io.IOException;\n"
				+ "\n"
				+ "/**\n"
				+ " * Jackson JSON Serializer for com.x.MyPojo\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " * @see MyPojo\n"
				+ " */\n"
				+ "public class MyPojoSerializer extends StdSerializer<MyPojo> {\n"
				+ "  public MyPojoSerializer() {\n"
				+ "    super(MyPojo.class);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public void serialize(MyPojo value, JsonGenerator gen, SerializerProvider provider) throws IOException {\n"
				+ "    gen.writeStartObject();\n"
				+ "    if (value.getId() != null){\n"
				+ "      gen.writeNumberField(\"id\", value.getId());\n"
				+ "    }\n"
				+ "    if (value.getScore() != null){\n"
				+ "      gen.writeNumberField(\"score\", value.getScore());\n"
				+ "    }\n"
				+ "    if (value.getFoo() != null){\n"
				+ "      gen.writeObjectField(\"f\", value.getFoo());\n"
				+ "    }\n"
				+ "    if (value.getToto() != null){\n"
				+ "      gen.writeObjectField(\"toto\", value.getToto());\n"
				+ "    }\n"
				+ "    gen.writeEndObject();\n"
				+ "  }\n"
				+ "}\n", generator.generate());
	}

}
