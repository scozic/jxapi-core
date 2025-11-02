package org.jxapi.generator.java.exchange.api.pojo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.jxapi.exchange.descriptor.CanonicalType;
import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.Field;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.generator.java.exchange.constants.ConstantsGenUtil;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.DeepCloneable;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Helper methods used in generation of exchange API REST/WEBSOCKET endpoints
 * POJOs and associated JSON serializer/deserializers.
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
    Type type = ExchangeGenUtil.getFieldType(f);
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
    Type type = ExchangeGenUtil.getFieldType(f);
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
              sb.append(";").append(f.getName()).append(":").append(ExchangeGenUtil.getFieldType(f).toString());
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
  
  public static String getDefaultValueConstantName(Field field) {
    return field.getName() + "DefaultValue";
  }
  
  public static Map<String, String> generateDefaultValuesStaticFieldDeclarations(
      List<Field> fields, 
      Imports imports,
      PlaceHolderResolver docPlaceHolderResolver, 
      PlaceHolderResolver defaultValuePlaceHolderResolver,
      StringBuilder classBody) {
    Map<String, String> res = CollectionUtil.createMap();
    Map<String, Constant> constants = CollectionUtil.createMap();
    for (Field f : fields) {
      Type fieldType = ExchangeGenUtil.getFieldType(f);
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

}
