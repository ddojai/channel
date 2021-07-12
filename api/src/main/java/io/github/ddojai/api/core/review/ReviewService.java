package io.github.ddojai.api.core.review;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

public interface ReviewService {

    /**
     * Sample usage:
     *
     * curl -X POST $HOST:$PORT/review \
     *   -H "Content-Type: application/json" --data \
     *   '{"productId":123,"reviewId":456,"author":"me","subject":"yada, yada, yada","content":"yada, yada, yada"}'
     */
    @PostMapping(
        value    = "/review",
        consumes = "application/json",
        produces = "application/json")
    Review createReview(@RequestBody Review body);

    /**
     * Sample usage: curl $HOST:$PORT/review?productId=1
     */
    @GetMapping(
        value    = "/review",
        produces = "application/json")
    Flux<Review> getReviews(@RequestParam(value = "productId", required = true) int productId);

    /**
     * Sample usage:
     *
     * curl -X DELETE $HOST:$PORT/review?productId=1
     */
    @DeleteMapping(value = "/review")
    void deleteReviews(@RequestParam(value = "productId", required = true)  int productId);
}