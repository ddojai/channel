package io.github.ddojai.api.core.product;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

public interface ProductService {

    /**
     * Sample usage:
     *
     * curl -X POST $HOST:$PORT/product \
     *   -H "Content-Type: application/json" --data \
     *   '{"productId":123,"name":"product 123","weight":123}'
     */
    @PostMapping(
        value    = "/product",
        consumes = "application/json",
        produces = "application/json")
    Product createProduct(@RequestBody Product body);

    /**
     * Sample usage: curl $HOST:$PORT/product/1
     */
    @GetMapping(
        value    = "/product/{productId}",
        produces = "application/json")
    Mono<Product> getProduct(@PathVariable int productId);

    /**
     * Sample usage:
     *
     * curl -X DELETE $HOST:$PORT/product/1
     */
    @DeleteMapping(value = "/product/{productId}")
    void deleteProduct(@PathVariable int productId);
}