package org.jxapi.generator.md.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import org.jxapi.exchange.descriptor.DefaultConfigProperty;
import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import org.jxapi.generator.html.HtmlGenerationUtil;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ExchangeJavaGenUtil;
import org.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import org.jxapi.generator.java.exchange.api.demo.EndpointDemoGenUtil;
import org.jxapi.util.CollectionUtil;

/**
 * Generates a README.md file for an exchange API wrapper.
 * This file contains a description of the API, properties, constants, and endpoints.
 */
public class ExchangeReadmeMdGenerator {
  
  private static String findDemoClassName(ExchangeDescriptor exchangeDescriptor) {
    for (ExchangeApiDescriptor apiDescriptor : Optional.ofNullable(exchangeDescriptor.getApis()).orElse(List.of())) {
      if (!CollectionUtil.isEmpty(apiDescriptor.getRestEndpoints())) {
        return EndpointDemoGenUtil.getRestApiDemoClassName(exchangeDescriptor, apiDescriptor, apiDescriptor.getRestEndpoints().get(0));
      }
      
      if (!CollectionUtil.isEmpty(apiDescriptor.getWebsocketEndpoints())) {
        return EndpointDemoGenUtil.getWebsocketApiDemoClassName(exchangeDescriptor, apiDescriptor, apiDescriptor.getWebsocketEndpoints().get(0));
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

  /**
   * Constructor.
   * 
   * @param exchangeDescriptor the exchange descriptor to generate the README for
   * @param baseJavadocUrl     base URL for Javadoc links
   * @param baseSourceUrl      base URL for source links
   */
  public ExchangeReadmeMdGenerator(ExchangeDescriptor exchangeDescriptor, String baseJavadocUrl, String baseSourceUrl) {
    this.exchangeDescriptor = exchangeDescriptor;
    this.baseJavadocUrl = baseJavadocUrl;
    this.baseSourceUrl = baseSourceUrl;
    this.exchangeInterfaceName = ExchangeJavaGenUtil.getExchangeInterfaceName(exchangeDescriptor);
    this.exchangeInterfaceImplementationName = ExchangeJavaGenUtil.getExchangeInterfaceImplementationName(exchangeInterfaceName);
    this.demoClassName = findDemoClassName(exchangeDescriptor);
    this.constantsInterfaceName = ExchangeJavaGenUtil.getExchangeConstantsInterfaceName(exchangeDescriptor);
  }

  /**
   * Generates the README.md file content.
   * 
   * @return the README.md file content
   */
  public String generate() {
    StringBuilder s = new StringBuilder().append("# ")
      .append(exchangeDescriptor.getId())
      .append(" API Java wrapper\n\n")
      .append(exchangeDescriptor.getDescription())
      .append("\n");
    String docUrl = exchangeDescriptor.getDocUrl();
    if (docUrl != null) {
      s.append("See ")
       .append(JavaCodeGenUtil.getHtmlLink(docUrl, "reference documentation"))
       .append("\n");
      
    }
    s.append("### Quick start\n")
     .append("\n")
     .append("This wrapper offers Java interfaces for using ")
     .append(exchangeDescriptor.getId())
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
     .append(JavaCodeGenUtil.getClassNameWithoutPackage(exchangeInterfaceName))
     .append("   exchange = new ")
     .append(JavaCodeGenUtil.getClassNameWithoutPackage(exchangeInterfaceImplementationName))
     .append("(properties);\n")
     .append("  // Access API groups and their endpoints through 'exchange' methods.\n")
     .append("}\n")
        .append("```\n");
    if (demoClassName != null) {
      s.append("You may have a look at ")
       .append(getSourceFileLink(demoClassName, "test"))
       .append(" class for full usage example\n");
    }
    List<DefaultConfigProperty> properties = exchangeDescriptor.getProperties();
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
    String apiInterfaceSimpleClassName = JavaCodeGenUtil.getClassNameWithoutPackage(apiInterfaceClassName);
    s.append("\n### ")
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
       s.append("\n#### REST endpoints\n\n")
        .append(generateRestEndpointsTable(api));
     }
    
     if (!CollectionUtil.isEmpty(api.getWebsocketEndpoints())) {
       s.append("\n#### Websocket endpoints\n\n")
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
        requestClassName = ExchangeApiGenUtil.generateWebsocketEndpointRequestPojoClassName(
                    exchangeDescriptor, 
                    api, 
                    w);
      }
      List<String> row = new ArrayList<>();
      String method = new StringBuilder()
          .append(ExchangeApiGenUtil.getWebsocketSubscribeMethodName(w))
          .append("(")
          .append(JavaCodeGenUtil.getMethodArgumentJavadoc(requestDataType, requestClassName))
          .append(")")
          .toString();
      row.add(getInterfaceMethodJavadocLink(apiInterfaceClassName, method));
      row.add(Optional.ofNullable(w.getDescription()).orElse(""));
      String refDocLink = null;
      if (w.getDocUrl() != null) {
        refDocLink = JavaCodeGenUtil.getHtmlLink(w.getDocUrl(), "link");
      }
      row.add(Optional.ofNullable(refDocLink).orElse(""));
      cells.add(row);
    });
    String caption = new StringBuilder()
        .append(exchangeDescriptor.getId())
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
        requestClassName = ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(
                    exchangeDescriptor, 
                    api, 
                    r);
      }
      List<String> row = new ArrayList<>();
      String method = new StringBuilder()
          .append(ExchangeApiGenUtil.getRestApiMethodName(r))
          .append("(")
          .append(JavaCodeGenUtil.getMethodArgumentJavadoc(requestDataType, requestClassName))
          .append(")")
          .toString();
      row.add(getInterfaceMethodJavadocLink(apiInterfaceClassName, method));
      row.add(Optional.ofNullable(r.getDescription()).orElse(""));
      String refDocLink = null;
      if (r.getDocUrl() != null) {
        refDocLink = JavaCodeGenUtil.getHtmlLink(r.getDocUrl(), "link");
      }
      row.add(Optional.ofNullable(refDocLink).orElse(""));
      cells.add(row);
    });
    String caption = new StringBuilder()
        .append(exchangeDescriptor.getId())
        .append(" ")
        .append(api.getName())
        .append(" REST endpoints").toString();
    return HtmlGenerationUtil.generateTable(caption, columns, cells);
  }
  
  private String getInterfaceJavadocLink(String interfaceClass) {
    return JavaCodeGenUtil.getHtmlLink(
        JavaCodeGenUtil.getClassJavadocUrl(baseJavadocUrl, interfaceClass), 
        JavaCodeGenUtil.getClassNameWithoutPackage(interfaceClass));
  }
  
  private String getInterfaceMethodJavadocLink(String interfaceClass, String methodJavadocLink) {
    return JavaCodeGenUtil.getHtmlLink(
        JavaCodeGenUtil.getClassJavadocUrl(baseJavadocUrl, interfaceClass) + "#" + methodJavadocLink, 
        JavaCodeGenUtil.getClassNameWithoutPackage(StringUtils.substringBefore(methodJavadocLink, "(")));
  }
  
  /**
   * Returns a link to the source file for the given class name.
   * @param className Fully qualified class name
   * @param srcFolderName Source folder name, either "main" or "test"
   * @return HTML link to the source file
   */
  private String getSourceFileLink(String className, String srcFolderName) {
    String baseUrl = String.format("%s/src/%s/java/", baseSourceUrl, srcFolderName);
        return JavaCodeGenUtil.getHtmlLink(
                JavaCodeGenUtil.getClassUrl(baseUrl, className, ".java"), 
                JavaCodeGenUtil.getClassNameWithoutPackage(className));
  }
  
  private String generatePropertiesTable(List<DefaultConfigProperty> properties) {
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
    Files.writeString(sourceFolder.resolve(exchangeDescriptor.getId() + "_README.md"), generate());
  }
}
