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
import com.scz.jxapi.generator.java.JavaCodeGenUtil;
import com.scz.jxapi.generator.java.JavaTypeGenerator;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaGenUtil;
import com.scz.jxapi.util.PropertiesUtil;

/**
 * Generates a Java class exposing {@link ConfigProperty} instances and
 * <code>static</code> methods for retrieving value of configuration properties
 * of a given exchange.
 * <p>
 * Example:
 * 
 * <pre>
 * {@code
 *  public class MyProperties {
 *  
 *    private MyProperties(){}
 *  
 *    public static final ConfigProperty HOST = DefaultConfigProperty.create(
 *      "host",
 *      Type.STRING,
 *      "Mock HTTP server host",
 *      "localhost");
 *      
 *    public static final ConfigProperty HTTP_PORT = DefaultConfigProperty.create(
 *      "httpPort",
 *      Type.INT,
 *      "Mock HTTP/Websocket server port",
 *      80);
 * 
 *    public static String getHost(Properties properties) {return PropertiesUtil.getStringProperty(properties, HOST.getName(), HOST.getDefaultValue());}
 *
 *    public static Integer getHttpPort(Properties properties) {return PropertiesUtil.getIntProperty(properties, HTTP_PORT.getName(), HTTP_PORT.getDefaultValue());}
 *    
 *   }
 * }  
 * </pre>
 * 
 * Where {@code MyProperties} is the interface name, {@code MY_STRING},
 * {@code MY_INT} and {@code MY_TIMESTAMP} are the property names,
 * {@code String}, {@code Integer} and {@code Long} are the types of the
 * properties and {@code "myString"}, {@code 42} and
 * {@code System.currentTimeMillis()} are the default values of the properties.
 */
public class PropertiesClassGenerator extends JavaTypeGenerator {
	
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
	public PropertiesClassGenerator(String fullClassName, String exchangeName, List<DefaultConfigProperty> properties) {
		super(fullClassName);
		this.exchangeName = exchangeName;
		this.properties = properties;
		setTypeDeclaration("public class");
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
		sb.append(HtmlGenerationUtil.generateTable(exchangeName + " properties", columns, rows))
		  .append("<br>\nExposes helper methods are available to retrieve value of each of these properties ")
		  .append("with right type, returning default value if not present in properties.")
		  .append("\n@see ConfigProperty");
		return sb.toString();
	}

	@Override
	public String generate() {
		appendToBody("\nprivate ")
		.append(getSimpleName())
		.append("(){}\n");
		properties
			.forEach(c -> appendToBody("\n")
					      .append(ConstantsGenerationUtil.getPropertyValueDeclation(c, getImports()))
					      .append("\n"));
		this.properties.forEach(this::generatePropertyGetterMethod);
		generatePropertiesListMethod();
		return super.generate();
	}

	private void generatePropertiesListMethod() {
		addImport(List.class);
		appendToBody("\n")
			.append(JavaCodeGenUtil.generateJavaDoc("List of all configuration properties defined in this class"))
			.append("\npublic static final List<ConfigProperty> ALL = List.of(\n")
			.append(JavaCodeGenUtil.INDENTATION)
			.append(properties.stream()
				.map(p -> JavaCodeGenUtil.getStaticVariableName(ConstantsGenerationUtil.getPropertyKeyPropertyName(p)))
				.collect(Collectors.joining(", \n" + JavaCodeGenUtil.INDENTATION)))
			.append(");");
		
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
				.append(JavaCodeGenUtil.NULL)
				.append("</code>");
		}
		desc.append(" if not found.");
				
		sb.append(JavaCodeGenUtil.generateJavaDoc(desc.toString()))
		  .append("\n");
		String typeClass = ExchangeJavaGenUtil.getClassNameForType(type, getImports(), null);
		String methodName = JavaCodeGenUtil.getGetAccessorMethodName(
			  					name, 
			  					typeClass, 
			  					properties.stream().map(p -> p.getName()).collect(Collectors.toList())
							);
		String getPropertiesUtilMethodName = getPropertiesUtilGetPropertyMethodName(property);
		String keyVariableName = JavaCodeGenUtil.getStaticVariableName(ConstantsGenerationUtil.getPropertyKeyPropertyName(property));
		addImport(Properties.class);
		addImport(PropertiesUtil.class);
		sb.append("public static ")
		  .append(typeClass)
		  .append(" ")
		  .append(methodName)
		  .append("(Properties properties) {return PropertiesUtil.")
		  .append(getPropertiesUtilMethodName)
		  .append("(properties, ")
		  .append(keyVariableName)
		  .append(".getName(), ")
		  .append(keyVariableName)
		  .append(".getDefaultValue());}\n");
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
