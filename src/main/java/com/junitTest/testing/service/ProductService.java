package com.junitTest.testing.service;

import com.junitTest.testing.model.Category;
import com.junitTest.testing.model.Product;
import com.junitTest.testing.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public void addProduct(Product product) {
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá sản phẩm phải lớn hơn 0");
        }
        repository.save(product);
    }

    public Product getProductById(String id) {
        Optional<Product> product = repository.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new NoSuchElementException("Không tìm thấy sản phẩm với ID: " + id);
        }
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public List<Product> getProductsByCategory(Category category) {
        return repository.findByCategory(category);
    }

    public List<Product> getProductsByPriceRange(BigDecimal min, BigDecimal max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Giá tối thiểu phải nhỏ hơn hoặc bằng giá tối đa");
        }
        return repository.findByPriceRange(min, max);
    }

    public List<Product> getProductsCreatedBetween(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước hoặc bằng ngày kết thúc");
        }
        return repository.findByCreatedDateBetween(start, end);
    }

    public void updateProductQuantity(String id, int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Số lượng không được âm");
        }

        Product product = getProductById(id);
        product.setQuantity(newQuantity);
        repository.save(product);
    }

    public void sellProduct(String id, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng bán phải lớn hơn 0");
        }

        Product product = getProductById(id);
        product.decreaseQuantity(quantity);
        repository.save(product);
    }

    public BigDecimal calculateInventoryValue() {
        return repository.findAll().stream()
                .map(Product::calculateTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void removeProduct(String id) {
        repository.delete(id);
    }

    public void removeAllProducts() {
        repository.deleteAll();
    }
}
