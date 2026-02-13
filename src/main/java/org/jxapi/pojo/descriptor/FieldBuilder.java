package org.jxapi.pojo.descriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Builder for {@link Field}.
 */
public class FieldBuilder {

  private final Field field = new Field();

  /**
   * Builds the field.
   * 
   * @return the field
   */
  public Field build() {
    return field.deepClone();
  }

  /**
   * Sets the name of the field.
   * 
   * @param name the name of the field to set, see {@link Field#getName()}
   * @return this builder
   */
  public FieldBuilder name(String name) {
    field.setName(name);
    return this;
  }

  /**
   * Sets the type of the field.
   * 
   * @param type the type of the field to set, see {@link Field#getType()}
   * @return this builder
   */
  public FieldBuilder type(Type type) {
    field.setType(type);
    return this;
  }

  /**
   * Sets the type of the field using type name like <code>STRING_LIST</code>.
   * 
   * @param type the type of the field to set, see {@link Field#setType(String)}
   * @return this builder
   */
  public FieldBuilder type(String type) {
    field.setType(type);
    return this;
  }

  /**
   * Sets the description of the field.
   * 
   * @param description the description of the field to set, see
   *                    {@link Field#getDescription()}
   * @return this builder
   */
  public FieldBuilder description(String description) {
    field.setDescription(description);
    return this;
  }

  /**
   * Sets the sample value of the field.
   * 
   * @param sampleValue the sample value of the field to set, see
   *                    {@link Field#getSampleValue()}
   * @return this builder
   */
  public FieldBuilder sampleValue(Object sampleValue) {
    field.setSampleValue(sampleValue);
    return this;
  }

  /**
   * Sets the message field of the field.
   * 
   * @param msgField the message field of the field to set, see
   *                 {@link Field#getMsgField()}
   * @return this builder
   */
  public FieldBuilder msgField(String msgField) {
    field.setMsgField(msgField);
    return this;
  }

  /**
   * Sets the parameters of the field.
   * 
   * @param properties the parameters of the field to set, see
   *                   {@link Field#getProperties()}
   * @return this builder
   */
  public FieldBuilder properties(List<Field> properties) {
    field.setProperties(properties);
    return this;
  }

  /**
   * Adds a parameter to the field.
   * 
   * @param property the parameter to add, see {@link Field#getProperties()}
   * @return this builder
   */
  public FieldBuilder property(Field property) {
    List<Field> properties = Optional.ofNullable(field.getProperties())
        .orElse(List.of());
    List<Field> res = new ArrayList<>(properties.size() + 1);
    res.addAll(properties);
    res.add(property);
    field.setProperties(List.copyOf(res));
    return this;
  }

  /**
   * Sets the object name of the field.
   * 
   * @param objectName the object name of the field to set, see
   *                   {@link Field#getObjectName()}
   * @return this builder
   */
  public FieldBuilder objectName(String objectName) {
    field.setObjectName(objectName);
    return this;
  }
  
  /**
   * Sets the object description of the field.
   * 
   * @param objectDescription the object description of the field to set, see
   *                          {@link Field#getObjectDescription()}
   * @return this builder
   */
  public FieldBuilder objectDescription(String objectDescription) {
    field.setObjectDescription(objectDescription);
    return this;
  }

  /**
   * Sets the implemented interfaces of the field.
   * 
   * @param implementedInterfaces the implemented interfaces of the field to set,
   *                              see {@link Field#getImplementedInterfaces()}
   * @return this builder
   */
  public FieldBuilder implementedInterfaces(List<String> implementedInterfaces) {
    field.setImplementedInterfaces(implementedInterfaces);
    return this;
  }
  
  /**
   * Sets the URL parameter type of the field.
   * 
   * @param in the URL parameter type of the field to set, see
   *           {@link Field#getIn()}
   * @return this builder
   */
  public FieldBuilder in(UrlParameterType in) {
    field.setIn(in);
    return this;
  }
  
  /**
   * Sets the default value of the field.
   * @param defaultValue the default value of the field to set, see
   *                  {@link Field#getDefaultValue()}
   * @return this builder
   */
  public FieldBuilder defaultValue(Object defaultValue) {
    field.setDefaultValue(defaultValue);
    return this;
  }

  /**
   * Adds an implemented interface to the field.
   * 
   * @param implementedInterface the implemented interface to add, see
   *                             {@link Field#getImplementedInterfaces()}
   * @return this builder
   */
  public FieldBuilder implementedInterface(String implementedInterface) {
    List<String> implementedInterfaces = Optional.ofNullable(field.getImplementedInterfaces())
        .orElse(List.of());
    List<String> res = new ArrayList<>(implementedInterfaces.size() + 1);
    res.addAll(implementedInterfaces);
    res.add(implementedInterface);
    field.setImplementedInterfaces(List.copyOf(res));
    return this;
  }

}
