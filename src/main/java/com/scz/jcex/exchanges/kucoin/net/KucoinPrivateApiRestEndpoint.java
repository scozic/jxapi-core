package com.scz.jcex.exchanges.kucoin.net;


import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;

import com.scz.jcex.netutils.deserialization.MessageDeserializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
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
//		long timestamp = 1679606913035L;
		String timestampStr = "" + timestamp;
		connection.setRequestProperty("KC-API-TIMESTAMP ", "" + timestampStr);
		String requestPath = request.getUrl();
		int requestPathOff = requestPath.indexOf("/api/");
		if (requestPathOff < 0)
			throw new IllegalArgumentException("Request URL:" + requestPath + " is missing expected '/api/' subpart");
		requestPath = requestPath.substring(requestPathOff);
		String urlParams = ((RestEndpointUrlParameters) request.getRequest()).getUrlParameters();
		if (urlParams != null && !urlParams.isEmpty()) {
			requestPath += "?" + urlParams;
		}
		String strToSign = timestampStr + request.getHttpMethod().toUpperCase() + requestPath + (body != null? body: "");
//		String signature = HmacSHA256Signer.base64Sign(strToSign, apiSecret);
		String signature = digest(strToSign);
		
//		connection.setRequestProperty("Content-TypeN", "application/json");
		System.out.println("Api passPhrase:" + apiPassphrase + ", encrypted:" + digest(apiPassphrase) + " signature:" + signature + " strToSign:" + strToSign);
		connection.setRequestProperty("KC-API-SIGN", signature);
		connection.setRequestProperty("KC-API-PASSPHRASE", digest(apiPassphrase));
		connection.setRequestProperty("KC-API-KEY-VERSION", "2");
		connection.setRequestProperty("User-Agent", "KuCoin-Java-SDK:2");
	}
	
	@SuppressWarnings("deprecation")
	private String digest(String toDigest) {
		return Base64.encodeBase64String(HmacUtils.hmacSha256(apiSecret, toDigest));
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
