# REST endpoint pagination

<!-- BEGIN TABLE OF CONTENTS -->
  - [REST endpoint pagination](#rest-endpoint-pagination)
    - [Developing a wrapper: Have a REST endpoint implement pagination features.](#developing-a-wrapper-have-a-rest-endpoint-implement-pagination-features)
    - [Using a wrapper: handle pagination with PaginationUtil class](#using-a-wrapper-handle-pagination-with-paginationutil-class)
      - [Key Features](#key-features)
      - [Example Usage of a paginated endpoint](#example-usage-of-a-paginated-endpoint)
        - [Step 1: Define Your API and Request](#step-1-define-your-api-and-request)
        - [Step 2: Prepare the Initial Request](#step-2-prepare-the-initial-request)
        - [Step 3: Use `PaginationUtil.fetchAllPages(FutureRestResponse)` to fetch response remaining pages.](#step-3-use-paginationutilfetchallpagesfuturerestresponse-to-fetch-response-remaining-pages)

<!-- END TABLE OF CONTENTS -->

A common feature in REST APIs is the 'pagination' of responses to an endpoint. When returned data to an endpoint call may be too big to be returned in a single response, the endpoint provides pagination logic, with parameters to select a page of data, and eventually the max number of items to return in a single 'page' response. Such enpoint response data exposes properties to guess wether it is last page of data, and next page index to fetch.

JXAPI wrapper allows to define pagination for such endpoint, so that fetching next or remaining pages from a response can be handled easily regardless of pagination protocol.


## Developing a wrapper: Have a REST endpoint implement pagination features.

Here is an example of a paginated endpoint definition in a descriptor file:
```yaml
id: "Employee"
apis:
  - name: V1    
    restEndpoints:
    - name: getAllEmployees
      description: Get all employees
      httpMethod: GET
      docUrl: https://www.example.com/docs/employee/getAll
      url: /employees
      # Indicates endpoint supports pagination.
      paginated: true
      request:
        implementedInterfaces: 
        # The request must be of OBJECT type and implement a custom interface extending org.jxapi.netutils.rest.pagination.PaginatedRestRequest
        - org.jxapi.exchanges.employee.EmployeePaginatedRequest
        properties:
        - name: page
          type: INT
          description: Page number to return, defaults to 1.
          sampleValue: 1
        - name: size
          type: INT
          description: >
            Number of employees to return per page.<br>
            Defaults to ${constants.defaultPageSize}.<br>
            Maximum is ${constants.maxPageSize}.
          sampleValue: 10
      response:
        # The response must be of OBJECT type and implement a custom interface extending org.jxapi.netutils.rest.pagination.PaginatedRestResponse
        implementedInterfaces:
        - org.jxapi.exchanges.employee.EmployeePaginatedResponse
        properties:
        - name: page
          type: INT
          description: Page index, starting from 1
          sampleValue: 1
        - name: totalPages
          type: INT
          description: Total number of pages available
          sampleValue: 10
        - name: employees
          description: List of employees in this page
          objectName: Employee			
          type: OBJECT_LIST
```

As stated in snippet above comments, a REST endpoint is declared as supporting pagination using the `paginated` property.
In this case:
 - **The request must be of OBJECT type and implement a custom interface extending org.jxapi.netutils.rest.pagination.PaginatedRestRequest**
   This means a custom interface must be created by wrapper that extends org.jxapi.netutils.rest.pagination.PaginatedRestRequest interface.
   Such interface should provide a **default** implementation of `setNextPage(PaginatedRestResponse)` method that relies on specific properties of response (that can be cast to specific [PaginatedRestResponse](../../src/main/java/org/jxapi/netutils/rest/pagination/PaginatedRestResponse.java) implementation).
   You can have a look at [EmployeePaginatedRequest](../../src/test/java/org/jxapi/exchanges/employee/EmployeePaginatedRequest.java) as an example.
 - **The response must be of OBJECT type and implement a custom interface extending org.jxapi.netutils.rest.pagination.PaginatedRestResponse**
   This means a custom interface must be created by wrapper that extends [PaginatedRestResponse](../../src/main/java/org/jxapi/netutils/rest/pagination/PaginatedRestResponse.java) interface.
   Such interface should provide a **default** implementation of `hasNextPage()` method that relies on specific properties of response (that can be cast to specific [PaginatedRestResponse](../../src/main/java/org/jxapi/netutils/rest/pagination/PaginatedRestResponse.java) implementation).
   You can have a look at [EmployeePaginatedResponse](../../src/test/java/org/jxapi/exchanges/employee/EmployeePaginatedResponse.java) as an example.

## Using a wrapper: handle pagination with PaginationUtil class 

The [PaginationUtil](../../src/main/java/org/jxapi/netutils/rest/pagination/PaginationUtil.java) class provides utility methods to handle REST API requests that return paginated responses. It simplifies fetching next page or all remaining pages of a paginated REST endpoint call response by automatically handling pagination logic.

It can be used when an REST API is designed with an expected 'object' requests that carries the page index to fetch results for, and next page index can be retrieved from response.

---

### Key Features
- Automatically fetches all pages of a paginated REST API response.
- Merges results from multiple pages into a single response.
- Handles errors gracefully during the pagination process.

---

### Example Usage of a paginated endpoint 

Here’s how to use the [PaginationUtil](../../src/main/java/org/jxapi/netutils/rest/pagination/PaginationUtil.java) class to fetch next or all 
remaining pages of a paginated API response:

#### Step 1: Define Your API and Request
Assume you have an API interface with a method to fetch a single page of data:
```java
FutureRestResponse<MyApiPaginatedResponse> getPage(MyApiPaginatedRequest request);
```

#### Step 2: Prepare the Initial Request
Create an instance of your request object with eventual initial page index set:
```java
MyApiPaginatedResponse request = new MyApiRequest();
request.setPage(1);
```

The example above assumes there is a `page` property available on request object to set the desired page index.
Paginated REST APIs often also provide a parameter to set the desired page size (max number of items per page).

#### Step 3: Use `PaginationUtil.fetchAllPages(FutureRestResponse)` to fetch response remaining pages.
Call the `fetchAllPages` method to fetch all pages of data:
```java
FutureRestResponse<List<MyApiPaginatedResponse>> response = PaginationUtil.fetchAllPages(getPage(request));
```

The returned `response` is a [FutureRestResponse](../../src/main/java/org/jxapi/netutils/rest/FutureRestResponse.java) that will resolve when all the pages have been fetched.
The resolved [RestResponse](../../src/main/java/org/jxapi/netutils/rest/RestResponse.java) carries status and eventual exception of last endpoint call response, and a `List` carrying responses for each page.

*Remark*: When an endpoint supports pagination, the resolved [RestResponse](../../src/main/java/org/jxapi/netutils/rest/RestResponse.java) of a call has `paginated` property set to `true`, and its payload data should be an object implementing [PaginatedRestResponse](../../src/main/java/org/jxapi/netutils/rest/pagination/PaginatedRestResponse.java) interface.

