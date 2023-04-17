package com.scz.jcex.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetUntriggeredStopOrderListResponseData;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetUntriggeredStopOrderListResponseDataItems;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.readNextInteger;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetUntriggeredStopOrderListResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetUntriggeredStopOrderListResponseData
 */
public class KucoinGetUntriggeredStopOrderListResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinGetUntriggeredStopOrderListResponseData> {
  private final KucoinGetUntriggeredStopOrderListResponseDataItemsDeserializer kucoinGetUntriggeredStopOrderListResponseDataItemsDeserializer = new KucoinGetUntriggeredStopOrderListResponseDataItemsDeserializer();
  private final StructListFieldDeserializer<KucoinGetUntriggeredStopOrderListResponseDataItems> kucoinGetUntriggeredStopOrderListResponseDataItemsListDeserializer = new StructListFieldDeserializer<KucoinGetUntriggeredStopOrderListResponseDataItems>(kucoinGetUntriggeredStopOrderListResponseDataItemsDeserializer);
  
  @Override
  public KucoinGetUntriggeredStopOrderListResponseData deserialize(JsonParser parser) throws IOException {
    KucoinGetUntriggeredStopOrderListResponseData msg = new KucoinGetUntriggeredStopOrderListResponseData();
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
        msg.setItems(kucoinGetUntriggeredStopOrderListResponseDataItemsListDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
