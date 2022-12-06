# Products API - product-core

API docs are auto generated based on the OpenAPI 3 specification, available at

- http://localhost:8080/v3/api-docs (json format)
- http://localhost:8080/v3/api-docs.yaml (yaml format)

Swagger integration available at http://localhost:8080/swagger-ui.html

Tests can be run with `./mvnw clean test`

## Microservice

### I made the DTOs the basis of my microservice

Does anyone but the developers even cares whether it is using Hibernate or jooq or plain JDBC, as long as the customers have
their DTOs?).

We can rapidly notice that the DTO is the core of the REST API, way before the domain or business services.

So the DTO was created _before_ the domain, and let it drive the design.

### I made the controller agnostic of the core

An important part of the idea is also that the controllers will be de facto _static_ once it will be consumed by at least one
client.

This means that the developer will need its controller to be agnostic (as for the DTOs) of the business implementation. Once
again, does the customer care if the microservice is using a new table which requires to update to a new API?

### Core abstraction

The controller needs to call some business code, whether to access direct domain data, or to compute some complex operations.
So the whole point of making the controller "business-agnostic" is to create a core abstraction, depending only on DTOs.
The contracts of this implementation should be something like:

![image](https://i.imgur.com/hRHSSnu.png)

### Implementing a new core

As we have an abstract core, how do we register cores implementations so that the built application can use them?

It is quite simple: create a factory in the core abstraction, and let your core implementation register against this factory.

Once you do that, your business implementation will be picked by the factory, instead of the controllers directly. Controllers
will only need to integrate the factory.

## Versioning APIs

Following a [REST API versioning guide](https://www.baeldung.com/rest-versioning), I think versioning using content negotiation is
pretty relevant for our purpose:

- we will be able to return different DTO for the same endpoint
- we may simply fetch the `version` from the content type, and fetch our core implementation accordingly
- our controllers will not depend on core implementation, but on the factory providing us core implementation

## Test the microservice

To run the microservice, you can run the following command in the root folder of the project:

```shell script
./mvnw clean package && java -jar controller/target/controller-0.1.0.jar
```

### Datasets

To test version 1, you can use the following calls:

1. Shop is initialized via Flyway. You can retrieve all the products (paginated):

```shell script
   curl -X 'GET' 'http://localhost:8080/shop/product' -H 'version: 1'
```

2. Add a new product with some
   sizes (or update
   them):

```shell script
   curl -X 'POST' 'http://localhost:8080/shop/product' -H 'version: 1' -H 'Content-Type: application/json' -d '{
   "sequence": 2,
   "sizes": [
   {
   "backSoon": true,
   "special": false,
   "quantity": 0
   },
   {
   "backSoon": false,
   "special": false,
   "quantity": 4
   },
   {
   "backSoon": false,
   "special": false,
   "quantity": 5
   }
   ]
   }'
```

3. Test posting a product with invalid
   fields (400 Bad request is expected with error messages in the
   body):

```shell script
   curl -X 'POST' 'http://localhost:8080/shop/product' -H 'version: 1' -H 'Content-Type: application/json' -d '{
   "sequence": -3,
   "sizes": [
   {
   "backSoon": true,
   "special": true,
   "quantity": -2
   }
   ]
   }'
```

```json
[
   "sizes[0].quantity debe ser mayor que o igual a 0",
   "sequence debe ser mayor que o igual a 0"
]
```

4. You can retrieve a product:

```shell script
curl -X 'GET' 'http://localhost:8080/shop/product/1' -H 'version: 1'
```

## Exercise: the algorithm

The algorithm is developed in `product-core` module in `com.inditex.visibilityalgorithm.core.csv.ProductVisibility.visibleIds()`
method and tested
in `com.inditex.visibilityalgorithm.core.csv.ProductVisibilityTest.shouldReturnVisibleProductsOnlyOrderedBySequenceField()` test.

### Order of time complexity

Let's define `p` as the number of products, `s` as the number of sizes and `t` as the number of stocks.

The algorithm reads the sizes (`s`) + reads the stocks (`t`) + reads the products (`p`) + matches sizes and products (`s`) +
filters visible products (`p + p*3*s`) + adds visible products to Treeset (`log2(p)`) + iterates over visible products in order to
extract the ids (`< p`).

We can see three orders of complexity:

- lineal: `O(n)`
- Quadratic: `O(n^2)`
- Logarithmic `O(log2(n))`

If we consider the most complex order, we can conclude that the order of time complexity of the algorithm is `O(n^2)`.
