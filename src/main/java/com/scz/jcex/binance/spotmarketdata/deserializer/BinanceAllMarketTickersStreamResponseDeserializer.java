package com.scz.jcex.binance.spotmarketdata.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.binance.spotmarketdata.pojo.BinanceAllMarketTickersStreamResponse;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;

public class BinanceAllMarketTickersStreamResponseDeserializer extends AbstractJsonMessageDeserializer<BinanceAllMarketTickersStreamResponse> {

	@Override
	public BinanceAllMarketTickersStreamResponse deserialize(JsonParser parser) throws IOException {
		BinanceAllMarketTickersStreamResponse msg = new BinanceAllMarketTickersStreamResponse();
		
		while(parser.nextToken() != JsonToken.END_OBJECT) {
			switch(parser.getCurrentName()) {
			case "C":
				msg.setC(parser.nextLongValue(0L));
				
			break;
			default:
			}
		}
		
		return msg;
	}

}
