package com.scz.jxapi.generator.java.exchange.api.pojo;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link EndpointPojoGeneratorUtil}
 */
public class EndpointPojoGeneratorUtilTest {

	@Test
	public void testGetSerializerClassName() {
        Assert.assertEquals("com.x.y.serializers.MyPojoSerializer", 
                            EndpointPojoGeneratorUtil.getSerializerClassName("com.x.y.pojo.MyPojo"));
	}
}
