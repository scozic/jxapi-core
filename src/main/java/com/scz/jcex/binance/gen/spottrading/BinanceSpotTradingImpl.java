package com.scz.jcex.binance.gen.spottrading;

import com.scz.jcex.binance.gen.spottrading.deserializers.BinanceAccountResponseDeserializer;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceAccountRequest;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceAccountResponse;
import com.scz.jcex.binance.net.BinancePrivateApiRestEndpointFactory;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestRequest;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Actual implementation of {@link BinanceSpotTrading}<br/>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class  BinanceSpotTradingImpl implements BinanceSpotTrading {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotTradingImpl.class);
  
  private final BinancePrivateApiRestEndpointFactory restEndpointFactory = new BinancePrivateApiRestEndpointFactory();
  
  
  private final RestEndpoint<BinanceAccountRequest, BinanceAccountResponse> accountApi;
  
  @Override
  public BinanceAccountResponse account(BinanceAccountRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET account > " + request);
    BinanceAccountResponse response = accountApi.call(RestRequest.create("https://api.binance.com/api/v3/account", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET account < " + response);
    return response;
  }
  public BinanceSpotTradingImpl(Properties properties) {
    this.restEndpointFactory.setProperties(properties);
    this.accountApi = restEndpointFactory.createRestEndpoint(new BinanceAccountResponseDeserializer());
  }
}
