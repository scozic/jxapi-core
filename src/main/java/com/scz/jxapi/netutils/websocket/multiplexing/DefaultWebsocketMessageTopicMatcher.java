package com.scz.jxapi.netutils.websocket.multiplexing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scz.jxapi.util.CollectionUtil;

/**
 * Default {@link WebsocketMessageTopicMatcher} implementation. Is configured a list of fields and values incoming messages must match to be considered as matched.
 * 
 * @see WebsocketMessageTopicMatcher
 * @see WebsocketMessageTopicMatchStatus
 */
public class DefaultWebsocketMessageTopicMatcher implements WebsocketMessageTopicMatcher {
	
	private final Map<String, ValueToMatch> valuesToMatch;
	
	private int valuesToMatchCount = 0;
	
	
	private WebsocketMessageTopicMatchStatus status = WebsocketMessageTopicMatchStatus.NO_MATCH;
	
	public DefaultWebsocketMessageTopicMatcher(List<WebsocketMessageTopicMatcherField> fields) {
		this.valuesToMatch = new HashMap<>(fields.size());
		this.valuesToMatchCount = fields.size();
		if (CollectionUtil.isEmpty(fields)) {
			this.status = WebsocketMessageTopicMatchStatus.MATCHED;
		} else {
			this.status = WebsocketMessageTopicMatchStatus.NO_MATCH;
			fields.forEach(f -> valuesToMatch.put(f.getName(), new ValueToMatch(f.getValue())));
		}
	}

	@Override
	public WebsocketMessageTopicMatchStatus matches(String fieldName, String value) {
		if (this.status != WebsocketMessageTopicMatchStatus.NO_MATCH) {
			// Other statuses are terminal statuses
			return this.status;
		}
		ValueToMatch v = valuesToMatch.get(fieldName);
		if (v == null) {
			return this.status;
		}
		if (!v.value.equals(value)) {
			this.status = WebsocketMessageTopicMatchStatus.CANT_MATCH;
		} else if (!v.matched) {
			v.matched = true;
			valuesToMatchCount--;
			if (valuesToMatchCount <= 0) {
				this.status = WebsocketMessageTopicMatchStatus.MATCHED;
			}
		}
		
		return this.status;
	}

	@Override
	public WebsocketMessageTopicMatchStatus getStatus() {
		return this.status;
	}

	@Override
	public void reset() {
		if (!valuesToMatch.isEmpty()) {
			this.valuesToMatchCount = valuesToMatch.size();
			this.status = WebsocketMessageTopicMatchStatus.NO_MATCH;
			this.valuesToMatch.values().forEach(v -> v.matched = false);
		}
	}
	
	private static class ValueToMatch {
		final String value;
		boolean matched = false;
		
		public ValueToMatch(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
					.append(getClass().getSimpleName())
					.append(valuesToMatch).toString();
	}

}
