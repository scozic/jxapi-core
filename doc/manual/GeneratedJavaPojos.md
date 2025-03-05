
# Generated Java POJOs

JXAPI generator for Java API wrappers generates [POJOs](https://en.wikipedia.org/wiki/Plain_old_Java_object) for REST and websocket endpoints request, response, and messages. Such POJOs carry the properties defined in the exchange descriptor with expected 'getter' and 'setter' methods.

They also carry additional features:

## Equals, hashcode consistency

Methods `equals(Object)` and `hashCode()` are overridden to be consistent (equal objects should have the same hashcode).

The `equals()` method performs a deep comparison, comparing each property of both objects in the order properties have been defined.

The `hashCode()` method computes a hash from every property hash.

## Inner Builder classes

Each generated POJO class exposes a public static inner class named `Builder`. This class follows the builder pattern to create an object incrementally.

## Ordering

POJOs also implement the [Comparable](https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html) interface, and comparison is performed by comparing each property value in the order it is defined in the descriptor file. This makes POJOs suitable for use in sorted collections.

## Deep cloning

The JXAPI `DeepCloneable` interface is implemented, so a POJO can be 'deep' cloned, which means object properties will be deep cloned too.

## Serialization

The [Serializable](https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html) interface is implemented in generated POJOs by adding a generated `serialVersionUID`.  This is enough to enforce `Serializable` contract as properties are serializable too.

## JSON serialization

For each generated POJO, a JSON `Serializer` class extending [StdSerializer](https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-databind/latest/com/fasterxml/jackson/databind/ser/std/StdSerializer.html), and `Deserializer` class is also generated, allowing serialization using the [Jackson](https://github.com/FasterXML/jackson) library.



