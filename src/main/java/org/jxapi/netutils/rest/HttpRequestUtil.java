package org.jxapi.netutils.rest;

/**
 * Helper methods around {@link HttpRequest} processing (for instance in {@link HttpRequestInterceptor} implementations.
 */
public class HttpRequestUtil {

	private HttpRequestUtil() {}
	
	/**
	 * Extract query parameters from full URL, that is the substring after first occurrence of '?' character in URL.
	 * For instance:
	 * <ul>
	 * <li><code>http://example.com</code> &rarr; <code>null</code></li>
	 * <li><code>http://example.com?</code> &rarr; <code>""</code></li>
	 * <li><code>http://example.com?name=foo&amp;age=12</code> &rarr; <code>"name=foo&amp;age=12"</code></li>
	 * </ul>
	 * @param url URL to extract query parameters from
	 * @return Query parameters of <code>url</code>, or <code>null</code> if '?' is not found in <code>url</code>
	 */
	public static String getUrlQueryParams(String url) {
		int off = url.indexOf('?');
		if (off < 0) {
			return null;
		}
		if (off >= url.length() - 1) {
			return "";
		}
		return url.substring(off + 1);
	}

}
