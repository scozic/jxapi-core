package org.jxapi.exchange.descriptor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.jxapi.netutils.rest.HttpMethod;

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
        descriptor.setDocUrl("https://doc.myexchange.com/rest/name");
        descriptor.setUrl("url");
        descriptor.setHttpMethod(HttpMethod.GET);
        Field request = Field.builder()
                   .type(Type.STRING)
                   .name("request")
                   .msgField("r")
                   .description("Request data")
                   .sampleValue("sampleValue")
                   .build();
        descriptor.setRequest(request);
        Field response = Field.builder()
         .type(Type.STRING)
         .name("response")
         .msgField("r")
         .description("Request data")
         .sampleValue("sampleValue")
         .build();
        descriptor.setResponse(response);
        descriptor.setUrlParameters("urlParameters");
        descriptor.setUrlParametersListSeparator("urlParametersListSeparator");
        descriptor.setQueryParams(true);
        descriptor.setRequestWeight(1);
        descriptor.setRateLimits(List.of());
        Assert.assertEquals("name", descriptor.getName());
        Assert.assertEquals("description", descriptor.getDescription());
        Assert.assertEquals("url", descriptor.getUrl());
        Assert.assertEquals("https://doc.myexchange.com/rest/name", descriptor.getDocUrl());
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
        Assert.assertEquals("RestEndpointDescriptor{\"name\":\"name\",\"description\":\"description\",\"url\":\"url\",\"httpMethod\":\"GET\",\"queryParams\":false,\"paginated\":false}", 
                            descriptor.toString());
        
    }
}
