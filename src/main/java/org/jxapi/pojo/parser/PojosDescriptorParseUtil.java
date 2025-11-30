package org.jxapi.pojo.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.PojosDescriptor;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.MergeUtil;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Parser for POJOs descriptor. 
 */
public class PojosDescriptorParseUtil {
  
  private PojosDescriptorParseUtil() {}

  /**
   * Parses a JSON file into an PojosDescriptor object.
   * 
   * @param jsonFile the path to the JSON file
   * @return the PojosDescriptor object parsed from the JSON file
   * @throws IOException if an I/O error occurs while reading the JSON file
   */
  public static PojosDescriptor fromJson(Path jsonFile) throws IOException {
    return fromJson(Files.readString(jsonFile));
  }
  
  /**
   * Parses a JSON string into an PojosDescriptor object.
   * 
   * @param jsonString the JSON string to parse
   * @return the PojosDescriptor object parsed from the JSON string
   * @throws JsonMappingException if there is an error mapping the JSON to the ExchangeDescriptor object
   * @throws JsonProcessingException if there is an error processing the JSON string
   */
  public static PojosDescriptor fromJson(String jsonString) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    return mapper.readValue(jsonString, PojosDescriptor.class);
  }
  
  /**
   * Parses a YAML file into an PojosDescriptor object.
   * 
   * @param yamlFile the path to the YAML file
   * @return the PojosDescriptor object parsed from the YAML file
   * @throws IOException if an I/O error occurs while reading the YAML file
   */
  public static  PojosDescriptor fromYaml(Path yamlFile) throws IOException {
        return fromYaml(Files.readString(yamlFile));
  }
  
  /**
   * Parses a YAML string into an PojosDescriptor object.
   * 
   * <p>
   * Relies on SnakeYAML library for YAML parsing.
   * @param yaml String the YAML string to parse
   * @return the PojosDescriptor object parsed from the YAML string
   */
  public static PojosDescriptor fromYaml(String yaml) {
    return new Yaml().loadAs(yaml, PojosDescriptor.class);
  }
  
  /**
   * Will visit all files in the directory and subdirectories and collect all the exchange descriptors.
   * Every JSON and YAML file will be parsed into an ExchangeDescriptor object.
   * If multiple files have the same exchange name, the exchange descriptors will be merged.
   * @param descriptorsPath Path to a single exchange descriptor file or a directory containing the exchange descriptor files 
   * @return a list of ExchangeDescriptor objects parsed from the JSON and YAML files
   * @throws IOException If an I/O error occurs while visiting the files
   * @throws IllegalArgumentException If an error occurs while parsing the files
   */
  public static List<PojosDescriptor> collectAndMergePojosDescriptors(Path descriptorsPath) throws IOException {
    Map<String, PojosDescriptor> exchangeDescriptors = CollectionUtil.createMap();
    try (Stream<Path> p = Files.walk(descriptorsPath)) {
       p.filter(Files::isRegularFile)
        .sorted(Comparator.naturalOrder())
        .filter(f -> f.toString().endsWith(".json") || f.toString().endsWith(".yaml"))
        .forEach(f -> {
           try {
             PojosDescriptor exchange = f.toString().endsWith(".json") ? fromJson(f) : fromYaml(f);
                      exchangeDescriptors.merge(
                          exchange.getBasePackage(), 
                          exchange, 
                          PojosDescriptorParseUtil::mergePojosDescriptors);
           } catch (Exception e) {
             // Catch all exceptions and rethrow as IllegalArgumentException providing the file path as context
             throw new IllegalArgumentException("Error parsing file:" + f.toAbsolutePath().toString(), e);
           }
       });
    }
    return List.copyOf(exchangeDescriptors.values());
  }
  
  /**
   * Merges two PojosDescriptor objects into one.
   * 
   * @param pd1 the first PojosDescriptor object
   * @param pd2 the second PojosDescriptor object
   * @return the merged PojosDescriptor object
   */
  public static PojosDescriptor mergePojosDescriptors(PojosDescriptor pd1, PojosDescriptor pd2) {
    PojosDescriptor res = new PojosDescriptor();
    res.setBasePackage(pd1.getBasePackage());
    res.setPojos(MergeUtil.mergeLists("List of POJOs", pd1.getPojos(), pd2.getPojos(), Field::getName));
    return res;
  }

}
