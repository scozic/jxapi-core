package org.jxapi.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link HmacSHA256Signer}.
 */
public class HmacSHA2356SignerTest {

    @Test
    public void testHexSign() {
        String secret = "mySecret";
        String data = "dataToSign";
        String expected = "9fbdfd662a9426802b98f635efa52bbea4cf51c1979a38524c7bfaa3cb70af9e";
        String result = HmacSHA256Signer.hexSign(data, secret);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSign() {
        String secret = "mySecret";
        String data = "dataToSign";
        byte[] expected = new byte[] { -97, -67, -3, 102, 42, -108, 38, -128, 43, -104, -10, 53, -17, -91, 43, -66, -92, -49, 81, -63, -105, -102, 56, 82, 76, 123, -6, -93, -53, 112, -81, -98};
        byte[] result = HmacSHA256Signer.sign(data, secret);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSignNullMessage() {
      HmacSHA256Signer.sign(null, null);
    }

    @Test
    public void testBase64Sign() {
        String secret = "mySecret";
        String data = "dataToSign";
        String expected = "n739ZiqUJoArmPY176UrvqTPUcGXmjhSTHv6o8twr54=";
        String result = HmacSHA256Signer.base64Sign(data, secret);
        Assert.assertEquals(expected, result);
    }
}
