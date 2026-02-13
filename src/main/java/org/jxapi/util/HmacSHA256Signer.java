package org.jxapi.util;

import org.apache.commons.codec.binary.Hex;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Helper methods around HMAC-SHA256 encryption.
 * <p>
 * Such encryption is used to sign messages in order to verify their
 * authenticity.
 * It is ofen used in APIs to sign requests and verify responses.
 * </p>
 * 
 */
public class HmacSHA256Signer {

  private HmacSHA256Signer() {
  }

  /**
   * Encrypt given message with HMAC using the given secret and returns
   * Hexadecimal encoded value of the result.
   * 
   * @param message message to sign
   * @param secret  secret key
   * @return MAC operation result as hex string
   */
  public static String hexSign(String message, String secret) {
    return new String(Hex.encodeHex(sign(message, secret)));
  }

  /**
   * Sign the given message using the given secret.
   * 
   * @param message message to sign
   * @param secret  secret key
   * @return MAC result of HmacSHA256
   */
  public static byte[] sign(String message, String secret) {
    try {
      Mac sha256HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
      sha256HMAC.init(secretKeySpec);
      return sha256HMAC.doFinal(message.getBytes());
    } catch (Exception e) {
      throw new IllegalArgumentException("Unable to sign message.", e);
    }
  }

  /**
   * Encrypt given message with HMAC using the given secret and returns base64
   * encoded value of the result.
   * 
   * @param message message to sign
   * @param secret  secret key
   * @return MAC operation result as base64 string
   */
  public static String base64Sign(String message, String secret) {
    return Base64.getEncoder().encodeToString(sign(message, secret));
  }

}
