package com.scz.jcex.binance.net;

import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.scz.jcex.netutils.deserialization.MessageDeserializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.netutils.rest.RestRequest;
import com.scz.jcex.netutils.rest.javaxnet.JavaxNetRestEndpoint;

public class BinancePrivateApiRestEndpoint<R, A> extends JavaxNetRestEndpoint<R, A> {
	
	private String apiKey;
	private String apiSecret;

	public BinancePrivateApiRestEndpoint(MessageDeserializer<A> deserializer) {
		super(deserializer);
	}
	
	@Override
	protected void setHeadersForRequest(RestRequest<R> request, HttpsURLConnection connection, String body) {
		if (apiKey == null) {
			throw new IllegalStateException("Missing apiKey");
		}
		connection.setRequestProperty("X-MBX-APIKEY", apiKey);
	}
	
	@Override
	protected URL getFullUrl(RestRequest<R> request) {
		try {
			String url = request.getUrl();
			String urlParams = ((RestEndpointUrlParameters) request.getRequest()).getUrlParameters();
			url += "?" + urlParams;
			if (apiSecret == null) {
				throw new IllegalStateException("Missing apiSecret");
			}
			url += "&signature=" + HmacSHA256Signer.sign(urlParams, apiSecret);
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid URL in request:" + request, e);
		}
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

}
