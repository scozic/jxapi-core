package com.scz.jxapi.util;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link CollectionUtil}
 */
public class CollectionUtilTest {

	@Test
    public void testIsEmpty() {
        Assert.assertTrue(CollectionUtil.isEmpty(null));
        Assert.assertTrue(CollectionUtil.isEmpty(Collections.emptyList()));
        Assert.assertFalse(CollectionUtil.isEmpty(Collections.singletonList("a")));
    }
}
