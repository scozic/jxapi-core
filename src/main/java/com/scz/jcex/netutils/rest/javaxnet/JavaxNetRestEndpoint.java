package com.scz.jcex.netutils.rest.javaxnet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.netutils.deserialization.MessageDeserializer;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.netutils.rest.RestRequest;
import com.scz.jcex.util.EncodingUtil;

public class JavaxNetRestEndpoint<R, A> implements RestEndpoint<R, A> {
	
	private static final Logger log = LoggerFactory.getLogger(JavaxNetRestEndpoint.class);
	private static final int BUFFER_SIZE = 1024 * 512;
	private final char[] buf = new char[BUFFER_SIZE];
	private final StringBuilder sb = new StringBuilder(BUFFER_SIZE);
	protected final MessageDeserializer<A> messageDeserializer;
	
	public JavaxNetRestEndpoint(MessageDeserializer<A> deserializer) {
		this.messageDeserializer = deserializer;
	}

	@Override
	public A call(RestRequest<R> request) throws IOException {
		URL url = getFullUrl(request);
		String body = getBody(request);
		if (log.isDebugEnabled())
			log.debug("Issuing REST request:" + request + ", full URL:[" + url + "], request body:" + body);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod(request.getHttpMethod());
		setHeadersForRequest(request, con, body);
		con.setUseCaches(false);
		con.setDoOutput(true);
		
		if (body != null && !"GET".equalsIgnoreCase(request.getHttpMethod())) {
			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.writeBytes(body.toString());
				wr.flush();
			}
		}
		int responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			sb.delete(0, sb.length());
			try (InputStreamReader reader = new InputStreamReader(con.getInputStream())) {
				for (int read = reader.read(buf); read >= 0; read = reader.read(buf)) {
					sb.append(buf, 0, read);
				}
			}
			String response = sb.toString();
			if (log.isDebugEnabled())
				log.debug("Got response to request:[" + request + "]:" + response);
			return deserialize(response);
		} else {
			throw new IOException("Got HTTP code:" + responseCode + " in response to request:" + request + ", " + con.getResponseMessage());
		}
		
	}
	
	protected A deserialize(String response) {
		return messageDeserializer.deserialize(response);
	}

	/**
	 * 2nd hook called in {@link #call(RestRequest)}, can be overridden if for
	 * instance, exchange API requires POST rest calls not to carry a body but URL
	 * parameters. Default implementation returns JSON String resulting of
	 * {@link RestRequest#getRequest()} encoding if request is a POST,
	 * <code>null</code> otherwise.
	 * 
	 * @param request
	 * @return
	 */
	protected String getBody(RestRequest<R> request) {
		if ("POST".equalsIgnoreCase(request.getHttpMethod())) {
			return EncodingUtil.pojoToJsonString(request.getRequest());
		}
		return null;
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
	 * @param connection
	 * @param body
	 */
	protected void setHeadersForRequest(RestRequest<R> request, HttpsURLConnection connection, String body) {
		// Nothing by default, can be overridden
	}

}
