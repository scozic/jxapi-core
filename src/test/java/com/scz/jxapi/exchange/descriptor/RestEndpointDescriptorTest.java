package com.scz.jxapi.exchange.descriptor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.netutils.rest.HttpMethod;

/**
 * Unit test for {@link RestEndpointDescriptor}.
 */
public class RestEndpointDescriptorTest {

    @Test
    public void testCreateRestEndpointDescriptor() {
        RestEndpointDescriptor descriptor = new RestEndpointDescriptor();
        Assert.assertNotNull(descriptor);
    }

    @Test
    public void testGettersAndSetters() {
        RestEndpointDescriptor descriptor = new RestEndpointDescriptor();
        descriptor.setName("name");
        descriptor.setDescription("description");
        descriptor.setUrl("url");
        descriptor.setHttpMethod(HttpMethod.GET);
        Field request = Field.create("STRING", "request", "r", "request", "sampleValue");
        descriptor.setRequest(request);
        Field response = Field.create("STRING", "response", "r", "response", "sampleValue");
        descriptor.setResponse(response);
        descriptor.setUrlParameters("urlParameters");
        descriptor.setUrlParametersListSeparator("urlParametersListSeparator");
        descriptor.setQueryParams(true);
        descriptor.setRequestWeight(1);
        descriptor.setRateLimits(List.of());
        Assert.assertEquals("name", descriptor.getName());
        Assert.assertEquals("description", descriptor.getDescription());
        Assert.assertEquals("url", descriptor.getUrl());
        Assert.assertEquals(HttpMethod.GET, descriptor.getHttpMethod());
        Assert.assertEquals(request, descriptor.getRequest());
        Assert.assertEquals(response, descriptor.getResponse());
        Assert.assertEquals("urlParameters", descriptor.getUrlParameters());
        Assert.assertEquals("urlParametersListSeparator", descriptor.getUrlParametersListSeparator());
        Assert.assertTrue(descriptor.isQueryParams());
        Assert.assertEquals(Integer.valueOf(1), descriptor.getRequestWeight());
        Assert.assertEquals(List.of(), descriptor.getRateLimits());
    }

    @Test
    public void testToString() {
        RestEndpointDescriptor descriptor = new RestEndpointDescriptor();
        descriptor.setName("name");
        descriptor.setDescription("description");
        descriptor.setUrl("url");
        descriptor.setHttpMethod(HttpMethod.GET);
        Assert.assertEquals("RestEndpointDescriptor{\"name\":\"name\",\"description\":\"description\",\"url\":\"url\",\"httpMethod\":\"GET\",\"queryParams\":false}", 
                            descriptor.toString());
        
    }
}
