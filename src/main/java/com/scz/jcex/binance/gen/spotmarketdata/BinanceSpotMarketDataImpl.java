package com.scz.jcex.binance.gen.spotmarketdata;

import com.scz.jcex.binance.gen.spotmarketdata.deserializers.BinanceExchangeInformationAllResponseDeserializer;
import com.scz.jcex.binance.gen.spotmarketdata.deserializers.BinanceExchangeInformationResponseDeserializer;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponse;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponse;
import com.scz.jcex.binance.net.BinancePublicApiRestEndpointFactory;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestRequest;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Actual implementation of {@link BinanceSpotMarketData}<br/>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class  BinanceSpotMarketDataImpl implements BinanceSpotMarketData {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotMarketDataImpl.class);
  
  private final BinancePublicApiRestEndpointFactory restEndpointFactory = new BinancePublicApiRestEndpointFactory();
  
  
  private final RestEndpoint<BinanceExchangeInformationAllRequest, BinanceExchangeInformationAllResponse> exchangeInformationAllApi;
  
  @Override
  public BinanceExchangeInformationAllResponse exchangeInformationAll(BinanceExchangeInformationAllRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET exchangeInformationAll > " + request);
    BinanceExchangeInformationAllResponse response = exchangeInformationAllApi.call(RestRequest.create("https://api.binance.com/api/v3/exchangeInfo", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET exchangeInformationAll < " + response);
    return response;
    
  }
  
  private final RestEndpoint<BinanceExchangeInformationRequest, BinanceExchangeInformationResponse> exchangeInformationApi;
  
  @Override
  public BinanceExchangeInformationResponse exchangeInformation(BinanceExchangeInformationRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET exchangeInformation > " + request);
    BinanceExchangeInformationResponse response = exchangeInformationApi.call(RestRequest.create("https://api.binance.com/api/v3/exchangeInfo", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET exchangeInformation < " + response);
    return response;
    
  }
  public BinanceSpotMarketDataImpl(Properties properties) {
    this.restEndpointFactory.setProperties(properties);
    this.exchangeInformationAllApi = restEndpointFactory.createRestEndpoint(new BinanceExchangeInformationAllResponseDeserializer());
    this.exchangeInformationApi = restEndpointFactory.createRestEndpoint(new BinanceExchangeInformationResponseDeserializer());
    
  }
  
}
