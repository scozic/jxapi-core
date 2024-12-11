# ${exchangeName} API Java wrapper

${exchangeDescription}

## Usage

### Quick start

This wrapper offers Java interfaces for using ${exchangeName} APIs.

Add maven dependency to your project, then you can use the wrapper by instantiating ${exchangeInterfaceJavaDocLink} in your code:

``` java
import ${exchangeFullInterfaceClassName};
import ${exchangeFullInterfaceImplementationClassName};

public void call() {
  Properties properties = new Properties();
  // Set relevant configuration properties (see documentation) in 'props'
  ${exchangeFullInterfaceClassName} exchange = new {exchangeFullInterfaceImplementationClassName}(properties);
  // Access API groups and their endpoints through 'exchange' methods.
}
```

You may have a look at ${sampleDemoClassSourceCodeLink} for full usage example.

### Properties

${exchangeConfigPropertiesTable}

${exchangeConstants}

${exchangeReferenceDocLink}

## API Groups
exchangeName API endpoints are listed in following group(s)

### MyApiName

MyApiDescription

#### REST endpoints

The following REST endpoints are available:

${restEndpointGrids}