package org.jxapi.netutils.websocket.multiplexing;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link WebsocketMessageTopicMatcherField}
 */
public class WebsocketMessageTopicMatcherFieldTest {

    @Test
    public void testCreateList() {
        List<WebsocketMessageTopicMatcherField> fields = WebsocketMessageTopicMatcherField.createList("f1", "value1", "f2", "value2");
        Assert.assertNotNull(fields);
        Assert.assertEquals(2, fields.size());
        WebsocketMessageTopicMatcherField f1 = fields.get(0);
        Assert.assertEquals("f1", f1.getName());
        Assert.assertEquals("value1", f1.getValue());
        WebsocketMessageTopicMatcherField f2 = fields.get(1);
        Assert.assertEquals("f2", f2.getName());
        Assert.assertEquals("value2", f2.getValue());
    }

    @Test
    public void testCreateListEmpty() {
        List<WebsocketMessageTopicMatcherField> fields = WebsocketMessageTopicMatcherField.createList();
        Assert.assertNotNull(fields);
        Assert.assertTrue(fields.isEmpty());
    }

    @Test
    public void testGettersAndSetters() {
        WebsocketMessageTopicMatcherField field = new WebsocketMessageTopicMatcherField();
        field.setName("name");
        field.setValue("value");
        Assert.assertEquals("name", field.getName());
        Assert.assertEquals("value", field.getValue());
    }

    @Test
    public void testToString() {
        WebsocketMessageTopicMatcherField field = new WebsocketMessageTopicMatcherField();
        field.setName("fname");
        field.setValue("avalue");
        Assert.assertEquals("WebsocketMessageTopicMatcherField{\"name\":\"fname\",\"value\":\"avalue\"}", field.toString());
    }
}
