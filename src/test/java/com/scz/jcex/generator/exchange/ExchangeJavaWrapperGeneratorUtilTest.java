package com.scz.jcex.generator.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.scz.jcex.generator.JavaCodeGenerationUtil;

/**
 * Unit test for {@link ExchangeJavaWrapperGeneratorUtil}
 */
public class ExchangeJavaWrapperGeneratorUtilTest {
	
	private Path srcFolder;
	
	@After
	public void tearDown() throws IOException {
		if (srcFolder != null) {
			JavaCodeGenerationUtil.deletePath(srcFolder);
			srcFolder = null;
		}
	}
	
	@Test
	public void testGeneratePojoFromEndpointParameters() throws Exception {
		srcFolder = Paths.get("tmp" + Math.random());
		
		/*
		 * Expected pojo structure:
		 * MyPojo
		 *  +- id: string
		 *  +- score: int
		 *  +- foos
		 *     +- time: timestamp
		 *     +- bar
		 *        +- name: string
		 *               
		 */
		String typeName = "com.x.MyPojo";
		String typeDescription = "Used in ExchangeJavaWrapperGeneratorUtilTest";
		List<EndpointParameter> endpointParameters = new ArrayList<>();
		endpointParameters.add(EndpointParameter.create(EndpointParameterType.STRING, "id", "identifier", "toto"));
		endpointParameters.add(EndpointParameter.create(EndpointParameterType.INT, "score", "Current score", "0"));
		endpointParameters.add(EndpointParameter.create(EndpointParameterType.STRUCT_LIST, "foo", "Foo list",
				Arrays.asList(EndpointParameter.create(EndpointParameterType.TIMESTAMP, "time", "Creation time", "0"),
							  EndpointParameter.create(EndpointParameterType.STRUCT, "bar", "The bar",
									  Arrays.asList(EndpointParameter.create(EndpointParameterType.STRING, "name", "Bar name", "my bar")))
						)
				));
		
		ExchangeJavaWrapperGeneratorUtil.generatePojo(srcFolder, typeName, typeDescription, endpointParameters);
		
		Path pkgFolder = srcFolder.resolve(Paths.get("com", "x"));
		Assert.assertEquals("package com.x;\n"
				+ "\n"
				+ "import com.scz.jcex.util.EncodingUtil;\n"
				+ "import java.util.List;\n"
				+ "\n"
				+ "/**\n"
				+ " * Used in ExchangeJavaWrapperGeneratorUtilTest\n"
				+ " */\n"
				+ "public class MyPojo {\n"
				+ "  private List<MyPojoFoo> foo;\n"
				+ "  private String id;\n"
				+ "  private int score;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Foo list\n"
				+ "   */\n"
				+ "  public List<MyPojoFoo> getFoo(){\n"
				+ "    return foo;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param foo Foo list\n"
				+ "   */\n"
				+ "  public void setFoo(List<MyPojoFoo> foo) {\n"
				+ "    this.foo = foo;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return identifier\n"
				+ "   */\n"
				+ "  public String getId(){\n"
				+ "    return id;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param id identifier\n"
				+ "   */\n"
				+ "  public void setId(String id) {\n"
				+ "    this.id = id;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Current score\n"
				+ "   */\n"
				+ "  public int getScore(){\n"
				+ "    return score;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param score Current score\n"
				+ "   */\n"
				+ "  public void setScore(int score) {\n"
				+ "    this.score = score;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString(){\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "  \n"
				+ "}\n"
				+ ""
				, Files.readString(pkgFolder.resolve(Paths.get("MyPojo.java"))));
		
		Assert.assertEquals("package com.x;\n"
				+ "\n"
				+ "import com.scz.jcex.util.EncodingUtil;\n"
				+ "\n"
				+ "/**\n"
				+ " * Foo list\n"
				+ " */\n"
				+ "public class MyPojoFoo {\n"
				+ "  private MyPojoFooBar bar;\n"
				+ "  private long time;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return The bar\n"
				+ "   */\n"
				+ "  public MyPojoFooBar getBar(){\n"
				+ "    return bar;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param bar The bar\n"
				+ "   */\n"
				+ "  public void setBar(MyPojoFooBar bar) {\n"
				+ "    this.bar = bar;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Creation time\n"
				+ "   */\n"
				+ "  public long getTime(){\n"
				+ "    return time;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param time Creation time\n"
				+ "   */\n"
				+ "  public void setTime(long time) {\n"
				+ "    this.time = time;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString(){\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "  \n"
				+ "}\n"
				+ "", Files.readString(pkgFolder.resolve(Paths.get("MyPojoFoo.java"))));
		
		Assert.assertEquals("package com.x;\n"
				+ "\n"
				+ "import com.scz.jcex.util.EncodingUtil;\n"
				+ "\n"
				+ "/**\n"
				+ " * The bar\n"
				+ " */\n"
				+ "public class MyPojoFooBar {\n"
				+ "  private String name;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Bar name\n"
				+ "   */\n"
				+ "  public String getName(){\n"
				+ "    return name;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param name Bar name\n"
				+ "   */\n"
				+ "  public void setName(String name) {\n"
				+ "    this.name = name;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString(){\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "  \n"
				+ "}\n"
				+ "" , Files.readString(pkgFolder.resolve(Paths.get("MyPojoFooBar.java"))));
		
	}

}
