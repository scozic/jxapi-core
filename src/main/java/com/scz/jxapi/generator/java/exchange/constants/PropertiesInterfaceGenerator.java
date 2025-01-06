package com.scz.jxapi.generator.java.exchange.constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import com.scz.jxapi.exchange.descriptor.ConfigProperty;
import com.scz.jxapi.exchange.descriptor.DefaultConfigProperty;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.html.HtmlGenerationUtil;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.JavaTypeGenerator;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaGenUtil;
import com.scz.jxapi.util.PropertiesUtil;

/**
 * Generates a Java interface with properties.
 * <p>
 * Example:
 * 
 * <pre>
 * {@code
 * public interface MyProperties {
 * 
 * 	String MY_STRING_PROPERTY = "myString";
 * 	Integer MY_INT_PROPERTY = "myInt";
 	Integer MY_INT_DEFAULT_VALUE = Integer.valueOf(42);
 * 	Long MY_TIMESTAMP = Long.valueOf(System.currentTimeMillis());
 * 
 * 	default String getMyString(Properties properties) {return PropertiesUtil.getStringProperty(properties, MY_STRING_PROPERTY, null);}
 * 	default Integer getMyInt(Properties properties) {return PropertiesUtil.getIntProperty(properties, MY_INT, MY_INT_DEFAULT_VALUE);}
 * 	default Long getMyTimestamp(Properties properties) {return PropertiesUtil.getLongProperty(properties, MY_TIMESTAMP, null);}
 * }
 * }
 * </pre>
 * 
 * Where {@code MyProperties} is the interface name, {@code MY_STRING},
 * {@code MY_INT} and {@code MY_TIMESTAMP} are the property names,
 * {@code String}, {@code Integer} and {@code Long} are the types of the
 * properties and {@code "myString"}, {@code 42} and
 * {@code System.currentTimeMillis()} are the default values of the properties.
 */
public class PropertiesInterfaceGenerator extends JavaTypeGenerator {
	
	private final String exchangeName;
	private final List<DefaultConfigProperty> properties;

	/**
	 * Constructor
	 * 
	 * @param fullClassName Full name of the interface to generate, example:
	 *                      com.example.MyProperties
	 * @param exchangeName  The name of exchange configuration properties are
	 *                      generated for
	 * @param properties    List of properties to generate in the interface
	 */
	public PropertiesInterfaceGenerator(String fullClassName, String exchangeName, List<DefaultConfigProperty> properties) {
		super(fullClassName);
		this.exchangeName = exchangeName;
		this.properties = properties;
		setTypeDeclaration("public interface");
		setDescription(generateDescription());
	}
	
	private String generateDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("Configurable properties for <strong>")
		  .append(exchangeName)
		  .append("</strong> exchange:<br>\n");
		  
		List<String> columns = List.of("Name", "Type", "Description", "Default value");
		List<List<String>> rows = new ArrayList<>();
		for (ConfigProperty p : properties) {
			String pName = String.valueOf(p.getName());
			String pType = String.valueOf(p.getType());
			String pDesc = Optional.ofNullable(p.getDescription()).orElse("");
			String pDef = Optional.ofNullable(p.getDefaultValue()).orElse("").toString();
			rows.add(List.of(pName, pType, pDesc, pDef));
		}
		sb.append(HtmlGenerationUtil.generateTable(exchangeName + " properties", columns, rows));
		sb.append("<br>\nExposes helper methods are available to retrieve value of each of these properties "
					+ "with right type, returning default value if not present in properties");
		return sb.toString();
	}

	@Override
	public String generate() {
		ConstantsGenerationUtil.getConstantsForProperties(properties)
			.forEach(c -> appendToBody("\n")
						  .append(ConstantsGenerationUtil.generateConstantDeclaration(c, getImports())));
		this.properties.forEach(this::generatePropertyGetterMethod);
		return super.generate();
	}

	private void generatePropertyGetterMethod(ConfigProperty property) {
		StringBuilder sb = new StringBuilder()
				.append("\n");
		String name = property.getName();
		Type type = property.getType();
		Object def = property.getDefaultValue();
		StringBuilder desc = new StringBuilder()
				.append("Retrieves value of '")
				.append(name)
				.append("' property.\n")
				.append("@param properties Properties to look for value of '")
				.append(name)
				.append("' property into.\n")
				.append("@return Value found in properties or ");
		if (def != null) {
			desc.append("default value '")
				.append(property.getDefaultValue())
				.append("'");
				
		} else {
			desc.append("<code>")
				.append(JavaCodeGenerationUtil.NULL)
				.append("</code>");
		}
		desc.append(" if not found.");
				
		sb.append(JavaCodeGenerationUtil.generateJavaDoc(desc.toString()))
		  .append("\n");
		String typeClass = ExchangeJavaGenUtil.getClassNameForType(type, getImports(), null);
		String methodName = JavaCodeGenerationUtil.getGetAccessorMethodName(
			  					name, 
			  					typeClass, 
			  					properties.stream().map(p -> p.getName()).collect(Collectors.toList())
							);
		String getPropertiesUtilMethodName = getPropertiesUtilGetPropertyMethodName(property);
		String keyVariableName = JavaCodeGenerationUtil.getStaticVariableName(ConstantsGenerationUtil.getPropertyKeyPropertyName(property));
		String defaultValueVariableName = null;
		if (def != null) {
			defaultValueVariableName = JavaCodeGenerationUtil.getStaticVariableName(ConstantsGenerationUtil.getPropertyDefaultValuePropertyName(property));
		}
		addImport(Properties.class);
		addImport(PropertiesUtil.class);
		sb.append("static ")
		  .append(typeClass)
		  .append(" ")
		  .append(methodName)
		  .append("(Properties properties) {return PropertiesUtil.")
		  .append(getPropertiesUtilMethodName)
		  .append("(properties, ")
		  .append(keyVariableName)
		  .append(", ")
		  .append(defaultValueVariableName)
		  .append(");}\n");
		appendToBody(sb.toString());
	}
	
	private String getPropertiesUtilGetPropertyMethodName(ConfigProperty property) {
		switch (property.getType().getCanonicalType()) {
		case BIGDECIMAL:
			return "getBigDecimalProperty";
		case BOOLEAN:
			return "getBooleanProperty";
		case INT:
			return "getIntProperty";
		case LONG:
		case TIMESTAMP:
			return "getLongProperty";
		default:
			return "getStringProperty";
		}
	}
}
