package org.jxapi.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * Converts an ExchangeDescriptor object from JSON to YAML format.
 */
public class ExchangeDescriptorFromJsonToYaml {
  
  private static final int BEST_WIDTH = 160;
  
  private static final Logger log = LoggerFactory.getLogger(ExchangeDescriptorFromJsonToYaml.class);
  
  /**
   * Converts an ExchangeDescriptor object from JSON to YAML format.
   * Will write the YAML content to a file with the same name as the JSON file, but with a .yaml extension.
   * @param jsonDescriptorFile the path to the JSON file
   * @throws IOException if an I/O error occurs while parsing the JSON file or writing
   */
  public static void writeAsYaml(Path jsonDescriptorFile) throws IOException {
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromJson(jsonDescriptorFile);
    Path outputYamlFile = jsonDescriptorFile.resolveSibling(jsonDescriptorFile.getFileName().toString().replace(".json", ".yaml"));
    DumperOptions options = new DumperOptions();
    options.setWidth(BEST_WIDTH);
    options.setDereferenceAliases(true);
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    Yaml yaml = new Yaml(options);
      String outputContent = yaml.dump(exchangeDescriptor);
      log.info("Writing ExchangeDescriptor to YAML file: {}", outputYamlFile);
      Files.writeString(outputYamlFile, outputContent);
  }
  
  /**
   * Main method to convert an ExchangeDescriptor object from JSON to YAML format.
   * @param 1 arg expected: args the path to the JSON file
   */
  public static void main(String[] args) throws IOException {
    try {
      if (args.length != 1) {
        System.err.println("Usage: ExchangeDescriptorFromJsonToYaml <jsonDescriptorFile>");
        System.exit(1);
      }
      Path jsonDescriptorFile = Path.of(args[0]);
      writeAsYaml(jsonDescriptorFile);
      System.exit(0);
    } catch (Throwable t) {
      log.error("Error converting JSON to YAML: " + t.getMessage(), t);
      t.printStackTrace();
    }
  }

}
