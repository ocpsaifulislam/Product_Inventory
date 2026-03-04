package com.ostad.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@Builder
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    private Long id;

    @NotBlank(message = "Product name must not be blank")
    @Column(nullable=false)
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotBlank(message = "SKU must not be blank")
    @Column(nullable=false,unique = true)
    private String sku;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be a positive number")
    @Column(nullable=false)
    private Double price;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 0, message = "Quantity must be zero or more")
    @Column(nullable=false)
    private Integer quantity;

    @NotNull(message = "Status must not be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private ProductStatus status;
}
