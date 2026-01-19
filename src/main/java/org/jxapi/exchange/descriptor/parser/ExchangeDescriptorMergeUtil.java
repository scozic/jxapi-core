package org.jxapi.exchange.descriptor.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ConstantDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.HttpClientDescriptor;
import org.jxapi.exchange.descriptor.gen.NetworkDescriptor;
import org.jxapi.exchange.descriptor.gen.RateLimitRuleDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketClientDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.DefaultConfigProperty;
import org.jxapi.util.MergeUtil;

/**
 * Helper methods around exchange descriptor merging.
 * @see ExchangeDescriptor
 * @see #mergeExchangeDescriptors(ExchangeDescriptor, ExchangeDescriptor)
 */
public class ExchangeDescriptorMergeUtil {
  
  private ExchangeDescriptorMergeUtil() {}
  
  /**
   * Merges two exchange descriptors.
   * <p>
   * Merging is used to load a descriptor split into multiple files. This allows
   * to make more readable descriptors, providing for instance one API group or
   * endpoint per file.<br>
   * Each file must describe whole or part of a single exchange, and must be a
   * valid desriptor in itself<br>
   * Example of exchange descriptor split into multiple files:
   * 
   * <pre>
   * exchanges/
   * ├── exchange1/
   * │   ├── exchange1.json
   * │   ├── constants.json
   * │   ├── api1.json
   * │   ├── api1RestEndpoint1.json
   * │   └── api1Restendpoint2.json
   * │   └── api1WebsocketEndpoint1.json 
   * ├── exchange2/
   * │   ├── exchange2.json
   * │   ├── api2.json
   * │   ├── api3.json
   * │   ├── api2Restendpoint1.json
   * │   └── api2RestEnddpoint2.json
   * │   └── api3WebsocketEndpoint1.json
   * │   └── api3WebsocketEndpoint2.json
   * </pre>
   * <ul>
   * <li>A subdirectory <i>exchange1</i> that contains all the files for exchange
   * 'exchange1'</li>
   * <li><i>exchange1.json</i> is general description of one exchange 'exchange1'
   * with with description, configuration properties but no API group</li>
   * <li><i>constants.json</i> contains a descriptor for same exchange
   * 'exchange1', but contains constants for the exchange</li>
   * <li><i>network.json</i> contains a descriptor for same exchange
   * 'exchange1', but contains only the network definition of the exchange</li>
   * <li><i>api1.json</i> contains a descriptor for same exchange 'exchange1' and
   * describes one API group 'api1' with common properties like
   * <i>websocketHookFactory</i>, no endpoints</li>
   * <li><i>api1RestEndpoint1.json</i> contains a descriptor for same exchange
   * 'exchange1' and describes one API group 'api1' with one Rest endpoint
   * 'restEndpoint2'</li>
   * <li><i>api1RestEndpoint2.json</i> contains is similar to
   * api1RestEndpoint1.json but describes one another Rest endpoint
   * 'restEndpoint2'</li>
   * <li><i>api1WebsocketEndpoint1.json</i> contains a descriptor for same
   * exchange 'exchange1' and describes one API group 'api1' with one websocket
   * endpoint 'websocketEndpoint1'</li>
   * <li>A subdirectory <i>exchange2</i> that contains all the files for exchange
   * 'exchange2' that is structured the same way description + configuration
   * properties and two API groups with one REST endpoint each defined in their
   * own files</li>
   * </ul>
   * The result of merging 'exchange1' descriptors is a single exchange descriptor
   * containing all the information: exchange 'exchange1' with description, config
   * properties, constants, API group 'api1' with 3 endpoints 'restEndpoint1',
   * 'restEndpoint2' and 'websocketEndpoint1'.
   * <p>
   * The general rules for merging two descriptors rule are:
   * <ul>
   * <li>If a property for a given exchange, API group or endpoint has value in
   * only one of the descriptors, the value is copied to the result
   * descriptor.</li>
   * <li>If a primitive value property for a given exchange, API group or endpoint
   * has different (non <code>null</code>) value in descriptors, an exception is
   * thrown.</li>
   * <li>Values of a 'list' properties (api groups, constants...) for a given
   * exchange, API group or endpoint present in both descriptors are merged.</li>
   * <li>When merging lists, if an item with the same identifier (name) is present
   * in both lists, an exception is thrown: You cannot define two constants or
   * REST endpoints... with same name</li>
   * <li>Endpoints cannot be split between files: One REST or Websocket endpoint
   * must be defined fully in the same file.</li>
   * </ul>
   * 
   * @param e1 An exchange descriptor
   * @param e2 Another exchange descriptor
   * @return A new exchange descriptor, result of the merge of the two input
   *         descriptors.
   * @throws IllegalArgumentException if a primitive value property is present in
   *                                  both descriptors, if exchanges do not carry
   *                                  the same exchange ID, or if an item with the
   *                                  same identifier (name) is present in both
   *                                  lists of constants, rate limits, properties,
   *                                  API groups or endpoints.
   * @see ExchangeDescriptor
   * @see ExchangeApiDescriptor
   * @see Constant
   * @see RateLimitRule
   * @see DefaultConfigProperty
   * @see RestEndpointDescriptor
   * @see WebsocketEndpointDescriptor
   */
  public static ExchangeDescriptor mergeExchangeDescriptors(ExchangeDescriptor e1, ExchangeDescriptor e2) {
    ExchangeDescriptor res = new ExchangeDescriptor();
    String exchangeName = e1.getId();
    if (!e1.getId().equals(e2.getId())) {
      throw new IllegalArgumentException(String.format("Cannot merge exchanges with different IDs:'%s' and '%s'", e1.getId(), e2.getId()));
    }
    res.setId(e1.getId());
    res.setVersion(MergeUtil.merge("version of exchange " + exchangeName, e1.getVersion(), e2.getVersion()));
    res.setJxapi(MergeUtil.merge("JXAPI version of exchange " + exchangeName, e1.getJxapi(), e2.getJxapi()));
    res.setDescription(MergeUtil.merge("description of exchange " + exchangeName, e1.getDescription(), e2.getDescription()));
    res.setDocUrl(MergeUtil.merge("docUrl of exchange " + exchangeName, e1.getDocUrl(), e2.getDocUrl()));
    res.setBasePackage(MergeUtil.merge("basePackage of exchange " + exchangeName, e1.getBasePackage(), e2.getBasePackage()));
    res.setHttpUrl(MergeUtil.merge("httpUrl of exchange " + exchangeName, e1.getHttpUrl(), e2.getHttpUrl()));
    res.setAfterInitHookFactory(MergeUtil.merge("afterInitHookFactory of exchange " + exchangeName, e1.getAfterInitHookFactory(), e2.getAfterInitHookFactory()));
    res.setConstants(mergeConstants(e1.getConstants(), e2.getConstants()));
    res.setNetwork(mergeNetworks(e1.getNetwork(), e2.getNetwork()));
    res.setRateLimits(MergeUtil.mergeLists("rateLimits of exchange " + exchangeName, e1.getRateLimits(), e2.getRateLimits(), RateLimitRuleDescriptor::getId));
    res.setProperties(MergeUtil.mergeLists("properties of exchange " + exchangeName, e1.getProperties(), e2.getProperties(), ConfigPropertyDescriptor::getName));
    res.setApis(MergeUtil.mergeLists("APIs of exchange " + exchangeName, e1.getApis(), e2.getApis(), ExchangeApiDescriptor::getName, ExchangeDescriptorMergeUtil::mergeExchangeApiDescriptors));
    return res;
  }
  
  /**
   * Merges two lists of constants.<br>
   * If two constants have the same name, the result will contain a single
   * constant with the name and merged properties of the two input constants.
   * 
   * @param l1 First list of constants
   * @param l2 Second list of constants
   * @return A new list of constants, result of the merge of the two input lists
   */
  public static List<ConstantDescriptor> mergeConstants(List<ConstantDescriptor> l1, List<ConstantDescriptor> l2) {
    l1 = CollectionUtil.emptyIfNull(l1);
    l2 = new ArrayList<>(CollectionUtil.emptyIfNull(l2));
    List<ConstantDescriptor> res = new ArrayList<>();
    for (ConstantDescriptor c1 : l1) {
      String cName = c1.getName();
      for (Iterator<ConstantDescriptor> it = l2.iterator(); it.hasNext();) {
        ConstantDescriptor c2 = it.next();
        if (cName.equals((c2.getName()))) {
          if (!CollectionUtil.isEmpty(c1.getConstants()) 
              && !CollectionUtil.isEmpty(c2.getConstants())) {
            c1.setDescription(MergeUtil.merge("description of constant group " + cName, c1.getDescription(), c2.getDescription()));
            c1.setConstants(mergeConstants(c1.getConstants(), c2.getConstants()));
          } else {
            MergeUtil.merge("constant " + cName, c1, c2);
          }
          it.remove();
        }
      }
      res.add(c1);
    }
    res.addAll(l2);
    return res;
  }
  
  /**
   * Merges two network descriptors.
   * 
   * @param n1 First network descriptor
   * @param n2 Second network descriptor
   * @return A new network descriptor, result of the merge of the two input
   *         descriptors
   */
  public static NetworkDescriptor mergeNetworks(NetworkDescriptor n1, NetworkDescriptor n2) {
    if (n1 == null) return n2;
    if (n2 == null) return n1;
    NetworkDescriptor res = new NetworkDescriptor();
    res.setHttpClients(MergeUtil.mergeLists("httpClients of network", n1.getHttpClients(), n2.getHttpClients(), HttpClientDescriptor::getName));
    res.setWebsocketClients(MergeUtil.mergeLists("websocketClients of network", n1.getWebsocketClients(), n2.getWebsocketClients(), WebsocketClientDescriptor::getName));    
    return res;
  }
  
  /**
   * Merges two exchange API descriptors. The result will contain a single API
   * descriptor with the name and merged properties and endpoints of the two input
   * APIs.
   * 
   * @param a1 First API descriptor
   * @param a2 Second API descriptor
   * @return A new API descriptor, result of the merge of the two input API
   * 
   * @throws IllegalArgumentException If API descriptors do not have equal names.
   */
  public static ExchangeApiDescriptor mergeExchangeApiDescriptors(ExchangeApiDescriptor a1, ExchangeApiDescriptor a2) {
    ExchangeApiDescriptor res = new ExchangeApiDescriptor();
    String apiName = a1.getName();
    if (!Objects.equals(apiName, a2.getName())) {
      throw new IllegalArgumentException(String.format("Cannot merge API groups with different names:'%s' and '%s'", a1.getName(), a2.getName()));
    }
    res.setName(apiName);
    res.setDescription(MergeUtil.merge("description of API " + apiName, a1.getDescription(), a2.getDescription()));
    res.setRestEndpoints(MergeUtil.mergeLists("restEndpoints of API " + apiName, a1.getRestEndpoints(), a2.getRestEndpoints(), RestEndpointDescriptor::getName));
    res.setWebsocketEndpoints(MergeUtil.mergeLists("websocketEndpoints of API " + apiName, a1.getWebsocketEndpoints(), a2.getWebsocketEndpoints(), WebsocketEndpointDescriptor::getName));
    res.setHttpUrl(MergeUtil.merge("httpUrl of API " + apiName, a1.getHttpUrl(), a2.getHttpUrl()));
    res.setDefaultWebsocketClient(MergeUtil.merge("defaultWebsocketClient of API " + apiName, a1.getDefaultWebsocketClient(), a2.getDefaultWebsocketClient()));
    res.setDefaultHttpClient(MergeUtil.merge("defaultHttpClient of API " + apiName, a1.getDefaultHttpClient(), a2.getDefaultHttpClient()));
    res.setRestEndpoints(MergeUtil.mergeLists("REST endpoints of API " + apiName, a1.getRestEndpoints(), a2.getRestEndpoints(), RestEndpointDescriptor::getName, ExchangeDescriptorMergeUtil::mergeRestEndpointDescriptors));
    return res;
  }

  /**
   * Merges two REST endpoint descriptors. The result will contain a single REST
   * endpoint descriptor with the name and merged properties of the two input
   * endpoints.
   * 
   * @param r1 First REST endpoint descriptor
   * @param r2 Second REST endpoint descriptor
   * @return A new REST endpoint descriptor, result of the merge of the two input
   *         REST endpoint descriptors
   * @throws IllegalArgumentException If endpoints do not have equal names.        
   */
  public static RestEndpointDescriptor mergeRestEndpointDescriptors(
		  RestEndpointDescriptor r1,
		  RestEndpointDescriptor r2) {
    if (r1 == null) return r2;
    if (r2 == null) return r1;
  	RestEndpointDescriptor res = new RestEndpointDescriptor();
  	String endpointName = r1.getName();
  	if (!Objects.equals(endpointName, r2.getName())) {
  	  throw new IllegalArgumentException(String.format("Cannot merge REST endpoints with different names:'%s' and '%s'", r1.getName(), r2.getName()));
  	}
  	res.setName(endpointName);
  	res.setDescription(MergeUtil.merge("description of REST endpoint " + endpointName, r1.getDescription(), r2.getDescription()));
  	res.setHttpMethod(MergeUtil.merge("httpMethod of REST endpoint " + endpointName, r1.getHttpMethod(), r2.getHttpMethod()));
  	res.setDocUrl(MergeUtil.merge("docUrl of REST endpoint " + endpointName, r1.getDocUrl(), r2.getDocUrl()));
  	res.setHttpClient(MergeUtil.merge("httpClient of REST endpoint " + endpointName, r1.getHttpClient(), r2.getHttpClient()));
  	res.setPaginated(MergeUtil.merge("paginated of REST endpoint " + endpointName, r1.isPaginated(), r2.isPaginated()));
  	res.setRateLimits(MergeUtil.mergeLists("rateLimit of REST endpoint " + endpointName, r1.getRateLimits(), r2.getRateLimits(), Function.identity()));
  	res.setRequest(mergeFields(r1.getRequest(), r2.getRequest()));
  	res.setResponse(mergeFields(r1.getResponse(), r2.getResponse()));
  	res.setUrl(MergeUtil.merge("url of REST endpoint " + endpointName, r1.getUrl(), r2.getUrl()));
  	res.setRequestHasBody(MergeUtil.merge("requestHasBody of REST endpoint " + endpointName, r1.isRequestHasBody(), r2.isRequestHasBody()));
  	res.setRequestWeight(MergeUtil.merge("requestWeight of REST endpoint " + endpointName, r1.getRequestWeight(), r2.getRequestWeight()));
  	return res;
  }

  /**
   * Merges two field descriptors. The result will contain a single field
   * descriptor with the name and merged properties of the two input fields.
   * 
   * @param f1 First field descriptor
   * @param f2 Second field descriptor
   * @return A new field descriptor, result of the merge of the two input field
   *         descriptors
   * @throws IllegalArgumentException if fields do not have equal names.        
   */
  public static Field mergeFields(Field f1, Field f2) {
    if (f1 == null) return f2;
    if (f2 == null) return f1;
  	Field res = new Field();
  	String fieldName = f1.getName();
  	if (!Objects.equals(fieldName, f2.getName())) {
  	  throw new IllegalArgumentException(String.format("Cannot merge fields with different names:'%s' and '%s'", f1.getName(), f2.getName()));
  	}
  	res.setName(fieldName);
  	res.setType(MergeUtil.merge("type of field " + fieldName, f1.getType(), f2.getType()));
  	res.setDescription(MergeUtil.merge("description of field " + fieldName, f1.getDescription(), f2.getDescription()));
  	res.setMsgField(MergeUtil.merge("msgField of field " + fieldName, f1.getMsgField(), f2.getMsgField()));
  	res.setObjectDescription(MergeUtil.merge("objectDescription of field " + fieldName, f1.getObjectDescription(), f2.getObjectDescription()));
  	res.setObjectName(MergeUtil.merge("objectName of field " + fieldName, f1.getObjectName(), f2.getObjectName()));
  	res.setIn(MergeUtil.merge("in of field " + fieldName, f1.getIn(), f2.getIn()));
  	res.setImplementedInterfaces(MergeUtil.mergeLists("implementedInterfaces of field " + fieldName, f1.getImplementedInterfaces(), f2.getImplementedInterfaces(), Function.identity()));
  	res.setDefaultValue(MergeUtil.merge("defaultValue of field " + fieldName, f1.getDefaultValue(), f2.getDefaultValue()));
  	res.setSampleValue(MergeUtil.merge("sampleValue of field " + fieldName, f1.getSampleValue(), f2.getSampleValue()));
  	res.setProperties(MergeUtil.mergeLists("properties of field " + fieldName, f1.getProperties(), f2.getProperties(), Field::getName, ExchangeDescriptorMergeUtil::mergeFields));
  	return res;
    }
  
  /**
   * Merges two Websocket endpoint descriptors. The result will contain a single
   * Websocket endpoint descriptor with the name and merged properties of the two
   * input endpoints.
   * 
   * @param w1 First Websocket endpoint descriptor
   * @param w2 Second Websocket endpoint descriptor
   * @return A new Websocket endpoint descriptor, result of the merge of the two
   *         input Websocket endpoint descriptors
   * @throws IllegalArgumentException If endpoints do not have equal names.        
   */
  public static WebsocketEndpointDescriptor mergeWebsocketEndpointDescriptors(
		  WebsocketEndpointDescriptor w1,
		  WebsocketEndpointDescriptor w2) {
    if (w1 == null) return w2;
    if (w2 == null) return w1;
  	WebsocketEndpointDescriptor res = new WebsocketEndpointDescriptor();
  	String endpointName = w1.getName();
  	if (!Objects.equals(endpointName, w2.getName())) {
  	  throw new IllegalArgumentException(String.format("Cannot merge Websocket endpoints with different names:'%s' and '%s'", w1.getName(), w2.getName()));
  	}
  	res.setName(endpointName);
  	res.setDescription(MergeUtil.merge("description of Websocket endpoint " + endpointName, w1.getDescription(), w2.getDescription()));
  	res.setDocUrl(MergeUtil.merge("docUrl of Websocket endpoint " + endpointName, w1.getDocUrl(), w2.getDocUrl()));
  	res.setWebsocketClient(MergeUtil.merge("websocketClient of Websocket endpoint " + endpointName, w1.getWebsocketClient(), w2.getWebsocketClient()));
  	res.setTopic(MergeUtil.merge("topic of Websocket endpoint " + endpointName, w1.getTopic(), w2.getTopic()));
  	res.setTopicMatcher(MergeUtil.merge("topicMatcher of Websocket endpoint " + endpointName, w1.getTopicMatcher(), w2.getTopicMatcher()));
  	res.setRequest(mergeFields(w1.getRequest(), w2.getRequest()));
  	res.setMessage(mergeFields(w1.getMessage(), w2.getMessage()));
  	return res;
  }

}
