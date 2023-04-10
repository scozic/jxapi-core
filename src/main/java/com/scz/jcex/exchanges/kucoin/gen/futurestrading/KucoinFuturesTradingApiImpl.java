package com.scz.jcex.exchanges.kucoin.gen.futurestrading;

import com.scz.jcex.exchanges.kucoin.gen.futurestrading.deserializers.KucoinGetAccountOverviewResponseDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewResponse;
import com.scz.jcex.exchanges.kucoin.net.KucoinPrivateApiRestEndpointFactory;
import com.scz.jcex.exchanges.kucoin.net.KucoinPrivateWebsocketEndpointFactory;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestRequest;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Actual implementation of {@link KucoinFuturesTradingApi}<br/>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class  KucoinFuturesTradingApiImpl implements KucoinFuturesTradingApi {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesTradingApiImpl.class);
  
  private final KucoinPrivateApiRestEndpointFactory restEndpointFactory = new KucoinPrivateApiRestEndpointFactory();
  
  private final KucoinPrivateWebsocketEndpointFactory websocketEndpointFactory = new KucoinPrivateWebsocketEndpointFactory();
  
  
  private final RestEndpoint<KucoinGetAccountOverviewRequest, KucoinGetAccountOverviewResponse> getAccountOverviewApi;
  
  @Override
  public KucoinGetAccountOverviewResponse getAccountOverview(KucoinGetAccountOverviewRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET GetAccountOverview > " + request);
    KucoinGetAccountOverviewResponse response = getAccountOverviewApi.call(RestRequest.create("https://api-futures.kucoin.com/api/v1/account-overview", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET GetAccountOverview < " + response);
    return response;
  }
  public KucoinFuturesTradingApiImpl(Properties properties) {
    this.restEndpointFactory.setProperties(properties);
    this.websocketEndpointFactory.setProperties(properties);
    this.getAccountOverviewApi = restEndpointFactory.createRestEndpoint(new KucoinGetAccountOverviewResponseDeserializer());
  }
}
