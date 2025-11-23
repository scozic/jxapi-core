package org.jxapi.exchange.descriptor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link WebsocketTopicMatcherDescriptor}
 */
public class WebsocketMessageTopicMatcherDescriptorTest {

    @Test
    public void testCreateWebsocketMessageTopicMatcherDescriptor() {
        WebsocketTopicMatcherDescriptor descriptor = new WebsocketTopicMatcherDescriptor();
        Assert.assertNotNull(descriptor);
    }

    @Test
    public void testGettersAndSetters() {
        WebsocketTopicMatcherDescriptor descriptor = new WebsocketTopicMatcherDescriptor();
        descriptor.setFieldName("testField");
        descriptor.setFieldValue("testValue");
        descriptor.setFieldRegexp("test.*");
        List<WebsocketTopicMatcherDescriptor> orList = List.of(new WebsocketTopicMatcherDescriptor());
        descriptor.setOr(orList);
        List<WebsocketTopicMatcherDescriptor> andList = List.of(new WebsocketTopicMatcherDescriptor());
        descriptor.setAnd(andList);
        Assert.assertEquals("testField", descriptor.getFieldName());
        Assert.assertEquals("testValue", descriptor.getFieldValue());
        Assert.assertEquals("test.*", descriptor.getFieldRegexp());
        Assert.assertSame(orList, descriptor.getOr());
        Assert.assertSame(andList, descriptor.getAnd());
    }

    @Test
    public void testToString() {
        WebsocketTopicMatcherDescriptor fieldValuedescriptor = new WebsocketTopicMatcherDescriptor();
        fieldValuedescriptor.setFieldName("foo");
        fieldValuedescriptor.setFieldValue("bar");
        Assert.assertEquals(
            "WebsocketTopicMatcherDescriptor{\"fieldName\":\"foo\",\"fieldValue\":\"bar\"}",
            fieldValuedescriptor.toString());
        WebsocketTopicMatcherDescriptor fieldRegexpDescriptor = new WebsocketTopicMatcherDescriptor();
        fieldRegexpDescriptor.setFieldName("foo");
        fieldRegexpDescriptor.setFieldRegexp("ba.*");
        Assert.assertEquals(
            "WebsocketTopicMatcherDescriptor{\"fieldName\":\"foo\",\"fieldRegexp\":\"ba.*\"}",
            fieldRegexpDescriptor.toString());
        WebsocketTopicMatcherDescriptor orDescriptor = new WebsocketTopicMatcherDescriptor();
        orDescriptor.setOr(List.of(fieldValuedescriptor, fieldRegexpDescriptor));
        Assert.assertEquals(
            "WebsocketTopicMatcherDescriptor{\"or\":[{\"fieldName\":\"foo\",\"fieldValue\":\"bar\"},{\"fieldName\":\"foo\",\"fieldRegexp\":\"ba.*\"}]}",
            orDescriptor.toString());
        WebsocketTopicMatcherDescriptor andDescriptor = new WebsocketTopicMatcherDescriptor();
        andDescriptor.setAnd(List.of(fieldValuedescriptor, fieldRegexpDescriptor));
        Assert.assertEquals(
            "WebsocketTopicMatcherDescriptor{\"and\":[{\"fieldName\":\"foo\",\"fieldValue\":\"bar\"},{\"fieldName\":\"foo\",\"fieldRegexp\":\"ba.*\"}]}",
            andDescriptor.toString());
    }
}
