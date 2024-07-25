package com.scz.jxapi.generator.java.exchange.api.pojo;

import org.apache.commons.lang3.StringUtils;

import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;

/**
 * Helper methods used in generation of exchange API REST/WEBSOCKET endpoints
 * POJOs and associated JSON serializer/deserializers.
 */
public class EndpointPojoGeneratorUtil {

	/**
	 * Generates the expected full class name of JSON serializer class for a given
	 * POJO full class name.
	 * 
	 * @param pojoClassName The full class name of POJO. It is intended to be a
	 *                      generated POJO with '.pojo' in package name.
	 * @return Full class name of JSON serializer class, that is a class in sub
	 *         package 'serializers' of parent package of 'pojo' package, named
	 *         <code>&lt;POJO simple class name&gt; + 'Serializer'</code>.
	 */
	public static String getSerializerClassName(String pojoClassName) {
		String pkg = StringUtils.substringBefore(JavaCodeGenerationUtil.getClassPackage(pojoClassName), ".pojo");
		return pkg + ".serializers." + JavaCodeGenerationUtil.getClassNameWithoutPackage(pojoClassName) + "Serializer";
	}

}
