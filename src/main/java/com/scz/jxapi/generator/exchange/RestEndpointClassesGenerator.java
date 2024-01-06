package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Generates all classes used by a particular REST endpoint defined in an exchange descriptor 
 * exchange descriptor file. This covers:
 * <ul>
 * <li>POJOs for endpoint request and response
 * <li>JSON deserializers for those POJOs
 * <li>JSON serializers for those POJOs
 * </ul>
 * @see RestEndpointDescriptor
 */
public class RestEndpointClassesGenerator implements ClassesGenerator {
	
	private static String generateRestEndpointGetUrlParametersMethod(RestEndpointDescriptor restEndpointDescriptor) {
		String getUrlParametersBody = "return \"\";\n";
		if (restEndpointDescriptor.getUrlParameters() != null) {
			getUrlParametersBody =	ExchangeJavaWrapperGeneratorUtil.generateGetUrlParametersBodyFromTemplate(
												restEndpointDescriptor.getUrlParameters(), 
												restEndpointDescriptor.getParameters(), 
												restEndpointDescriptor.getUrlParametersListSeparator());
		} else if (restEndpointDescriptor.getParameters().size() > 0
					&& (restEndpointDescriptor.isQueryParams()	|| "GET".equalsIgnoreCase(restEndpointDescriptor.getHttpMethod()))) {
			getUrlParametersBody = generateGetUrlParametersBodyUsingQueryParams(restEndpointDescriptor.getParameters());
		}
		StringBuilder sb = new StringBuilder();
		sb.append("@Override\npublic String getUrlParameters() {\n")
		  .append(JavaCodeGenerationUtil.INDENTATION)
		  .append(getUrlParametersBody)
		  .append("}\n\n");
		return sb.toString();
	}
	
	private static String generateGetUrlParametersBodyUsingQueryParams(List<EndpointParameter> endpointParameters) {
		StringBuilder s = new StringBuilder().append("return " + EncodingUtil.class.getSimpleName() + ".createUrlQueryParameters(");
		for (int i = 0; i < endpointParameters.size(); i++) {
			if (i > 0) {
				s.append(",");
			}
			EndpointParameter param = endpointParameters.get(i);
			String name = param.getName();
			s.append("\"")
			 .append(name)
			 .append("\", ");
			if (param.getEndpointParameterType().getCanonicalType() == CanonicalEndpointParameterTypes.LIST) {
				s.append(EncodingUtil.class.getSimpleName())
				 .append(".listToUrlParamString(")
				 .append(name)
				 .append(")");
			} else {
				s.append(name);
			}
			
		}
		return s.append(");\n").toString();
	}

	protected final ExchangeDescriptor exchangeDescriptor;
	protected final ExchangeApiDescriptor apiDescriptor;
	protected final RestEndpointDescriptor restEndpointDescriptor;

	/**
	 * @param exchangeDescriptor Exchange descriptor where API with REST endpoint are defined
	 * @param apiDescriptor API of exchange descriptor defining the REST endpoint
	 * @param restEndpointDescriptor REST endpoint descriptor to generate related Java classes for.
	 */
	public RestEndpointClassesGenerator(ExchangeDescriptor exchangeDescriptor, 
										ExchangeApiDescriptor apiDescriptor, 
										RestEndpointDescriptor restEndpointDescriptor) {
		this.exchangeDescriptor = exchangeDescriptor;
		this.apiDescriptor = apiDescriptor;
		this.restEndpointDescriptor = restEndpointDescriptor;
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

	private void generatePojos(Path outputFolder) throws IOException {
		// Generate POJOs for request and response
		List<String> requestInterfaces = new ArrayList<>();
		requestInterfaces.add(RestEndpointUrlParameters.class.getName());
		if (restEndpointDescriptor.getRequestInterfaces() != null) {
			requestInterfaces.addAll(restEndpointDescriptor.getRequestInterfaces());
		}
		String additionalBody = null;
		additionalBody = generateRestEndpointGetUrlParametersMethod(restEndpointDescriptor);
		
		
		new EndpointPojoClassesGenerator(
				ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, apiDescriptor, restEndpointDescriptor), 
				"Request for " + exchangeDescriptor.getName() + " " + apiDescriptor.getName() + " API " 
						+ restEndpointDescriptor.getName() + " REST endpoint<br/>\n"
						+ restEndpointDescriptor.getDescription()
						+ "\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
						restEndpointDescriptor.getParameters(),
						requestInterfaces,
						additionalBody).generateClasses(outputFolder);
		
		
		if (restEndpointDescriptor.getResponse() != null) {
			new EndpointPojoClassesGenerator( 
					ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							restEndpointDescriptor), 
					"Response to " + exchangeDescriptor.getName() 
						+ " " + apiDescriptor.getName() + " API <br/>\n" 
						+ restEndpointDescriptor.getName() 
						+ " REST endpoint request<br/>\n"
						+ restEndpointDescriptor.getDescription()
						+ "\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
					restEndpointDescriptor.getResponse(),
					restEndpointDescriptor.getResponseInterfaces(),
					null).generateClasses(outputFolder);
		}
	}
	
	private void generateDeserializers(Path outputFolder) throws IOException {
		new JsonMessageDeserializerClassesGenerator( 
						ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(
								exchangeDescriptor, 
								apiDescriptor, 
								restEndpointDescriptor),
						restEndpointDescriptor.getResponse()).generateClasses(outputFolder);
	}
	
	private void generateSerializers(Path ouputFolder) throws IOException {
		new JsonPojoSerializerClassesGenerator( 
				ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(
						exchangeDescriptor, 
						apiDescriptor, 
						restEndpointDescriptor),
				restEndpointDescriptor.getParameters()).generateClasses(ouputFolder);
		
		new JsonPojoSerializerClassesGenerator(  
				ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(
						exchangeDescriptor, 
						apiDescriptor, 
						restEndpointDescriptor),
				restEndpointDescriptor.getResponse()).generateClasses(ouputFolder);
	}
}
