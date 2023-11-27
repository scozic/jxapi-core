# JXAPI

Generate a Java REST and/or Websocket API wrapper efficiently using code generation tools.

## Introduction
Most web services are published as REST/Websocket APIs. Those APIs disseminate data as structured JSON objects.
JXAPI is for generating java code to call those REST APIs using a simple function with a Java POJO as request and response data.

The generator will take as input a JSON file describing APIs, and generate request/response POJOs, Java interfaces to REST APIs and Websockets and their implementation, and also demo snippets.
You will also need to write a few lines of code for API specific implementation aspects like:
 * Authentication challenge (like computing authorization header added requests using API key/secret and request parameters)
 * Request parameter formatting (use as query parameters or request body parameters)

That can be achieved easily by specifying implementation REST+Websocket endpoint factories using hooks, check wrapper development guide below.

The JSON API descriptor file structure is simple, and can be AI generated from online API documentation.
 
## Java API Wrapper using JXAPI development guide.

You need a dev environment with GIT, Maven, and a JDK >= 8 installed.
I will use here an  [Bybit](https://www.bybit.com/en/) exchange API, which [V5 API documentation is available here](https://bybit-exchange.github.io/docs/v5/guide)

Let's build a java wrapper to this API by creating a java project first.

### Project bootstrap
Go to your 'workspace' directory where your JXAPI projects will be created and clone this repo using `git clone` and run maven install in it to install it as latest artefact in your repo.

```
cd jxapi-core
mvn clean install
cd..
```


Init Git `jxapi-bybit` project from workspace folder:

```
git init jxapi-bybit
```
And cd in newly created folder:

```
cd jxapi-bybit
```
Initialize [maven project](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)

```
mvn archetype:generate -DgroupId=com.scz.exchanges.bybit -DartifactId=jxapi-bybit -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
```
#### Initial changes to Maven pom.xml
Maven pom.xml file needs to be customized.

1. Change java.version file to 1.8


```xml
  <properties>
...
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
  </properties>
```

2. Add dependency to jxapi-core project:

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

3. Add `exec-maven-plugin` which will allow performing code generation using Maven command `mvn exec:java`

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

### Manage specific REST/Websocket API authentication
A few lines of Java code have to be written manually to manage API specific authentication challenge.

#### REST requests factory

The generator needs and implementation of `com.scz.jxapi.netutils.rest.RestEndpointFactory` interface. 
Let's do this in a class named `com.scz.jxapi.exchanges.bybit.net.BybitRestEndpointFactory`:

```java
public class BybitRestEndpointFactory implements RestEndpointFactory {
	
	public static final String API_KEY_PROPERTY = "apiKey";
	public static final String API_SECRET_PROPERTY = "apiSecret";
	private Properties properties;
	
	private final HttpRequestExecutor executor = new JavaNetHttpRequestExecutor(HttpClient.newHttpClient());
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public <R, A> RestEndpoint<R, A> createRestEndpoint(MessageDeserializer<A> messageDeserializer) {		
		return new DefaultRestEndpoint<>(new BybitHttpRequestBuilder(properties.getProperty(API_KEY_PROPERTY), 
																	 properties.getProperty(API_SECRET_PROPERTY)), 
										 executor, 
										 messageDeserializer);
	}

}
```
 * API specific configuration properties will be passed in a `java.util.Properties` object, passed from `setProperties` object
 * We need a generic `com.scz.jxapi.netutils.rest.HttpRequestExecutor` implementation to execute REST requests. The default `JavaNetHttpRequestExecutor` is usually suitable.
 * Actual REST requests customization are performed in `com.scz.jxapi.exchanges.bybit.net.BybitHttpRequestBuilder`. In this class we intercept outgoing REST request calls by overriding `com.scz.jxapi.netutils.rest.DefaultHttpRequestBuilder.build()` method:
 
```java
	@Override
	public HttpRequest build(RestRequest<?> restRequest) {
		HttpRequest httpRequest = super.build(restRequest);
		if (apiKey == null) {
			throw new IllegalStateException("Missing apiKey");
		}
		String timestampStr = "" + System.currentTimeMillis();
		httpRequest.setHeader("X-BAPI-API-KEY", apiKey);
       httpRequest.setHeader("X-BAPI-SIGN", genSignature(restRequest, httpRequest, timestampStr));
		httpRequest.setHeader("X-BAPI-SIGN-TYPE", "2");
		httpRequest.setHeader("X-BAPI-TIMESTAMP", timestampStr);
		httpRequest.setHeader("X-BAPI-RECV-WINDOW", RECV_WINDOW);
		httpRequest.setHeader("Content-Type", "application/json");
		return httpRequest;
	}
```

This method transcodes generic `com.scz.jxapi.netutils.rest.RestRequest` to raw `com.scz.jxapi.netutils.rest.HttpRequest` to be executed. The result is added specific headers here but body, request URL with query string parameters and body can be also tuned.

#### Websocket management
TODO

### Write JSON file for API descriptor
Now the tedious work is done, let's create some API wrappers.

Create a file named BybitCEXDescriptor.json file. This file in `src/main/resources` with a name ending with `Descriptor.json` makes it eligible for API wrapper generation using `mvn exec:java` command.

Such API description file looks as follows:

```json
{
	"name": "Bybit",
	"description": "Bybit V5 unified API",
	"basePackage": "com.scz.jxapi.exchanges.bybit.gen",
	"rateLimits": [{"id":"BybitV5GetPostSharedRateLimit", "timeFrame": 1000,  "maxRequestCount": 120}],
	"apis":[
		{ 
			"name": "V5",
			"description": "Bybit V5 unified API",
			"restEndpointFactory": "com.scz.jxapi.exchanges.bybit.net.BybitRestEndpointFactory",
			"restEndpoints": [
				{
				}
			}
		}
	]
}	
```

TODO


## Supported exchanges
TODO! Currently under development :)

