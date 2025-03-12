package org.jxapi.generator.java.exchange.api.pojo;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.jxapi.exchange.descriptor.Field;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.generator.java.Imports;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.DeepCloneable;

/**
 * Unit test for {@link PojoGenUtil}
 */
public class PojoGenUtilTest {

	@Test
	public void testGetSerializerClassName() {
        Assert.assertEquals("com.x.y.serializers.MyPojoSerializer", 
                            PojoGenUtil.getSerializerClassName("com.x.y.pojo.MyPojo"));
	}
	
	@Test
	public void testGenerateDeepCloneFieldInstruction() {
		doTestGenerateDeepCloneFieldInstruction(Type.INT, 
				"this.myField");
		doTestGenerateDeepCloneFieldInstruction(Type.OBJECT, 
				"this.myField != null ? this.myField.deepClone() : null");
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_LIST"), 
				"CollectionUtil.cloneList(this.myField)", 
				CollectionUtil.class.getName());
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("OBJECT_LIST"), 
				"CollectionUtil.deepCloneList(this.myField, DeepCloneable::deepClone)", 
				CollectionUtil.class.getName(), 
				DeepCloneable.class.getName());
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_LIST_LIST"), 
				"CollectionUtil.deepCloneList(this.myField, CollectionUtil::cloneList)", 
				CollectionUtil.class.getName());
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_LIST_LIST_LIST"),
				"CollectionUtil.deepCloneList(this.myField, l0 -> CollectionUtil.deeplCloneList(l0, CollectionUtil::cloneList))", 
				CollectionUtil.class.getName());
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_MAP"),
				"CollectionUtil.cloneMap(this.myField)", 
				CollectionUtil.class.getName());
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_MAP_MAP"),
				"CollectionUtil.deepCloneMap(this.myField, CollectionUtil::cloneMap)", 
				CollectionUtil.class.getName());
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_MAP_MAP_MAP"),
				"CollectionUtil.deepCloneMap(this.myField, m0 -> CollectionUtil.deepCloneMap(m0, CollectionUtil::cloneMap))", 
				CollectionUtil.class.getName());
		doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("OBJECT_MAP_LIST_MAP_LIST"),
				"CollectionUtil.deepCloneList(this.myField, m0 -> CollectionUtil.deepCloneMap(m0, l1 -> CollectionUtil.deeplCloneList(l1, m2 -> CollectionUtil.deepCloneMap(m2, DeepCloneable::deepClone))))", 
				CollectionUtil.class.getName(),
				DeepCloneable.class.getName());
				
	}
	
	private void doTestGenerateDeepCloneFieldInstruction(Type type, String expectedInstruction, String... expectedImports) {
		Imports imports = new Imports();
		Field f = Field.builder().name("myField").type(type).build();
		Assert.assertEquals(expectedInstruction, PojoGenUtil.generateDeepCloneFieldInstruction(f, imports));
		Assert.assertEquals(expectedImports.length, imports.size());
		for (String expectedImport : expectedImports) {
			Assert.assertTrue(imports.contains(expectedImport));
		}
	}
	
	@Test
	public void testGenerateCompareFieldsInstruction() {
		doTestGenerateCompareFieldsInstruction(Type.INT, "CompareUtil.compare(this.myField, other.myField)");
		doTestGenerateCompareFieldsInstruction(Type.OBJECT, "CompareUtil.compare(this.myField, other.myField)");
		doTestGenerateCompareFieldsInstruction(Type.fromTypeName("INT_LIST"),
				"CompareUtil.compareLists(this.myField, other.myField, CompareUtil::compare)");
		doTestGenerateCompareFieldsInstruction(Type.fromTypeName("OBJECT_LIST"),
				"CompareUtil.compareLists(this.myField, other.myField, CompareUtil::compare)");
		doTestGenerateCompareFieldsInstruction(Type.fromTypeName("INT_LIST_LIST"),
				"CompareUtil.compareLists(this.myField, other.myField, (l0a, l0b) -> CollectionUtil.compareLists(l0a,l0b, CompareUtil::compare))");
		doTestGenerateCompareFieldsInstruction(Type.fromTypeName("INT_LIST_LIST_LIST"),
				"CompareUtil.compareLists(this.myField, other.myField, (l0a, l0b) -> CollectionUtil.compareLists(l0a,l0b, (l1a, l1b) -> CollectionUtil.compareLists(l1a,l1b, CompareUtil::compare)))");
		doTestGenerateCompareFieldsInstruction(Type.fromTypeName("INT_MAP"),
				"CompareUtil.compareMaps(this.myField, other.myField, CompareUtil::compare)");
		doTestGenerateCompareFieldsInstruction(Type.fromTypeName("INT_MAP_MAP"),
				"CompareUtil.compareMaps(this.myField, other.myField, (m0a, m0b) -> CollectionUtil.compareMaps(m0a,m0b, CompareUtil::compare))");
		doTestGenerateCompareFieldsInstruction(Type.fromTypeName("INT_MAP_MAP_MAP"),
				"CompareUtil.compareMaps(this.myField, other.myField, (m0a, m0b) -> CollectionUtil.compareMaps(m0a,m0b, (m1a, m1b) -> CollectionUtil.compareMaps(m1a,m1b, CompareUtil::compare)))");
		doTestGenerateCompareFieldsInstruction(Type.fromTypeName("OBJECT_MAP_LIST_MAP_LIST"),
				"CompareUtil.compareLists(this.myField, other.myField, (m0a, m0b) -> CollectionUtil.compareMaps(m0a,m0b, (l1a, l1b) -> CollectionUtil.compareLists(l1a,l1b, (m2a, m2b) -> CollectionUtil.compareMaps(m2a,m2b, CompareUtil::compare))))");
	}
	
	private void doTestGenerateCompareFieldsInstruction(Type type, String expectedInstruction) {
		Field f = Field.builder().name("myField").type(type).build();
		Assert.assertEquals(expectedInstruction, PojoGenUtil.generateCompareFieldsInstruction(f));
	}
	
	@Test
	public void testGenerateSerialVersionUid() {
		String className = "com.x.y.MyClass";
		List<Field> fields = List.of(
				Field.builder().name("field1").type(Type.INT).build(),
				Field.builder().name("field2").type(Type.fromTypeName("OBJECT_LIST_MAP")).build()
			);
		List<String> implementedInterfaces = List.of("com.x.y.MyInterface", "com.x.z.MyOtherInterface");
		Assert.assertEquals(-1752733059768616242L, PojoGenUtil.generateSerialVersionUid(className, fields, implementedInterfaces));
	}
}
