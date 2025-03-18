package com.junitTest.testing.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.junitTest.testing.model.Category;
import com.junitTest.testing.model.Product;
import com.junitTest.testing.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class) // Viết unit test kết hợp với Mockito
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    private Product laptop;
    private Product phone;

    @BeforeEach
    void setUp() {
        laptop = new Product("Laptop", new BigDecimal("1000"), 5, Category.ELECTRONICS);
        phone = new Product("Phone", new BigDecimal("500"), 10, Category.ELECTRONICS);
    }

    @Test
    @DisplayName("Kiểm tra phương thức addProduct() với sản phẩm hợp lệ")
    void testAddProductWithValidProduct() {
        service.addProduct(laptop);
        verify(repository, times(1)).save(laptop);
    }

    @Test
    @DisplayName("Kiểm tra phương thức addProduct() với giá âm")
    void testAddProductWithNegativePrice() {
        Product invalidProduct = new Product("Invalid", new BigDecimal("-10"), 5, Category.ELECTRONICS);
        assertThrows(IllegalArgumentException.class, () -> service.addProduct(invalidProduct));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Kiểm tra phương thức getProductById() với ID hợp lệ")
    void testGetProductByIdWithValidId() {
        when(repository.findById(laptop.getId())).thenReturn(Optional.of(laptop));
        Product result = service.getProductById(laptop.getId());
        assertNotNull(result);
        assertEquals(laptop, result);
        verify(repository, times(1)).findById(laptop.getId());
    }

    @Test
    @DisplayName("Kiểm tra phương thức getProductById() với ID không tồn tại")
    void testGetProductByIdWithNonExistentId() {
        String nonExistentId = "non-existent-id";
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.getProductById(nonExistentId));
        verify(repository, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("Kiểm tra phương thức getAllProducts()")
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(laptop, phone);
        when(repository.findAll()).thenReturn(products);
        List<Product> result = service.getAllProducts();
        assertEquals(2, result.size());
        assertEquals(products, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Kiểm tra phương thức sellProduct() với số lượng hợp lệ")
    void testSellProductWithValidQuantity() {
        when(repository.findById(laptop.getId())).thenReturn(Optional.of(laptop));
        int initialQuantity = laptop.getQuantity();
        int sellAmount = 2;
        service.sellProduct(laptop.getId(), sellAmount);
        assertEquals(initialQuantity - sellAmount, laptop.getQuantity());
        verify(repository, times(1)).findById(laptop.getId());
        verify(repository, times(1)).save(laptop);
    }

    @Test
    @DisplayName("Kiểm tra phương thức sellProduct() với số lượng âm")
    void testSellProductWithNegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () -> service.sellProduct(laptop.getId(), -1));
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Kiểm tra phương thức calculateInventoryValue()")
    void testCalculateInventoryValue() {
        List<Product> products = Arrays.asList(laptop, phone);
        when(repository.findAll()).thenReturn(products);
        BigDecimal expectedValue = new BigDecimal("10000");
        BigDecimal result = service.calculateInventoryValue();
        assertEquals(expectedValue, result);
        verify(repository, times(1)).findAll();
    }
}
