package org.jxapi.exchanges.demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jxapi.exchanges.demo.gen.DemoExchangeConstants;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers.DemoExchangeMarketDataExchangeInfoResponseSerializer;
import org.jxapi.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Test serialization of POJOs to JSON with and without serializer registration on object mapper.
 * <p>
 * This test demonstrates the performance impact of registering a custom
 * serializer for a POJO.
 * </p>
 * Results: The test shows that registering a custom serializer using Jackson's
 * ObjectMapper.registerModule() method does not have a significant performance
 * impact on serialization speed / GC activity versus defining
 * {@link JsonSerialize} annotaton on POJO class.
 * 
 */
public class TestSerializePojoToJson {
  
  private static final Logger log = LoggerFactory.getLogger(TestSerializePojoToJson.class);
  
  private static final int ITERATION_COUNT = 20;
  private static final int POJO_COUNT = 1000000;
  
  public void run() {
    try {
      List<DemoExchangeMarketDataExchangeInfoResponse> pojos = new ArrayList<>();
      for (int i = 0; i < POJO_COUNT; i++) {
        pojos.add(createRandomPojo());
      }
      long totalTimewithSerializer = 0;
      long totalTimewithoutSerializer = 0;
      
      // Warm up
      for (int i = 0; i < ITERATION_COUNT; i++) {
        toTestSerializePojos(pojos, true);
            toTestSerializePojos(pojos, false);
      }
      
      for (int i = 0; i < ITERATION_COUNT; i++) {
        totalTimewithSerializer += toTestSerializePojos(pojos, true);
            totalTimewithoutSerializer += toTestSerializePojos(pojos, false);
      }
      log.info("Serialization of {} POJOs to JSON with serializer registration took: {} ms",
          POJO_COUNT,  
          (totalTimewithSerializer / ITERATION_COUNT) / 1_000_000);
      log.info("Serialization of {} POJOs to JSON without serializer registration took: {} ms",
          POJO_COUNT,  
          (totalTimewithoutSerializer / ITERATION_COUNT) / 1_000_000);
    } catch (JsonProcessingException e) {
      log.error("Error during serialization load test", e);
    }
      }
  
  private long toTestSerializePojos(List<DemoExchangeMarketDataExchangeInfoResponse> pojos, boolean registerSerializer) throws JsonProcessingException {
    List<String> serialized = new ArrayList<>();
    ObjectMapper objectMapper = createObjectMapper(registerSerializer);
    long start = System.nanoTime();
    for (DemoExchangeMarketDataExchangeInfoResponse pojo : pojos) {
      serialized.add(objectMapper.writeValueAsString(pojo));
    }
    long end = System.nanoTime();
    return end - start;
  }

  
  private static DemoExchangeMarketDataExchangeInfoResponse createRandomPojo() {
    DemoExchangeMarketDataExchangeInfoResponse expectedResponse = new DemoExchangeMarketDataExchangeInfoResponse();
    expectedResponse.setResponseCode(DemoExchangeConstants.RESPONSE_CODE_OK);
    
    DemoExchangeMarketDataExchangeInfoResponsePayload market1 = new DemoExchangeMarketDataExchangeInfoResponsePayload();
    market1.setSymbol("BTC_USDT");
    market1.setOrderTickSize(new BigDecimal("0.1"));
    market1.setMinOrderSize(new BigDecimal("0.0001"));
    
    DemoExchangeMarketDataExchangeInfoResponsePayload market2 = new DemoExchangeMarketDataExchangeInfoResponsePayload();
    market2.setSymbol("ETH_USDT");
    market2.setOrderTickSize(new BigDecimal("0.01"));
    market2.setMinOrderSize(new BigDecimal("0.005"));
    
    expectedResponse.setPayload(List.of(market1, market2));
    return expectedResponse;
  }
  
  private static ObjectMapper createObjectMapper(boolean registerSerializer) {
    ObjectMapper om = JsonUtil.createDefaultObjectMapper();
    if (registerSerializer) {
      om.registerModule(new SimpleModule().addSerializer(
          DemoExchangeMarketDataExchangeInfoResponse.class, 
          new DemoExchangeMarketDataExchangeInfoResponseSerializer()));
    }
    return om;
  }
  
  public static void main(String[] args) {
    new TestSerializePojoToJson().run();
    System.exit(0);
  }
}
