package org.jxapi.generator.java.pojo;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;


/**
 * Unit test for {@link JsonPojoDeserializerGenerator}
 */
public class JsonPojoDeserializerGeneratorTest {

  @Test
  public void testGenerateDeserializer() {
    String deserialiazedTypeName = "com.x.MyPojo";
    List<Field> endpointParameters = List.of(
      Field.builder().type(Type.LONG).name("id").build(),
      Field.builder().type(Type.STRING).name("name").build(),
      Field.builder().type(Type.INT).name("level").build(),
      Field.builder().type(Type.BIGDECIMAL).name("score").build(),
      Field.builder().type(Type.BOOLEAN).name("over").build(),
      Field.builder().type("OBJECT_LIST").name("foo").msgField("f")
             .property(Field.builder().type(Type.LONG).name("time").build())
             .property(Field.builder().type(Type.OBJECT).name("bar").msgField("b")
                        .property(Field.builder().type(Type.STRING).name("name").build())
                    .build())
             .build(),
      Field.builder().type("OBJECT_LIST_MAP").name("toto")
             .property(Field.builder().type(Type.STRING).name("id").build())
             .build(),
      Field.builder().type(Type.OBJECT).name("titi").msgField("ti")
             .property(Field.builder().type(Type.STRING).name("name").build())
             .build(),
      Field.builder().type(Type.OBJECT).name("myRawObject").msgField("raw")
             .objectName(Object.class.getName())
             .build(),
      Field.builder().type(Type.OBJECT).name("myExternalClassObject").msgField("ext")
             .objectName("com.x.y.z.ExternalClass")
             .build()
    );
    
    JsonPojoDeserializerGenerator generator = new JsonPojoDeserializerGenerator(deserialiazedTypeName, endpointParameters);
    Assert.assertEquals("package com.x.deserializers;\n"
        + "\n"
        + "import java.io.IOException;\n"
        + "import java.util.List;\n"
        + "\n"
        + "import com.fasterxml.jackson.core.JsonParser;\n"
        + "import com.fasterxml.jackson.core.JsonToken;\n"
        + "import com.x.MyPojo;\n"
        + "import com.x.MyPojoFoo;\n"
        + "import com.x.MyPojoToto;\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;\n"
        + "import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;\n"
        + "import org.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;\n"
        + "import static org.jxapi.util.JsonUtil.readNextBigDecimal;\n"
        + "import static org.jxapi.util.JsonUtil.readNextBoolean;\n"
        + "import static org.jxapi.util.JsonUtil.readNextInteger;\n"
        + "import static org.jxapi.util.JsonUtil.readNextLong;\n"
        + "import static org.jxapi.util.JsonUtil.readNextObject;\n"
        + "import static org.jxapi.util.JsonUtil.readNextString;\n"
        + "import static org.jxapi.util.JsonUtil.skipNextValue;\n"
        + "\n"
        + "/**\n"
        + " * Parses incoming JSON messages into com.x.MyPojo instances\n"
        + " * @see com.x.MyPojo\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator\")\n"
        + "public class MyPojoDeserializer extends AbstractJsonMessageDeserializer<MyPojo> {\n"
        + "  private ListJsonFieldDeserializer<MyPojoFoo> fooDeserializer;\n"
        + "  private MapJsonFieldDeserializer<List<MyPojoToto>> totoDeserializer;\n"
        + "  private MyPojoTitiDeserializer titiDeserializer;\n"
        + "  \n"
        + "  @Override\n"
        + "  public MyPojo deserialize(JsonParser parser) throws IOException {\n"
        + "    MyPojo msg = new MyPojo();\n"
        + "    while(parser.nextToken() != JsonToken.END_OBJECT) {\n"
        + "      switch(parser.currentName()) {\n"
        + "      case \"id\":\n"
        + "        msg.setId(readNextLong(parser));\n"
        + "      break;\n"
        + "      case \"name\":\n"
        + "        msg.setName(readNextString(parser));\n"
        + "      break;\n"
        + "      case \"level\":\n"
        + "        msg.setLevel(readNextInteger(parser));\n"
        + "      break;\n"
        + "      case \"score\":\n"
        + "        msg.setScore(readNextBigDecimal(parser));\n"
        + "      break;\n"
        + "      case \"over\":\n"
        + "        msg.setOver(readNextBoolean(parser));\n"
        + "      break;\n"
        + "      case \"f\":\n"
        + "        parser.nextToken();\n"
        + "        if(fooDeserializer == null) {\n"
        + "          fooDeserializer = new ListJsonFieldDeserializer<>(new MyPojoFooDeserializer());\n"
        + "        }\n"
        + "        msg.setFoo(fooDeserializer.deserialize(parser));\n"
        + "      break;\n"
        + "      case \"toto\":\n"
        + "        parser.nextToken();\n"
        + "        if(totoDeserializer == null) {\n"
        + "          totoDeserializer = new MapJsonFieldDeserializer<>(new ListJsonFieldDeserializer<>(new MyPojoTotoDeserializer()));\n"
        + "        }\n"
        + "        msg.setToto(totoDeserializer.deserialize(parser));\n"
        + "      break;\n"
        + "      case \"ti\":\n"
        + "        parser.nextToken();\n"
        + "        if(titiDeserializer == null) {\n"
        + "          titiDeserializer = new MyPojoTitiDeserializer();\n"
        + "        }\n"
        + "        msg.setTiti(titiDeserializer.deserialize(parser));\n"
        + "      break;\n"
        + "      case \"raw\":\n"
        + "        msg.setMyRawObject(readNextObject(parser));\n"
        + "      break;\n"
        + "      case \"ext\":\n"
        + "        msg.setMyExternalClassObject(readNextObject(parser, com.x.y.z.ExternalClass.class));\n"
        + "      break;\n"
        + "      default:\n"
        + "        skipNextValue(parser);\n"
        + "      }\n"
        + "    }\n"
        + "    \n"
        + "     return msg;\n"
        + "  }\n"
        + "}\n"
        + "", 
        generator.generate());
  }
}
