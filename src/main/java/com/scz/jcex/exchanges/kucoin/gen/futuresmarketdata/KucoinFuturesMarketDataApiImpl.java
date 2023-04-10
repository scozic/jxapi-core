package com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata;

import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.deserializers.KucoinGetOpenContractListResponseDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListRequest;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListResponse;
import com.scz.jcex.exchanges.kucoin.net.KucoinPublicApiRestEndpointFactory;
import com.scz.jcex.exchanges.kucoin.net.KucoinPublicWebsocketEndpointFactory;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestRequest;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Actual implementation of {@link KucoinFuturesMarketDataApi}<br/>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class  KucoinFuturesMarketDataApiImpl implements KucoinFuturesMarketDataApi {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesMarketDataApiImpl.class);
  
  private final KucoinPublicApiRestEndpointFactory restEndpointFactory = new KucoinPublicApiRestEndpointFactory();
  
  private final KucoinPublicWebsocketEndpointFactory websocketEndpointFactory = new KucoinPublicWebsocketEndpointFactory();
  
  
  private final RestEndpoint<KucoinGetOpenContractListRequest, KucoinGetOpenContractListResponse> getOpenContractListApi;
  
  @Override
  public KucoinGetOpenContractListResponse getOpenContractList(KucoinGetOpenContractListRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET GetOpenContractList > " + request);
    KucoinGetOpenContractListResponse response = getOpenContractListApi.call(RestRequest.create("https://api-futures.kucoin.com/api/v1/contracts/active", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET GetOpenContractList < " + response);
    return response;
  }
  public KucoinFuturesMarketDataApiImpl(Properties properties) {
    this.restEndpointFactory.setProperties(properties);
    this.websocketEndpointFactory.setProperties(properties);
    this.getOpenContractListApi = restEndpointFactory.createRestEndpoint(new KucoinGetOpenContractListResponseDeserializer());
  }
}
