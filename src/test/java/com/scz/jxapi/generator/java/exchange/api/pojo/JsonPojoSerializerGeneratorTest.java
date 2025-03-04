package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;

/**
 * Unit test for {@link JsonPojoSerializerGenerator}
 */
public class JsonPojoSerializerGeneratorTest {
	
	@Test
	public void testGenerateSerializer() {
		String serializedTypeName = "com.x.MyPojo";
		List<Field> endpointParameters = List.of(
				  Field.builder().type(Type.LONG).name("id").build(),
				  Field.builder().type(Type.STRING).name("name").build(),
				  Field.builder().type(Type.INT).name("level").build(),
				  Field.builder().type(Type.BIGDECIMAL).name("score").build(),
				  Field.builder().type(Type.BOOLEAN).name("over").build(),
				  Field.builder().type("OBJECT_LIST").name("foo").msgField("f")
								 .property(Field.builder().type(Type.TIMESTAMP).name("time").build())
								 .property(Field.builder().type(Type.OBJECT).name("bar").msgField("b")
								  			    .property(Field.builder().type(Type.STRING).name("name").build())
												.build())
								 .build(),
				  Field.builder().type("OBJECT_LIST_MAP").name("toto")
								 .property(Field.builder().type(Type.STRING).name("id").build())
								 .build(),
				  Field.builder().type(Type.OBJECT).name("titi").msgField("ti")
								 .property(Field.builder().type(Type.STRING).name("name").build())
								 .build()
				);
		
		JsonPojoSerializerGenerator generator = new JsonPojoSerializerGenerator(serializedTypeName, endpointParameters);
		Assert.assertEquals("package com.x.serializers;\n"
				+ "\n"
				+ "import java.io.IOException;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.core.JsonGenerator;\n"
				+ "import com.fasterxml.jackson.databind.SerializerProvider;\n"
				+ "import com.fasterxml.jackson.databind.ser.std.StdSerializer;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import com.x.MyPojo;\n"
				+ "import javax.annotation.processing.Generated;\n"
				+ "\n"
				+ "/**\n"
				+ " * Jackson JSON Serializer for com.x.MyPojo\n"
				+ " * @see MyPojo\n"
				+ " */\n"
				+ "@Generated(\"com.scz.jxapi.generator.java.exchange.api.pojo.JsonPojoSerializerGenerator\")\n"
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
				+ "    if (value.getName() != null){\n"
				+ "      gen.writeStringField(\"name\", String.valueOf(value.getName()));\n"
				+ "    }\n"
				+ "    if (value.getLevel() != null){\n"
				+ "      gen.writeNumberField(\"level\", value.getLevel());\n"
				+ "    }\n"
				+ "    if (value.getScore() != null){\n"
				+ "      gen.writeStringField(\"score\", EncodingUtil.bigDecimalToString(value.getScore()));\n"
				+ "    }\n"
				+ "    if (value.isOver() != null){\n"
				+ "      gen.writeBooleanField(\"over\", value.isOver());\n"
				+ "    }\n"
				+ "    if (value.getFoo() != null){\n"
				+ "      gen.writeObjectField(\"f\", value.getFoo());\n"
				+ "    }\n"
				+ "    if (value.getToto() != null){\n"
				+ "      gen.writeObjectField(\"toto\", value.getToto());\n"
				+ "    }\n"
				+ "    if (value.getTiti() != null){\n"
				+ "      gen.writeObjectField(\"ti\", value.getTiti());\n"
				+ "    }\n"
				+ "    gen.writeEndObject();\n"
				+ "  }\n"
				+ "}\n", 
				generator.generate());
	}

}
