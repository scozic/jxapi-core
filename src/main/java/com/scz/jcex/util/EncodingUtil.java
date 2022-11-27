package com.scz.jcex.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Helper methods around String encoding.
 */
public class EncodingUtil {
	
	private static final Logger log = LoggerFactory.getLogger(EncodingUtil.class);

	private EncodingUtil() {}
	
	public static String pojoToString(Object pojo) {
		String res = pojo.getClass().getSimpleName(); 
		try {
			res += new ObjectMapper().writeValueAsString(pojo);
		} catch (JsonProcessingException e) {
			log.error("Error while trying to serialize " + pojo.getClass().getName() + " instance to JSON", e);
		}
		return res;
	}
}
