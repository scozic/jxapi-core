package com.scz.jcex.netutils.websocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default {@link WebsocketMessageTopicMatcher} implementation. Is configured a list of fields and values.
 * Upon each call to  {@link #matches(String, String)}, checks if its status is still {@link WebsocketMessageTopicMatchStatus#NO_MATCH}:
 * <ul>
 *  <li>If it is not, parser is already in a terminal status which is returned.
 *  <li>If it is, checks if field corresponds to an expected one:
 *  <ul>
 *   <li>If not, parser remains in {@link WebsocketMessageTopicMatchStatus#NO_MATCH} status
 *   <li>If it does, expected value is checked against input:
 *   <ul>
 *    <li>If matches expected input, the field has expected value. That field is removed from expected fields, if there are no more expected fields, parser switches to {@link WebsocketMessageTopicMatchStatus#MATCHED} if
 *    <li>If does not, message carries a field with a matching field but not matching expected value, this message cannot be matched and parser switches to {@link WebsocketMessageTopicMatchStatus#CANT_MATCH}.   
 *   </ul>
 *  </ul>
 * </ul>
 */
public class DefaultWebsocketMessageTopicMatcher implements WebsocketMessageTopicMatcher {
	
	public static DefaultWebsocketMessageTopicMatcher create(String... fieldNamesAndValues) {
		return new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList(fieldNamesAndValues));
	}
	
	private final List<WebsocketMessageTopicMatcherField> fields;
	
	private Map<String, String> valuesToMatch = new HashMap<>();
	
	private WebsocketMessageTopicMatchStatus status = WebsocketMessageTopicMatchStatus.NO_MATCH;
	
	public DefaultWebsocketMessageTopicMatcher(List<WebsocketMessageTopicMatcherField> fields) {
		this.fields = fields;
		reset();
	}

	@Override
	public WebsocketMessageTopicMatchStatus matches(String fieldName, String value) {
		if (this.status != WebsocketMessageTopicMatchStatus.NO_MATCH) {
			// Other statuses are terminal statuses
			return this.status;
		}
		String v = valuesToMatch.get(fieldName);
		if (v == null) {
			return this.status;
		}
		if (!v.equals(value)) {
			this.status = WebsocketMessageTopicMatchStatus.CANT_MATCH;
		} else {
			valuesToMatch.remove(fieldName);
			if (valuesToMatch.isEmpty()) {
				this.status = WebsocketMessageTopicMatchStatus.MATCHED;
			}
		}
		
		return this.status;
	}

	@Override
	public void reset() {
		this.status = WebsocketMessageTopicMatchStatus.NO_MATCH;
		valuesToMatch.clear();
		fields.forEach(f -> valuesToMatch.put(f.getName(), f.getValue()));
	}

	@Override
	public WebsocketMessageTopicMatchStatus getStatus() {
		return this.status;
	}

}
