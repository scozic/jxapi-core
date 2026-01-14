package org.jxapi.pojo.descriptor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Describes the type of data of a {@link Field}.
 * It can be a primitive type (see
 * {@link CanonicalType#isPrimitive}, an object (
 * {@link CanonicalType#OBJECT}), a list of data of same subtype (see
 * {@link CanonicalType#LIST}) , or a
 * map indexed with {@link String} keys and values of same subtype (see
 * {@link CanonicalType#MAP}) .<p>
 * When a field canonical type is primitive or
 * {@link CanonicalType#OBJECT}, it subtype is
 * <code>null</code> as not relevant.<br>
 * Otherwise, the type stands for a list or {@link String} key map of values of
 * same subtype which can be any type.<br>
 * Such data ypes are extensible: for instance
 * <code>BIGDECIMAL_LIST_MAP</code> means value of a field is a map of list of
 * {@link BigDecimal} values. <p>
 * When leaf subtype is {@link CanonicalType#OBJECT} (see
 * {@link #isObject()}), the field definition must specify the list of
 * {@link Field} that defines the object
 * 
 */
public class Type implements Serializable {
  
  private static final long serialVersionUID = 6954595325720722821L;

  private static final Map<String, Type> CANONICAL_TYPES = new HashMap<>(6);

  /**
   * Type with {@link CanonicalType#OBJECT} as canonical type.
   */
  public static final Type OBJECT = createCanonicalType(CanonicalType.OBJECT);

  /**
   * Type with {@link CanonicalType#STRING} as canonical type.
   */
  public static final Type STRING = createCanonicalType(CanonicalType.STRING);
  
  /**
   * Type with {@link CanonicalType#INT} as canonical type.
   */
  public static final Type INT = createCanonicalType(CanonicalType.INT);
  
  /**
   * Type with {@link CanonicalType#LONG} as canonical type.
   */
  public static final Type LONG = createCanonicalType(CanonicalType.LONG);
  
  /**
   * Type with {@link CanonicalType#BIGDECIMAL} as canonical type.
   */
  public static final Type BIGDECIMAL = createCanonicalType(CanonicalType.BIGDECIMAL);
  
  /**
   * Type with {@link CanonicalType#BOOLEAN} as canonical type.
   */
  public static final Type BOOLEAN = createCanonicalType(CanonicalType.BOOLEAN);
  
  /**
   * Create a type for a given canonical type
   * @param canonicalType
   * @return the type
   */
  private static Type createCanonicalType(CanonicalType canonicalType) {
    Type type = new Type();
    type.setCanonicalType(canonicalType);
    CANONICAL_TYPES.put(canonicalType.name(), type);
    return type;
  }
  
  /**
   * Get the leaf subtype of a type
   * 
   * @param type The type to get leaf subtype of
   * @return if type is null, return null, otherwise if type has no subtype,
   *         return type canonical type, otherwise return the leaf subtype of
   *         subtype
   */
  public static Type getLeafSubType(Type type) {
    if (type == null) {
      return null;
    }
    Type res = type;
    while (res.subType != null) {
      res = res.subType;
    }
    return res;
  }
  
  /**
   * Create a type from a type name
   * A type name can be:
   * <ul>
   * <li> a canonical type name (OBJECT, STRING, INT, LONG, BIGDECIMAL, BOOLEAN)
   * <li> a list of values of same subtype: SUBTYPE_LIST where SUBTYPE is a type name
   * <li> a map of values of same subtype: SUBTYPE_MAP where SUBTYPE is a type name
   * </ul>
   * @param typeName type name
   * @return the type for given type name
   * @throws IllegalArgumentException if type name is invalid
   */
  public static Type fromTypeName(String typeName) {
    if (typeName == null) {
      return null;
    }
    CanonicalType canonicalType = null;
    Type subType = null;
    int off = typeName.lastIndexOf('_');
    if (off >= 0) {
      if (off >= typeName.length() - 1) {
        throw new IllegalArgumentException("Invalide type:[" 
                          + typeName 
                          + "], expected "
                          + CanonicalType.class.getName() 
                          + " (non primitive) value after last '_'");
      }
      String typeStr = typeName.substring(off + 1);
      String subTypeStr = typeName.substring(0, off);
      canonicalType = CanonicalType.valueOf(typeStr);
      if (canonicalType.isPrimitive) {
        throw new IllegalArgumentException("Invalid type:[" 
                          + canonicalType 
                          + "] in type name[" 
                          + typeName
                          + "], should not be a primitive type when a subtype [" 
                          + subTypeStr 
                          + "] is used");
      }
      subType = fromTypeName(subTypeStr);
    } else {
      return Optional.ofNullable(CANONICAL_TYPES.get(typeName))
               .orElseThrow(() -> new IllegalArgumentException("Invalid type:" + typeName));
    }
    Type et = new Type();
    et.canonicalType = canonicalType;
    et.subType = subType;
    return et;
  }
  
  private CanonicalType canonicalType;
  
  private Type subType;
  
  /**
   * Creates a type with no canonical type and no subtype (setters should be used to set these values)
   */
  public Type() {
    this(null);
  }
  
  /**
   * Creates a type from a type name
   * 
   * @param typeName the type name
   * @see #fromTypeName(String)
   */
  public Type(String typeName) {
    if (typeName != null) {
      Type t = fromTypeName(typeName);
      this.canonicalType = t.canonicalType;
      this.subType = t.subType;
    }
  }



  /**
   * Get the canonical type of the type
   * @return the canonical type
   */
  public CanonicalType getCanonicalType() {
    return canonicalType;
  }

  /**
   * Set the canonical type of the type
   * @param type the canonical type
   * @see CanonicalType
   */
  public void setCanonicalType(CanonicalType type) {
    this.canonicalType = type;
  }

  /**
   * Get the subtype of this type:
   * 
   * <ul>
   * <li>If the type is a primitive type or an object, the subtype is null
   * <li>If the type is a list or a map, the subtype is the type of the values
   * </ul>
   * Example:
   * <ul>
   * <li> STRING: subtype is null
   * <li> OBJECT: subtype is null
   * <li> STRING_LIST: subtype is STRING
   * <li> STRING_MAP: subtype is STRING
   * <li> INT_LIST_MAP: subtype is INT_LIST
   * </ul>
   * 
   * @return the subtype of this type.
   */
  public Type getSubType() {
    return subType;
  }

  /**
   * @param subType the subtype of this type, see {@link #getSubType()}
   */
  public void setSubType(Type subType) {
    this.subType = subType;
  }
  
  /**
   * Check if the type is an object that is if leaf subtype is OBJECT
   * @return true if the type is an object
   */
  public boolean isObject() {
    return getLeafSubType(this).canonicalType == CanonicalType.OBJECT;
  }
  
  /**
   * The name of this type displayed as <code>canonical_type_name[subtype_name]</code>
   * When canonical type is null, <code>UNDEFINED_TYPE</code> String is returned.
   * @return type name
   */
  public String toString() {
    StringBuilder s = new StringBuilder();
    if (subType != null) {
      s.append(subType.toString()).append("_");
    }
    String ctype = "UNDEFINED_TYPE";
    if (canonicalType != null) {
      ctype = canonicalType.name();
    }
    return s.append(ctype).toString();
  }
  
  @Override
  public boolean equals(Object other) {
      if (this == other) {
          return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
          return false;
        }
    Type t = (Type) other;
    if (this.canonicalType != t.canonicalType) {
      return false;
    }
    return Objects.equals(this.subType, t.subType);  
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(canonicalType, subType);
  }
}
