package org.jxapi.pojo.descriptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.jxapi.generator.java.pojo.PojoGenUtil;

/**
 * Canonical types for API interface fields, see {@link Type}.<br>
 * 
 * @see Type
 */
public enum CanonicalType {
  /** Plain {@link String} value.<br>TypeClass: {@link String} */ 
  STRING(String.class),
  
  /** Boolean value. TypeClass: {@link Boolean} */
  BOOLEAN(Boolean.class),
  
  /** Floating point value. TypeClass:<br>{@link BigDecimal} */
  BIGDECIMAL(BigDecimal.class),
  
  /** Integer value. TypeClass:<br>{@link Integer} */
  INT(Integer.class),
  
  /** Long value. TypeClass:<br>{@link Long} */
  LONG(Long.class),
  
  /** 
   * Nested structure (JSON block) like:<br>
   * <code>{"a":"val", "b":1}</code>
   * Such structure will contain a list of fields of a type matching one {@link Type} values.
   */
  OBJECT(false, null),
  
  /**
   * Nested JSON map with keys of type String and values of same type (which can be of any {@link Type} ).
   * <br>
   * Remark: OBJECT and MAP types are bound to similar data structure storing key/values pairs (properties) but: 
   * <ul>
   * <li>in case of OBJECT, the property keys used are defined by API interface and a POJO can be associated to that data structures with properties matching expected keys.</li>
   * <li>in case of MAP the keys used in received data are arbitrary and not known in advance.
   * <li>
   * </ul>
   * <br>TypeClass: {@link Map}
   **/
  MAP(false, Map.class),
  
  /**
   * Nested JSON array with values of same type (which can be any of {@link Type}).
   */
  LIST(false, List.class);
  
  /**
   * Flag set <code>true</code> when type stands for a primitive value e.g. not a JSON object, array or map.
   * Matching types:
   * <ul>
   * <li>{@link #INT}</li>
   * <li>{@link #LONG}</li>
   * <li>{@link #BIGDECIMAL}</li>
   * <li>{@link #STRING}</li>
   * <li>{@link #BOOLEAN}</li>
   * </ul>
   */
  public final boolean isPrimitive;
  
  /**
   * The {@link Class} holding values of this type (see {@link #isPrimitive}, or
   * <code>null</code> for {@link #OBJECT} type (in which case the associated class is custom). To guess the class associated to a
   * non-primitive type, see
   * {@link PojoGenUtil#getClassNameForType(Type, org.jxapi.generator.java.Imports, String)}
   */
  public final Class<?> typeClass;
  
  /**
   * Constructor for primitive type
   * @param typeClass
   */
  private CanonicalType(Class<?> typeClass) {
    this(true, typeClass);
  }
  
  /**
   * Constructor for non-primitive type
   * @param isPrimitive
   * @param typeClass
   */
  private CanonicalType(boolean isPrimitive, Class<?> typeClass) {
    this.isPrimitive = isPrimitive;
    this.typeClass = typeClass;
  }
}
