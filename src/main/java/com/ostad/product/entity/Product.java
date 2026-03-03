package com.ostad.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Product name must not be blank")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotBlank(message = "SKU must not be blank")
    @Column(unique = true)
    private String sku;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be a positive number")
    private Double price;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 0, message = "Quantity must be zero or more")
    private Integer quantity;

    @NotNull(message = "Status must not be null")
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
}
