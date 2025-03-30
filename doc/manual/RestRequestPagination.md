# Using the `RestRequestPagination` Class

The `RestRequestPagination` class provides utility methods to handle REST API requests that return paginated responses. It simplifies fetching all pages of data by automatically handling pagination logic.

It can be used when an REST API is designed with an expected 'object' requests that carries the page index to fetch results for, and next page index can be retrieved from response.

---

## Key Features
- Automatically fetches all pages of a paginated REST API response.
- Merges results from multiple pages into a single response.
- Handles errors gracefully during the pagination process.

---

## Example Usage

Here’s how to use the `RestRequestPagination` class to fetch all pages of a paginated API response:

### Step 1: Define Your API and Request
Assume you have an API interface with a method to fetch a single page of data:
```java
FutureRestResponse<MyApiResponse> getPage(MyApiRequest request);
```

### Step 2: Prepare the Initial Request
Create an instance of your request object with the initial page index set:
```java
MyApiRequest request = new MyApiRequest();
request.setCursor("initialPageIndex");
```

### Step 3: Use `RestRequestPagination.fetchAllPages`
Call the `fetchAllPages` method to fetch all pages of data:
```java
FutureRestResponse<MyApiResponse> response = RestRequestPagination.fetchAllPages(
    request, 
    api::getPage, // API method to fetch a single page
    (index, req) -> req.setCursor(index), // Function to set the next page index in the request
    res -> res.getNextPageCursor(), // Function to retrieve the next page index from the response
    (res1, res2) -> { // Function to merge results from two pages
        List<MyData> combinedList = new ArrayList<>(res1.getData());
        combinedList.addAll(res2.getData());
        res2.setData(combinedList);
        return res2;
    }
);
```

### Step 4: Handle the Response
Once all pages are fetched, process the combined response:
```java
response.thenAccept(finalResponse -> {
    if (finalResponse.isOk()) {
        List<MyData> allData = finalResponse.getData();
        System.out.println("Fetched all data: " + allData);
    } else {
        System.err.println("Error fetching data: " + finalResponse.getError());
    }
});
```

---

## Method Parameters

### `fetchAllPages` Method
```java
public static <R, A> FutureRestResponse<A> fetchAllPages(
    R request,
    Function<R, FutureRestResponse<A>> endpoint,
    BiConsumer<String, R> setRequestIndex,
    Function<A, String> getResponseIndex,
    BinaryOperator<A> responseAccumulator
)
```

| Parameter             | Description                                                                                    |
|-----------------------|------------------------------------------------------------------------------------------------|
| `request`             | The initial request object with the first page index set.                                      |
| `endpoint`            | Function to fetch a single page of data.                                                       |
| `setRequestIndex`     | Function to set the next page index in the request object.                                     |
| `getResponseIndex`    | Function to retrieve the next page index from the response, or `null` if last page is reached  |
| `responseAccumulator` | Function to merge results from two pages into a single response object.                        |

---

## Notes
- **Thread Safety**: Ensure that the request object is thread-safe if reused across multiple threads.
- **Error Handling**: If an error occurs while fetching a page, the response will contain the error details.
- **Logging**: The class uses SLF4J for logging. Ensure proper logging configuration in your application.

---