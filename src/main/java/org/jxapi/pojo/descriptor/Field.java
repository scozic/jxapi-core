package org.jxapi.pojo.descriptor;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.pojo.PojoGenUtil;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Element of exchange descriptor JSON document describing a given field of a
 * REST request or response or websocket subscribe request or message.<br>
 * A field is defined by:<br>
 * <ul>
 * <li>Its <code>name</code> property, which has to be unique among fields of
 * same object structure</li>
 * <li>Its <code>type</code> property, see {@link Type} which qualifies the type
 * of data found as value. This type can be either primitive
 * (<code>STRING</code>, <code>INT</code>... see
 * {@link CanonicalType#isPrimitive}) or defining an object structure (JSON map)
 * as a list of nested {@link Field} of any type. Therefore a field can describe
 * most tree data structure.</li>
 * <li>Its <code>description</code>, a human readable description of the
 * field</li>
 * <li>Its <code>sample Value</code>, a sample value of the field, used for
 * documentation purpose and sample value creation in demo classes</li>
 * <li>Its <code>msgField</code>, the name of the field in the message sent or
 * received to the endpoint. This means the actual key in JSON structure. This
 * can differ from actual name when APIs are designed to minify JSON data
 * transferred by using single letter as field names. In this case it is
 * recommended to define <code>name</code> property in descriptor with
 * understandable name, and set <code>msgField</code> value</li>
 * <li>Its <code>objectName</code>, the simple (without package) name of java
 * class to represent corresponding to object defined by this parameter.
 * Relevant only when type is an object see {@link Type#isObject()}.<br>
 * <strong>Remarks:</strong>
 * <ul>
 * <li>In a descriptor file, the first field defining a given object name should
 * define that object sub-fields, other properties using same object name need
 * not define sub-fields. This allow not to repeat identical structures in
 * different APIs.</li>
 * <li>Defining <code>objectName</code> property is not mandatory for. For an
 * 'object' field, the object name will be generated as concatenation of
 * exchange, api, and field names</li>
 * <li>Defining <code>objectName</code> property is important for an 'object'
 * field, when the object is used in multiple APIs. This will allow to generate
 * a single class for the object, and reuse it in multiple APIs.</li>
 * <li>The destination package of the generated class is sub package 'pojo' of
 * enclosing API group package, which is sub package 'apiName' of
 * <code>basePackage</code> property in the descriptor file.<br>
 * Example: for a field with objectName <i>Foo</i> in an endpoint of API group
 * named <i>myapi</i> group of exchange with base package <i>com.x.y</i>, the
 * class Foo will be generated in package <i>com.x.y.myapi.pojo</i></li>
 * </ul>
 * <li>Its <code>properties</code>, for an <code>object</code> type field, see
 * {@link Type#isObject()}, the fields in nested structure,
 * <code>null</code> otherwise.</li>
 * <li>Its <code>implementedInterfaces</code>, the list of interfaces
 * implemented by the object defined by this field. Relevant only when type is
 * an object see {@link Type#isObject()}.</li>
 * </ul>
 * 
 * JSON examples:<br>
 * <i>Field of simple type STRING:</i><br>
 * 
 * <pre>
 * {
 *     "name": "myField",
 *     "type": "STRING",
 *     "description": "A field to store a string",
 *     "sampleValue": "Hello",
 *     "msgField": "f"
 * }
 * </pre>
 * 
 * Expected sample json value of data represented by such field:
 * 
 * <pre>
 * ["f":"Hello"]
 * </pre>
 * 
 * <br>
 * <br>
 * <i>Field of object type OBJECT_MAP:</i><br>
 * 
 * <pre>
 * {
 *     "name": "myField",
 *     "type": "OBJECT_MAP",
 *     "description": "A field to store a map of objects",
 *     "msgField": "f",
 *     "objectName": "MyObject",
 *     "sampleMapKeyValue": ["foo", "bar"],
 *     "properties": [
 *         {
 *             "name": "myField1",
 *             "type": "STRING",
 *             "description": "A field to store a string",
 *             "sampleValue": "Hello",
 *             "msgField": "f1"
 *         },
 *         {
 *             "name": "myField2",
 *             "type": "INT",
 *             "description": "A field to store an integer",
 *             "sampleValue": 123,
 *             "msgField": "f2"
 *         }
 *     ]
 * }
 * </pre>
 * 
 * Expected sample json value of data represented by such field: <br>
 * 
 * <pre>
 * [
 *     "f": {
 *         "foo": {
 *             "f1": "Hello",
 *             "f2": 123
 *         },
 *         "bar": {
 *             "f1": "World",
 *             "f2": 456
 *         }
 *     }
 * ]
 * </pre>
 * 
 * <br>
 * Notice in example above f1 and f2 are expected fields in json structure, but
 * "foo" and "bar" are arbitrary keys of MAP field type.<br>
 * <br>
 * 
 * @see RestEndpointDescriptor
 * @see Type
 * @see Field
 * @see WebsocketEndpointDescriptor
 */
public class Field implements Pojo<Field> {
  
  private static final long serialVersionUID = 8123456789012345678L;

  /**
   * @return New Builder for incrementally building a {@link Field} instance
   * @see FieldBuilder
   */
  public static FieldBuilder builder() {
    return new FieldBuilder();
  }
  
  private static <T> List<T> cloneList(List<T> l) {
    return l == null? null: new ArrayList<>(l);
  }
  
  private String name;
  
  private String description;
  
  private Type type;
  
  private Object sampleValue;
  
  private Object defaultValue;
  
  private String msgField;
  
  private String objectName;
  
  private String objectDescription;
  
  private List<Field> properties;
  
  private List<String> implementedInterfaces;
  
  private UrlParameterType in;
  
  /**
   * Clone this field
   * @return Deep cloned instance of this field.
   */
  @Override
  public Field deepClone() {
    Field clone = new Field();
    clone.name = this.name;
    clone.description = this.description;
    clone.objectDescription = this.objectDescription;
    clone.type = this.type;
    clone.sampleValue = this.sampleValue;
    clone.defaultValue = this.defaultValue;
    clone.msgField = this.msgField;
    clone.objectName = this.objectName;
    clone.properties = cloneList(this.properties);
    clone.implementedInterfaces = cloneList(this.implementedInterfaces);
    clone.in = this.in;
    return clone;
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    // Serialize all normal fields
    out.defaultWriteObject();

    // Validate and write sampleValue
    if (sampleValue != null && !(sampleValue instanceof Serializable)) {
        throw new NotSerializableException(
            "sampleValue must be Serializable but was: " + sampleValue.getClass()
        );
    }
    out.writeObject(sampleValue);

    // Validate and write defaultValue
    if (defaultValue != null && !(defaultValue instanceof Serializable)) {
        throw new NotSerializableException(
            "defaultValue must be Serializable but was: " + defaultValue.getClass()
        );
    }
    out.writeObject(defaultValue);
}


private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // Deserialize all normal fields
    in.defaultReadObject();

    // Read sampleValue
    Object sv = in.readObject();
    if (sv != null && !(sv instanceof Serializable)) {
        throw new NotSerializableException(
            "Deserialized sampleValue is not Serializable: " + sv.getClass()
        );
    }
    this.sampleValue = sv;

    // Read defaultValue
    Object dv = in.readObject();
    if (dv != null && !(dv instanceof Serializable)) {
        throw new NotSerializableException(
            "Deserialized defaultValue is not Serializable: " + dv.getClass()
        );
    }
    this.defaultValue = dv;
}


  /**
   * @return The name of the field
   */
  public String getName() {
    return name;
  }

  /**
   * @param name The name of the field
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Remark: when processing a field accoring to its type, use {@link PojoGenUtil#getFieldType(Field)} instead of this method to retrieve the actual type.
   * @return The type of the field, see {@link Type}
   */
  public Type getType() {
    return type;
  }

  /**
   * @param type The type of the field, see {@link Type}
   */
  public void setType(Type type) {
    this.type = type;
  }

  /**
   * Shortcut for <code>setType(Type.fromTypeName(type))</code>
   * @param type The type name see {@link Type#toString()}
   */
  public void setType(String type) {
    this.type = Type.fromTypeName(type);
  }

  /**
   * @return The description of the field
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description The description of the field
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return The sample value of the field, used for documentation purpose and sample value creation in demo classes
   */
  public Object getSampleValue() {
    return sampleValue;
  }

  /**
   * @param sampleValue The sample value of the field, used for documentation purpose and sample value creation in demo classes
   */
  public void setSampleValue(Object sampleValue) {
    this.sampleValue = sampleValue;
  }
  
  /**
   * @return The name of the field in the message sent or received to the endpoint. Usually the actual name of field.
   */
  public String getMsgField() {
    return msgField;
  }

  /**
   * @param msgField The name of the field in the message sent or received to the endpoint. Usually the actual key in JSON structure.
   */
  public void setMsgField(String msgField) {
    this.msgField = msgField;
  }
  
  /**
   * @return For an 'object' type field, see {@link Type#isObject()}, the
   *         properties in nested structure, <code>null</code> otherwise. An
   *         object type property may have <code>null</code> properties if it is
   *         defined an object name (see {@link #getObjectName()}). That means
   *         properties of that object are defined in another {@link Field}.
   */
  public List<Field> getProperties() {
    return properties;
  }

  /**
   * @param fields For an 'object' type parameter, see
   *               {@link Type#isObject()}, the properties in nested
   *               structure, <code>null</code> otherwise.
   */
  public void setProperties(List<Field> fields) {
    this.properties = fields;
  }
  
  /**
   * @return The simple (without package) name of java class to represent
   *         corresponding to object defined by this field. Relevant only when
   *         type is an object see {@link Type#isObject()}.<br> Remark: in a descriptor
   *         file, the first field defining a given object name should define that object properties
   *         see {@link Field#getProperties()} , other properties using same object name need not
   *         define sub-properties. This allow not to repeat identical structures
   *         in different APIs.
   */
  public String getObjectName() {
    return objectName;
  }

  /**
   * @param objectName The simple (without package) name of java class corresponding to object defined by this field.
   * @see #getObjectName()
   */
  public void setObjectName(String objectName) {
    this.objectName = objectName;
  }

  /**
   * @return The list of interfaces implemented by the object defined by this
   *         parameter. Relevant only when type is an object see
   *         {@link Type#isObject()}.
   */
  public List<String> getImplementedInterfaces() {
    return implementedInterfaces;
  }

  /**
   * @param implementedInterfaces The list of interfaces implemented by the object
   *                              defined by this parameter. Relevant only when
   *                              type is an object see
   *                              {@link Type#isObject()}.
   */
  public void setImplementedInterfaces(List<String> implementedInterfaces) {
    this.implementedInterfaces = implementedInterfaces;
  }

  /**
   * @return The string representation of this field, see
   *         {@link EncodingUtil#pojoToString(Object)}
   */
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * When this field represents a URL parameter, get its type.
   * <br>
   * This is relevant when the field is used in a REST endpoint descriptor,
   * see {@link RestEndpointDescriptor#getRequest()}, as part of the request.
   * In this case, field hierarchy representing the request will be serialzed
   * as URL parameters, when the request HTTP method request has no body,
   * like GET, DELETE, etc.
   * <br>
   * The URL parameters can be path parameters or query parameters, see {@link UrlParameterType}.
   * When the request has no body, all fields representing the request
   * are considered as {@link UrlParameterType#QUERY} parameters. Unless specified otherwise by setting
   * this property to {@link UrlParameterType#PATH}.
   * <br>
   * 
   * @return The type of URL parameter, see {@link UrlParameterType}
   */
  public UrlParameterType getIn() {
    return in;
  }

  /**
   * When this field represents a URL parameter, set its type. <br>
   * This is relevant when the field is used in a REST endpoint descriptor, see
   * {@link RestEndpointDescriptor#getRequest()}, as part of the request. In
   * this case, field hierarchy representing the request will be serialzed as URL
   * parameters, when the request HTTP method request has no body, like GET,
   * DELETE, etc. <br>
   * The URL parameters can be path parameters or query parameters, see
   * {@link UrlParameterType}. When the request has no body, all fields
   * representing the request are considered as {@link UrlParameterType#QUERY}
   * parameters. Unless specified otherwise by setting this property to
   * {@link UrlParameterType#PATH}. <br>
   * The path parameters are provided before query parameters in the URL.
   * The order of path and query parameters is determined by the order they are
   * declared in the descriptor file.
   * 
   * @param in The type of URL parameter, see {@link UrlParameterType}
   */
  public void setIn(UrlParameterType in) {
    this.in = in;
  }
  
  /**
   * Gets the default value of this field.
   * @return The default value of this field.
   */
  public Object getDefaultValue() {
    return defaultValue;
  }
  
  /**
   * Gets the description of the object represented by this field. This relevant especially when the field type is an object, see {@link Type#isObject()}, with defined objectName.
   * Such objects may be reused across multiple APIs, so having a description of the object itself is useful. It can be different from the field description, which explains 
   * the role of the field in the enclosing structure.<p>
   * This description will be used in generated JavaDoc of the generated class representing the object.
   * When it is not set, the field description is used instead.
   * @return The description of the object represented by this field.
   */
  public String getObjectDescription() {
    return objectDescription;
  }

  /**
   * Sets the description of the object represented by this field. This relevant
   * especially when the field type is an object, see {@link Type#isObject()},
   * with defined objectName. Such objects may be reused across multiple APIs, so
   * having a description of the object itself is useful. It can be different from
   * the field description, which explains the role of the field in the enclosing
   * structure.
   * <p>
   * This description will be used in generated JavaDoc of the generated class
   * representing the object. When it is not set, the field description is used
   * instead.
   * 
   * @param objectDescription The description of the object represented by this
   *                          field.
   */
  public void setObjectDescription(String objectDescription) {
    this.objectDescription = objectDescription;
  }

  /**
   * Sets the default value of this field. 
   * 
   * @param defaultValue The default value of this field.
   */
  public void setDefaultValue(Object defaultValue) {
    this.defaultValue = defaultValue;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    return toString().equals(other.toString());
  }
  
  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public int compareTo(Field o) {
    return CompareUtil.compare(this.name, o.name);
  }
}
