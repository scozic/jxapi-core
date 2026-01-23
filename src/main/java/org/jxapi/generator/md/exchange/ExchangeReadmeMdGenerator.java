package org.jxapi.generator.md.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.generator.html.HtmlGenUtil;
import org.jxapi.generator.html.XmlElement;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import org.jxapi.generator.java.exchange.api.demo.EndpointDemoGenUtil;
import org.jxapi.generator.java.exchange.properties.PropertiesGenUtil;
import org.jxapi.generator.java.pojo.PojoGenUtil;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.PlaceHolderResolver;

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
  
  private final boolean hasDemoProperties;

  /**
   * Constructor.
   * 
   * @param exchangeDescriptor the exchange descriptor to generate the README for
   * @param baseJavadocUrl     base URL for Javadoc links
   * @param baseSourceUrl      base URL for source links
   * @param hasDemoProperties  <code>true</code> if the exchange has demo
   *                           properties, which is the case if at least one
   *                           endpoint is defined with a request containing
   *                           parameters.
   */
  public ExchangeReadmeMdGenerator(ExchangeDescriptor 
                                   exchangeDescriptor, 
                                   String baseJavadocUrl, 
                                   String baseSourceUrl, 
                                   boolean hasDemoProperties) {
    this.exchangeDescriptor = exchangeDescriptor;
    this.baseJavadocUrl = baseJavadocUrl;
    this.baseSourceUrl = baseSourceUrl;
    this.exchangeInterfaceName = ExchangeGenUtil.getExchangeInterfaceName(exchangeDescriptor);
    this.exchangeInterfaceImplementationName = ExchangeGenUtil.getExchangeInterfaceImplementationName(exchangeInterfaceName);
    this.demoClassName = findDemoClassName(exchangeDescriptor);
    this.constantsInterfaceName = ExchangeGenUtil.getExchangeConstantsClassName(exchangeDescriptor);
    this.hasDemoProperties = hasDemoProperties;
  }

  /**
   * Generates the README.md file content.
   * 
   * @return the README.md file content
   */
  public String generate() {
    PlaceHolderResolver docPlaceHolderResolver = PlaceHolderResolver
        .create(ExchangeGenUtil.getDescriptionReplacements(exchangeDescriptor, baseJavadocUrl));
    StringBuilder s = new StringBuilder().append("# ")
      .append(exchangeDescriptor.getId())
      .append(" API Java wrapper\n\n")
      .append(docPlaceHolderResolver.resolve(exchangeDescriptor.getDescription()))
      .append("\n")
      .append(ExchangeReadmeMdGeneratorUtil.BEGIN_TABLE_OF_CONTENTS_PLACEHOLDER)
      .append("\n")
      .append(ExchangeReadmeMdGeneratorUtil.END_TABLE_OF_CONTENTS_PLACEHOLDER)
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
    List<ConfigPropertyDescriptor> properties = exchangeDescriptor.getProperties();
    if (!CollectionUtil.isEmpty(properties) || hasDemoProperties) {
      s.append("\n### Properties\n\n");
      if (!CollectionUtil.isEmpty(properties)) {
        s.append(generatePropertiesTable("Configuration properties", properties, null, docPlaceHolderResolver));
      }
      if (hasDemoProperties) {
        String exchangeDemoPropertiesInterfaceName = 
          ExchangeGenUtil.getExchangeDemoPropertiesClassName(exchangeDescriptor);
        s.append(
            "\nSome demo configuration properties are available to tune common request parameters used in demo snippets, as ")
            .append(getSourceFileLink(exchangeDemoPropertiesInterfaceName, "test"))
            .append(" class.\n These properties are used to configure default values for request parameters used in demo snippets.\n\n")
            .append("In order to run demo snippets, you can uncomment and set properties values in __demo-")
            .append(exchangeDescriptor.getId())
            .append(".properties__ properties file you create from .dist template in src/test/resources folder.\n");
      }
    }
    
    List<Constant> exchangeConstants = Constant.fromDescriptors(exchangeDescriptor.getConstants());
    if (!CollectionUtil.isEmpty(exchangeConstants)) {
      s.append("\n\n### Constants\n\n")
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
      apiDescriptors.forEach(api -> s.append(generateApiDescriptorDoc(api, docPlaceHolderResolver)));
    }
    
    s.append(generateDemoSnippetsDocumentation());
    return ExchangeReadmeMdGeneratorUtil.generateTableOfContent(s.toString());    
  }
  
  private String generateApiDescriptorDoc(ExchangeApiDescriptor api, PlaceHolderResolver docPlaceHolderResolver) {
    StringBuilder s = new StringBuilder();
    String apiInterfaceClassName = ExchangeGenUtil.getApiInterfaceClassName(exchangeDescriptor, api);
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
       s.append(docPlaceHolderResolver.resolve(api.getDescription())).append("\n");
     }
     if (!CollectionUtil.isEmpty(api.getRestEndpoints())) {
       s.append("\n#### REST endpoints\n\n")
        .append(generateRestEndpointsTable(api, docPlaceHolderResolver));
     }
    
     if (!CollectionUtil.isEmpty(api.getWebsocketEndpoints())) {
       s.append("\n\n#### Websocket endpoints\n\n")
        .append(generateWebsocketEndpointsTable(api, docPlaceHolderResolver));
       
     }
    
    return s.toString();
  }
  
  private Object generateWebsocketEndpointsTable(ExchangeApiDescriptor api, PlaceHolderResolver docPlaceHolderResolver) {
    List<WebsocketEndpointDescriptor> websocketEndpoints = api.getWebsocketEndpoints();
    String apiInterfaceClassName = ExchangeGenUtil.getApiInterfaceClassName(exchangeDescriptor, api);
    List<String> columns = List.of("Subscription method", "Description", "API Reference");
    List<List<String>> cells = new ArrayList<>();
    websocketEndpoints.forEach(w -> {
      Type requestDataType = PojoGenUtil.getFieldType(w.getRequest());
      String requestClassName = null;
      if (requestDataType != null && requestDataType.isObject()) {
        requestClassName = ExchangeApiGenUtil.generateWebsocketEndpointRequestPojoClassName(
                    exchangeDescriptor, 
                    api, 
                    w);
      }
      List<String> row = new ArrayList<>();
      String method = new StringBuilder()
          .append(ExchangeApiGenUtil.getWebsocketSubscribeMethodName(w, websocketEndpoints))
          .append("(")
          .append(JavaCodeGenUtil.getMethodArgumentJavadoc(requestDataType, requestClassName))
          .append(")")
          .toString();
      row.add(getInterfaceMethodJavadocLink(apiInterfaceClassName, method));
      row.add(Optional.ofNullable(docPlaceHolderResolver.resolve(w.getDescription())).orElse(""));
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
    return HtmlGenUtil.generateTable(caption, columns, cells);
  }

  private String generateRestEndpointsTable(ExchangeApiDescriptor api, PlaceHolderResolver docPlaceHolderResolver) {
    List<RestEndpointDescriptor> restEndpoints = api.getRestEndpoints();
    String apiInterfaceClassName = ExchangeGenUtil.getApiInterfaceClassName(exchangeDescriptor, api);
    List<String> columns = List.of("Endpoint", "Description", "API Reference");
    List<List<String>> cells = new ArrayList<>();
    restEndpoints.forEach(r -> {
      boolean hasArguments = ExchangeApiGenUtil.restEndpointHasArguments(r, api);
      boolean requestHasBody = ExchangeApiGenUtil.restEndpointRequestHasBody(r);
      if (requestHasBody && !hasArguments) {
        r = r.deepClone();
        r.setRequest(ExchangeApiGenUtil.createDefaultRawBodyRequest());
        hasArguments = true;
      }
      Type requestDataType = PojoGenUtil.getFieldType(r.getRequest());
      String requestClassName = null;
      if (requestDataType != null && requestDataType.isObject()) {
        requestClassName = ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(
                    exchangeDescriptor, 
                    api, 
                    r);
      }
      List<String> row = new ArrayList<>();
      String method = new StringBuilder()
          .append(ExchangeApiGenUtil.getRestApiMethodName(r, restEndpoints))
          .append("(")
          .append(JavaCodeGenUtil.getMethodArgumentJavadoc(requestDataType, requestClassName))
          .append(")")
          .toString();
      row.add(getInterfaceMethodJavadocLink(apiInterfaceClassName, method));
      row.add(docPlaceHolderResolver.resolve(Optional.ofNullable(docPlaceHolderResolver.resolve(r.getDescription())).orElse("")));
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
    return HtmlGenUtil.generateTable(caption, columns, cells);
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
                JavaCodeGenUtil.getClassUrl(baseUrl, className, null, ".java"), 
                JavaCodeGenUtil.getClassNameWithoutPackage(className));
  }
  
  private String generatePropertiesTable(String 
                                         tableName, 
                                         List<ConfigPropertyDescriptor> properties, 
                                         String propertiesPrefix, 
                                         PlaceHolderResolver docPlaceHolderResolver) {
    List<String> columns = List.of("Name", "Type", "description", "Default value");
    List<List<XmlElement>> cells = new ArrayList<>();
    collectPropertiesTableRows(propertiesPrefix, properties, cells, docPlaceHolderResolver);
    XmlElement tableElement = XmlElement.builder()
        .tag("table")
        .child(XmlElement.builder().tag("caption").content(tableName).build())
        .child(XmlElement.builder().tag("tr").children(columns.stream()
            .map(col -> XmlElement.builder().tag("th").content(col).build())
            .toList()
          ).build())
        .children(cells.stream()
            .map(row -> XmlElement.builder().tag("tr").children(row).build())
            .toList())
        .build();
    return HtmlGenUtil.generateHtmlForElement(tableElement);
  }
  
  private void collectPropertiesTableRows(String prefix, 
                                          List<ConfigPropertyDescriptor> properties, 
                                          List<List<XmlElement>> cells, 
                                          PlaceHolderResolver docPlaceHolderResolver) {
    properties.forEach(p -> {
      List<XmlElement> row = new ArrayList<>();
      String name = StringUtils.defaultString(p.getName());
      String fullName = PropertiesGenUtil.getPropertyFullName(prefix, name);
      row.add(createTd(fullName));
      if (PropertiesGenUtil.isGroup(p)) {
        row.add(createTd("group"));
        String descr = StringUtils.defaultString(docPlaceHolderResolver.resolve(p.getDescription()));
        XmlElement descriptionTd = createTd(descr);
        descriptionTd.addAttribute("colspan", "2");
        row.add(descriptionTd);
        cells.add(row);
        collectPropertiesTableRows(fullName, p.getProperties(), cells, docPlaceHolderResolver);
        return;
      } else {
        row.add(createTd(String.valueOf(Optional.ofNullable(Type.fromTypeName(p.getType())).orElse(Type.STRING))));
        row.add(createTd(docPlaceHolderResolver.resolve(p.getDescription())));
        String defValue = String.valueOf(Optional.ofNullable(p.getDefaultValue()).orElse(""));
        defValue = docPlaceHolderResolver.resolve(defValue);
        row.add(createTd(defValue));
        cells.add(row);
      }
    });
  }
  
  private XmlElement createTd(String content) {
    return XmlElement.builder().tag("td").content(content).build();
  }
  
  private String generateDemoSnippetsDocumentation() {
    if (!hasDemoSection()) {
      return "";
    }
    String exchangeDemoPropertiesInterfaceName = ExchangeGenUtil.getExchangeDemoPropertiesClassName(exchangeDescriptor);
    StringBuilder s = new StringBuilder();
    s.append("\n\n## Demo snippets\n\n")
     .append("This wrapper contains demo snippets for the most important endpoints. These snippets are generated in _src/test/java/_ source folder.\n\n") 
     .append("Some demo configuration properties are available to tune common request parameters used in snippets, as ")
     .append(getSourceFileLink(exchangeDemoPropertiesInterfaceName, "test"))
     .append(" class.\n These properties are used to configure default values for request parameters used in demo snippets.\n\n")
     .append("In order to run demo snippets, you can set properties values in __demo-")
     .append(exchangeDescriptor.getId())
     .append(".properties__ properties file in src/test/resources folder.\n");
    
    if (hasEndpoints()) {
      s.append("\n\n### Endpoint demo snippets\n\n");
      exchangeDescriptor.getApis().stream()
        .map(this::generateApiDescriptorDemoSnippetsDocumentation)
        .forEach(s::append);
    }
    
    return s.toString();
  }
  
  private String generateApiDescriptorDemoSnippetsDocumentation(ExchangeApiDescriptor apiDescriptor) {
    StringBuilder s = new StringBuilder();
    if (!CollectionUtil.isEmpty(apiDescriptor.getRestEndpoints())) {
      s.append("\n#### ")
       .append(apiDescriptor.getName())
       .append(" REST endpoints demo snippets:\n\n");
       for (RestEndpointDescriptor restEndpoint : apiDescriptor.getRestEndpoints()) {
         String demoSnippetClassName = EndpointDemoGenUtil.getRestApiDemoClassName(exchangeDescriptor, apiDescriptor, restEndpoint);
         s.append(" - __")
          .append(restEndpoint.getName())
          .append("__: ")
          .append(getSourceFileLink(demoSnippetClassName, "test"))
          .append("\n");
       }
    }
    
    if (!CollectionUtil.isEmpty(apiDescriptor.getWebsocketEndpoints())) {
      s.append("\n#### ")
      .append(apiDescriptor.getName())
      .append(" Websocket endpoints demo snippets\n\n");
      for (WebsocketEndpointDescriptor websocketEndpoint : apiDescriptor.getWebsocketEndpoints()) {
        String demoSnippetClassName = EndpointDemoGenUtil.getWebsocketApiDemoClassName(exchangeDescriptor, apiDescriptor, websocketEndpoint);
        s.append(" - __")
         .append(websocketEndpoint.getName())
         .append("__: ")
         .append(getSourceFileLink(demoSnippetClassName, "test"))
         .append("\n");
      }
    }
    return s.toString();
  }
  
  /**
   * Checks if the exchange has a demo section, which is determined by the
   * presence of demo properties or any REST or WS endpoints.
   * 
   * @return true if there is a demo section, false otherwise
   */
  private boolean hasDemoSection() {
    return hasEndpoints();
  }
  
  private boolean hasEndpoints() {
    return CollectionUtil.emptyIfNull(exchangeDescriptor.getApis()).stream()
        .anyMatch(api -> !CollectionUtil.isEmpty(api.getRestEndpoints())
                          || !CollectionUtil.isEmpty(api.getWebsocketEndpoints()));
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
