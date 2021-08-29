package io.github.ddojai.channel.core.product;

import io.github.ddojai.api.core.product.Product;
import io.github.ddojai.api.event.Event;
import io.github.ddojai.channel.core.product.persistence.ProductRepository;
import io.github.ddojai.util.exceptions.InvalidInputException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.http.HttpStatus;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static io.github.ddojai.api.event.Event.Type.CREATE;
import static io.github.ddojai.api.event.Event.Type.DELETE;
import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"spring.data.mongodb.port: 0", "spring.cloud.config.enabled=false", "server.error.include-message=always"})
public class ProductServiceApplicationTests {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private Sink channels;

    private AbstractMessageChannel input = null;

    @Before
    public void setupDb() {
        input = (AbstractMessageChannel) channels.input();
        repository.deleteAll().block();
    }

    @Test
    public void getProductById() {

        int productId = 1;

        assertNull(repository.findByProductId(productId).block());
        assertEquals(0, (long)repository.count().block());

        sendCreateProductEvent(productId);

        assertNotNull(repository.findByProductId(productId).block());
        assertEquals(1, (long)repository.count().block());

        getAndVerifyProduct(productId, OK)
            .jsonPath("$.productId").isEqualTo(productId);
    }

    @Test
    public void duplicateError() {

        int productId = 1;

        assertNull(repository.findByProductId(productId).block());

        sendCreateProductEvent(productId);

        assertNotNull(repository.findByProductId(productId).block());

        try {
            sendCreateProductEvent(productId);
            fail("Expected a MessagingException here!");
        } catch (MessagingException me) {
            if (me.getCause() instanceof InvalidInputException)	{
                InvalidInputException iie = (InvalidInputException)me.getCause();
                assertEquals("Duplicate key, Product Id: " + productId, iie.getMessage());
            } else {
                fail("Expected a InvalidInputException as the root cause!");
            }
        }
    }

    @Test
    public void deleteProduct() {

        int productId = 1;

        sendCreateProductEvent(productId);
        assertNotNull(repository.findByProductId(productId).block());

        sendDeleteProductEvent(productId);
        assertNull(repository.findByProductId(productId).block());

        sendDeleteProductEvent(productId);
    }

    @Test
    public void getProductInvalidParameterString() {

        getAndVerifyProduct("/no-integer", BAD_REQUEST)
            .jsonPath("$.path").isEqualTo("/product/no-integer")
            .jsonPath("$.message").isEqualTo("Type mismatch.");
    }

    @Test
    public void getProductNotFound() {

        int productIdNotFound = 13;
        getAndVerifyProduct(productIdNotFound, NOT_FOUND)
            .jsonPath("$.path").isEqualTo("/product/" + productIdNotFound)
            .jsonPath("$.message").isEqualTo("No product found for productId: " + productIdNotFound);
    }

    @Test
    public void getProductInvalidParameterNegativeValue() {

        int productIdInvalid = -1;

        getAndVerifyProduct(productIdInvalid, UNPROCESSABLE_ENTITY)
            .jsonPath("$.path").isEqualTo("/product/" + productIdInvalid)
            .jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
    }

    private WebTestClient.BodyContentSpec getAndVerifyProduct(int productId, HttpStatus expectedStatus) {
        return getAndVerifyProduct("/" + productId, expectedStatus);
    }

    private WebTestClient.BodyContentSpec getAndVerifyProduct(String productIdPath, HttpStatus expectedStatus) {
        return client.get()
            .uri("/product" + productIdPath)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isEqualTo(expectedStatus)
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody();
    }

    private void sendCreateProductEvent(int productId) {
        Product product = new Product(productId, "Name " + productId, productId, "SA");
        Event<Integer, Product> event = new Event(CREATE, productId, product);
        input.send(new GenericMessage<>(event));
    }

    private void sendDeleteProductEvent(int productId) {
        Event<Integer, Product> event = new Event(DELETE, productId, null);
        input.send(new GenericMessage<>(event));
    }
}