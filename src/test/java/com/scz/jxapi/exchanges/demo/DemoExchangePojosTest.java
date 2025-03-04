package com.scz.jxapi.exchanges.demo;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload;

/**
 * Tests behavior of methods of a few generated POJOs from exchange wrapper
 * classes for DemoExchange. This is to ensure specific methods for POJOs :
 * <code>builder()</code>, <code>equals()</code>, <code>compareTo()</code>,
 * <code>hashCode()</code>, <code>deepClone</code> behave as expected.
 */
public class DemoExchangePojosTest {

	
	@Test
	public void testBuildDemoExchangeMarketDataExchangeInfoResponse() {
		DemoExchangeMarketDataExchangeInfoResponse pojo = DemoExchangeMarketDataExchangeInfoResponse.builder()
				.responseCode(200)
				.addToPayload(DemoExchangeMarketDataExchangeInfoResponsePayload.builder()
					.symbol("BTCUSD")
					.minOrderSize(new BigDecimal("5.0"))
					.orderTickSize(new BigDecimal("0.01"))
					.build())
				.build();
		
		Assert.assertEquals(200, pojo.getResponseCode().intValue());
		Assert.assertEquals(1, pojo.getPayload().size());
		DemoExchangeMarketDataExchangeInfoResponsePayload payload = pojo.getPayload().get(0);
		Assert.assertEquals("BTCUSD", payload.getSymbol());
		Assert.assertEquals(new BigDecimal("5.0"), payload.getMinOrderSize());
		Assert.assertEquals(new BigDecimal("0.01"), payload.getOrderTickSize());
	}

}
