package com.scz.jxapi.exchanges.kucoin.net;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;
import com.scz.jxapi.netutils.rest.RestRequest;
import com.scz.jxapi.netutils.rest.okhttp.OkHttpRestEndpoint;

import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;

public class KucoinOkHttpPrivateApiRestEndpoint<R, A> extends OkHttpRestEndpoint<R, A> {
	
	private String apiKey;
	private String apiPassphrase;
	private HmacUtils hmacUtils;

	public KucoinOkHttpPrivateApiRestEndpoint(OkHttpClient okHttpClient, MessageDeserializer<A> deserializer) {
		super(okHttpClient, deserializer);
	}
	
	@Override
	protected void setHeadersForRequest(RestRequest<R> request, Builder builder, String body) {
		if (apiKey == null) {
			throw new IllegalStateException("Missing apiKey");
		}
		builder.addHeader("KC-API-KEY", apiKey);
		long timestamp = System.currentTimeMillis();
		String timestampStr = "" + timestamp;
		builder.addHeader("KC-API-TIMESTAMP", "" + timestampStr);
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
		String signature = digest(strToSign);
		
//		System.out.println("Api passPhrase:" + apiPassphrase + ", encrypted:" + digest(apiPassphrase) + " signature:" + signature + " strToSign:" + strToSign);
		builder.addHeader("KC-API-SIGN", signature);
		builder.addHeader("KC-API-PASSPHRASE", digest(apiPassphrase));
		builder.addHeader("KC-API-KEY-VERSION", "2");
		builder.addHeader("User-Agent", "KuCoin-Java-SDK:2");
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
