# JXAPI JAVA POJO or API wrapper module creation

The following section is about how to create a Maven/Java project exporting a JXAPI wrapper as JAR library. The following examples show module creation for a wrapper of an API called "myApi".

Go to your 'workspace' directory where your JXAPI projects will be created and clone this repo using `git clone` and run maven install in it to install it as latest artefact in your repo.

```
cd jxapi-core
mvn clean install
cd..
```


Init Git `jxapi-myapi` project from workspace folder:

```
git init jxapi-myapi
```
And cd in newly created folder:

```
cd jxapi-myapi
```
Initialize [maven project](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)

```
mvn archetype:generate -DgroupId=com.yourdomain.jxapi.exchanges.myapi -DartifactId=jxapi-myapi -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false
```
#### Initial changes to Maven pom.xml
Maven pom.xml file needs to be customized. You may first adjust compiler or JUnit version you want to use.
Next, define dependency to `jxapi-core` module, and `jxapi-maven-plugin` Maven plugin to integrate code generation to your wrapper module build cycle.

1. Add properties to pom.xml:
```xml
<project>
...
<properties>
    <!-- Use latest jxapi-core artefact version -->
    <jxapi.version>1.0.0</jxapi.version>

    <!-- This is the base URL that will be used for links to Javadoc in generated jxapi-myapi_README.md -->
    <baseJavaDocUrl>https://www.javadoc.io/static/com.yourdomain.jxapi.exchanges.myapi/jxapi-myapi/${project.version}/index.html?</baseJavaDocUrl>

    <!-- This is the base URL that will be used for links to source files in generated jxapi-myapi_README.md -->
    <baseSrcUrl>http://github.com/jxapi-myapi/blob/master/src/</baseSrcUrl>
</properties>
...
</project>

```

1. Add dependency to jxapi-core project:

```xml
  <dependencies>
... 
    <dependency>
      <groupId>org.jxapi</groupId>
      <artifactId>jxapi-core</artifactId>
      <version>${jxapi.version}</version>
    </dependency>
  </dependencies>
```

2. Add `jxapi-maven-plugin` to have code generation included in default Maven build cycle:

```xml
<project>
...
  <build>
    <plugins>
			<plugin>
				<groupId>org.jxapi</groupId>
				<artifactId>jxapi-maven-plugin</artifactId>
				<version>${jxapi.version}</version>
        <!-- Configure 'executions' if you want code generation integrated in build cycle -->
				<executions>
					<execution>
						<id>jxapi-exchange-generator</id>
						<phase>generate-sources</phase>
						<goals>
							<goal>jxapi-exchange-generator</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<baseProjectDir>${project.basedir}</baseProjectDir>
					<baseJavaDocUrl>${baseJavaDocUrl}/</baseJavaDocUrl>
					<baseSrcUrl>${baseSrcUrl}/</baseSrcUrl>
				</configuration>
			</plugin>
    </plugins>
  </build>
</project>  
```    
_Remark_: Either 2. or 3. above is mandatory: You may choose to have either code generation triggered manually using `mvn exec:java` command using 2. or integrated to the build cycle using dedicated plugin using 3., or both. The plugin way is recommended. 

You can now run `mvn clean install` to install initial maven project. Do not forget to update `.gitignore` file to ignore java classes and files in `/target`