package org.jxapi.generator.java.pojo;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;

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
          Field.builder().type(Type.fromTypeName("BIGDECIMAL_LIST_MAP")).name("bestScores").build(),
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
          Field.builder().type(Type.OBJECT).name("ext").msgField("e")
                 .objectName("com.z.MyExternalPojo")
                 .build()       
                 
        );
    
    JsonPojoSerializerGenerator generator = new JsonPojoSerializerGenerator(serializedTypeName, endpointParameters);
    Assert.assertEquals("package com.x.serializers;\n"
    		+ "\n"
    		+ "import java.io.IOException;\n"
    		+ "import java.math.BigDecimal;\n"
    		+ "import java.util.List;\n"
    		+ "\n"
    		+ "import com.fasterxml.jackson.core.JsonGenerator;\n"
    		+ "import com.fasterxml.jackson.databind.SerializerProvider;\n"
    		+ "import com.x.MyPojo;\n"
    		+ "import com.x.MyPojoFoo;\n"
    		+ "import com.x.MyPojoToto;\n"
    		+ "import javax.annotation.processing.Generated;\n"
    		+ "import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;\n"
    		+ "import org.jxapi.netutils.serialization.json.BigDecimalJsonValueSerializer;\n"
    		+ "import org.jxapi.netutils.serialization.json.ListJsonValueSerializer;\n"
    		+ "import org.jxapi.netutils.serialization.json.MapJsonValueSerializer;\n"
    		+ "import static org.jxapi.util.JsonUtil.writeBigDecimalField;\n"
    		+ "import static org.jxapi.util.JsonUtil.writeBooleanField;\n"
    		+ "import static org.jxapi.util.JsonUtil.writeCustomSerializerField;\n"
    		+ "import static org.jxapi.util.JsonUtil.writeIntField;\n"
    		+ "import static org.jxapi.util.JsonUtil.writeLongField;\n"
    		+ "import static org.jxapi.util.JsonUtil.writeObjectField;\n"
    		+ "import static org.jxapi.util.JsonUtil.writeStringField;\n"
    		+ "\n"
    		+ "/**\n"
    		+ " * Jackson JSON Serializer for com.x.MyPojo\n"
    		+ " * @see MyPojo\n"
    		+ " */\n"
    		+ "@Generated(\"org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator\")\n"
    		+ "public class MyPojoSerializer extends AbstractJsonValueSerializer<MyPojo> {\n"
    		+ "  \n"
    		+ "  private static final long serialVersionUID = -6215927632509720159L;\n"
    		+ "  \n"
    		+ "  /**\n"
    		+ "   * Constructor\n"
    		+ "   */\n"
    		+ "  public MyPojoSerializer() {\n"
    		+ "    super(MyPojo.class);\n"
    		+ "  }\n"
    		+ "  \n"
    		+ "  private ListJsonValueSerializer<MyPojoFoo> fooSerializer;\n"
    		+ "  private MapJsonValueSerializer<List<MyPojoToto>> totoSerializer;\n"
    		+ "  private MyPojoTitiSerializer titiSerializer;\n"
    		+ "  private final MapJsonValueSerializer<List<BigDecimal>> bestScoresSerializer = new MapJsonValueSerializer<>(new ListJsonValueSerializer<>(BigDecimalJsonValueSerializer.getInstance()));\n"
    		+ "  \n"
    		+ "  @Override\n"
    		+ "  public void serialize(MyPojo value, JsonGenerator gen, SerializerProvider provider) throws IOException {\n"
    		+ "    gen.writeStartObject();\n"
    		+ "    writeLongField(gen, \"id\", value.getId());\n"
    		+ "    writeStringField(gen, \"name\", value.getName());\n"
    		+ "    writeIntField(gen, \"level\", value.getLevel());\n"
    		+ "    writeBigDecimalField(gen, \"score\", value.getScore());\n"
    		+ "    writeBooleanField(gen, \"over\", value.isOver());\n"
    		+ "    writeCustomSerializerField(gen, \"bestScores\", value.getBestScores(), bestScoresSerializer, provider);\n"
    		+ "    if(fooSerializer == null) {\n"
    		+ "      fooSerializer = new ListJsonValueSerializer<>(new MyPojoFooSerializer());\n"
    		+ "    }\n"
    		+ "    writeCustomSerializerField(gen, \"f\", value.getFoo(), fooSerializer, provider);\n"
    		+ "    if(totoSerializer == null) {\n"
    		+ "      totoSerializer = new MapJsonValueSerializer<>(new ListJsonValueSerializer<>(new MyPojoTotoSerializer()));\n"
    		+ "    }\n"
    		+ "    writeCustomSerializerField(gen, \"toto\", value.getToto(), totoSerializer, provider);\n"
    		+ "    if(titiSerializer == null) {\n"
    		+ "      titiSerializer = new MyPojoTitiSerializer();\n"
    		+ "    }\n"
    		+ "    writeCustomSerializerField(gen, \"ti\", value.getTiti(), titiSerializer, provider);\n"
    		+ "    writeObjectField(gen, \"e\", value.getExt());\n"
    		+ "    gen.writeEndObject();\n"
    		+ "  }\n"
    		+ "}\n"
    		+ "", 
        generator.generate());
  }

}
