# Java POJOS generation

JXAPI generator for Java API wrappers generates [POJOs](https://en.wikipedia.org/wiki/Plain_old_Java_object) for REST and websocket endpoints request, response, and messages. Those POJOs are used as data transfer objects (DTOs) in the generated Java wrapper code, and carry features detailed below to facilitate their usage in Java applications.

Alternatively, you may use the generator to generate only POJOs for any JSON or YAML descriptor file describing data structures.
This is explained in the [Generating only POJOs using jxapi-maven-plugin](#generating-only-pojos-using-jxapi-maven-plugin) section below.

## Generated POJOs features

All of generated pojos implement the [Pojo](../../src/main/java/org/jxapi/util/Pojo.java) interface and expose the following features:

## Equals, hashcode consistency

Methods `equals(Object)` and `hashCode()` are overridden to be consistent (equal objects should have the same hashcode).

The `equals()` method performs a deep comparison, comparing each property of both objects in the order properties have been defined.

The `hashCode()` method computes a hash from every property hash.

### Inner Builder classes

Each generated POJO class exposes a public static inner class named `Builder`. This class follows the builder pattern to create an object incrementally.

### Ordering

POJOs also implement the [Comparable](https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html) interface, and comparison is performed by comparing each property value in the order it is defined in the descriptor file. This makes POJOs suitable for use in sorted collections.

### Deep cloning

The JXAPI `DeepCloneable` interface is implemented, so a POJO can be 'deep' cloned, which means object properties will be deep cloned too.

### Serialization

The [Serializable](https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html) interface is implemented in generated POJOs by adding a generated `serialVersionUID`.  This is enough to enforce `Serializable` contract as properties are serializable too.

### JSON serialization

For each generated POJO, a JSON `Serializer` class extending [StdSerializer](https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-databind/latest/com/fasterxml/jackson/databind/ser/std/StdSerializer.html), and `Deserializer` class is also generated, allowing serialization using the [Jackson](https://github.com/FasterXML/jackson) library.


## Generating only POJOs using jxapi-maven-plugin

If you want to generate only POJOs for using JSON or YAML descriptor files, you may use the `jxapi-maven-plugin`.
The plugin recursively looks for descriptor files in the `src/main/resources/jxapi/pojos` folder of your Maven project, and generates POJOs for each descriptor file found.
A POJO descriptor file has a structure composed of a `basePackage` property defining the base package for generated POJOs, and a `pojos` property defining the list of POJOs to generate. This property is an array of POJO definitions each following the same structure as one defining request/response/message POJOs in an exchange descriptor file. See [Field.java](../../src/main/java/org/jxapi/pojo/descriptor/Field.java) and [Pojo.java](../../src/main/java/org/jxapi/codegen/pojo/Pojo.java) classes for more details about POJO and field definition structure.
You can find an example of POJOs descriptor file in [src/main/resources/jxapi/pojos/exchange.yaml](../../src/main/resources/jxapi/pojos/exchange.yaml) file of this project. This file defines POJOs used in exchange descriptor files of this project (exchange descriptor fileds are mapped to those POJOs).
