package com.scz.jxapi.generator.properties.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.scz.jxapi.exchange.CommonConfigProperties;
import com.scz.jxapi.exchange.descriptor.ConfigProperty;
import com.scz.jxapi.generator.java.JavaCodeGenUtil;
import com.scz.jxapi.util.CollectionUtil;
import com.scz.jxapi.util.DemoProperties;

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
	private List<ConfigProperty> exchangeProperties;
	
	/**
	 * Constructor
     */
	public ExchangeDemoPropertiesFileGenerator() {
		this(null, List.of());
	}

	/**
	 * Constructor
	 * 
	 * @param exchangeId         the exchange id
	 * @param exchangeProperties the exchange configuration properties
	 */
	public ExchangeDemoPropertiesFileGenerator(String exchangeId, List<ConfigProperty> exchangeProperties) {
		this.exchangeId = exchangeId;
		this.exchangeProperties = exchangeProperties;
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
	public List<ConfigProperty> getExchangeProperties() {
		return exchangeProperties;
	}

	/**
	 * @param exchangeProperties the exchange configuration properties
	 */
	public void setExchangeProperties(List<ConfigProperty> exchangeProperties) {
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
		 .append(generateCommentedOutproperties("Common configuration properties", CommonConfigProperties.ALL))
		 .append(generateCommentedOutproperties("Demo REST/WEBSOCKET snippets configuration properties", DemoProperties.ALL));
		return s.toString();
	}
	
	private String generateCommentedOutproperties(String description, List<ConfigProperty> properties) {
		if (CollectionUtil.isEmpty(properties)) {
			return "";
		}
		StringBuilder s = new StringBuilder()
			.append("\n\n")
			.append(generatePropertiesFileComment(description));
		for (ConfigProperty p: properties) {
			s.append("\n");
			String pdesc = p.getDescription();
			if (!"".equals(pdesc)) {
				s.append(generatePropertiesFileComment(p.getDescription()));
			}
			StringBuilder propDeclaration = new StringBuilder()
					.append(p.getName())
					.append("=");
			if (p.getDefaultValue() != null) {
				propDeclaration.append(p.getDefaultValue());
			}
			s.append(generatePropertiesFileComment(propDeclaration.toString()));
			
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
	
	
}
