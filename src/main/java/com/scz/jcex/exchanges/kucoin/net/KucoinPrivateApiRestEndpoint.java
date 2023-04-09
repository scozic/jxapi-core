package com.scz.jcex.exchanges.kucoin.net;


import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import com.scz.jcex.netutils.deserialization.MessageDeserializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.netutils.rest.RestRequest;
import com.scz.jcex.netutils.rest.javaxnet.JavaxNetRestEndpoint;

public class KucoinPrivateApiRestEndpoint<R, A> extends JavaxNetRestEndpoint<R, A> {
	
	private String apiKey;
	private String apiPassphrase;
	private HmacUtils hmacUtils;

	public KucoinPrivateApiRestEndpoint(MessageDeserializer<A> deserializer) {
		super(deserializer);
	}
	
	@Override
	protected void setHeadersForRequest(RestRequest<R> request, HttpsURLConnection connection, String body) {
		if (apiKey == null) {
			throw new IllegalStateException("Missing apiKey");
		}
		connection.setRequestProperty("KC-API-KEY", apiKey);
		long timestamp = System.currentTimeMillis();
		String timestampStr = "" + timestamp;
		connection.setRequestProperty("KC-API-TIMESTAMP", timestampStr);
		String requestPath = request.getUrl();
		int requestPathOff = requestPath.indexOf("/api/");
		if (requestPathOff < 0)
			throw new IllegalArgumentException("Request URL:" + requestPath + " is missing expected '/api/' subpart");
		requestPath = requestPath.substring(requestPathOff);
		String urlParams = ((RestEndpointUrlParameters) request.getRequest()).getUrlParameters();
		if (urlParams != null && !urlParams.isEmpty()) {
			requestPath += urlParams;
		}
		String strToSign = timestampStr + request.getHttpMethod().toUpperCase() + requestPath + (body != null? body: "");
		String signature = digest(strToSign);
		
//		System.out.println(getClass().getName() + " Api passPhrase:" + apiPassphrase + ", encrypted:" + digest(apiPassphrase) + " signature:" + signature + " strToSign:" + strToSign);
		connection.setRequestProperty("KC-API-SIGN", signature);
		connection.setRequestProperty("KC-API-PASSPHRASE", digest(apiPassphrase));
		connection.setRequestProperty("KC-API-KEY-VERSION", "2");
		connection.setRequestProperty("User-Agent", "KuCoin-Java-SDK:2");
		connection.setRequestProperty("Content-Type", "application/json");
	}
	
	private String digest(String toDigest) {
		if (hmacUtils == null)
			throw new IllegalStateException("Api secret not set");
		return Base64.encodeBase64String(hmacUtils.hmac(toDigest));
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	public void setApiSecret(String apiSecret) {
		if (apiSecret != null) {
			this.hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, apiSecret);
		} else {
			this.hmacUtils = null;
		}
		
	}
	
	public void setApiPassphrase(String apiPassPhrase) {
		this.apiPassphrase = apiPassPhrase;
	}

}
