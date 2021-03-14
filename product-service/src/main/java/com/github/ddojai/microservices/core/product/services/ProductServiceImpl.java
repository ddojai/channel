package com.github.ddojai.microservices.core.product.services;

import com.github.ddojai.microservices.api.core.product.Product;
import com.github.ddojai.microservices.api.core.product.ProductService;
import com.github.ddojai.microservices.core.product.persistence.ProductEntity;
import com.github.ddojai.microservices.core.product.persistence.ProductRepository;
import com.github.ddojai.microservices.util.exceptions.InvalidInputException;
import com.github.ddojai.microservices.util.exceptions.NotFoundException;
import com.github.ddojai.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ServiceUtil serviceUtil;

    @Autowired
    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper,
                              ServiceUtil serviceUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Product createProduct(Product body) {
        try {
            ProductEntity entity = mapper.apiToEntity(body);
            ProductEntity newEntity = repository.save(entity);

            return mapper.entityToApi(newEntity);
        } catch (DuplicateKeyException e) {
            throw new InvalidInputException("Duplicate key, Product Id: " + body.getProductId());
        }
    }

    @Override
    public Product getProduct(int productId) {
        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);
        ProductEntity entity =
            repository.findByProductId(productId).orElseThrow(() -> new NotFoundException("No " +
                "product found for productId: " + productId));
        Product response = mapper.entityToApi(entity);
        response.setServiceAddress(serviceUtil.getServiceAddress());

        return response;
    }

    @Override
    public void deleteProduct(int productId) {
        repository.findByProductId(productId).ifPresent(repository::delete);
    }
}
