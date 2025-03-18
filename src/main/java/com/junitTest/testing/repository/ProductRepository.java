package com.junitTest.testing.repository;

import com.junitTest.testing.model.Category;
import com.junitTest.testing.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ProductRepository {
    private final Map<String, Product> products = new HashMap<>();

    public void save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Sản phẩm không được null");
        }
        products.put(product.getId(), product);
    }

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public List<Product> findByCategory(Category category) {
        return products.values().stream()
                .filter(product -> product.getCategory() == category)
                .collect(Collectors.toList());
    }

    public List<Product> findByPriceRange(BigDecimal min, BigDecimal max) {
        return products.values().stream()
                .filter(product -> product.getPrice().compareTo(min) >= 0 &&
                        product.getPrice().compareTo(max) <= 0)
                .collect(Collectors.toList());
    }

    public List<Product> findByCreatedDateBetween(LocalDate start, LocalDate end) {
        return products.values().stream()
                .filter(product -> !product.getCreatedDate().isBefore(start) &&
                        !product.getCreatedDate().isAfter(end))
                .collect(Collectors.toList());
    }

    public void delete(String id) {
        products.remove(id);
    }

    public void deleteAll() {
        products.clear();
    }

    public long count() {
        return products.size();
    }
}
