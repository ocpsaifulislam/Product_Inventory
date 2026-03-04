package com.ostad.product.controller;

import com.ostad.product.entity.AppResponse;
import com.ostad.product.entity.Product;
import com.ostad.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        log.debug("Received request to create product: {}", product);

        Product createdProduct = productService.createProduct(product);
        log.info("Successfully created product with ID: {}", createdProduct.getId());

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        log.debug("Received request to fetch all products");
        List<Product> products = productService.getAllProducts();
        log.info("Returning {} products", products.size());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        log.debug("Received request to fetch product with ID: {}", id);

        Product product = productService.getProductById(id);
        log.info("Successfully returned product with ID: {}", id);

        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                 @Valid @RequestBody Product product) {
        log.debug("Received request to update product with ID: {} with data: {}", id, product);

        Product updatedProduct = productService.updateProduct(id, product);
        log.info("Successfully updated product with ID: {}", id);

        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse> deleteProduct(@PathVariable Long id,
                                                     HttpServletRequest request) {
        log.debug("Received request to delete product with ID: {}", id);

        productService.deleteProduct(id);
        log.info("Successfully deleted product with ID: {}", id);
        AppResponse response = new AppResponse(
                200,
                null,
                "Product deleted successfully",
                request.getRequestURI()
        );
//        return ResponseEntity.noContent().build();
//        return ResponseEntity.ok("Product deleted successfully");
        return ResponseEntity.ok(response);
    }


}
