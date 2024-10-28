package com.scz.jxapi.generator.java.exchange.constants;

import java.util.List;

import com.scz.jxapi.exchange.descriptor.Constant;
import com.scz.jxapi.generator.java.JavaTypeGenerator;

/**
 * Generates a Java interface with constants.
 * <p>
 * Example:
 * 
 * <pre>
 * {@code
 * public interface MyConstants {
 * 
 * 	String MY_STRING = "foo";
 * 	Integer MY_INT = Integer.valueOf(42);
 * 	Long MY_TIMESTAMP = Long.valueOf(System.currentTimeMillis());
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
public class ConstantsInterfaceGenerator extends JavaTypeGenerator {

	private final  List<Constant> constants;
	
	/**
	 * Creates a new instance of the generator.
	 * 
	 * @param fullTypeName the full name of the interface to generate, example:
	 *                     com.example.MyConstants
	 * @param constants    the list of constants to generate in the interface
	 */
	public ConstantsInterfaceGenerator(String fullTypeName, List<Constant> constants) {
		super(fullTypeName);
		setTypeDeclaration("public interface");
		this.constants = constants;
	}
	
	@Override
	public String generate() {
		constants.forEach(c -> appendToBody("\n").append(ConstantsGenerationUtil.generateConstantDeclaration(c, getImports())));
		return super.generate();
	}

}
