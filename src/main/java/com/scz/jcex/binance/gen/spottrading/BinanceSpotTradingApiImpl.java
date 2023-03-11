package com.scz.jcex.binance.gen.spottrading;

import com.scz.jcex.binance.gen.spottrading.deserializers.BinanceAccountResponseDeserializer;
import com.scz.jcex.binance.gen.spottrading.deserializers.BinanceSpotDeleteListenKeyResponseDeserializer;
import com.scz.jcex.binance.gen.spottrading.deserializers.BinanceSpotKeepAliveListenKeyResponseDeserializer;
import com.scz.jcex.binance.gen.spottrading.deserializers.BinanceSpotListenKeyResponseDeserializer;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceAccountRequest;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceAccountResponse;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyRequest;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyResponse;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyRequest;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyResponse;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceSpotListenKeyRequest;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceSpotListenKeyResponse;
import com.scz.jcex.binance.net.BinancePrivateApiRestEndpointFactory;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestRequest;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Actual implementation of {@link BinanceSpotTradingApi}<br/>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class  BinanceSpotTradingApiImpl implements BinanceSpotTradingApi {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotTradingApiImpl.class);
  
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
  
  private final RestEndpoint<BinanceSpotListenKeyRequest, BinanceSpotListenKeyResponse> spotListenKeyApi;
  
  @Override
  public BinanceSpotListenKeyResponse spotListenKey(BinanceSpotListenKeyRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("POST spotListenKey > " + request);
    BinanceSpotListenKeyResponse response = spotListenKeyApi.call(RestRequest.create("https://api.binance.com/api/v3/userDataStream", "POST", request));
    if (log.isDebugEnabled())
      log.debug("POST spotListenKey < " + response);
    return response;
  }
  
  private final RestEndpoint<BinanceSpotKeepAliveListenKeyRequest, BinanceSpotKeepAliveListenKeyResponse> spotKeepAliveListenKeyApi;
  
  @Override
  public BinanceSpotKeepAliveListenKeyResponse spotKeepAliveListenKey(BinanceSpotKeepAliveListenKeyRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("PUT spotKeepAliveListenKey > " + request);
    BinanceSpotKeepAliveListenKeyResponse response = spotKeepAliveListenKeyApi.call(RestRequest.create("https://api.binance.com/api/v3/userDataStream", "PUT", request));
    if (log.isDebugEnabled())
      log.debug("PUT spotKeepAliveListenKey < " + response);
    return response;
  }
  
  private final RestEndpoint<BinanceSpotDeleteListenKeyRequest, BinanceSpotDeleteListenKeyResponse> spotDeleteListenKeyApi;
  
  @Override
  public BinanceSpotDeleteListenKeyResponse spotDeleteListenKey(BinanceSpotDeleteListenKeyRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("DELETE spotDeleteListenKey > " + request);
    BinanceSpotDeleteListenKeyResponse response = spotDeleteListenKeyApi.call(RestRequest.create("https://api.binance.com/api/v3/userDataStream", "DELETE", request));
    if (log.isDebugEnabled())
      log.debug("DELETE spotDeleteListenKey < " + response);
    return response;
  }
  public BinanceSpotTradingApiImpl(Properties properties) {
    this.restEndpointFactory.setProperties(properties);
    this.accountApi = restEndpointFactory.createRestEndpoint(new BinanceAccountResponseDeserializer());
    this.spotListenKeyApi = restEndpointFactory.createRestEndpoint(new BinanceSpotListenKeyResponseDeserializer());
    this.spotKeepAliveListenKeyApi = restEndpointFactory.createRestEndpoint(new BinanceSpotKeepAliveListenKeyResponseDeserializer());
    this.spotDeleteListenKeyApi = restEndpointFactory.createRestEndpoint(new BinanceSpotDeleteListenKeyResponseDeserializer());
  }
}
