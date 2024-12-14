package com.scz.jxapi.exchanges.demo.net;

import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.exchanges.demo.gen.DemoExchangeConstants;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeProperties;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.rest.HttpRequestInterceptor;

public class DemoExchangeHttpRequestInterceptor implements HttpRequestInterceptor {
	
	private static final Logger log = LoggerFactory.getLogger(DemoExchangeHttpRequestInterceptor.class);
	
	private final String host;
	private final String port;

	public DemoExchangeHttpRequestInterceptor(Properties properties) {
		this.host = DemoExchangeProperties.getHost(properties);
		this.port = Optional.ofNullable(DemoExchangeProperties.getHttpPort(properties)).orElse(Integer.valueOf(8080)).toString();
	}

	@Override
	public void intercept(HttpRequest request) {
		String url = request.getUrl();
		url = StringUtils.replace(url, DemoExchangeConstants.HTTP_SERVER_HOST_WILDCARD, host);
		url = StringUtils.replace(url, DemoExchangeConstants.HTTP_SERVER_PORT_WILDCARD, port);
		request.setUrl(url);
		log.debug("Intercepted request:{}", request);
	}

}
