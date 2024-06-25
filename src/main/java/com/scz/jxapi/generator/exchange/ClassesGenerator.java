package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.nio.file.Path;

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;

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
