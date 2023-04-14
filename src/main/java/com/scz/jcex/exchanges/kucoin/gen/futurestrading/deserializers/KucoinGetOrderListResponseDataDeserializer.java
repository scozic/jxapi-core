package com.scz.jcex.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetOrderListResponseData;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetOrderListResponseDataItems;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.readNextInteger;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetOrderListResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetOrderListResponseData
 */
public class KucoinGetOrderListResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinGetOrderListResponseData> {
  private final KucoinGetOrderListResponseDataItemsDeserializer kucoinGetOrderListResponseDataItemsDeserializer = new KucoinGetOrderListResponseDataItemsDeserializer();
  private final StructListFieldDeserializer<KucoinGetOrderListResponseDataItems> kucoinGetOrderListResponseDataItemsListDeserializer = new StructListFieldDeserializer<KucoinGetOrderListResponseDataItems>(kucoinGetOrderListResponseDataItemsDeserializer);
  
  @Override
  public KucoinGetOrderListResponseData deserialize(JsonParser parser) throws IOException {
    KucoinGetOrderListResponseData msg = new KucoinGetOrderListResponseData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "currentPage":
        msg.setCurrentPage(readNextInteger(parser));
      break;
      case "pageSize":
        msg.setPageSize(readNextInteger(parser));
      break;
      case "totalPages":
        msg.setTotalPages(readNextInteger(parser));
      break;
      case "totalNum":
        msg.setTotalNum(readNextInteger(parser));
      break;
      case "items":
        parser.nextToken();
        msg.setItems(kucoinGetOrderListResponseDataItemsListDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
