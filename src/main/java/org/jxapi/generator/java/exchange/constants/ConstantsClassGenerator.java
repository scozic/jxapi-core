package org.jxapi.generator.java.exchange.constants;

import java.util.List;
import java.util.Optional;

import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.util.PlaceHolderResolver;

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
 *   public static final String MY_STRING = "foo";
 *   public static final Integer MY_INT = Integer.valueOf(42);
 *   public static Long MY_TIMESTAMP = Long.valueOf(System.currentTimeMillis());
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
  
  private final PlaceHolderResolver docPlaceHolderResolver;
  private PlaceHolderResolver constantValuePlaceHolderResolver;
  
  /**
   * Creates a new instance of the generator.
   * 
   * @param fullTypeName           the full name of the interface to generate,
   *                               example: com.example.MyConstants
   * @param constants              the list of constants to generate in the
   *                               interface
   * @param docPlaceHolderResolver the resolver to use to resolve placeholders in
   *                               description of constants.
   */
  public ConstantsClassGenerator(String fullTypeName, 
                                 List<Constant> constants, 
                                 PlaceHolderResolver docPlaceHolderResolver) {
    super(fullTypeName);
    setTypeDeclaration("public class");
    this.constants = constants;
    this.docPlaceHolderResolver = Optional.ofNullable(docPlaceHolderResolver).orElse(PlaceHolderResolver.NO_OP);
  }
  
  @Override
  public String generate() {
    appendToBody("\nprivate ")
      .append(getSimpleName())
      .append("(){}\n");
    constants.forEach(this::generateConstantDeclaration);
    setDescription(docPlaceHolderResolver.resolve(getDescription()));
    return super.generate();
  }
  
  private void generateConstantDeclaration(Constant constant) {
    if (constant.isGroup()) {
      ConstantsClassGenerator groupGenerator = new ConstantsClassGenerator(
        ConstantsGenUtil.getConstantVariableName(constant, constants), 
        constant.getConstants(), 
        docPlaceHolderResolver);
      groupGenerator.setTypeDeclaration("public static class");
      groupGenerator.setDescription(constant.getDescription());
      groupGenerator.setGeneratePackageAndImports(false);
      groupGenerator.setConstantValuePlaceHolderResolver(constantValuePlaceHolderResolver);
      appendToBody("\n").append(groupGenerator.generate());
      groupGenerator.getImports().forEach(this::addImport);
    } else {
      appendToBody("\n")
        .append(ConstantsGenUtil.generateConstantDeclaration(
                constant, 
                constants,
                getImports(), 
                docPlaceHolderResolver,
                constantValuePlaceHolderResolver));
    }
  }

  /**
   * Gets the list of constants to generate.
   * 
   * @return the resolver to use to resolve constant values. Can be null.
   * @see #setConstantValuePlaceHolderResolver(PlaceHolderResolver)
   */
  public PlaceHolderResolver getConstantValuePlaceHolderResolver() {
    return constantValuePlaceHolderResolver;
  }

  /**
   * Sets the resolver for constant values.
   * <p>
   * This resolver is used to resolve placeholders in constant values, which can be among other constant values.<br>
   * When not set, the default resolver is used, which does not resolve any placeholders. but encodes input as a quoted string.<br>
   * This should also the behavior of provided resolver
   * 
   * @param constantValuePlaceHolderResolver the resolver for constant values. Must return quoted strings.
   */
  public void setConstantValuePlaceHolderResolver(PlaceHolderResolver constantValuePlaceHolderResolver) {
    this.constantValuePlaceHolderResolver = constantValuePlaceHolderResolver;
  }

}
