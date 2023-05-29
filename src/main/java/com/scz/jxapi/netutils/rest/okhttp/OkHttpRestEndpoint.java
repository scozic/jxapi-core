package com.scz.jxapi.netutils.rest.okhttp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.rest.RestEndpoint;
import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;
import com.scz.jxapi.netutils.rest.RestRequest;
import com.scz.jxapi.util.EncodingUtil;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request.Builder;

public class OkHttpRestEndpoint<R, A> implements RestEndpoint<R, A> {
	
	private static final Logger log = LoggerFactory.getLogger(OkHttpRestEndpoint.class);
	
	private static final MediaType JSON_TYPE = MediaType.parse("application/json");

	protected final OkHttpClient client;

	protected final MessageDeserializer<A> deserializer;

	public OkHttpRestEndpoint(OkHttpClient client, MessageDeserializer<A> deserializer) {
		this.client = client;
		this.deserializer = deserializer;
	}

	@Override
	public A call(RestRequest<R> request) throws IOException {
		Builder builder = new Request.Builder().url(getFullUrl(request));
		String body = null;
		if (!"GET".equalsIgnoreCase(request.getHttpMethod())) {
			body = getBody(request);
			builder = builder.method(request.getHttpMethod(), RequestBody.create(JSON_TYPE, body));
		} else {
			builder = builder.get();
		}
		setHeadersForRequest(request, builder, body);
		if (log.isDebugEnabled())
			log.debug("Sending request:" + request);
		Response response = client.newCall(builder.build()).execute();
		if (log.isDebugEnabled())
			log.debug("Received response:" + response);
		A answer = null;
		if (response != null && response.body() != null) {
		   String msg = new String(response.body().bytes());
		   try {
			   if (log.isDebugEnabled())
				   log.debug("Response body:" + msg);
			   answer = deserializer.deserialize(msg);
		   } finally {
			   response.close();
		   }
        }
		return answer;
	}
	
	/**
	 * First hook called in {@link #call(RestRequest)}, can be overridden for instance when this rest enpoint API specifies that URL must be provided a signature parameter.
	 * Default implementation 
	 * @param request
	 * @return the full URL, including base url, endpoint suffix and URL parameters for given request
	 */
	protected URL getFullUrl(RestRequest<R> request) {
		try {
			String url = request.getUrl();
			if (request.getRequest() instanceof RestEndpointUrlParameters) {
				String urlParams = ((RestEndpointUrlParameters) request.getRequest()).getUrlParameters();
				if (urlParams != null && !urlParams.isEmpty()) {
					url += urlParams;
				}
			}
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid URL in request:" + request, e);
		}
	}
	
	/**
	 * Second hook called in {@link #call(RestRequest)}, can be overridden for instance when this rest enpoint
	 * @param request
	 * @return
	 */
	protected String getBody(RestRequest<R> request) {
		return EncodingUtil.pojoToJsonString(request.getRequest());
	}
	
	/**
	 * Last hook method called before request is actually sent. Implementation
	 * specific calls to
	 * {@link HttpsURLConnection#setRequestProperty(String, String)} or other tuning
	 * of connection can be performed here.
	 * <pre>
	 *  connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; JAVA AWT)");
	 *	connnection.setRequestProperty("Sign", hmacSignature);
	 *	connnetion.setRequestProperty("Key", apiKey);
	 * </pre>
	 * 
	 * @param request
	 * @param builder
	 * @param body
	 */
	protected void setHeadersForRequest(RestRequest<R> request, Builder builder, String body) {
		// Nothing by default, can be overridden
	}

}
