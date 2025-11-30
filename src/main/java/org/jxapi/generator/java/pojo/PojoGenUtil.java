package org.jxapi.generator.java.pojo;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.WebsocketTopicMatcherDescriptor;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.constants.ConstantsGenUtil;
import org.jxapi.pojo.descriptor.CanonicalType;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.DeepCloneable;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Helper methods used in generation of POJOs and associated JSON serializer/deserializers.
 */
public class PojoGenUtil {
  
  private static final String OTHER_TOEN = ", other.";
  /**
   * The hash algorithm used to generate the serial version UID hash.
     */
  public static final String SERIAL_VERSION_UID_HASH_ALGORITHM = "SHA-256";
  
  private PojoGenUtil() {}

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
    String pkg = StringUtils.substringBefore(JavaCodeGenUtil.getClassPackage(pojoClassName), ".pojo");
    return pkg + ".serializers." + JavaCodeGenUtil.getClassNameWithoutPackage(pojoClassName) + "Serializer";
  }
  
  /**
   * Generates the expected instruction to deep clone a field of a POJO.
   * @param f The field for which to generate the deep clone instruction
   * @param imports The imports to add the necessary imports to
   * @return The generated deep clone instruction
   */
  public static String generateDeepCloneFieldInstruction(Field f, Imports imports) {
    Type type = PojoGenUtil.getFieldType(f);
    String name = f.getName();
    if (type.getCanonicalType().isPrimitive) {
      return "this." + name;
    } else if (type.getCanonicalType() == CanonicalType.LIST) {
      imports.add(CollectionUtil.class);
      Type subType = type.getSubType();
      StringBuilder s = new StringBuilder()
          .append("CollectionUtil.");
      if (subType.getCanonicalType().isPrimitive) {
        return s.append("cloneList(this.")
            .append(name)
            .append(")").toString();
      } else {
        return s.append("deepCloneList(this.")
            .append(name)
            .append(", ")
            .append(generateItemClonerDeclaration(subType, 0, imports))
            .append(")").toString();
      }
    } else if (type.getCanonicalType() == CanonicalType.MAP) {
      imports.add(CollectionUtil.class);
      Type subType = type.getSubType();
      StringBuilder s = new StringBuilder()
          .append("CollectionUtil.");
      if (subType.getCanonicalType().isPrimitive) {
        return s.append("cloneMap(this.")
            .append(name)
            .append(")").toString();
      } else {
        return s.append("deepCloneMap(this.")
            .append(name)
            .append(", ")
            .append(generateItemClonerDeclaration(subType, 0, imports))
            .append(")").toString();
      }
    }
    // Object type
    return new StringBuilder("this.")
          .append(name)
          .append(" != null ? this.")
          .append(name)
          .append(".deepClone() : null").toString();
    
  }
  
  /**
   * Generates the expected instruction to compare a field of a POJO with the same field of another POJO.
   * @param f The field for which to generate the compare instruction
   * @return The generated compare instruction
   */
  public static String generateCompareFieldsInstruction(Field f) {
    String name = f.getName();
    Type type = PojoGenUtil.getFieldType(f);
    if (type.getCanonicalType().isPrimitive) {
      return "CompareUtil.compare(this." + name + OTHER_TOEN + name + ")";
    } else if (type.getCanonicalType() == CanonicalType.LIST) {
      return new StringBuilder()
          .append("CompareUtil.compareLists(this.")
          .append(name)
          .append(OTHER_TOEN)
          .append(name)
          .append(", ")
          .append(generateItemComparatorDeclaration(type.getSubType(), 0))
          .append(")")
          .toString();
    } else if (type.getCanonicalType() == CanonicalType.MAP) {
      return new StringBuilder()
          .append("CompareUtil.compareMaps(this.")
          .append(name)
          .append(OTHER_TOEN)
          .append(name)
          .append(", ")
          .append(generateItemComparatorDeclaration(type.getSubType(), 0))
          .append(")")
          .toString();
    }
    // Object type
    return "CompareUtil.compare(this." + f.getName() + OTHER_TOEN + f.getName() + ")";
  }
  
  private static String generateItemComparatorDeclaration(Type itemType, int depth) {
    if (itemType.getCanonicalType() == CanonicalType.LIST) {
      String lambdaArg1 = "l" + depth + "a";
      String lambdaArg2 = "l" + depth + "b";
      return new StringBuilder()
          .append("(")
          .append(lambdaArg1)
          .append(", ")
          .append(lambdaArg2)
          .append(") -> CompareUtil.compareLists(")
          .append(lambdaArg1)
          .append(",")
          .append(lambdaArg2)
          .append(", ")
          .append(generateItemComparatorDeclaration(itemType.getSubType(), depth + 1))
          .append(")")
          .toString();
    } else if (itemType.getCanonicalType() == CanonicalType.MAP) {
      String lambdaArg1 = "m" + depth + "a";
      String lambdaArg2 = "m" + depth + "b";
      return new StringBuilder()
          .append("(")
          .append(lambdaArg1)
          .append(", ")
          .append(lambdaArg2)
          .append(") -> CompareUtil.compareMaps(")
          .append(lambdaArg1)
          .append(",")
          .append(lambdaArg2)
          .append(", ")
          .append(generateItemComparatorDeclaration(itemType.getSubType(), depth + 1))
          .append(")")          
          .toString();
    }
    // Primitive or Object type
    return "CompareUtil::compare";
  }
  
  private static String generateItemClonerDeclaration(Type itemType, int depth, Imports imports) {
    // Remark: itemType is not a primitive
    if (itemType.getCanonicalType() == CanonicalType.LIST) {
      Type subType = itemType.getSubType();
      imports.add(CollectionUtil.class);
      if (subType.getCanonicalType().isPrimitive) {
        return "CollectionUtil::cloneList";
      } else {
        String lambdaArg = "l" + depth;
        return new StringBuilder()
            .append(lambdaArg)
            .append(" -> CollectionUtil.deepCloneList(")
            .append(lambdaArg)
            .append(", ")
            .append(generateItemClonerDeclaration(subType, depth + 1, imports))
            .append(")")
            .toString();
      }
    } else if (itemType.getCanonicalType() == CanonicalType.MAP) {
      Type subType = itemType.getSubType();
      imports.add(CollectionUtil.class);
      if (subType.getCanonicalType().isPrimitive) {
        return "CollectionUtil::cloneMap";
      } else {
        String lambdaArg = "m" + depth;
        return new StringBuilder()
            .append(lambdaArg)
            .append(" -> CollectionUtil.deepCloneMap(")
            .append(lambdaArg)
            .append(", ")
            .append(generateItemClonerDeclaration(subType, depth + 1, imports))
            .append(")").toString();
      }
    }
    // Object type
    imports.add(DeepCloneable.class);
    return "DeepCloneable::deepClone";
  }
  
  /**
   * Generates a serial version UID for a class using a hash based on its name, fields and implemented interfaces.
   * @param className The name of the class for which to generate the serial version UID
   * @param fields The fields of the class for which to generate the serial version UID
   * @param implementedInterfaces The implemented interfaces of the class for which to generate the serial version UID
   * @return The generated serial version UID
   * @throws IllegalStateException If the hash algorithm (SHA-256) is not supported
   */
  public static long generateSerialVersionUid(String className, List<Field> fields, List<String> implementedInterfaces) {
      try {
          MessageDigest md = MessageDigest.getInstance(SERIAL_VERSION_UID_HASH_ALGORITHM);
          StringBuilder sb = new StringBuilder().append(className);
          for (Field f : fields) {
              sb.append(";").append(f.getName()).append(":").append(PojoGenUtil.getFieldType(f).toString());
          }
          for (String i : implementedInterfaces) {
              sb.append(";").append(i);
          }
          byte[] hash = md.digest(sb.toString().getBytes());
          long serialVersionUID = 0;
          for (int i = 0; i < Math.min(8, hash.length); i++) {
              serialVersionUID = (serialVersionUID << 8) | (hash[i] & 0xFF);
          }
          return serialVersionUID;
      } catch (NoSuchAlgorithmException e) {
          throw new IllegalStateException("Unsupported hash algorithm '" + SERIAL_VERSION_UID_HASH_ALGORITHM + "'", e);
      }
  }
  
  /**
   * Generates the name of the constant representing the default value of a field.
   * 
   * @param field The field for which to generate the default value constant name
   * @return The generated default value constant name
   */
  public static String getDefaultValueConstantName(Field field) {
    return field.getName() + "DefaultValue";
  }
  
  /**
   * Generates static field declarations for default values of fields.
   * 
   * @param fields                          The fields to generate default value
   *                                        static field declarations for
   * @param imports                         The imports to add the necessary
   *                                        imports to
   * @param docPlaceHolderResolver          The placeholder resolver for
   *                                        documentation
   * @param defaultValuePlaceHolderResolver The placeholder resolver for default
   *                                        values
   * @param classBody                       The class body to append the generated
   *                                        static field declarations to
   * @return A map of field names to their corresponding default value constant
   *         names
   */
  public static Map<String, String> generateDefaultValuesStaticFieldDeclarations(
      List<Field> fields, 
      Imports imports,
      PlaceHolderResolver docPlaceHolderResolver, 
      PlaceHolderResolver defaultValuePlaceHolderResolver,
      StringBuilder classBody) {
    Map<String, String> res = CollectionUtil.createMap();
    Map<String, Constant> constants = CollectionUtil.createMap();
    for (Field f : fields) {
      Type fieldType = PojoGenUtil.getFieldType(f);
      if (f.getDefaultValue() != null) {
        if (fieldType.isObject()) {
          throw new IllegalArgumentException(
              "Field " + f.getName() + " is of object type, cannot carry a default value");
        }
        Constant c = Constant.create(
            PojoGenUtil.getDefaultValueConstantName(f),
            fieldType,
           "Default value for <code>" + f.getName() + "</code>",
           f.getDefaultValue()
         );
        constants.put(f.getName(), c);
      }
    }
    
    List<Constant> allConstants = new ArrayList<>(constants.values());
    constants.entrySet().forEach(e -> {
      Constant c = e.getValue();
      res.put(e.getKey(), ConstantsGenUtil.getConstantVariableName(c, allConstants));
      if (classBody != null) {
        classBody.append("\n")
        .append(ConstantsGenUtil.generateConstantDeclaration(
          c, 
          allConstants,
          imports, 
          docPlaceHolderResolver, 
          defaultValuePlaceHolderResolver));
      }
    });
    return res;     
  }

  /**
   * Find the type of a field in context of REST/Websocket API code generation: If
   * field type is specified, returns it, otherwise, if field properties or objectName are specified,
   * returns {@link Type#OBJECT}, otherwise returns {@link Type#STRING}.
   * 
   * @param field The field to retrieve type of in context of REST/Websocket API
   *              code generation
   * @return <code>null</code> if field is <code>null</code>, the field type if it is specified, or 
   */
  public static Type getFieldType(Field field) {
    if (field == null) {
      return null;
    }
    Type  type = field.getType();
    if (type != null) {
      return type;
    }
    if (field.getProperties() != null || field.getObjectName() != null) {
      return Type.OBJECT;
    }
    return Type.STRING;
  }

  /**
   * @param field The field to check if its type is an object type
   * @return <code>true</code> if the field is not <code>null</code> and its type is an object type,
   *         <code>false</code> otherwise
   * @see getFieldType  
   * @see Type#isObject()      
   */
  public static boolean isObjectField(Field field) {
    if (field == null) {
      return false;
    }
    return getFieldType(field).isObject();
  }

  /**
   * @param type            the type of field (see {@link Field}) to get type class name
   *                        of.
   * @param imports         The imports of generator context that will be
   *                        populated with classes used by returned type. That set
   *                        must be not <code>null</code> and mutable.
   * @param objectClassName the object (full) class name of leaf object type if <code>type</code> is of object type
   * @return The simple class name associated to given <code>type</code>:
   *        <ul> 
   *          <li>if this type is a 'Primitive' type, returns the simple class name of the corresponding Java class:
   *            <ul>
   *              <li>BIGDECIMAL: {@link BigDecimal}</li>
   *              <li>BOOLEAN: {@link Boolean}</li>
   *              <li>INT: {@link Integer}</li>
   *              <li>LONG: {@link Long}</li>
   *              <li>STRING: {@link String}</li>
   *            </ul>
   *          <li>If this type is a 'List' type, returns generic <code>List&lt;SubTypeClassName&gt;</code>, 
   *         where <code>subTypeClassName</code> is the simple class name of the subType of the list, see {@link Type#getSubType()}.
   *          <li>If this type is a 'Map' type, returns generic <code>Map&lt;String, SubTypeClassName&gt;</code>, 
   *         where <code>subTypeClassName</code> is the simple class name of the subType of the map, see {@link Type#getSubType()}.
   *          <li>If this type is an 'Object' type, returns the simple class name of the object class name, 
   *          see {@link JavaCodeGenUtil#getClassNameWithoutPackage(String)}.
   *          </li>
   *        </ul>
   * @see #getClassNameForType(Type, Imports, String)
   * @throws IllegalArgumentException if the type is not recognized.
   */
  public static String getClassNameForType(Type type, 
                         Imports imports, 
                       String objectClassName) {
    if (type == null) {
      return null;
    }
    String subTypeClassName = null;
    CanonicalType canonicalType = type.getCanonicalType();
    Class<?> canonicalTypeClass = canonicalType.typeClass;
    switch(canonicalType) {
    case BIGDECIMAL:
      if (imports != null) {
        imports.add(BigDecimal.class.getName());
      }
      return canonicalTypeClass.getSimpleName();
    case BOOLEAN:
    case INT:
    case LONG:
    case STRING:
      return canonicalTypeClass.getSimpleName();
    case LIST:
      subTypeClassName = getClassNameForType(type.getSubType(), imports, objectClassName);
      if (imports != null) {
        imports.add(canonicalTypeClass.getName());
      }
      return canonicalTypeClass.getSimpleName() 
          + "<" 
          + JavaCodeGenUtil.getClassNameWithoutPackage(subTypeClassName) 
          + ">";
    case MAP:
      subTypeClassName = getClassNameForType(type.getSubType(), imports, objectClassName);
      if (imports != null) {
        imports.add(canonicalTypeClass.getName());
      }
      return canonicalTypeClass.getSimpleName() 
          + "<String, " 
          + JavaCodeGenUtil.getClassNameWithoutPackage(subTypeClassName) 
          + ">";
    case OBJECT:
      if (imports != null) {
        imports.add(objectClassName);
      }
      return JavaCodeGenUtil.getClassNameWithoutPackage(objectClassName);
    default:
      throw new IllegalArgumentException("Unexpected type for:" + type);
    }
  }

  /**
   * Generates expected class name for an {@link Field} instance
   * according to its type (see
   * {@link Field#getType()} .
   * <ul>
   * <li>For a primitive (see {@link CanonicalType#isPrimitive}
   * field type, the corresponding primitive type class is returned:
   * <code>java.lang.String</code>, <code>java.lang.Integer</code> ...)</li>
   * <li>For an object (see {@link Type#OBJECT}
   * field type), if the <code>objectName</code> (see
   * {@link Field#getObjectName()} is defined, that object name is
   * returned.</li>
   * <li>For list or map (see {@link CanonicalType#LIST},
   * {@link CanonicalType#MAP}), the corresponding generic class
   * is returned e.g. <code>java.util.List</code> or <code>java.util.Map</code>,
   * with generic parameter set with the class for subtype (see
   * {@link Type#getSubType()} of <code>field</code>
   * as returned by this method.</li>
   * </ul>
   * The returned value is the class simple name (without package prefix). The
   * class full name is added to <code>imports</code> set passed in parameter. If
   * type is a list or map, the generic parameter {@link List} or {@link Map} is
   * also added to imports.
   * <p>
   * A few examples of returned values and imports added for some examples of a
   * field named <code>Bar</code>:
   * <table border="1">
   * <caption>Examples of returned values and imports added</caption>
   * <thead>
   * <tr>
   * <th>Type</th>
   * <th>Enclosing class name</th>
   * <th>objectName</th>
   * <th>Returned value</th>
   * <th>Imports added</th>
   * <th>Notes</th>
   * </tr>
   * </thead> <tbody>
   * <tr>
   * <td><code>STRING</code></td>
   * <td><i>any</i></td>
   * <td><i>any</i></td>
   * <td><code>String</code></td>
   * <td><i>none</i></td>
   * <td>Primitive field type, and {@link String} is in <code>java.lang</code>
   * package, no import added.</td>
   * </tr>
   * 
   * <tr>
   * <td><code>BIGDECIMAL</code></td>
   * <td><i>any</i></td>
   * <td><i>any</i></td>
   * <td><code>BigDecimal</code></td>
   * <td><ul><li><code>java.lang.BigDecimal</code></li>
   * </ul>
   * </td>
   * <td>Primitive field type, and {@link BigDecimal} is in
   * <code>java.math</code> package, <code>java.lang.BigDecimal</code> import
   * added.</td>
   * </tr>
   * 
   * <tr>
   * <td><code>OBJECT</code></td>
   * <td><code>com.x.y.gen.pojo.Foo</code></td>
   * <td><code>null</code></td>
   * <td><code>FooBar</code></td>
   * <td>
   * <ul>
   * <li><code>com.x.y.gen.pojo.FooBar</code></li>
   * </ul>
   * </td>
   * <td><code>OBJECT</code> field type and no object name specified, the returned
   * type is generated name by concatenating enclosing class name with field
   * name</td>
   * </tr>
   * 
   * <tr>
   * <td><code>OBJECT</code></td>
   * <td><code>com.x.y.gen.pojo.Foo</code></td>
   * <td><code>MySpecialObjectName</code></td>
   * <td><code>MySpecialObjectName</code></td>
   * <td><ul><li><code>com.x.y.gen.pojo.MySpecialObjectName</code>
   * </ul>
   * </td>
   * <td><code>OBJECT</code> field with <code>objectName</code> specified, the
   * returned type is <code>objectName</code> as class name, assumed to be in same
   * package as enclosing class name package.</td>
   * </tr>
   * 
   * <tr>
   * <td><code>OBJECT_MAP_LIST</code></td>
   * <td><code>com.x.y.gen.pojo.Foo</code></td>
   * <td><code>null</code></td>
   * <td><code>List&lt;Map&lt;String, FooBar&gt;&gt;</code></td>
   * <td>
   * <ul>
   * <li><code>java.util.List</code></li>
   * <li><code>java.util.Map</code></li>
   * <li><code>com.x.y.gen.pojo.FooBar</code></li>
   * </ul>
   * </td>
   * <td><code>OBJECT</code> field nested in list of map with no
   * <code>objectName</code> specified, the returned type is
   * <code>List&lt;Map&lt;String, FooBar&gt;&gt;</code> and {@link Map},
   * {@link List} and class for object field are added to imports.</td>
   * </tr>
   * </tbody>
   * </table>
   * 
   * @param field  The field
   * @param imports            The imports of generator context that will be
   *                           populated with classes used by returned type. That
   *                           set must be not <code>null</code> and mutable.
   * @param enclosingClassName The POJO class containing the endpoint to generate
   *                           class name for type of.
   * @return The simple class name for type of <code>field</code>
   *         parameter.
   */
  public static String getClassNameForField(Field field, 
                        Imports imports, 
                        String enclosingClassName) {
    Type fieldType = getFieldType(field);
    String objectClassName = null;
    if (fieldType.isObject()) {
       objectClassName = PojoGenUtil.getFieldObjectClassName(field, enclosingClassName);
       
    }
    return getClassNameForType(
          fieldType,
          imports, 
          objectClassName);
  }

  /**
   * Generates the expected full class name for the object described by an
   * {@link Field}, assuming it is of 'object' type (see
   * {@link Type#isObject()}).<br>
   * The returned class package will be one of
   * <code>enclosingClassName</code>.<br>
   * 
   * @param field  The {@link Type#OBJECT} type field to get object class name for 
   * @param enclosingClassName The POJO class containing the endpoint to generate
   *                           class name for type of.
   * @return field object name with first letter to uppercase if
   *         {@link Field#getObjectName()} is not <code>null</code>,
   *         else concatenation of <code>&lt;enclosingClassName&gt;+&lt;Field name&gt;</code>
   */
  public static String getFieldObjectClassName(Field field, 
                         String enclosingClassName) {
    String objectName = field.getObjectName();
    if (objectName != null) {
       if (JavaCodeGenUtil.isFullClassName(objectName)) {
         return objectName;
       }
       return String.format("%s.%s", 
           JavaCodeGenUtil.getClassPackage(enclosingClassName), 
           JavaCodeGenUtil.firstLetterToUpperCase(field.getObjectName()));
     } else {
       return enclosingClassName + JavaCodeGenUtil.firstLetterToUpperCase(field.getName());
     }
  }

  /**
   * Generates the expected full class name for the leaf subType of an endpoint.
   * <ul>
   * <li>If the leaf subType canonical type is a primitive type (see
   * {@link CanonicalType#isPrimitive}), the corresponding primitive
   * type class, see {@link CanonicalType#typeClass } is returned.</li>
   * <li>If the supplied <code>objectName</code> is not <code>null</code>, the
   * class name is the object name with first letter to
   * uppercase, and its package is the package of the enclosing class name.</li>
   * <li>Otherwise, the class name is the concatenation of the package of the
   * enclosing class name and the class name generated
   * by
   * {@link getClassNameForType}
   * for the
   * leaf subType of the field type.</li>
   * </ul>
   * {@link Type#getLeafSubType(Type)}.
   * 
   * @param fieldName       See {@link Field#getName()}
   * @param type                  See {@link Field#getType()}
   * @param objectName            See {@link Field#getObjectName()}
   * @param enclosingClassName    The POJO class containing the endpoint to
   *                              generate class name for type of.
   * @return the full class name of leaf subType of field type.
   */
  public static String getFieldLeafSubTypeClassName(
      String fieldName, 
      Type type, 
      String objectName,  
      String enclosingClassName) {
    Type leafSubType = Type.getLeafSubType(type);
    if (leafSubType.getCanonicalType().isPrimitive) {
      return leafSubType.getCanonicalType().typeClass.getName();
    }
    String pkg = JavaCodeGenUtil.getClassPackage(enclosingClassName) + ".";
    if (objectName != null) {
      return pkg + objectName;
    }
    
    return pkg + getClassNameForType(
          leafSubType,
          new Imports(),
          enclosingClassName) 
        + JavaCodeGenUtil.firstLetterToUpperCase(fieldName);
  }

  /**
   * Finds the properties of an object name in a field: return properties (see
   * {@link Field#getProperties()}) found in provided field if it has the same
   * object name as the one provided, or properties of first field found in its
   * sub-properties that carries expected objectName recursively.
   * 
   * @param objectName The object name to find properties for.
   * @param param      The field to search for properties of the object name.
   * @return The list of properties of the object name in the field or <code>null</code> if properties could not be resolved.
   */
  public static List<Field> findPropertiesForObjectNameInField(String objectName, Field param) {
    if (objectName == null) {
      return null;
    }
    if (param == null) {
      return null;
    }
    List<Field> res = param.getProperties();
    if (res == null) {
      return null;
    }
    if (Objects.equals(objectName, param.getObjectName())) {
      return res;
    }
    for (Field p: res) {
      res = findPropertiesForObjectNameInField(objectName, p);
      if (res != null) {
        return res;
      }
    }
    return null;
  }

  /**
   * Finds actual field name from a {@link Field} or one of its child fields. <br>
   * If provided field name matches name of provided field, then its msgFieldName
   * is returned (see {@link Field#getMsgField()}) or the provided name if
   * msgFieldName is <code>null</code>.<br>
   * Otherwise, if provided field is of 'object' type (see
   * {@link Type#isObject()}) then its child fields (see
   * {@link Field#getProperties()}) are searched. <br>
   * This is intented for determining actual field name in websocket message, from
   * field name provided in
   * {@link WebsocketTopicMatcherDescriptor#getFieldName()}.
   * 
   * @param name The field name provided in
   *             {@link WebsocketTopicMatcherDescriptor#getFieldName()}. 
   *             Cannot be <code>null</code>
   * @param msg  The websocket stream data type
   * @return The msgFieldName (see {@link Field#getMsgField()}) of provided field
   *         or first of its child fields (see {@link Field#getProperties()} with
   *         name see {@link Field#getName()} matching that field. If no match, or
   *         <code>msg</code> is <code>null</code>, <code>null</code> is returned.
   * @throws IllegalArgumentException if <code>name</code> is <code>null</code>
   */
  public static String getMsgFieldName(String name, Field msg) {
    if (name == null) {
      throw new IllegalArgumentException("null name");
    }
    if (msg == null) {
      return null;
    }
    
    if (Objects.equals(msg.getName(), name)) {
      return Optional.ofNullable(msg.getMsgField()).orElse(name);
    } else if (getFieldType(msg).isObject()) {
      for (Field c: msg.getProperties()) {
        String res = getMsgFieldName(name, c);
        if (res != null) {
          return res;
        }
      }
    }
    return null;
  }

}
