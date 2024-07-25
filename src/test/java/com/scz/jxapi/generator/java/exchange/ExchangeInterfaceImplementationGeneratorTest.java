package com.scz.jxapi.generator.java.exchange;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;

/**
 * Unit test for {@link ExchangeInterfaceImplementationGenerator}
 */
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
				+ "import com.scz.jxapi.exchange.AbstractExchange;\n"
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
				+ "public class FooExchangeImpl extends AbstractExchange implements FooExchange {\n"
				+ "  \n"
				+ "  private final FooApi1Api fooApi1Api;\n"
				+ "  private final FooApi2Api fooApi2Api;\n"
				+ "  \n"
				+ "  public FooExchangeImpl(String exchangeName, Properties properties) {\n"
				+ "    super(ID, exchangeName, properties);\n"
				+ "    this.fooApi1Api = addApi(new FooApi1ApiImpl(getName(), properties));\n"
				+ "    this.fooApi2Api = addApi(new FooApi2ApiImpl(getName(), properties));\n"
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
				+ "}\n"
				+ "", exchangeGenerator.generate());
	}
	
	@Test
	public void testGenerateExchangeApiWithExchangeRateLimits() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("Foo");
		exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
		exchangeDescriptor.setDescription("Foo exchange description");
		List<ExchangeApiDescriptor> apis = new ArrayList<>();
		ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
		api1.setName("api1");
		api1.setRestEndpoints(List.of(new RestEndpointDescriptor()));
		apis.add(api1);
		// api2 has 0 REST endpoints and should not be provided request throttler in constructor.
		ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
		api2.setName("api2");
		api2.setRestEndpoints(List.of());
		apis.add(api2);
		ExchangeApiDescriptor api3 = new ExchangeApiDescriptor();
		api3.setName("api3");
		apis.add(api3);
		exchangeDescriptor.setApis(apis);
		exchangeDescriptor.setRateLimits(List.of(RateLimitRule.createRule("exchangeGlobalRateLimit", 60000, 1000)));
		ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
		Assert.assertEquals("package com.xyz.foo.gen;\n"
				+ "\n"
				+ "import com.scz.jxapi.exchange.AbstractExchange;\n"
				+ "import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;\n"
				+ "import com.scz.jxapi.netutils.rest.ratelimits.RequestThrottler;\n"
				+ "import com.xyz.foo.gen.api1.FooApi1Api;\n"
				+ "import com.xyz.foo.gen.api1.FooApi1ApiImpl;\n"
				+ "import com.xyz.foo.gen.api2.FooApi2Api;\n"
				+ "import com.xyz.foo.gen.api2.FooApi2ApiImpl;\n"
				+ "import com.xyz.foo.gen.api3.FooApi3Api;\n"
				+ "import com.xyz.foo.gen.api3.FooApi3ApiImpl;\n"
				+ "import java.util.Properties;\n"
				+ "\n"
				+ "/**\n"
				+ " * Actual implementation of {@link FooExchange}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class FooExchangeImpl extends AbstractExchange implements FooExchange {\n"
				+ "  \n"
				+ "  public static final RateLimitRule RATE_LIMIT_EXCHANGE_GLOBAL_RATE_LIMIT = RateLimitRule.createRule(\"exchangeGlobalRateLimit\", 60000, 1000);\n"
				+ "  \n"
				+ "  private final RequestThrottler requestThrottler = new RequestThrottler(\"Foo\");\n"
				+ "  private final FooApi1Api fooApi1Api;\n"
				+ "  private final FooApi2Api fooApi2Api;\n"
				+ "  private final FooApi3Api fooApi3Api;\n"
				+ "  \n"
				+ "  public FooExchangeImpl(String exchangeName, Properties properties) {\n"
				+ "    super(ID, exchangeName, properties);\n"
				+ "    this.fooApi1Api = addApi(new FooApi1ApiImpl(getName(), properties, requestThrottler));\n"
				+ "    this.fooApi2Api = addApi(new FooApi2ApiImpl(getName(), properties));\n"
				+ "    this.fooApi3Api = addApi(new FooApi3ApiImpl(getName(), properties));\n"
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
				+ "  @Override\n"
				+ "  public FooApi3Api getFooApi3Api() {\n"
				+ "    return this.fooApi3Api;\n"
				+ "  }\n"
				+ "  \n"
				+ "}\n", 
				exchangeGenerator.generate());
	}
	
	@Test
	public void testGenerateExchangeApiWithEmptyExchangeRateLimits() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("Foo");
		exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
		exchangeDescriptor.setDescription("Foo exchange description");
		List<ExchangeApiDescriptor> apis = new ArrayList<>();
		ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
		api1.setName("api1");
		api1.setRestEndpoints(List.of(new RestEndpointDescriptor()));
		apis.add(api1);
		exchangeDescriptor.setApis(apis);
		exchangeDescriptor.setRateLimits(List.of());
		ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
		Assert.assertEquals("package com.xyz.foo.gen;\n"
				+ "\n"
				+ "import com.scz.jxapi.exchange.AbstractExchange;\n"
				+ "import com.xyz.foo.gen.api1.FooApi1Api;\n"
				+ "import com.xyz.foo.gen.api1.FooApi1ApiImpl;\n"
				+ "import java.util.Properties;\n"
				+ "\n"
				+ "/**\n"
				+ " * Actual implementation of {@link FooExchange}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class FooExchangeImpl extends AbstractExchange implements FooExchange {\n"
				+ "  \n"
				+ "  private final FooApi1Api fooApi1Api;\n"
				+ "  \n"
				+ "  public FooExchangeImpl(String exchangeName, Properties properties) {\n"
				+ "    super(ID, exchangeName, properties);\n"
				+ "    this.fooApi1Api = addApi(new FooApi1ApiImpl(getName(), properties));\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public FooApi1Api getFooApi1Api() {\n"
				+ "    return this.fooApi1Api;\n"
				+ "  }\n"
				+ "  \n"
				+ "}\n"
				+ "", 
				exchangeGenerator.generate());
	}
	
	@Test
	public void testGenerateExchangeApiWithExchangeRateLimitsButNoApis() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("Foo");
		exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
		exchangeDescriptor.setDescription("Foo exchange description");
		exchangeDescriptor.setRateLimits(List.of(RateLimitRule.createRule("exchangeGlobalRateLimit", 60000, 1000)));
		ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
		Assert.assertEquals("package com.xyz.foo.gen;\n"
				+ "\n"
				+ "import com.scz.jxapi.exchange.AbstractExchange;\n"
				+ "import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;\n"
				+ "import java.util.Properties;\n"
				+ "\n"
				+ "/**\n"
				+ " * Actual implementation of {@link FooExchange}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class FooExchangeImpl extends AbstractExchange implements FooExchange {\n"
				+ "  \n"
				+ "  public static final RateLimitRule RATE_LIMIT_EXCHANGE_GLOBAL_RATE_LIMIT = RateLimitRule.createRule(\"exchangeGlobalRateLimit\", 60000, 1000);\n"
				+ "  \n"
				+ "  public FooExchangeImpl(String exchangeName, Properties properties) {\n"
				+ "    super(ID, exchangeName, properties);\n"
				+ "  }\n"
				+ "  \n"
				+ "}\n", 
				exchangeGenerator.generate());
	}
	
	@Test
	public void testGenerateExchangeApiWithExchangeRateLimitsButApisHaveNoRestEndpoint() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("Foo");
		exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
		exchangeDescriptor.setDescription("Foo exchange description");
		List<ExchangeApiDescriptor> apis = new ArrayList<>();
		// api2 has 0 REST endpoints and should not be provided request throttler in constructor.
		ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
		api1.setName("api1");
		api1.setRestEndpoints(List.of());
		apis.add(api1);
		ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
		api2.setName("api2");
		apis.add(api2);
		exchangeDescriptor.setApis(apis);
		exchangeDescriptor.setRateLimits(List.of(RateLimitRule.createRule("exchangeGlobalRateLimit", 60000, 1000)));
		ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
		Assert.assertEquals("package com.xyz.foo.gen;\n"
				+ "\n"
				+ "import com.scz.jxapi.exchange.AbstractExchange;\n"
				+ "import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;\n"
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
				+ "public class FooExchangeImpl extends AbstractExchange implements FooExchange {\n"
				+ "  \n"
				+ "  public static final RateLimitRule RATE_LIMIT_EXCHANGE_GLOBAL_RATE_LIMIT = RateLimitRule.createRule(\"exchangeGlobalRateLimit\", 60000, 1000);\n"
				+ "  private final FooApi1Api fooApi1Api;\n"
				+ "  private final FooApi2Api fooApi2Api;\n"
				+ "  \n"
				+ "  public FooExchangeImpl(String exchangeName, Properties properties) {\n"
				+ "    super(ID, exchangeName, properties);\n"
				+ "    this.fooApi1Api = addApi(new FooApi1ApiImpl(getName(), properties));\n"
				+ "    this.fooApi2Api = addApi(new FooApi2ApiImpl(getName(), properties));\n"
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
				+ "}\n", 
				exchangeGenerator.generate());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGenerateExchangeApiWithExchangeRateLimitNullRateLimitName() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("Foo");
		exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
		exchangeDescriptor.setDescription("Foo exchange description");
		exchangeDescriptor.setRateLimits(List.of(RateLimitRule.createRule(null, 60000, 1000)));
		ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
		exchangeGenerator.generate();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGenerateExchangeApiWithDuplicateExchangeRateLimitName() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("Foo");
		exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
		exchangeDescriptor.setDescription("Foo exchange description");
		exchangeDescriptor.setRateLimits(List.of(RateLimitRule.createRule("myReule", 60000, 1000), 
												 RateLimitRule.createRule("myReule", 60000, 1000)));
		ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
		exchangeGenerator.generate();
	}
	
	@Test
	public void testGenerateExchangeApiWithWeightedExchangeRateLimitRule() throws Exception {
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
		exchangeDescriptor.setName("Foo");
		exchangeDescriptor.setBasePackage("com.xyz.foo.gen");
		exchangeDescriptor.setDescription("Foo exchange description");
		exchangeDescriptor.setRateLimits(List.of(RateLimitRule.createWeightedRule("exchangeGlobalRateLimit", 60000, 1000)));
		ExchangeInterfaceImplementationGenerator exchangeGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
		Assert.assertEquals("package com.xyz.foo.gen;\n"
				+ "\n"
				+ "import com.scz.jxapi.exchange.AbstractExchange;\n"
				+ "import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;\n"
				+ "import java.util.Properties;\n"
				+ "\n"
				+ "/**\n"
				+ " * Actual implementation of {@link FooExchange}<br/>\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "public class FooExchangeImpl extends AbstractExchange implements FooExchange {\n"
				+ "  \n"
				+ "  public static final RateLimitRule RATE_LIMIT_EXCHANGE_GLOBAL_RATE_LIMIT = RateLimitRule.createWeightedRule(\"exchangeGlobalRateLimit\", 60000, 1000);\n"
				+ "  \n"
				+ "  public FooExchangeImpl(String exchangeName, Properties properties) {\n"
				+ "    super(ID, exchangeName, properties);\n"
				+ "  }\n"
				+ "  \n"
				+ "}\n", 
				exchangeGenerator.generate());
	}

}
