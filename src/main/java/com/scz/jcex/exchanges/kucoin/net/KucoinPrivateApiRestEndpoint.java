package com.scz.jcex.exchanges.kucoin.net;

import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;

import com.scz.jcex.netutils.deserialization.MessageDeserializer;
import com.scz.jcex.netutils.rest.RestRequest;
import com.scz.jcex.netutils.rest.javaxnet.JavaxNetRestEndpoint;
import com.scz.jcex.util.HmacSHA256Signer;

public class KucoinPrivateApiRestEndpoint<R, A> extends JavaxNetRestEndpoint<R, A> {
	
	private String apiKey;
	private String apiSecret;
	private String apiPassphrase;

	public KucoinPrivateApiRestEndpoint(MessageDeserializer<A> deserializer) {
		super(deserializer);
	}
	
	@Override
	protected void setHeadersForRequest(RestRequest<R> request, HttpsURLConnection connection, String body) {
		if (apiKey == null) {
			throw new IllegalStateException("Missing apiKey");
		}
		connection.setRequestProperty("KC-API-KEY ", apiKey);
		long timestamp = System.currentTimeMillis();
		String timestampStr = "" + timestamp;
		connection.setRequestProperty("KC-API-TIMESTAMP ", "" + timestampStr);
		String requestPath = request.getUrl();
		int requestPathOff = requestPath.indexOf("/api/");
		if (requestPathOff < 0)
			throw new IllegalArgumentException("Request URL:" + requestPath + " is missing expected '/api/' subpart");
		requestPath = requestPath.substring(requestPathOff);
		String strToSign = timestampStr + request.getHttpMethod().toUpperCase() + requestPath + body;
		String signature = HmacSHA256Signer.sign(strToSign, apiSecret);
		signature = Base64.getEncoder().encodeToString(signature.getBytes());
		connection.setRequestProperty("KC-API-SIGN", signature);
		connection.setRequestProperty("KC-API-PASSPHRASE", HmacSHA256Signer.sign(apiPassphrase, apiSecret));
		connection.setRequestProperty("KC-API-KEY-VERSION", "2");
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}
	
	public void setApiPassphrase(String apiPassPhrase) {
		this.apiPassphrase = apiPassPhrase;
	}

}
