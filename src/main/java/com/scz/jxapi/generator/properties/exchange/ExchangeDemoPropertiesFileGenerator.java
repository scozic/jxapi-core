package com.scz.jxapi.generator.properties.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.scz.jxapi.exchange.CommonConfigProperties;
import com.scz.jxapi.exchange.descriptor.ConfigProperty;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.util.CollectionUtil;
import com.scz.jxapi.util.DemoProperties;

public class ExchangeDemoPropertiesFileGenerator {
	
	private static final String DESCRIPTION = "Demo configuration properties file for %s exchange.\n"
			+ "You should create a copy of this file without the '.dist' extension and add that .properties file\n" 
			+ "to your .gitignore because properties may carry sensitive data you do not want to commit.\n"
			+ "The resulting file will be read as default configuration file for demo snippets.\n"
			+ "Uncomment and set following properties and adjust their values per your needs.";
	
	public static String generatePropertiesFileComment(String comment) {
		if (comment == null)
			return "";
		return JavaCodeGenerationUtil.indent(comment, "# ") + "\n";
	}

	private String exchangeId;
	private List<ConfigProperty> exchangeProperties;
	
	public ExchangeDemoPropertiesFileGenerator() {
		this(null, List.of());
	}

	public ExchangeDemoPropertiesFileGenerator(String exchangeId, List<ConfigProperty> exchangeProperties) {
		this.exchangeId = exchangeId;
		this.exchangeProperties = exchangeProperties;
	}

	public String getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}

	public List<ConfigProperty> getExchangeProperties() {
		return exchangeProperties;
	}

	public void setExchangeProperties(List<ConfigProperty> exchangeProperties) {
		this.exchangeProperties = exchangeProperties;
	}

	public String generate() {
		StringBuilder s = new StringBuilder();
		s.append(generatePropertiesFileComment(DESCRIPTION))
		 .append(generateCommentedOutproperties(String.format("%s specific configuration properties", exchangeId), exchangeProperties))
		 .append(generateCommentedOutproperties("Common configuration properties", CommonConfigProperties.ALL))
		 .append(generateCommentedOutproperties("Demo REST/WEBSOCKET snippets configuration properties", DemoProperties.ALL));
		return s.toString();
	}
	
	private String generateCommentedOutproperties(String description, List<ConfigProperty> properties) {
		if (CollectionUtil.isEmpty(exchangeProperties)) {
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
