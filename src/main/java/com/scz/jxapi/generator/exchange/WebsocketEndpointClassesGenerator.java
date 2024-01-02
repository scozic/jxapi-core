package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Generates all classes used by a particular Websocket endpoint defined in an  
 * exchange descriptor file. This covers:
 * <ul>
 * <li>POJOs for endpoint request and response
 * <li>JSON deserializers for those POJOs
 * <li>JSON serializers for those POJOs
 * </ul>
 * @see RestEndpointDescriptor
 */
public class WebsocketEndpointClassesGenerator implements ClassesGenerator {
	
	private static final String DEFAULT_STRING_LIST_SEPARATOR = ",";
	
	private static String generateWebsocketSubscribeParametersGetTopicMethod(WebsocketEndpointDescriptor wsEndpointDescriptor) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n@Override\npublic String getTopic() {\n")
		  .append(JavaCodeGenerationUtil.INDENTATION)
		  .append(generateGetUrlParametersBodyFromTemplate(wsEndpointDescriptor.getTopic(), wsEndpointDescriptor.getParameters(), wsEndpointDescriptor.getTopicParametersListSeparator()))
		  .append("}\n\n");
		return sb.toString(); 
	}
	
	private static String generateGetUrlParametersBodyFromTemplate(String urlParametersTemplate, List<EndpointParameter> endpointParameters, String stringListSeparator) {
		if (endpointParameters.isEmpty()) {
			return "return \"" + urlParametersTemplate + "\";\n";
		}
		if (stringListSeparator == null) {
			stringListSeparator = DEFAULT_STRING_LIST_SEPARATOR;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("return ")
		  .append(EncodingUtil.class.getSimpleName())
		  .append(".substituteArguments(\"")
		  .append(urlParametersTemplate)
		  .append("\", ");
		int n = endpointParameters.size();
		for (int i = 0; i < n; i++) {
			String name = endpointParameters.get(i).getName();
			String value = name;
			if (endpointParameters.get(i).getEndpointParameterType().getType() == EndpointParameterTypes.LIST) {
				value = EncodingUtil.class.getSimpleName() + ".listToString(" + name + ", \"" + stringListSeparator + "\")"; 
			}
			sb.append("\"").append(name).append("\", ").append(value);
			if (i < n - 1) {
				sb.append(", ");
			}
		}
		sb.append(");\n");
		return sb.toString();
	}
	
	private final ExchangeDescriptor exchangeDescriptor;
	private final ExchangeApiDescriptor apiDescriptor;
	private final WebsocketEndpointDescriptor websocketEndpointDescriptor;
	
	/**
	 * 
	 */
	public WebsocketEndpointClassesGenerator(ExchangeDescriptor exchangeDescriptor, 
			ExchangeApiDescriptor apiDescriptor, 
			WebsocketEndpointDescriptor websocketEndpointDescriptor) {
		this.exchangeDescriptor = exchangeDescriptor;
		this.apiDescriptor = apiDescriptor;
		this.websocketEndpointDescriptor = websocketEndpointDescriptor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generateClasses(Path outputFolder) throws IOException {
		// Generate POJOs for request and response
		generatePojos(outputFolder);
		
		// Generate deserializers for request/response pojos
		generateDeserializers(outputFolder);
		
		// Generate serializers
		generateSerializers(outputFolder);
	}

	private void generateSerializers(Path outputFolder) throws IOException {
		ExchangeJavaWrapperGeneratorUtil.generateSerializer(outputFolder, 
				ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointRequestClassName(exchangeDescriptor, apiDescriptor, websocketEndpointDescriptor),
				websocketEndpointDescriptor.getParameters());
	
		ExchangeJavaWrapperGeneratorUtil.generateSerializer(outputFolder, 
				ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointMessageClassName(exchangeDescriptor, apiDescriptor, websocketEndpointDescriptor),
				websocketEndpointDescriptor.getResponse());
		
	}

	private void generateDeserializers(Path outputFolder) throws IOException {
		ExchangeJavaWrapperGeneratorUtil.generateDeserializer(outputFolder, 
				ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointMessageClassName(exchangeDescriptor, apiDescriptor, websocketEndpointDescriptor),
				websocketEndpointDescriptor.getResponse());
	}

	private void generatePojos(Path outputFolder) throws IOException {
		new EndpointPojoClassesGenerator( 
				ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointRequestClassName(exchangeDescriptor, apiDescriptor, websocketEndpointDescriptor), 
				"Subscription request to" + exchangeDescriptor.getName() 
					+ " " + apiDescriptor.getName() + " API " 
					+ websocketEndpointDescriptor.getName() 
					+ " websocket endpoint<br/>\n" 
					+ websocketEndpointDescriptor.getDescription()
					+ "\n"
					+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
					websocketEndpointDescriptor.getParameters(), 
				Arrays.asList(WebsocketSubscribeParameters.class.getName()), 
				generateWebsocketSubscribeParametersGetTopicMethod(websocketEndpointDescriptor)).generateClasses(outputFolder);;
		new EndpointPojoClassesGenerator( 
				ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointMessageClassName(exchangeDescriptor, apiDescriptor, websocketEndpointDescriptor), 
				"Message disseminated upon subscription to " 
					+ exchangeDescriptor.getName() + " " 
					+ apiDescriptor.getName() + " API " 
					+ websocketEndpointDescriptor.getName() + " websocket endpoint request<br/>\n"
					+ websocketEndpointDescriptor.getDescription()
					+ "\n"
					+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
				websocketEndpointDescriptor.getResponse(), 
				websocketEndpointDescriptor.getResponseInterfaces(), 
				null).generateClasses(outputFolder);;
		
	}

}
