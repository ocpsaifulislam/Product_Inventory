package com.ostad.product.service;

import com.ostad.product.entity.Product;
import com.ostad.product.exception.InvalidSkuFormatException;
import com.ostad.product.exception.ProductNotFoundException;
import com.ostad.product.exception.SkuAlreadyExistsException;
import com.ostad.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    private static final Pattern SKU_PATTERN = Pattern.compile("^SKU-[A-Za-z0-9]{8}$");

    @Transactional
    public Product createProduct(Product product) {
        log.debug("Creating product with data: {}", product);
        validateSkuFormat(product.getSku());
        validateSkuUniqueness(product.getSku());
        Product savedProduct = productRepository.save(product);
        log.info("Product created with ID: {} and SKU: {}", savedProduct.getId(), savedProduct.getSku());
        return savedProduct;
    }


    public List<Product> getAllProducts() {
        log.debug("Fetching all products");
        List<Product> products = productRepository.findAll();
        log.info("Retrieved {} products", products.size());
        return products;
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        log.debug("Fetching product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to find product with ID: {}", id);
                    return new ProductNotFoundException(id);
                });

        log.info("Retrieved product with ID: {} and SKU: {}", product.getId(), product.getSku());
        return product;
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        log.debug("Updating product with ID: {} with data: {}", id, updatedProduct);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to find product with ID: {} for update", id);
                    return new ProductNotFoundException(id);
                });

        // Ensure the SKU is not being changed
        if (!existingProduct.getSku().equals(updatedProduct.getSku())) {
            log.warn("Attempt to change SKU from '{}' to '{}' for product ID: {}",
                    existingProduct.getSku(), updatedProduct.getSku(), id);
            throw new InvalidSkuFormatException(
                    "SKU cannot be changed. Existing SKU: '" + existingProduct.getSku()
                            + "', attempted new SKU: '" + updatedProduct.getSku() + "'");
        }// Update allowed fields
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setStatus(updatedProduct.getStatus());

        Product savedProduct = productRepository.save(existingProduct);
        log.info("Product updated with ID: {} and SKU: {}", savedProduct.getId(), savedProduct.getSku());

        return savedProduct;
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.debug("Deleting product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to find product with ID: {} for deletion", id);
                    return new ProductNotFoundException(id);
                });

        productRepository.delete(product);
        log.info("Product deleted with ID: {} and SKU: {}", product.getId(), product.getSku());
    }

    //    Validates that the SKU matches the required format: SKU- followed by 8 alphanumeric characters.
    private void validateSkuFormat(String sku) {
        if (sku == null || !SKU_PATTERN.matcher(sku).matches()) {
            log.warn("Invalid SKU format detected: '{}'", sku);
            throw new InvalidSkuFormatException(sku);
        }
        log.debug("SKU format validation passed for: {}", sku);
    }

    //    Validates that the SKU does not already exist in the database.
    private void validateSkuUniqueness(String sku) {
        if (productRepository.existsBySku(sku)) {
            log.warn("Duplicate SKU detected: '{}'", sku);
            throw new SkuAlreadyExistsException(sku);
        }
        log.debug("SKU uniqueness validation passed for: {}", sku);
    }


}
