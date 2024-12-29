# JXAPI JAVA API wrapper module creation

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
Next,

1. Add dependency to jxapi-core project:

```
  <dependencies>
... 
    <dependency>
      <groupId>com.scz.jxapi</groupId>
      <artifactId>jxapi-core</artifactId>
      <version>0.7.5</version>
    </dependency>
  </dependencies>
```

2. Add `exec-maven-plugin` which will allow performing code generation using Maven command `mvn exec:java`

```
  <build>
    <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
      <plugins>
...
        <plugin>
		  <groupId>org.codehaus.mojo</groupId>
		  <artifactId>exec-maven-plugin</artifactId>
		  <configuration>
			  <mainClass>com.scz.jxapi.generator.exchange.ExchangeGeneratorMain</mainClass>
		  </configuration>
		</plugin>
      </plugins>
    </pluginManagement>
  </build>
```


You can now run `mvn clean install` to install initial maven project. Do not forget to update `.gitignore` file to ignore java classes and files in `/target`