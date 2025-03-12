package org.jxapi.util;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jxapi.netutils.deserialization.json.JsonDeserializer;

/**
 * Interface for 'Plain Old Java Objects' generated with the JXAPI API wrapper
 * generator.
 * <p>
 * Such objects are intended to be used as data transfer objects (DTOs) in API
 * calls, for instance as request or response bodies.
 * <p>
 * Due to the recursive nature of {@link Field}, such POJOs can contain
 * properties that are themselves POJOs (or collections of POJOs).
 * <p>
 * Generated POJOs implement this interface to provide the following features:
 * <ul>
 * <li>{@link Serializable} for serialization and deserialization: Since all
 * properties are serializable,
 * the whole object can be serialized and deserialized. The generator will
 * generate a <code>serialVersionUID</code>
 * field for each POJO using a hash that is generated from its name, implemented
 * interfaces and properties.
 * <li>{@link Cloneable} for shallow cloning: The default {@link Object#clone()}
 * method is suitable for shallow cloning, as all properties are immutable or
 * serializable (collections, other POJOs).
 * <li>{@link DeepCloneable} for deep cloning: The {@link #deepClone()} method
 * is generated to provide deep cloning,
 * i.e. a new object is created with new instances of all properties deep cloned
 * from original object, recursively.
 * <li>{@link Comparable} for comparison: The {@link #compareTo(Object)} method
 * is generated to compare two POJOs based on their properties.
 * </ul>
 * 
 * The generated POJOs also override the default {@link Object#equals(Object)}
 * to perform deep comparison of all properties, {@link Object#hashCode()} to
 * create a hash from all properties values, so these methods are consistent
 * (same hash for equal objects).
 * <br>
 * {@link Object#toString()} methods is also overridden to display a JSON
 * representation of the object.
 * <p>
 * Since they are intended to be used as data transfer objects serialized to
 * JSON, they are also annotated with {@link JsonSerialize} pointing to a custom
 * serializer class that will serialize the object to JSON.
 * A class with the same name as the POJO and the suffix "Serializer" is
 * generated to provide the serialization logic. Another class with the same
 * name and the suffix "Deserializer", implementing {@link JsonDeserializer} is
 * generated to provide the deserialization logic.
 * <p>
 * Regarding Comparable interface, the generated POJOs are compared based on
 * their properties, in the order they are declared in the class.
 * This is same order as property declaration in the descriptor file, therefore
 * the order of properties in the descriptor file can be used to define the
 * comparison order.
 * 
 * @param <T> The type of the POJO
 * @see DeepCloneable
 * @see JsonSerialize
 * @see JsonDeserializer
 * @see Serializable
 * @see Cloneable
 * @see Comparable
 */
public interface Pojo<T> extends Serializable, Cloneable, DeepCloneable<T>, Comparable<T> {

}
