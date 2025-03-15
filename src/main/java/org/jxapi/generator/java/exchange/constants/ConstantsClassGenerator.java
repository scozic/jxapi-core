package org.jxapi.generator.java.exchange.constants;

import java.util.List;

import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.generator.java.JavaTypeGenerator;

/**
 * Generates a Java interface with constants.
 * <p>
 * Example:
 * 
 * <pre>
 * {@code
 * public class MyConstants {
 * 
 *  private MyConstants(){}
 * 
 * 	public static final String MY_STRING = "foo";
 * 	public static final Integer MY_INT = Integer.valueOf(42);
 * 	public static Long MY_TIMESTAMP = Long.valueOf(System.currentTimeMillis());
 * }
 * }
 * </pre>
 * 
 * Where {@code MyConstants} is the interface name, {@code MY_STRING},
 * {@code MY_INT} and {@code MY_TIMESTAMP} are the constant names,
 * {@code String}, {@code Integer} and {@code Long} are the types of the
 * constants and {@code "foo"}, {@code 42} and
 * {@code System.currentTimeMillis()} are the values of the constants.
 */
public class ConstantsClassGenerator extends JavaTypeGenerator {

	private final  List<Constant> constants;
	
	/**
	 * Creates a new instance of the generator.
	 * 
	 * @param fullTypeName the full name of the interface to generate, example:
	 *                     com.example.MyConstants
	 * @param constants    the list of constants to generate in the interface
	 */
	public ConstantsClassGenerator(String fullTypeName, List<Constant> constants) {
		super(fullTypeName);
		setTypeDeclaration("public class");
		this.constants = constants;
	}
	
	@Override
	public String generate() {
		appendToBody("\nprivate ")
			.append(getSimpleName())
			.append("(){}\n");
		constants.forEach(c -> appendToBody("\n").append(ConstantsGenerationUtil.generateConstantDeclaration(c, getImports())));
		return super.generate();
	}

}
