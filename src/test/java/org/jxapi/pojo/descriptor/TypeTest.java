package org.jxapi.pojo.descriptor;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link Type}
 */
public class TypeTest {

    @Test
    public void testFromTypeName() {
        Type type = Type.fromTypeName("STRING");
        Assert.assertEquals(Type.STRING, type);
    }

    @Test
    public void testFromTypeNameWithNull() {
        Type type = Type.fromTypeName(null);
        Assert.assertEquals(null, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromTypeNameWithEmpty() {
        Type.fromTypeName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromTypeNameWithInvalid() {
        Type.fromTypeName("INVALID");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFromTypeNameWithInvalidSubType() {
        Type.fromTypeName("OBJECT_INT_MAP");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFromTypeNameInvalidEmptySubTypeNameAfterLastSeparator() {
        Type.fromTypeName("OBJECT_MAP_");
    }

    @Test
    public void testFromTypeNameWithOBJECT() {
        Type type = Type.fromTypeName("OBJECT");
        Assert.assertEquals(Type.OBJECT, type);
    }
    
    @Test
    public void testFromTypeNameWithOBJECT_MAP() {
        Type type = Type.fromTypeName("OBJECT_MAP");
        Assert.assertEquals(CanonicalType.MAP, type.getCanonicalType());
        Assert.assertNotNull(type.getSubType());
        Assert.assertEquals(CanonicalType.OBJECT, type.getSubType().getCanonicalType());
        Assert.assertEquals(CanonicalType.OBJECT, Type.getLeafSubType(type).getCanonicalType());
    }

    @Test
    public void testGetLeafSubTypeWithNull() {
        Assert.assertNull(Type.getLeafSubType(null));
    }

    @Test
    public void testGetLeafSubTypeWithOBJECT() {
        Type type = Type.fromTypeName("OBJECT");
        Assert.assertEquals(CanonicalType.OBJECT, Type.getLeafSubType(type).getCanonicalType());
    }

    @Test
    public void testGetLeafSubTypeWithINT_LIST_MAP() {
        Type type = Type.fromTypeName("INT_LIST_MAP");
        Assert.assertEquals(CanonicalType.INT, Type.getLeafSubType(type).getCanonicalType());
    }

    @Test
    public void testFromTypeNameWithOBJECT_LIST() {
        Type type = Type.fromTypeName("OBJECT_LIST");
        Assert.assertEquals(CanonicalType.LIST, type.getCanonicalType());
        Assert.assertNotNull(type.getSubType());
        Assert.assertEquals(CanonicalType.OBJECT, type.getSubType().getCanonicalType());
    }

    @Test
    public void testFromTypeNameWithINT_LIST_MAP() {
        Type type = Type.fromTypeName("INT_LIST_MAP");
        Assert.assertEquals(CanonicalType.MAP, type.getCanonicalType());
        type = type.getSubType();
        Assert.assertNotNull(type);
        Assert.assertEquals(CanonicalType.LIST, type.getCanonicalType());
        type = type.getSubType();
        Assert.assertNotNull(type);
        Assert.assertEquals(CanonicalType.INT, type.getCanonicalType());
    }

   @Test
   public void testGettersAndSetters() {
         Type type = new Type();
         type.setCanonicalType(CanonicalType.OBJECT);
         type.setSubType(new Type());
         Assert.assertEquals(CanonicalType.OBJECT, type.getCanonicalType());
         Assert.assertNotNull(type.getSubType());
   }

   @Test
   public void testToStringSimpleType() {
         Type type = new Type();
         type.setCanonicalType(CanonicalType.OBJECT);
         Assert.assertEquals("OBJECT", type.toString());
   }

   @Test
   public void testToStringComplexType() {
         Type type = new Type();
         type.setCanonicalType(CanonicalType.MAP);
         Type subType = new Type();
         subType.setCanonicalType(CanonicalType.LIST);
         Type subSubType = new Type();
         subSubType.setCanonicalType(CanonicalType.INT);
         subType.setSubType(subSubType);
         type.setSubType(subType);
         Assert.assertEquals("INT_LIST_MAP", type.toString());
   }

   @Test
   public void testToStringInvalidType() {
         Type type = new Type();
         Assert.assertEquals("UNDEFINED_TYPE", type.toString());
   }

   @Test
   public void testEquals() {
         Type type1 = new Type();
         type1.setCanonicalType(CanonicalType.OBJECT);
         Type type2 = new Type();
         type2.setCanonicalType(CanonicalType.OBJECT);
         Assert.assertEquals(type1, type2);
   }

    @Test
    public void testEqualsWithNull() {
        Type type1 = new Type();
        type1.setCanonicalType(CanonicalType.OBJECT);
        Assert.assertNotEquals(type1, null);
    }

    @Test
    public void testEqualsWithDifferentType() {
        Type type1 = new Type();
        type1.setCanonicalType(CanonicalType.OBJECT);
        Type type2 = new Type();
        type2.setCanonicalType(CanonicalType.LIST);
        Assert.assertNotEquals(type1, type2);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        Type type1 = new Type();
        type1.setCanonicalType(CanonicalType.OBJECT);
        Assert.assertNotEquals(type1, new Object());
    }

    @Test
    public void testHashCode() {
        Type type1 = new Type();
        type1.setCanonicalType(CanonicalType.OBJECT);
        Type type2 = new Type();
        type2.setCanonicalType(CanonicalType.OBJECT);
        Assert.assertEquals(type1.hashCode(), type2.hashCode());
    }

    @Test
    public void testHashCodeWithNull() {
        Type type1 = new Type();
        type1.setCanonicalType(CanonicalType.OBJECT);
        Assert.assertNotEquals(type1.hashCode(), 0);
    }

    @Test
    public void testIsObject() {
        Type type = new Type();
        type.setCanonicalType(CanonicalType.OBJECT);
        Assert.assertTrue(type.isObject());
    }

    @Test
    public void testIsObjectWithNull() {
        Type type = new Type();
        Assert.assertFalse(type.isObject());
    }

    @Test
    public void testIsObjectWithNotObjectType() {
        Type type = Type.fromTypeName("STRING_LIST");
        Assert.assertFalse(type.isObject());
    }


}
