package org.jxapi.generator.java.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.generator.java.exchange.ConstantValuePlaceholderResolverFactory;
import org.jxapi.pojo.descriptor.CanonicalType;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.DeepCloneable;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.PlaceHolderResolver;
import org.jxapi.util.Pojo;

/**
 * Generates a POJO class with a builder for the given fields.
 * <p>
 * The generated class will implement {@link Pojo} interface with expected
 * methods from the interface:
 * <ul>
 * <li>{@link DeepCloneable#deepClone()} - deep clone of the object
 * <li>{@link Comparable#compareTo(Object)} - comparison of two objects
 * </ul>
 * Private static final long serialVersionUID field will be generated for
 * serialization, using a hash that is generated from the class name,
 * implemented interfaces and properties.
 * This is enough to enforce Serializable interface, default object
 * serialization and deserialization will work since all properties are
 * serializable.
 * <p>
 * The generated class will also override the following methods:
 * <ul>
 * <li>{@link Object#hashCode()} - hash code of the object based on its
 * properties
 * <li>{@link Object#equals(Object)} - equality of two objects based on their
 * properties
 * <li>{@link Object#toString()} - To provide JSON representation of the object
 * instead of the default toString
 * </ul>
 * This way, equals, hashCode and compareTo methods are consistent.
 * Remark: There is no need to override default clone method, as all properties
 * are immutable or serializable (collections, other POJOs).
 * <p>
 * Other interfaces can be added to the class declaration, see
 * {@link #setImplementedInterfaces(List)}
 * <p>
 * Every property will be generated as a private field with public accessors
 * (getters and setters).
 * <p>
 * Generated class will carry @JsonSerialize annotation pointing to a custom serializer
 * class that will serialize the object to JSON. That serializer class is
 * expected to be generated too, in child package 'serializer' of parent package
 * of the generated class.
 * <p>
 * The generated class also exposes a static inner class 'Builder' that can be
 * used to build instances of the POJO. Static method 'builder()' is generated
 * to return a new instance of the builder.
 * 
 * @see Pojo
 */
public class PojoGenerator extends JavaTypeGenerator {

  private static final String PUBLIC_BUILDER_TOKEN = "public Builder ";
  private static final String END_BLOCK_TOKEN = "\n}\n";
  private static final String RETURN_THIS_TOKEN = "return this;\n";
  private static final String THIS_TOKEN = "this.";
  private static final String END_NO_PARAM_METHOD_INST_TOKEN = "();\n";
  
  private final List<Field> fields = new ArrayList<>();
  private final PlaceHolderResolver docPlaceHolderResolver;
  private PlaceHolderResolver defaultValuePlaceHolderResolver;
  
  
  
  /**
   * Constructor.
   * @param className The full name of the class to generate source code for
   * @param description The description to display in javadoc of the class
   * @param fields The properties of the class
   * @param implementedInterfaces The interfaces implemented by the class (in addition to {@link Pojo})
   * @param docPlaceHolderResolver PlaceHolderResolver to resolve placeholders in descriptions.
   * @param constantValuePlaceHolderResolverFactory Factory to create PlaceHolderResolver
   */
  public PojoGenerator(String className, 
       String description, 
       List<Field> fields, 
       List<String> implementedInterfaces,
       PlaceHolderResolver docPlaceHolderResolver,
       ConstantValuePlaceholderResolverFactory constantValuePlaceHolderResolverFactory) {
    super(className);
    this.docPlaceHolderResolver = Optional.ofNullable(docPlaceHolderResolver).orElse(PlaceHolderResolver.NO_OP);
    this.defaultValuePlaceHolderResolver = constantValuePlaceHolderResolverFactory != null
        ? constantValuePlaceHolderResolverFactory.createConstantValuePlaceholderResolver(getImports())
        : JavaCodeGenUtil::getQuotedString;
    this.fields.addAll(Optional.ofNullable(fields).orElse(List.of()));
    String serializerClassName = PojoGenUtil.getSerializerClassName(className);
    addImport(serializerClassName);
    
    setTypeDeclaration(generateTypeDeclaration());
    setDescription(this.docPlaceHolderResolver.resolve(description));
    addImport(Pojo.class.getName());
    String pojoInterface = Pojo.class.getName() + "<" + getSimpleName() + ">";
    setImplementedInterfaces(CollectionUtil.mergeLists(List.of(pojoInterface), implementedInterfaces));
  }
  
  private String generateTypeDeclaration() {
    String serializerClassName = PojoGenUtil.getSerializerClassName(getName());
    String deserializerClassName = PojoGenUtil.getJsonMessageDeserializerClassName(getName());
    addImport(serializerClassName);
    addImport(deserializerClassName);
    addImport(com.fasterxml.jackson.databind.annotation.JsonSerialize.class.getName());
    addImport(com.fasterxml.jackson.databind.annotation.JsonDeserialize.class.getName());
    return new StringBuilder()
        .append("@JsonSerialize(using = ")
        .append(JavaCodeGenUtil.getClassNameWithoutPackage(serializerClassName))
        .append(".class)\n")
        .append("@JsonDeserialize(using = ")
        .append(JavaCodeGenUtil.getClassNameWithoutPackage(deserializerClassName))
        .append(".class)\n")
        .append("public class").toString();
  }
  
  /**
   * Adds a field to the POJO. If field type is not a primitive or java.lang.*
   * type, it will be added to imports.
   * 
   * @param field Field to add
   */
  public void addField(Field field) {
    fields.add(field);
  }

  /**
   * @return Array of fields in the order they were added
   */
  public List<Field> getFields() {
    return fields;
  }
  
  @Override
  public String generate() {
    appendToBody("\n");
    appendToBody(generateSerialVersionUidDeclaration());
    appendToBody("\n");
    appendMethod("public static Builder builder()", 
           "return new Builder();" , 
           "@return A new builder to build {@link " + getSimpleName() + "} objects");
    Map<String, String> defaultValueStaticVariables = PojoGenUtil.generateDefaultValuesStaticFieldDeclarations(
        this.fields,
        getImports(),
        docPlaceHolderResolver,
        defaultValuePlaceHolderResolver,
        this.body
        ); 
    appendToBody("\n");
    appendToBody(generateAllFieldsDeclaration(defaultValueStaticVariables));
    appendToBody("\n");
    this.fields.forEach(f -> {
      appendToBody("\n");
        generateAccessorsDeclaration(f);    
    });
    appendToBody("\n");
    generateEqualsMethod();
    appendToBody("\n");
    generateCompareToMethod();
    appendToBody("\n");
    generateHashCodeMethod();
    appendToBody("\n");
    generateDeepCloneMethod();
    appendToBody("\n");
    generateToStringMethod();
    appendToBody("\n");
    generateBuilderClass(defaultValueStaticVariables);
    return super.generate();
  }

  private String generateSerialVersionUidDeclaration() {
    return "private static final long serialVersionUID = " 
        + PojoGenUtil.generateSerialVersionUid(getName(), fields, getImplementedInterfaces()) 
        + "L;\n";
  }
  
  private void generateCompareToMethod() {
    StringBuilder compareBody = new StringBuilder()
        .append("if (other == null) {\n")
        .append(JavaCodeGenUtil.indent("return 1;"))
        .append(END_BLOCK_TOKEN)
        .append("if (this == other) {\n")
        .append(JavaCodeGenUtil.indent("return 0;"))
        .append(END_BLOCK_TOKEN);
    if (CollectionUtil.isEmpty(this.fields)) {
      compareBody.append("return 0;\n");
    } else {
      compareBody.append("int res = 0;\n");
      for (int i = 0; i < fields.size(); i++) {
        addImport(CompareUtil.class);
        String ret = "return res;";
        compareBody.append("res = ")
          .append(PojoGenUtil.generateCompareFieldsInstruction(fields.get(i)))
          .append(";\n");
        if (i < fields.size() - 1) {
          compareBody.append("if (res != 0) {\n")
            .append(JavaCodeGenUtil.indent(ret))
            .append(END_BLOCK_TOKEN);
        } else {
          compareBody.append(ret)
            .append("\n");
        }
      }
    }
      
    appendMethod("@Override\npublic int compareTo(" + getSimpleName() + " other)", compareBody.toString());
    
  }

  private void generateBuilderClass(Map<String, String> defaultValueStaticVariables) {
    JavaTypeGenerator builder = new JavaTypeGenerator("Builder");
    builder.setGeneratePackageAndImports(false);
    String name = getSimpleName();
    builder.setDescription("Builder for {@link " + name + "}");
    builder.setTypeDeclaration("public static class");
    builder.appendToBody("\n");
    builder.appendToBody(generateAllFieldsDeclaration(defaultValueStaticVariables));
    builder.appendToBody("\n");
    fields.forEach(f -> generateBuilderMethodsDeclaration(f, builder));
    builder.appendToBody("\n");
    
    StringBuilder buildMethodBody = new StringBuilder();
    if (!CollectionUtil.isEmpty(fields)) {
      buildMethodBody.append(name)
        .append(" res = new ")
        .append(name)
        .append(END_NO_PARAM_METHOD_INST_TOKEN);
        fields.forEach(f -> buildMethodBody
        .append("res.")
        .append(f.getName())
        .append(" = ")
        .append(PojoGenUtil.generateDeepCloneFieldInstruction(f, getImports()))
        .append(";\n"));
      buildMethodBody.append("return res;\n");
    } else {
      buildMethodBody
        .append("return new ")
        .append(name).append(END_NO_PARAM_METHOD_INST_TOKEN);
    }
    
    builder.appendMethod(
        "public " + name + " build()", 
        buildMethodBody.toString(), 
        "@return a new instance of " + name + " using the values set in this builder");
    appendToBody(builder.generate());
  }
  
  private String generateAllFieldsDeclaration(Map<String, String> defaultValueStaticVariables) {
    return fields.stream()
        .map(f -> this.generateFieldDeclaration(f, defaultValueStaticVariables.get(f.getName())))
        .collect(Collectors.joining("\n"));
  }
  
  private String generateFieldDeclaration(Field field, String defaultValueStaticVariable) {
    String typeClass = getFieldClass(field);
    typeClass = JavaCodeGenUtil.getClassNameWithoutPackage(typeClass);
    StringBuilder sb = new StringBuilder()
       .append("private ")
       .append(typeClass)
       .append(" ")
       .append(field.getName());
    if (defaultValueStaticVariable != null) {
      sb.append(" = ").append(defaultValueStaticVariable);
    }
    return sb.append(";").toString();
  }
  
  private void generateAccessorsDeclaration(Field field) {
    
    String typeClass = getFieldClass(field);
    typeClass = JavaCodeGenUtil.getClassNameWithoutPackage(typeClass);
    String name = field.getName();
    String description = docPlaceHolderResolver.resolve(field.getDescription());
    String msgFieldDescription = "";
    if (field.getMsgField() != null) {
      msgFieldDescription = "<br>Message field <strong>" + field.getMsgField() + "</strong>";
      if (description == null) {
        description = msgFieldDescription;
      } else {
        description += " " + msgFieldDescription;
      }
    }
    
    String getJavadoc = null;
    String setJavadoc = null;
    if (description != null) {
      getJavadoc = "@return " + description;
      setJavadoc = "@param " + name + " " + description;
    }

    String getSignature = new StringBuilder()
      .append("public ")
      .append(typeClass)
      .append(" ")
      .append(JavaCodeGenUtil.getGetAccessorMethodName(
          field.getName(), 
          PojoGenUtil.getFieldType(field), 
          getAllFieldNames()))
      .append("()")
      .toString();
    
    appendMethod(getSignature, "return " + name + ";\n", getJavadoc);
    appendToBody("\n");
    
    String setMethodBody = new StringBuilder()
        .append(THIS_TOKEN)
        .append(name)
        .append(" = ")
        .append(name)
        .append(";\n")
        .toString();
    String argDeclaration = new StringBuilder()
        .append("(")
        .append(typeClass)
        .append(" ")
        .append(name)
        .append(")").toString();
    
    String setSignature = new StringBuilder()
      .append("public void ")
      .append(JavaCodeGenUtil.getSetAccessorMethodName(field.getName(), getAllFieldNames()))
      .append(argDeclaration)
      .toString();
    
    appendMethod(setSignature, setMethodBody, setJavadoc);
  }
  
  private void generateBuilderMethodsDeclaration(Field field, JavaTypeGenerator builder) {
    Type fieldType = PojoGenUtil.getFieldType(field);
    String typeClass = getFieldClass(field);
    typeClass = JavaCodeGenUtil.getClassNameWithoutPackage(typeClass);
    String name = field.getName();
    String description = docPlaceHolderResolver.resolve(field.getDescription());
    String msgFieldDescription = "";
    if (field.getMsgField() != null) {
      msgFieldDescription = "Message field <strong>" + field.getMsgField() + "</strong>";
      if (description == null) {
        description = msgFieldDescription;
      } else {
        description += " " + msgFieldDescription;
      }
    }

    String setMethodBody = new StringBuilder()
        .append(THIS_TOKEN)
        .append(name)
        .append(" = ")
        .append(name)
        .append(";\n")
        .append(RETURN_THIS_TOKEN)
        .toString();
    
    String argDeclaration = new StringBuilder()
        .append("(")
        .append(typeClass)
        .append(" ")
        .append(name)
        .append(") ").toString();
    
    StringBuilder javadoc = new StringBuilder()
        .append("Will set the value of <code>")
        .append(name)
        .append("</code> field in the builder");
    if (description != null) {
      javadoc.append("\n@param ")
         .append(name).append(" ")
         .append(description);
    }
    
        
    javadoc.append("\n@return Builder instance")
           .append("\n@see #")
         .append(JavaCodeGenUtil.getSetAccessorMethodName(field.getName(), getAllFieldNames()))
         .append("(")
         .append(typeClass)
         .append(")");
    
    String signature = new StringBuilder()
                .append(PUBLIC_BUILDER_TOKEN)
                .append(name)
                .append(argDeclaration)
                .toString();
    
    builder.appendToBody("\n");
    builder.appendMethod(signature, setMethodBody, javadoc.toString());
    if (fieldType.getCanonicalType() == CanonicalType.LIST) {
      builder.appendToBody("\n");
      generateAddToListBuilderMethod(field, builder);
    } else if (fieldType.getCanonicalType() == CanonicalType.MAP) {
      builder.appendToBody("\n");
      generateAddToMapBuilderMethod(field, builder);
    }
  }
  
  private void generateAddToListBuilderMethod(Field field, JavaTypeGenerator builder) {
    String name = field.getName();
    Type fieldType = PojoGenUtil.getFieldType(field);
    Field itemField = Field.builder()
                  .name(name)
                  .type(fieldType.getSubType())
                  .objectName(field.getObjectName())
                  .properties(field.getProperties())
                  .build();
    String itemTypeClass = getFieldClass(itemField);
    itemTypeClass = JavaCodeGenUtil.getClassNameWithoutPackage(itemTypeClass);
    
    String signature = new StringBuilder()
        .append(PUBLIC_BUILDER_TOKEN)
        .append(JavaCodeGenUtil.getAccessorMethodName("addTo", name, getAllFieldNames()))
        .append("(").append(itemTypeClass)
        .append(" item)").toString();
    
    String javadoc = new StringBuilder("Will add an item to the <code>")
        .append(name)
        .append("</code> list.\n")
        .append("@param item Item to add to current <code>")
        .append(name)
        .append("</code> list\n")
        .append("@return Builder instance\n")
        .append("@see ")
        .append(getSimpleName())
        .append("#")
        .append(JavaCodeGenUtil.getSetAccessorMethodName(name, getAllFieldNames()))
        .append("(").append(List.class.getSimpleName())
        .append(")").toString();
    addImport(CollectionUtil.class);
    String methodBody = new StringBuilder()
        .append("if (this.")
        .append(name)
        .append(" == null) {\n")
        .append(JavaCodeGenUtil.indent(THIS_TOKEN + name + " = CollectionUtil.createList();"))
        .append(END_BLOCK_TOKEN)
        .append(THIS_TOKEN)
        .append(name)
        .append(".add(item);\n")
        .append(RETURN_THIS_TOKEN)
        .toString();
    
    builder.appendToBody("\n");
    builder.appendMethod(signature, methodBody, javadoc);

  }
  
  private void generateAddToMapBuilderMethod(Field field, JavaTypeGenerator builder) {
    String name = field.getName();
    Type fieldType = PojoGenUtil.getFieldType(field);
    Field itemField = Field.builder()
                  .name(name)
                  .type(fieldType.getSubType())
                  .objectName(field.getObjectName())
                  .properties(field.getProperties())
                  .build();
    String itemTypeClass = getFieldClass(itemField);
    itemTypeClass = JavaCodeGenUtil.getClassNameWithoutPackage(itemTypeClass);
    
    String signature = new StringBuilder()
        .append(PUBLIC_BUILDER_TOKEN)
        .append(JavaCodeGenUtil.getAccessorMethodName("addTo", name, getAllFieldNames()))
        .append("(")
        .append("String key, ")
        .append(itemTypeClass)
        .append(" item)").toString();
    
    String javadoc = new StringBuilder("Will add or update a key/value pair to the <code>")
        .append(name)
        .append("</code> map.\n")
        .append("@param item Item to add to current <code>")
        .append(name)
        .append("</code> list\n")
        .append("@return Builder instance\n")
        .append("@see ")
        .append(getSimpleName())
        .append("#")
        .append(JavaCodeGenUtil.getSetAccessorMethodName(name, getAllFieldNames()))
        .append("(").append(Map.class.getSimpleName())
        .append(")").toString();
    
    addImport(CollectionUtil.class);
    String methodBody = new StringBuilder()
        .append("if (this.")
        .append(name)
        .append(" == null) {\n")
        .append(JavaCodeGenUtil.indent(THIS_TOKEN + name + " = CollectionUtil.createMap();"))
        .append(END_BLOCK_TOKEN)
        .append(THIS_TOKEN)
        .append(name)
        .append(".put(key, item);\n")
        .append(RETURN_THIS_TOKEN)
        .toString();
    
    builder.appendToBody("\n");
    builder.appendMethod(signature, methodBody, javadoc);
  }
  
  private List<String> getAllFieldNames() {
        return fields.stream().map(Field::getName).toList();
  }
  
  private String getFieldClass(Field field) {
    String fieldClass = null;
    Type fieldType = PojoGenUtil.getFieldType(field);
    if (PojoGenUtil.isObjectField(field)) {
      String className = getName();
      fieldClass = PojoGenUtil.getClassNameForField(field, getImports(), className);
    } else {
      fieldClass = PojoGenUtil.getClassNameForType(fieldType, getImports(), null);
    }
    return JavaCodeGenUtil.getClassNameWithoutPackage(fieldClass);
  }
  
  private void generateToStringMethod() {
    addImport(EncodingUtil.class.getName());
    appendMethod("@Override\npublic String toString()", 
           "return EncodingUtil.pojoToString(this);");

  }
  
  private void generateEqualsMethod() {
    StringBuilder body = new StringBuilder()
      .append("if (other == null) {\n")
      .append(JavaCodeGenUtil.indent("return false;"))
      .append(END_BLOCK_TOKEN)
      .append("if (this == other) {\n")
      .append(JavaCodeGenUtil.indent("return true;"))
      .append(END_BLOCK_TOKEN)
      .append("if (!getClass().equals(other.getClass()))\n")
      .append(JavaCodeGenUtil.indent("return false;"))
      .append("\n");
      
    if (fields.isEmpty()) {
      body.append("return true;\n");
    } else {
      body.append(getSimpleName())
        .append(" o = (")
        .append(getSimpleName())
        .append(") other;\nreturn ");
      boolean first = true;
      for (Field field : fields) {
        String f = field.getName();
        if (first) {
          addImport(Objects.class);
          first = false;
        } else {
          body.append("\n")
            .append(JavaCodeGenUtil.INDENTATION)
            .append(JavaCodeGenUtil.INDENTATION)
            .append("&& ");
        }
        body.append("Objects.equals(this.")
          .append(f)
          .append(", o.")
          .append(f)
          .append(")");
      }
      body.append(";\n");
    }
    appendMethod("@Override\npublic boolean equals(Object other)", body.toString());
  }
  
  private void generateHashCodeMethod() {
    StringBuilder body = new StringBuilder();
    if (fields.isEmpty()) {
      body.append("return 31 * getClass().hashCode();\n");
    } else {
      boolean first = true;
      body.append("return Objects.hash(");
      for (Field field : fields) {
        String f = field.getName();
        if (first) {
          first = false;
        } else {
          body.append(", ");
        }
        body.append(f);
      }
      body.append(");\n");
    }
    appendMethod("@Override\npublic int hashCode()", body.toString());
  }
  
  private void generateDeepCloneMethod() {
    String signature = "@Override\npublic " + getSimpleName() + " deepClone()";
    String newDeclaration = "new " + getSimpleName() + END_NO_PARAM_METHOD_INST_TOKEN;
    StringBuilder body = new StringBuilder();
    if (CollectionUtil.isEmpty(fields)) {
      body.append("return ").append(newDeclaration);
      
    } else {
      body.append(getSimpleName())
      .append(" clone = ")
      .append(newDeclaration);
    
      fields.forEach(f -> 
        body.append("clone.")
            .append(f.getName())
            .append(" = ")
            .append(PojoGenUtil.generateDeepCloneFieldInstruction(f, getImports()))
            .append(";\n")
      );
      body.append("return clone;\n");
    }
    appendMethod(signature, body.toString());
  }
}
