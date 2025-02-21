package com.scz.jxapi.generator.java.exchange.api.pojo;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.Imports;
import com.scz.jxapi.util.CollectionUtil;

/**
 * Unit test for {@link EndpointPojoGeneratorUtil}
 */
public class EndpointPojoGeneratorUtilTest {

	@Test
	public void testGetSerializerClassName() {
        Assert.assertEquals("com.x.y.serializers.MyPojoSerializer", 
                            EndpointPojoGeneratorUtil.getSerializerClassName("com.x.y.pojo.MyPojo"));
	}
	
	@Test
	public void testGenerateDeepCloneFieldInstruction() {
		doTestGenerateDeepCloneFieldInstruction(Type.INT, 
				"this.myField");
		doTestGenerateDeepCloneFieldInstruction(Type.OBJECT, 
				"this.myField != null ? this.myField.deepClone() : null");
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_LIST"), 
				"CollectionUtil.cloneList(this.myField)", CollectionUtil.class.getName());
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("OBJECT_LIST"), 
				"CollectionUtil.deepCloneList(this.myField, DeepCloneable::deepClone)", CollectionUtil.class.getName());
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_LIST_LIST"), 
				"CollectionUtil.deepCloneList(this.myField, CollectionUtil::cloneList)", CollectionUtil.class.getName());
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_LIST_LIST_LIST"),
				"CollectionUtil.deepCloneList(this.myField, l0 -> CollectionUtil.deeplCloneList(l0, CollectionUtil::cloneList))", CollectionUtil.class.getName());
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_MAP"),
				"CollectionUtil.cloneMap(this.myField)", CollectionUtil.class.getName());
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_MAP_MAP"),
				"CollectionUtil.deepCloneMap(this.myField, CollectionUtil::cloneMap)", CollectionUtil.class.getName());
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_MAP_MAP_MAP"),
				"CollectionUtil.deepCloneMap(this.myField, m0 -> CollectionUtil.deepCloneMap(m0, CollectionUtil::cloneMap))", CollectionUtil.class.getName());
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("OBJECT_MAP_LIST_MAP_LIST"),
				"CollectionUtil.deepCloneList(this.myField, m0 -> CollectionUtil.deepCloneMap(m0, l1 -> CollectionUtil.deeplCloneList(l1, m2 -> CollectionUtil.deepCloneMap(m2, DeepCloneable::deepClone))))", 
				CollectionUtil.class.getName());
				
	}
	
	private void doTestGenerateDeepCloneFieldInstruction(Type type, String expectedInstruction, String... expectedImports) {
		Imports imports = new Imports();
		Field f = Field.builder().name("myField").type(type).build();
		Assert.assertEquals(expectedInstruction, EndpointPojoGeneratorUtil.generateDeepCloneFieldInstruction(f, imports));
		Assert.assertEquals(expectedImports.length, imports.size());
		for (String expectedImport : expectedImports) {
			Assert.assertTrue(imports.contains(expectedImport));
		}
	}
	
}
