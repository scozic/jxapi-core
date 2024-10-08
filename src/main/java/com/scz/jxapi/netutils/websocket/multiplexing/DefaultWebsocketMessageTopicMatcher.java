package com.scz.jxapi.netutils.websocket.multiplexing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scz.jxapi.util.CollectionUtil;

/**
 * Default {@link WebsocketMessageTopicMatcher} implementation. Is configured a list of fields and values incoming messages must match to be considered as matched.
 * Upon each call to  {@link #matches(String, String)}, checks if its status is still {@link WebsocketMessageTopicMatchStatus#NO_MATCH}:
 * <ul>
 *  <li>If the list of matching fields configured is emtpty or <code>null</code>, the matcher will match any message field, and will be therefore always in {@link WebsocketMessageTopicMatchStatus#MATCHED} state.  
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
 * 
 * @see WebsocketMessageTopicMatcher
 * @see WebsocketMessageTopicMatchStatus
 */
public class DefaultWebsocketMessageTopicMatcher implements WebsocketMessageTopicMatcher {
	
	public static WebsocketMessageTopicMatcherFactory createFactory(String... fieldNamesAndValues) {
		List<WebsocketMessageTopicMatcherField> fieldList = WebsocketMessageTopicMatcherField.createList(fieldNamesAndValues);
		return () -> new DefaultWebsocketMessageTopicMatcher(fieldList);
	}
	
	private final List<WebsocketMessageTopicMatcherField> fields;
	
	private final Map<String, String> valuesToMatch;
	
	private WebsocketMessageTopicMatchStatus status = WebsocketMessageTopicMatchStatus.NO_MATCH;
	
	public DefaultWebsocketMessageTopicMatcher(List<WebsocketMessageTopicMatcherField> fields) {
		this.fields = fields;
		this.valuesToMatch = new HashMap<>(fields.size());
		if (CollectionUtil.isEmpty(fields)) {
			this.status = WebsocketMessageTopicMatchStatus.MATCHED;
		} else {
			this.status = WebsocketMessageTopicMatchStatus.NO_MATCH;
			valuesToMatch.clear();
			getFields().forEach(f -> valuesToMatch.put(f.getName(), f.getValue()));
		}
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
	public WebsocketMessageTopicMatchStatus getStatus() {
		return this.status;
	}

	public List<WebsocketMessageTopicMatcherField> getFields() {
		return fields;
	}

}
