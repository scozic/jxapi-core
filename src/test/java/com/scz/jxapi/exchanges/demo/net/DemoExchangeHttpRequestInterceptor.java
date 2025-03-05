package com.scz.jxapi.exchanges.demo.net;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.exchanges.demo.gen.DemoExchangeConstants;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeProperties;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.rest.HttpRequestInterceptor;

/**
 * {@link HttpRequestInterceptor} implementation for {@link DemoExchangeExchange}.<br>
 * Intercepted requests will have their url modified with host and port defined in configuration properties
 * @see DemoExchangeProperties#HOST
 * @see DemoExchangeProperties#HTTP_PORT
 */
public class DemoExchangeHttpRequestInterceptor implements HttpRequestInterceptor {
	
	private static final Logger log = LoggerFactory.getLogger(DemoExchangeHttpRequestInterceptor.class);
	
	private final String baseHttpUrl;

	/**
	 * Constructor
	 * @param properties Exchange configuration properties
	 */
	public DemoExchangeHttpRequestInterceptor(Properties properties) {
		this.baseHttpUrl = DemoExchangeProperties.getBaseHttpUrl(properties);
	}

	@Override
	public void intercept(HttpRequest request) {
		String url = request.getUrl();
		url = StringUtils.replace(url, DemoExchangeConstants.BASE_URL_PATTERN, baseHttpUrl);
		request.setUrl(url);
		log.debug("Intercepted request:{}", request);
	}

}
