package org.jxapi.generator.java;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Manages imports in Java code generation.
 * <p>
 * It keeps track of the imports that are needed in a Java class and generates the import statements for the class.
 * <p>
 * The imports are sorted in the following order:
 * <ol>
 * <li>java.* imports sorted lexicographically </li>
 * <li>Other imports sorted lexicographically </li>
 * </ol>
 * 
 */
public class Imports implements Iterable<String> {
  
  private static final String JAVA_PKG = "java.";
  
  private static final String JAVA_LANG_PKG = JAVA_PKG + "lang.";
  
  private static final Comparator<String> IMPORTS_COMPARATOR = new ImportComparator();
  
  private static boolean isJavaImport(String imp) {
    return imp.startsWith(JAVA_PKG);
  }
  
  private final Set<String> imps = new TreeSet<>(IMPORTS_COMPARATOR);
  
  /**
   * Adds the  given class to the imports.
   * @param cls the class to import
   */
  public void add(Class<?> cls) {
    add(cls.getName());
  }
  
  /**
   * Adds the given class to the imports.
   * <p>
   * The conventions with writing import are respected:
   * <ul>
   * <li>If the class is a 'java.lang' class, the import is not added.
   * <li>If the class is a 'java' class, the import is added before other package classes.
   * <li>If the class is a generic class, it is added without the generic part, which is not imported (the generic type must be explicitely imported).
   * </ul>
   * @param imp the full name of class to import
   */
  public void add(String imp) {
    if (imp == null) {
      throw new IllegalArgumentException("null import");
    }
    if (imp.startsWith(JAVA_LANG_PKG)) {
      return;
    }
    if (imp.contains("<")) {
      imp = imp.substring(0, imp.indexOf('<'));
    }
    imps.add(imp);
  }
  
  /**
   * Adds all the imports from the given set.
   * @param other the imports to add
   */
  public void addAll(Imports other) {
    imps.addAll(other.imps);
  }
  
  /**
   * @return the number of imports
   */
  public int size() {
    return imps.size();
  }

  /**
   * @return an iterator over the imports
   * 
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<String> iterator() {
    return imps.iterator();
  }
  
  /**
   * @return A copy of the list of all imports
   */
  public List<String> getAllImports() {
    return imps.stream().toList();
  }
  
  /**
   * Checks if the given class is already imported.
   * 
   * @param cls the class to check
   * @return <code>true</code> if the class is already imported,
   *         <code>false</code> otherwise
   */
  public boolean contains(Class<?> cls) {
    return contains(cls.getName());
  }
  
  /**
   * Checks if the given class is already imported.
   * 
   * @param im the full name of the class to check
   * @return <code>true</code> if the class is already imported,
   *         <code>false</code> otherwise
   */
  public boolean contains(String im) {
    return imps.contains(im);
  }
  
  /**
   * Generates the import statements for the given package.
   * <p>
   * The import statements are sorted in the following order:
   * <ol>
   * <li>java.* imports sorted lexicographically</li>
   * <li>Other imports sorted lexicographically</li>
   * </ol>
   * <p>
   * If the package of the class for which the imports are generated is the same
   * as the package of the imported class, the import is not generated.
   * <p>
   * An empty line is added between java.* imports and other imports.
   * </p>
   * 
   * @param currentPackage the package of the class for which the imports are
   *                       generated
   * @return the import statements
   */
  public String generate(String currentPackage) {
    StringBuilder s = new StringBuilder();
    boolean javaImport = false;
    boolean lineBreak = false;
    for (String im : imps) {
      if (isJavaImport(im)) {
        javaImport = true;
      } else if (javaImport) {
        javaImport = false;
        lineBreak = true;
      }
      if (currentPackage == null || !JavaCodeGenUtil.getClassPackage(im).equals(currentPackage)) {
        if (lineBreak) {
          s.append("\n");
          lineBreak = false;
        }
        s.append("import ")
         .append(im)
         .append(";\n");
      }
    }
    return s.toString();
  }
  
  private static class ImportComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
      if (o1 == null) {
        if (o2 != null) {
          return -1;
        }
        return 0;
      } else if (o2 == null) {
        return 1;
      }
      
      if (isJavaImport(o1)) {
        if (isJavaImport(o2)) {
          // Both imports are java.*
          return o1.compareTo(o2);
        }
        return -1;
      } else if (isJavaImport(o2)) {
        return 1;
      }
      
      return o1.compareTo(o2);
    }
    
  }

}
