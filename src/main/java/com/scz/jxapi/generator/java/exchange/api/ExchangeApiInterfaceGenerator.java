package com.scz.jxapi.generator.java.exchange.api;

import java.util.Optional;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.JavaTypeGenerator;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaWrapperGeneratorUtil;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.websocket.WebsocketListener;

/**
 * Generates source code for a Java interface for an
 * {@link ExchangeApiDescriptor} defined in an exchange descriptor file.
 * This interface contains:
 * <ul>
 * <li>A function for every REST endpoint call, and subscribe and unsubscribe to
 * websocket stream methods for every websocket endpoint.
 * <li>A static variable for every REST and Websocket endpoint name.
 * <li>A static variable for the API name.
 * </ul>
 * The static variables are used to identify the API and its endpoints in the
 * API wrapper implementation, as it is provided in observability events.
 * 
 * @see ExchangeApiDescriptor
 */
public class ExchangeApiInterfaceGenerator extends JavaTypeGenerator {
	
	public static final String EXCHANGE_API_NAME_VARIABLE = "ID";
	
	private final ExchangeDescriptor exchangeDescriptor;
	
	private final ExchangeApiDescriptor exchangeApiDescriptor;
	
	/**
	 * Constructor.
	 * 
	 * @param exchangeDescriptor    the exchange descriptor where the API is defined
	 * @param exchangeApiDescriptor the API descriptor to generate interface for
	 */
	public ExchangeApiInterfaceGenerator(ExchangeDescriptor exchangeDescriptor, 
										 ExchangeApiDescriptor exchangeApiDescriptor) {
		super(ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor));
		this.exchangeDescriptor = exchangeDescriptor;
		this.exchangeApiDescriptor = exchangeApiDescriptor;
		setDescription(exchangeDescriptor.getName() + " " + exchangeApiDescriptor.getName() + " API</br>\n" 
				+ exchangeApiDescriptor.getDescription() + "\n" 
				+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		setTypeDeclaration("public interface");
		this.setParentClassName(ExchangeApi.class.getName());
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Generates the whole interface source code, including:
	 * <ul>
	 * <li>Static variables for the API name and every REST and Websocket endpoint
	 * name.</li>
	 * <li>Method declarations for every REST endpoint call, and subscribe and
	 * unsubscribe to websocket stream methods for every websocket endpoint.</li>
	 * </ul>
	 */
	@Override
	public String generate() {
		appendToBody("String ")
			.append(EXCHANGE_API_NAME_VARIABLE)
			.append(" = ")
			.append(JavaCodeGenerationUtil.getQuotedString(exchangeApiDescriptor.getName()))
			.append(";\n");
		if (exchangeApiDescriptor.getRestEndpoints() != null) {
			for (RestEndpointDescriptor restApi: exchangeApiDescriptor.getRestEndpoints()) {
				generateApiNameVariableDeclaration(
						restApi.getName(), 
						ExchangeApiGeneratorUtil.getRestEndpointNameStaticVariable(restApi.getName()));
			}
		}
		
		if (exchangeApiDescriptor.getWebsocketEndpoints() != null) {
			for (WebsocketEndpointDescriptor websocketApi : exchangeApiDescriptor.getWebsocketEndpoints()) {
				generateApiNameVariableDeclaration(
						websocketApi.getName(), 
						ExchangeApiGeneratorUtil.getWebsocketEndpointNameStaticVariable(websocketApi.getName()));
			}
		}
		
		if (exchangeApiDescriptor.getRestEndpoints() != null) {
			for (RestEndpointDescriptor restApi: exchangeApiDescriptor.getRestEndpoints()) {
				generateRestEndpointMethodDeclaration(restApi);
			}
		}
		
		if (exchangeApiDescriptor.getWebsocketEndpoints() != null) {
			for (WebsocketEndpointDescriptor websocketApi : exchangeApiDescriptor.getWebsocketEndpoints()) {
				generateWebsocketApiMethodsDeclarations(websocketApi);
			}
		}
		
		return super.generate();
	}
	
	private void generateApiNameVariableDeclaration(String apiName, String apiNameVariable) {
		appendToBody("String ")
			.append(apiNameVariable)
			.append(" = ")
			.append(JavaCodeGenerationUtil.getQuotedString(apiName))
			.append(";\n");
	}

	private void generateWebsocketApiMethodsDeclarations(WebsocketEndpointDescriptor websocketApi) {
		Type requestDataType = ExchangeJavaWrapperGeneratorUtil.getFieldType(websocketApi.getRequest());
		boolean hasArguments = ExchangeApiGeneratorUtil.websocketEndpointHasArguments(websocketApi, exchangeApiDescriptor);
		String requestSimpleClassName = Object.class.getSimpleName();
		String requestDescription = null;
		String requestArgName = null;
		if (hasArguments) {
			String requestClassName = null;
			if (requestDataType != null && requestDataType.isObject()) {
				requestClassName = ExchangeApiGeneratorUtil.generateWebsocketEndpointRequestPojoClassName(
						exchangeDescriptor, 
						exchangeApiDescriptor, 
						websocketApi);
			}
			requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
													requestDataType, 
													getImports(), 
													requestClassName);
			requestDescription = websocketApi.getRequest().getDescription();
			requestArgName = ExchangeApiGeneratorUtil.getRequestArgName(websocketApi.getRequest().getName());
		}
		
		Type messageDataType = ExchangeJavaWrapperGeneratorUtil.getFieldType(websocketApi.getMessage());
		String messageClassSimpleName = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
				messageDataType, 
				getImports(), 
				ExchangeApiGeneratorUtil.generateWebsocketEndpointMessagePojoClassName(
						exchangeDescriptor, 
						exchangeApiDescriptor, 
						websocketApi));
		String subscribeMethodName = ExchangeApiGeneratorUtil.getWebsocketSubscribeMethodName(websocketApi);
		String unsubscribeMethodName = ExchangeApiGeneratorUtil.getWebsocketUnsubscribeMethodName(websocketApi);
		addImport(WebsocketListener.class);
		String subscribeMethodSignature = new StringBuilder()
				.append("String ")
				.append(subscribeMethodName)
				.append("(")
				.append(hasArguments? requestSimpleClassName 
										+ " " + requestArgName 
										+ ", "
									: "")
				.append("WebsocketListener<")
				.append(messageClassSimpleName)
				.append("> listener)").toString();
		
		StringBuilder subscribeMethodDoc = new StringBuilder()
				.append("Subscribe to ")
				.append(websocketApi.getName())
				.append(" stream.<br>\n");
		if (websocketApi.getDocUrl() != null) {
			subscribeMethodDoc.append(JavaCodeGenerationUtil.getHtmlLink(websocketApi.getDocUrl(), "Reference documentation"))
			 	   .append(subscribeMethodName)
				   .append("()\n");
		}
		if (websocketApi.getDescription() != null) {
			subscribeMethodDoc.append(websocketApi.getDescription())
								  .append("\n");	
		}
		subscribeMethodDoc.append("\n");
		if (requestDescription != null) {
			subscribeMethodDoc.append("@param ")
							  .append(requestArgName)
							  .append(" ")
							  .append(requestDescription)
							  .append("\n");
		}
		subscribeMethodDoc.append("@return client subscriptionId to use for unsubscription");
		
		appendToBody("\n");
		appendToBody(JavaCodeGenerationUtil.generateJavaDoc(subscribeMethodDoc.toString()));
		appendToBody("\n");
		appendToBody(subscribeMethodSignature + ";\n\n");
		
		String unsubscribeMethodSignature = "boolean " + unsubscribeMethodName + "(String subscriptionId)";
		StringBuilder unsubscribeMethodDoc = new StringBuilder()
				.append("Unsubscribe from ")
				.append(websocketApi.getName())
				.append(" stream.\n");
		if (websocketApi.getDocUrl() != null) {
			unsubscribeMethodDoc.append(JavaCodeGenerationUtil.getHtmlLink(websocketApi.getDocUrl(), "Reference documentation"))
			 	   .append(subscribeMethodName)
				   .append("()\n");
		}
		unsubscribeMethodDoc.append("\n@param subscriptionId ID of subscription returned by #")
			   				.append(subscribeMethodName)
			   				.append("()");
		appendToBody(JavaCodeGenerationUtil.generateJavaDoc(unsubscribeMethodDoc.toString()))
			.append("\n")
			.append(unsubscribeMethodSignature)
			.append(";\n");
	}

	private void generateRestEndpointMethodDeclaration(RestEndpointDescriptor restApi) {
		boolean hasArguments = ExchangeApiGeneratorUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor);
		Field request = restApi.getRequest();
		Type requestDataType = ExchangeJavaWrapperGeneratorUtil.getFieldType(request);
		Field response = restApi.getResponse();
		Type responseDataType = ExchangeJavaWrapperGeneratorUtil.getFieldType(response);
		String requestSimpleClassName = "Object";
		String requestArgName = null;
		if (hasArguments) {
			String requestClassName = null;
			if (requestDataType != null && requestDataType.isObject()) {
				requestClassName = ExchangeApiGeneratorUtil.generateRestEnpointRequestPojoClassName(
										exchangeDescriptor, 
										exchangeApiDescriptor, 
										restApi);
			}
			requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
										requestDataType, 
										getImports(), 
										requestClassName);
			requestArgName = ExchangeApiGeneratorUtil.getRequestArgName(request.getName());
		}
		boolean hasResponse = ExchangeApiGeneratorUtil.restEndpointHasResponse(restApi, exchangeApiDescriptor);
		String responseSimpleClassName = "String";
		if (hasResponse) {
			String restResponseClassName = null;
			if (responseDataType != null && responseDataType.isObject()) {
				restResponseClassName = ExchangeApiGeneratorUtil.generateRestEnpointResponsePojoClassName(
											exchangeDescriptor, 
											exchangeApiDescriptor, 
											restApi);
			}
			responseSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
					responseDataType, 
					getImports(), 
					restResponseClassName);
		}
		String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName());
		String apiMethodSignature =  new StringBuilder()
				.append(FutureRestResponse.class.getSimpleName())
				.append("<")
				.append(responseSimpleClassName)
				.append("> ")
				.append(apiMethodName)
				.append("(")
				.append(hasArguments? requestSimpleClassName 
									   + " " +
									   requestArgName
									 : "")
				.append(")").toString(); 
		StringBuilder javaDoc = new StringBuilder();
		if (restApi.getDescription() != null) {
			javaDoc.append(restApi.getDescription())
				   .append("\n");
		}
		if (hasArguments && request.getDescription() != null) {
			javaDoc.append("@param ")
				   .append(requestArgName)
				   .append(" ")
				   .append(request.getDescription())
				   .append("\n");
		}
		
		javaDoc.append("@return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed.");
		if (hasResponse && response.getDescription() != null) {
			javaDoc.append("If successful, will provide response:")
				   .append(response.getDescription())
			       .append("\n");
		}
		
		if (restApi.getDocUrl() != null) {
			javaDoc.append("@see ")
				   .append(JavaCodeGenerationUtil.getHtmlLink(restApi.getDocUrl(), "Reference documentation"))
			 	   .append("\n");
		}
		if (javaDoc.length() > 0) {
			// Remove trailing '\n'
			javaDoc.delete(javaDoc.length() - 1, javaDoc.length());
			appendToBody(JavaCodeGenerationUtil.generateJavaDoc(javaDoc.toString())).append("\n");
		}
		appendToBody(apiMethodSignature + ";\n");
		addImport(FutureRestResponse.class);
	}

}
