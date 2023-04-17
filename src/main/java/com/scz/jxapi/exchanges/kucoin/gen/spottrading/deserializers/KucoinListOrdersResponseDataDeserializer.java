package com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseData;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseDataItems;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jxapi.util.EncodingUtil.readNextInteger;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseData
 */
public class KucoinListOrdersResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinListOrdersResponseData> {
  private final KucoinListOrdersResponseDataItemsDeserializer kucoinListOrdersResponseDataItemsDeserializer = new KucoinListOrdersResponseDataItemsDeserializer();
  private final StructListFieldDeserializer<KucoinListOrdersResponseDataItems> kucoinListOrdersResponseDataItemsListDeserializer = new StructListFieldDeserializer<KucoinListOrdersResponseDataItems>(kucoinListOrdersResponseDataItemsDeserializer);
  
  @Override
  public KucoinListOrdersResponseData deserialize(JsonParser parser) throws IOException {
    KucoinListOrdersResponseData msg = new KucoinListOrdersResponseData();
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
        msg.setItems(kucoinListOrdersResponseDataItemsListDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
