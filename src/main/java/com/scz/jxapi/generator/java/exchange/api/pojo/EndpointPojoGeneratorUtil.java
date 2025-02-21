package com.scz.jxapi.generator.java.exchange.api.pojo;

import org.apache.commons.lang3.StringUtils;

import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.Imports;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaGenUtil;
import com.scz.jxapi.util.CollectionUtil;

/**
 * Helper methods used in generation of exchange API REST/WEBSOCKET endpoints
 * POJOs and associated JSON serializer/deserializers.
 */
public class EndpointPojoGeneratorUtil {
	
	private EndpointPojoGeneratorUtil() {}

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
	
	public static String generateDeepCloneFieldInstruction(Field f, Imports imports) {
		Type type = ExchangeJavaGenUtil.getFieldType(f);
		if (type.getCanonicalType().isPrimitive) {
			return "this." + f.getName();
		} else if (type.getCanonicalType() == CanonicalType.LIST) {
			imports.add(CollectionUtil.class);
			Type subType = type.getSubType();
			if (subType.getCanonicalType().isPrimitive) {
				return "CollectionUtil.cloneList(this." + f.getName() + ")";
			} else {
				return "CollectionUtil.deepCloneList(this." + f.getName() + ", " + getItemClonerDeclaration(subType, 0) + ")";
			}
		} else if (type.getCanonicalType() == CanonicalType.MAP) {
			imports.add(CollectionUtil.class);
			Type subType = type.getSubType();
			if (subType.getCanonicalType().isPrimitive) {
				return "CollectionUtil.cloneMap(this." + f.getName() + ")";
			} else {
				return "CollectionUtil.deepCloneMap(this." + f.getName() + ", " + getItemClonerDeclaration(subType, 0) + ")";
			}
		}
		// Object type
		return new StringBuilder("this.")
					.append(f.getName())
					.append(" != null ? this.")
					.append(f.getName())
					.append(".deepClone() : null").toString();
		
	}
	
	private static String getItemClonerDeclaration(Type itemType, int depth) {
		if (itemType.getCanonicalType().isPrimitive) {
			return "UnaryOperator.identity()";
		} else if (itemType.getCanonicalType() == CanonicalType.LIST) {
			Type subType = itemType.getSubType();
			if (subType.getCanonicalType().isPrimitive) {
				return "CollectionUtil::cloneList";
			} else {
				String lambdaArg = "l" + depth;
				return new StringBuilder()
						.append(lambdaArg)
						.append(" -> CollectionUtil.deeplCloneList(")
						.append(lambdaArg)
						.append(", ")
						.append(getItemClonerDeclaration(subType, depth + 1))
						.append(")")
						.toString();
			}
		} else if (itemType.getCanonicalType() == CanonicalType.MAP) {
			Type subType = itemType.getSubType();
			if (subType.getCanonicalType().isPrimitive) {
				return "CollectionUtil::cloneMap";
			} else {
				String lambdaArg = "m" + depth;
				return new StringBuilder()
						.append(lambdaArg)
						.append(" -> CollectionUtil.deepCloneMap(")
						.append(lambdaArg)
						.append(", ")
						.append(getItemClonerDeclaration(subType, depth + 1))
						.append(")").toString();
			}
		}
		// Object type
		return "DeepCloneable::deepClone";
	}

}
