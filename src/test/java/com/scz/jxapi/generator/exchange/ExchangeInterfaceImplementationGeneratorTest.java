package com.scz.jxapi.generator.exchange;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ExchangeInterfaceImplementationGeneratorTest {
	
	@Test
	public void testGenerateExchangeApi() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("Foo");
		exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
		exchangeDescriptor.setDescription("Foo exchange description");
		List<ExchangeApiDescriptor> apis = new ArrayList<>();
		ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
		api1.setName("api1");
		apis.add(api1);
		ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
		api2.setName("api2");
		apis.add(api2);
		exchangeDescriptor.setApis(apis);
		ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
		Assert.assertEquals("package com.xyz.foo.gen;\n"
				+ "\n"
				+ "import com.xyz.foo.gen.api1.FooApi1Api;\n"
				+ "import com.xyz.foo.gen.api1.FooApi1ApiImpl;\n"
				+ "import com.xyz.foo.gen.api2.FooApi2Api;\n"
				+ "import com.xyz.foo.gen.api2.FooApi2ApiImpl;\n"
				+ "import java.util.Properties;\n"
				+ "\n"
				+ "/**\n"
				+ " * Actual implementation of {@link FooExchange}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class  FooExchangeImpl implements FooExchange {\n"
				+ "  \n"
				+ "  private final FooApi1Api fooApi1Api;\n"
				+ "  private final FooApi2Api fooApi2Api;\n"
				+ "  \n"
				+ "  public FooExchangeImpl(Properties properties) {\n"
				+ "    this.fooApi1Api = new FooApi1ApiImpl(properties);\n"
				+ "    this.fooApi2Api = new FooApi2ApiImpl(properties);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public FooApi1Api getFooApi1Api() {\n"
				+ "    return this.fooApi1Api;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public FooApi2Api getFooApi2Api() {\n"
				+ "    return this.fooApi2Api;\n"
				+ "  }\n"
				+ "  \n"
				+ "}\n", exchangeGenerator.generate());
	}

}
