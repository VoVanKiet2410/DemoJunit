package com.junitTest.testing.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
public class ProductTest {
    @Test
    @DisplayName("Kiểm tra phương thức isInStock() khi số lượng > 0")
    void testIsInStockWhenQuantityGreaterThanZero() {
        // Arrange
        Product product = new Product("Laptop", new BigDecimal("1000"), 5, Category.ELECTRONICS);

        // Act & Assert
        assertTrue(product.isInStock(), "Sản phẩm phải ở trạng thái còn hàng khi số lượng > 0");
    }

    @Test
    @DisplayName("Kiểm tra phương thức isInStock() khi số lượng = 0")
    void testIsInStockWhenQuantityIsZero() {
        // Arrange
        Product product = new Product("Laptop", new BigDecimal("1000"), 0, Category.ELECTRONICS);

        // Act & Assert
        assertFalse(product.isInStock(), "Sản phẩm phải ở trạng thái hết hàng khi số lượng = 0");
    }

    @Test
    @DisplayName("Kiểm tra phương thức calculateTotalValue()")
    void testCalculateTotalValue() {
        // Arrange
        Product product = new Product("Laptop", new BigDecimal("1000"), 5, Category.ELECTRONICS);
        BigDecimal expected = new BigDecimal("5000");

        // Act
        BigDecimal result = product.calculateTotalValue();

        // Assert
        assertEquals(expected, result, "Giá trị tổng phải bằng đơn giá * số lượng");
    }

    @Test
    @DisplayName("Kiểm tra phương thức decreaseQuantity() trong trường hợp hợp lệ")
    void testDecreaseQuantityValid() {
        // Arrange
        Product product = new Product("Laptop", new BigDecimal("1000"), 5, Category.ELECTRONICS);
        int expectedQuantity = 3;

        // Act
        product.decreaseQuantity(2);

        // Assert
        assertEquals(expectedQuantity, product.getQuantity(),
                "Số lượng sau khi giảm phải đúng");
    }

    @Test
    @DisplayName("Kiểm tra phương thức decreaseQuantity() với số lượng giảm âm")
    void testDecreaseQuantityWithNegativeAmount() {
        // Arrange
        Product product = new Product("Laptop", new BigDecimal("1000"), 5, Category.ELECTRONICS);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> product.decreaseQuantity(-1),
                "Phải ném ngoại lệ khi số lượng giảm âm");
    }

    @Test
    @DisplayName("Kiểm tra phương thức decreaseQuantity() với số lượng giảm lớn hơn tồn kho")
    void testDecreaseQuantityWithAmountGreaterThanStock() {
        // Arrange
        Product product = new Product("Laptop", new BigDecimal("1000"), 5, Category.ELECTRONICS);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> product.decreaseQuantity(6),
                "Phải ném ngoại lệ khi số lượng giảm lớn hơn tồn kho");
    }

    @ParameterizedTest
    @MethodSource("provideProductsForTotalValueCalculation")
    @DisplayName("Kiểm tra tính toán giá trị tổng với nhiều trường hợp")
    void testCalculateTotalValueWithMultipleScenarios(String name, BigDecimal price,
                                                      int quantity, BigDecimal expectedTotal) {
        // Arrange
        Product product = new Product(name, price, quantity, Category.ELECTRONICS);

        // Act
        BigDecimal result = product.calculateTotalValue();

        // Assert
        assertEquals(expectedTotal, result,
                () -> "Giá trị tổng của " + name + " phải bằng " + expectedTotal);
    }

    private static Stream<Arguments> provideProductsForTotalValueCalculation() {
        return Stream.of(
                Arguments.of("Laptop", new BigDecimal("1000"), 5, new BigDecimal("5000")),
                Arguments.of("Phone", new BigDecimal("500"), 10, new BigDecimal("5000")),
                Arguments.of("Tablet", new BigDecimal("300"), 0, new BigDecimal("0")),
                Arguments.of("TV", new BigDecimal("2000"), 1, new BigDecimal("2000"))
        );
    }
}
