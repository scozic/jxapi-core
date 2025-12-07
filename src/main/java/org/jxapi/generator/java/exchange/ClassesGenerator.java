package org.jxapi.generator.java.exchange;

import java.io.IOException;
import java.nio.file.Path;

import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;

/**
 * Interface for Java code generators that manage generation of multiple
 * classes. There is one of such implementation for each part of an
 * {@link ExchangeDescriptor}: {@link ExchangeApiDescriptor},
 * {@link RestEndpointDescriptor}, {@link WebsocketEndpointDescriptor}
 */
public interface ClassesGenerator {

  /**
   * Calling this method will trigger generation of all .java files managed by this generator.
   * @param outputFolder The root source folder for java classes e.g. <code>src/main/java</code>
   * @throws IOException If I/O error occurs during generation
   */
  void generateClasses(Path outputFolder) throws IOException;
}
