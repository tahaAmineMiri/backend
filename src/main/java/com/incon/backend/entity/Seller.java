package com.incon.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("SELLER")
public class Seller extends User {

    @OneToMany(mappedBy = "productSeller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> sellerProducts = new ArrayList<>();

    // Additional seller-specific methods
    public void addProduct(Product product) {
        sellerProducts.add(product);
    }

    public void removeProduct(Product product) {
        sellerProducts.remove(product);
    }
}