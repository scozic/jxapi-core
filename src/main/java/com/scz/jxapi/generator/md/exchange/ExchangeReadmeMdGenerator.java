package com.scz.jxapi.generator.md.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.scz.jxapi.exchange.descriptor.ConfigProperty;
import com.scz.jxapi.exchange.descriptor.Constant;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.generator.html.HtmlGenerationUtil;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaGenUtil;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGeneratorUtil;
import com.scz.jxapi.generator.java.exchange.api.demo.EndpointDemoGeneratorUtil;
import com.scz.jxapi.util.CollectionUtil;

public class ExchangeReadmeMdGenerator {
	
	private static String findDemoClassName(ExchangeDescriptor exchangeDescriptor) {
		for (ExchangeApiDescriptor apiDescriptor : Optional.ofNullable(exchangeDescriptor.getApis()).orElse(List.of())) {
			if (!CollectionUtil.isEmpty(apiDescriptor.getRestEndpoints())) {
				return EndpointDemoGeneratorUtil.getRestApiDemoClassName(exchangeDescriptor, apiDescriptor, apiDescriptor.getRestEndpoints().get(0));
			}
			
			if (!CollectionUtil.isEmpty(apiDescriptor.getWebsocketEndpoints())) {
				return EndpointDemoGeneratorUtil.getWebsocketApiDemoClassName(exchangeDescriptor, apiDescriptor, apiDescriptor.getWebsocketEndpoints().get(0));
			}
		}
		return null;
	}
	
	private final ExchangeDescriptor exchangeDescriptor;
	
	private final String baseJavadocUrl;
	private final String baseSourceUrl;
	
	private final String exchangeInterfaceName;
	private final String exchangeInterfaceImplementationName;
	private final String demoClassName;

	private final String constantsInterfaceName;

	public ExchangeReadmeMdGenerator(ExchangeDescriptor exchangeDescriptor, String baseJavadocUrl, String baseSourceUrl) {
		this.exchangeDescriptor = exchangeDescriptor;
		this.baseJavadocUrl = baseJavadocUrl;
		this.baseSourceUrl = baseSourceUrl;
		this.exchangeInterfaceName = ExchangeJavaGenUtil.getExchangeInterfaceName(exchangeDescriptor);
		this.exchangeInterfaceImplementationName = ExchangeJavaGenUtil.getExchangeInterfaceImplementationName(exchangeInterfaceName);
		this.demoClassName = findDemoClassName(exchangeDescriptor);
		this.constantsInterfaceName = ExchangeJavaGenUtil.getExchangeConstantsInterfaceName(exchangeDescriptor);
	}
	
	public String generate() {
		StringBuilder s = new StringBuilder().append("# ")
			.append(exchangeDescriptor.getName())
			.append(" API Java wrapper\n\n")
			.append(exchangeDescriptor.getDescription())
			.append("\n");
		String docUrl = exchangeDescriptor.getDocUrl();
		if (docUrl != null) {
			s.append("See ")
			 .append(JavaCodeGenerationUtil.getHtmlLink(docUrl, "reference documentation"))
			 .append("\n");
			
		}
		s.append("### Quick start\n")
		 .append("\n")
		 .append("This wrapper offers Java interfaces for using ")
		 .append(exchangeDescriptor.getName())
		 .append(" API")
		 .append("\n")
		 .append("Add maven dependency to your project, then you can use the wrapper by instantiating ")
		 .append(getInterfaceJavadocLink(exchangeInterfaceName))
		 .append(" in your code:\n")
		 .append("``` java\n")
		 .append("import ")
		 .append(exchangeInterfaceName)
		 .append(";\n")
		 .append("import ")
		 .append(exchangeInterfaceImplementationName)
		 .append(";\n")
		 .append("\n")
		 .append("public void call() {\n")
		 .append("  Properties properties = new Properties();\n")
		 .append("  // Set relevant configuration properties (see documentation) in 'props'\n  ")
		 .append(JavaCodeGenerationUtil.getClassNameWithoutPackage(exchangeInterfaceName))
		 .append("   exchange = new ")
		 .append(JavaCodeGenerationUtil.getClassNameWithoutPackage(exchangeInterfaceImplementationName))
		 .append("(properties);\n")
		 .append("  // Access API groups and their endpoints through 'exchange' methods.\n")
		 .append("}\n")
				.append("```\n");
		if (demoClassName != null) {
			s.append("You may have a look at ")
			 .append(baseSourceUrl)
			 .append("/src/test/java/")
			 .append(demoClassName.replace('.', '/'))
			 .append(".java for full usage example\n");
		}
		List<ConfigProperty> properties = exchangeDescriptor.getProperties();
		if (!CollectionUtil.isEmpty(properties)) {
			s.append("\n### Properties\n\n")
			 .append(generatePropertiesTable(properties));
		}
		
		List<Constant> exchangeConstants = exchangeDescriptor.getConstants();
		if (!CollectionUtil.isEmpty(exchangeConstants)) {
			s.append("\n### Constants\n\n")
			 .append("Some useful constants are defined in ")
			 .append(getInterfaceJavadocLink(constantsInterfaceName))
			 .append("\n");
		}
		
		List<ExchangeApiDescriptor> apiDescriptors = exchangeDescriptor.getApis();
		if (!CollectionUtil.isEmpty(apiDescriptors)) {
			s.append("\n## API Groups\n")
			 .append("APIs are available using the following interfaces accessible from ")
			 .append(getInterfaceJavadocLink(exchangeInterfaceName))
			 .append(" interface\n");
			apiDescriptors.forEach(api -> s.append(generateApiDescriptorDoc(api)));
		}
		return s.toString();		
	}
	
	private String generateApiDescriptorDoc(ExchangeApiDescriptor api) {
		StringBuilder s = new StringBuilder();
		String apiInterfaceClassName = ExchangeJavaGenUtil.getApiInterfaceClassName(exchangeDescriptor, api);
		String apiInterfaceSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(apiInterfaceClassName);
		s.append("### ")
		 .append(api.getName())
		 .append("\n")
		 .append("Methods exposed in ")
		 .append(getInterfaceJavadocLink(apiInterfaceClassName))
		 .append(" accessible from ");
		String method = new StringBuilder()
				.append(exchangeInterfaceName)
				.append("#")
				.append("get")
				.append(apiInterfaceSimpleClassName)
				.append("()")
				.toString();
		 s.append(getInterfaceJavadocLink(method))
		  .append("\n\n");
		 if (api.getDescription() != null) {
			 s.append(api.getDescription()).append("\n");
		 }
		 if (!CollectionUtil.isEmpty(api.getRestEndpoints())) {
			 s.append("\n#### REST endpoints\n")
			  .append(generateRestEndpointsTable(api));
		 }
		
		 if (!CollectionUtil.isEmpty(api.getWebsocketEndpoints())) {
			 s.append("\n#### Websocket endpoints\n")
			  .append(generateWebsocketEndpointsTable(api));
			 
		 }
		
		List<Constant> apiConstants = api.getConstants();
		if (!CollectionUtil.isEmpty(apiConstants)) {
			String apiConstantsInterfaceName = ExchangeJavaGenUtil.getExchangeApiConstantsInterfaceName(exchangeDescriptor, api);
			s.append("Some useful constants are defined in ")
			 .append(getInterfaceJavadocLink(apiConstantsInterfaceName))
			 .append("\n");
		}
		return s.toString();
	}
	
	private Object generateWebsocketEndpointsTable(ExchangeApiDescriptor api) {
		List<WebsocketEndpointDescriptor> websocketEndpoints = api.getWebsocketEndpoints();
		String apiInterfaceClassName = ExchangeJavaGenUtil.getApiInterfaceClassName(exchangeDescriptor, api);
		List<String> columns = List.of("Subscription method", "Description", "API Reference");
		List<List<String>> cells = new ArrayList<>();
		websocketEndpoints.forEach(w -> {
			Type requestDataType = ExchangeJavaGenUtil.getFieldType(w.getRequest());
			String requestClassName = null;
			if (requestDataType != null && requestDataType.isObject()) {
				requestClassName = ExchangeApiGeneratorUtil.generateWebsocketEndpointRequestPojoClassName(
										exchangeDescriptor, 
										api, 
										w);
			}
			List<String> row = new ArrayList<>();
			String method = new StringBuilder()
					.append(ExchangeApiGeneratorUtil.getWebsocketSubscribeMethodName(w))
					.append("(")
					.append(JavaCodeGenerationUtil.getMethodArgumentJavadoc(requestDataType, requestClassName))
					.append(")")
					.toString();
			row.add(getInterfaceMethodJavadocLink(apiInterfaceClassName, method));
			row.add(Optional.ofNullable(w.getDescription()).orElse(""));
			String refDocLink = null;
			if (w.getDocUrl() != null) {
				refDocLink = JavaCodeGenerationUtil.getHtmlLink(w.getDocUrl(), "link");
			}
			row.add(Optional.ofNullable(refDocLink).orElse(""));
			cells.add(row);
		});
		String caption = new StringBuilder()
				.append(exchangeDescriptor.getName())
				.append(" ")
				.append(api.getName())
				.append(" websocket endpoints").toString();
		return HtmlGenerationUtil.generateTable(caption, columns, cells);
	}

	private String generateRestEndpointsTable(ExchangeApiDescriptor api) {
		List<RestEndpointDescriptor> restEndpoints = api.getRestEndpoints();
		String apiInterfaceClassName = ExchangeJavaGenUtil.getApiInterfaceClassName(exchangeDescriptor, api);
		List<String> columns = List.of("Endpoint", "Description", "API Reference");
		List<List<String>> cells = new ArrayList<>();
		restEndpoints.forEach(r -> {
			Type requestDataType = ExchangeJavaGenUtil.getFieldType(r.getRequest());
			String requestClassName = null;
			if (requestDataType != null && requestDataType.isObject()) {
				requestClassName = ExchangeApiGeneratorUtil.generateRestEnpointRequestPojoClassName(
										exchangeDescriptor, 
										api, 
										r);
			}
			List<String> row = new ArrayList<>();
			String method = new StringBuilder()
					.append(ExchangeApiGeneratorUtil.getRestApiMethodName(r))
					.append("(")
					.append(JavaCodeGenerationUtil.getMethodArgumentJavadoc(requestDataType, requestClassName))
					.append(")")
					.toString();
			row.add(getInterfaceMethodJavadocLink(apiInterfaceClassName, method));
			row.add(Optional.ofNullable(r.getDescription()).orElse(""));
			String refDocLink = null;
			if (r.getDocUrl() != null) {
				refDocLink = JavaCodeGenerationUtil.getHtmlLink(r.getDocUrl(), "link");
			}
			row.add(Optional.ofNullable(refDocLink).orElse(""));
			cells.add(row);
		});
		String caption = new StringBuilder()
				.append(exchangeDescriptor.getName())
				.append(" ")
				.append(api.getName())
				.append(" websocket endpoints").toString();
		return HtmlGenerationUtil.generateTable(caption, columns, cells);
	}
	
	private String getInterfaceJavadocLink(String interfaceClass) {
		return JavaCodeGenerationUtil.getHtmlLink(
				JavaCodeGenerationUtil.getClassJavadocUrl(baseJavadocUrl, interfaceClass), 
				JavaCodeGenerationUtil.getClassNameWithoutPackage(interfaceClass));
	}
	
	private String getInterfaceMethodJavadocLink(String interfaceClass, String methodJavadocLink) {
		return JavaCodeGenerationUtil.getHtmlLink(
				JavaCodeGenerationUtil.getClassJavadocUrl(baseJavadocUrl, interfaceClass) + "#" + methodJavadocLink, 
				JavaCodeGenerationUtil.getClassNameWithoutPackage(StringUtils.substringBefore(methodJavadocLink, "(")));
	}
	
	private String generatePropertiesTable(List<ConfigProperty> properties) {
		List<String> columns = List.of("Name", "Type", "description", "Default value");
		List<List<String>> cells = new ArrayList<>();
		properties.forEach(p -> {
			List<String> row = new ArrayList<>();
			row.add(Optional.ofNullable(p.getName()).orElse(""));
			row.add(String.valueOf(Optional.ofNullable(p.getType()).orElse(Type.STRING)));
			row.add(Optional.ofNullable(p.getDescription()).orElse(""));
			row.add(String.valueOf(Optional.ofNullable(p.getDefaultValue()).orElse("")));
			cells.add(row);
		});
		return HtmlGenerationUtil.generateTable("properties", columns, cells);
	}

	/**
	 * Performs generation and writes corresponding .md file.
	 * @param sourceFolder base directory (default package) for sources
	 * @throws IOException If an error occurs writing the file
	 */
	public void writeJavaFile(Path sourceFolder) throws IOException {
		sourceFolder.toFile().mkdirs();
		Files.writeString(sourceFolder.resolve(exchangeDescriptor.getName() + "_README.md"), generate());
	}
}
