package org.jxapi.generator.properties.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.exchange.CommonConfigProperties;
import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.properties.PropertiesGenUtil;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.DemoProperties;

/**
 * Generates a demo configuration properties file for an exchange.
 * 
 * <p>
 * The resulting file will be read as default configuration file for demo
 * snippets.
 * 
 * <p>
 * In order to run demo snippets for APIs generated in <code>src/test/java</code> should create a copy of this file without the '.dist' extension and add
 * that .properties file to your .gitignore because properties may carry
 * sensitive data you do not want to commit. Then uncomment and set properties you need.
 */
public class ExchangeDemoPropertiesFileGenerator {
  
  private static final String DESCRIPTION = "Demo configuration properties file for %s exchange.\n"
      + "You should create a copy of this file without the '.dist' extension and add that .properties file\n" 
      + "to your .gitignore because properties may carry sensitive data you do not want to commit.\n"
      + "The resulting file will be read as default configuration file for demo snippets.\n"
      + "Uncomment and set following properties and adjust their values per your needs.";
  
  /**
   * Generates a comment for a properties file.
   * This means that each line of the comment will be prefixed with a '# '.
   * @param comment the lines of the comment
   * @return the generated comment, followed by a newline
   */
  public static String generatePropertiesFileComment(String comment) {
    if (comment == null)
      return "";
    return JavaCodeGenUtil.indent(comment, "# ") + "\n";
  }

  private String exchangeId;
  private List<ConfigPropertyDescriptor> exchangeProperties;
  private List<ConfigPropertyDescriptor> demoProperties;
  
  /**
   * Constructor
     */
  public ExchangeDemoPropertiesFileGenerator() {
    this(null, List.of(), List.of());
  }

  /**
   * Constructor
   * 
   * @param exchangeId         the exchange id
   * @param exchangeProperties the exchange configuration properties
   */
  public ExchangeDemoPropertiesFileGenerator(String exchangeId, List<ConfigPropertyDescriptor> exchangeProperties, List<ConfigPropertyDescriptor> demoProperties) {
    this.exchangeId = exchangeId;
    this.exchangeProperties = exchangeProperties;
    this.demoProperties = demoProperties;
  }

  /**
   * @return the exchange id
   */
  public String getExchangeId() {
    return exchangeId;
  }

  /**
   * @param exchangeId the exchange id
   */
  public void setExchangeId(String exchangeId) {
    this.exchangeId = exchangeId;
  }

  /**
   * @return the exchange configuration properties
   */
  public List<ConfigPropertyDescriptor> getExchangeProperties() {
    return exchangeProperties;
  }

  /**
   * @param exchangeProperties the exchange configuration properties
   */
  public void setExchangeProperties(List<ConfigPropertyDescriptor> exchangeProperties) {
    this.exchangeProperties = exchangeProperties;
  }

  /**
   * Generates the properties file content.
   * 
   * @return the properties file content
   */
  public String generate() {
    StringBuilder s = new StringBuilder();
    s.append(generatePropertiesFileComment(String.format(DESCRIPTION, exchangeId)))
     .append(generateCommentedOutproperties(String.format("%s specific configuration properties", exchangeId), exchangeProperties))
     .append(generateCommentedOutproperties("Common configuration properties", convertToConfigPropertyDescriptors(CommonConfigProperties.ALL)))
     .append(generateCommentedOutproperties("Demo REST/WEBSOCKET snippets common configuration properties", convertToConfigPropertyDescriptors(DemoProperties.ALL)))
     .append(generateCommentedOutproperties("Demo REST/WEBSOCKET specific configuration properties", demoProperties));
    return s.toString();
  }
  
  private static List<ConfigPropertyDescriptor> convertToConfigPropertyDescriptors(List<ConfigProperty> properties) {
    return properties.stream().map(PropertiesGenUtil::asConfigPropertyDescriptor).collect(Collectors.toList());
  }
  
  private String generateCommentedOutproperties(String description, List<ConfigPropertyDescriptor> properties) {
    if (CollectionUtil.isEmpty(properties)) {
      return "";
    }
    StringBuilder s = new StringBuilder()
      .append("\n")
      .append(generatePropertiesFileComment(description))
      .append("\n")
      .append(generatePropertiesDeclarations(properties, 1));
    return s.toString();
  }
  
  private String generatePropertiesDeclarations(List<ConfigPropertyDescriptor> properties, int depth) {
    StringBuilder s = new StringBuilder();
    for (ConfigPropertyDescriptor property : properties) {
      if (property.isGroup()) {
        if (!StringUtils.isEmpty(property.getDescription())) {
          StringBuilder descr = new StringBuilder();
          descr.append(StringUtils.leftPad("", depth, '#'))
               .append(" ")
               .append(StringUtils.defaultString(property.getDescription()));
          s.append(generatePropertiesFileComment(descr.toString()));
        }
        
        s.append(generatePropertiesDeclarations(property.getProperties(), depth + 1));
      } else {
        if (!StringUtils.isEmpty(property.getDescription())) {
          s.append(generatePropertiesFileComment(property.getDescription()));
        }
        String defValue = Optional.ofNullable(property.getDefaultValue()).orElse("").toString();
        s.append(generatePropertiesFileComment(property.getName() + "=" + defValue))
         .append("\n");
      }
    }
    
    return s.toString();
  }
  
  /**
   * Performs generation and writes file containing result of {@link #generate()}.
   * @param propertiesFile file to write to
   * @throws IOException If an error occurs writing the file
   */
  public void writeJavaFile(Path propertiesFile) throws IOException {
    Files.writeString(propertiesFile, generate());
  }

  public List<ConfigPropertyDescriptor> getDemoProperties() {
    return demoProperties;
  }

  public void setDemoProperties(List<ConfigPropertyDescriptor> demoProperties) {
    this.demoProperties = demoProperties;
  }
  
  
}
