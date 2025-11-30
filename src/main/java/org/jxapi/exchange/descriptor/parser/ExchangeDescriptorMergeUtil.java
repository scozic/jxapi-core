package org.jxapi.exchange.descriptor.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.DefaultConfigProperty;
import org.jxapi.util.MergeUtil;
import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;

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
    res.setHttpRequestExecutorFactory(MergeUtil.merge("httpRequestExecutorFactory of exchange " + exchangeName, e1.getHttpRequestExecutorFactory(), e2.getHttpRequestExecutorFactory()));
    res.setHttpRequestInterceptorFactory(MergeUtil.merge("httpRequestInterceptorFactory of exchange " + exchangeName, e1.getHttpRequestInterceptorFactory(), e2.getHttpRequestInterceptorFactory()));
    res.setHttpRequestTimeout(MergeUtil.mergePositiveLongs("httpRequestTimeout of exchange " + exchangeName, e1.getHttpRequestTimeout(), e2.getHttpRequestTimeout()));
    res.setWebsocketFactory(MergeUtil.merge("websocketFactory of exchange " + exchangeName, e1.getWebsocketFactory(), e2.getWebsocketFactory()));
    res.setWebsocketHookFactory(MergeUtil.merge("websocketHookFactory of exchange " + exchangeName, e1.getWebsocketHookFactory(), e2.getWebsocketHookFactory()));
    res.setWebsocketUrl(MergeUtil.merge("websocketUrl of exchange " + exchangeName, e1.getWebsocketUrl(), e2.getWebsocketUrl()));
    res.setHttpUrl(MergeUtil.merge("httpUrl of exchange " + exchangeName, e1.getHttpUrl(), e2.getHttpUrl()));
    res.setAfterInitHookFactory(MergeUtil.merge("afterInitHookFactory of exchange " + exchangeName, e1.getAfterInitHookFactory(), e2.getAfterInitHookFactory()));
    res.setConstants(mergeConstants(e1.getConstants(), e2.getConstants()));
    res.setRateLimits(MergeUtil.mergeLists("rateLimits of exchange " + exchangeName, e1.getRateLimits(), e2.getRateLimits(), RateLimitRule::getId));
    res.setProperties(MergeUtil.mergeLists("properties of exchange " + exchangeName, e1.getProperties(), e2.getProperties(), ConfigPropertyDescriptor::getName));
    res.setApis(mergeExchangeApiDescriptorLists(e1.getApis(), e2.getApis()));
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
  public static List<Constant> mergeConstants(List<Constant> l1, List<Constant> l2) {
    l1 = CollectionUtil.emptyIfNull(l1);
    l2 = new ArrayList<>(CollectionUtil.emptyIfNull(l2));
    List<Constant> res = new ArrayList<>();
    for (Constant c1 : l1) {
      String cName = c1.getName();
      for (Iterator<Constant> it = l2.iterator(); it.hasNext();) {
        Constant c2 = it.next();
        if (cName.equals((c2.getName()))) {
          if (c1.isGroup() && c2.isGroup()) {
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
   * Merges two lists of exchange API descriptors.<br>
   * If two APIs have the same name, the result will contain a single API
   * descriptor with the name and merged properties and endpoints of the two input
   * APIs.
   * 
   * @param l1 First list of API descriptors
   * @param l2 Second list of API descriptors
   * @return A new list of API descriptors, result of the merge of the two input
   *         lists
   * @see #mergeExchangeApiDescriptors(ExchangeApiDescriptor,
   *      ExchangeApiDescriptor)
   */
  public static List<ExchangeApiDescriptor> mergeExchangeApiDescriptorLists(List<ExchangeApiDescriptor> l1,
      List<ExchangeApiDescriptor> l2) {
    List<ExchangeApiDescriptor> res = new ArrayList<>();
    for (List<ExchangeApiDescriptor> l: List.of(Optional.ofNullable(l1).orElse(List.of()), 
                          Optional.ofNullable(l2).orElse(List.of()))) {
      for (ExchangeApiDescriptor api : l) {
        ExchangeApiDescriptor existing = res.stream().filter(a -> a.getName().equals(api.getName())).findFirst().orElse(null);
        if (existing != null) {
          res.set(res.indexOf(existing), mergeExchangeApiDescriptors(existing, api));
        } else {
          res.add(api);
        }
      }
    }
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
    res.setHttpRequestExecutorFactory(MergeUtil.merge("httpRequestExecutorFactory of API " + apiName, a1.getHttpRequestExecutorFactory(), a2.getHttpRequestExecutorFactory()));
    res.setHttpRequestInterceptorFactory(MergeUtil.merge("httpRequestInterceptorFactory of API " + apiName, a1.getHttpRequestInterceptorFactory(), a2.getHttpRequestInterceptorFactory()));
    res.setHttpRequestTimeout(MergeUtil.mergePositiveLongs("httpRequestTimeout of API " + apiName, a1.getHttpRequestTimeout(), a2.getHttpRequestTimeout()));
    res.setWebsocketFactory(MergeUtil.merge("websocketFactory of API " + apiName, a1.getWebsocketFactory(), a2.getWebsocketFactory()));
    res.setWebsocketHookFactory(MergeUtil.merge("websocketHookFactory of API " + apiName, a1.getWebsocketHookFactory(), a2.getWebsocketHookFactory()));
    res.setWebsocketUrl(MergeUtil.merge("websocketUrl of API " + apiName, a1.getWebsocketUrl(), a2.getWebsocketUrl()));
    res.setHttpUrl(MergeUtil.merge("httpUrl of API " + apiName, a1.getHttpUrl(), a2.getHttpUrl()));
    res.setRateLimits(MergeUtil.mergeLists("rateLimits of API " + apiName, a1.getRateLimits(), a2.getRateLimits(), RateLimitRule::getId));
    res.setRestEndpoints(MergeUtil.mergeLists("REST endpoints of API " + apiName, a1.getRestEndpoints(), a2.getRestEndpoints(), RestEndpointDescriptor::getName));
    return res;
  }

}
