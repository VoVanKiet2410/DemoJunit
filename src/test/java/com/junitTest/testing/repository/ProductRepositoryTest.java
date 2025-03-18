package com.junitTest.testing.repository;

import com.junitTest.testing.model.Category;
import com.junitTest.testing.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductRepositoryTest {
    private ProductRepository repository;
    private Product laptop;
    private Product phone;
    private Product book;

    @BeforeEach
    void setUp() {
        repository = new ProductRepository();

        laptop = new Product("Laptop", new BigDecimal("1000"), 5, Category.ELECTRONICS);
        phone = new Product("Phone", new BigDecimal("500"), 10, Category.ELECTRONICS);
        book = new Product("Book", new BigDecimal("20"), 100, Category.BOOKS);

        repository.save(laptop);
        repository.save(phone);
        repository.save(book);
    }

    @Nested
    @DisplayName("Tests for save method")
    class SaveTests {
        @Test
        @DisplayName("Kiểm tra phương thức save() với sản phẩm hợp lệ")
        void testSaveValidProduct() {
            // Arrange
            Product newProduct = new Product("TV", new BigDecimal("1500"), 3, Category.ELECTRONICS);

            // Act
            repository.save(newProduct);
            Optional<Product> found = repository.findById(newProduct.getId());

            // Assert
            assertTrue(found.isPresent(), "Sản phẩm phải được tìm thấy sau khi lưu");
            assertEquals(newProduct, found.get(), "Sản phẩm trả về phải giống với sản phẩm đã lưu");
        }

        @Test
        @DisplayName("Kiểm tra phương thức save() với sản phẩm null")
        void testSaveNullProduct() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> repository.save(null),"Phải ném ngoại lệ khi lưu sản phẩm null");
        }
    }

    @Nested
    @DisplayName("Tests for find methods")
    class FindTests {
        @Test
        @DisplayName("Kiểm tra phương thức findById() với ID hợp lệ")
        void testFindByIdWithValidId() {
            // Act
            Optional<Product> found = repository.findById(laptop.getId());

            // Assert
            assertTrue(found.isPresent(), "Sản phẩm phải được tìm thấy khi ID hợp lệ");
            assertEquals(laptop, found.get(), "Sản phẩm trả về phải đúng");
        }

        @Test
        @DisplayName("Kiểm tra phương thức findById() với ID không tồn tại")
        void testFindByIdWithNonExistentId() {
            // Act
            Optional<Product> found = repository.findById("non-existent-id");

            // Assert
            assertFalse(found.isPresent(), "Không nên tìm thấy sản phẩm với ID không tồn tại");
        }

        @Test
        @DisplayName("Kiểm tra phương thức findAll()")
        void testFindAll() {
            // Act
            List<Product> products = repository.findAll();

            // Assert
            assertEquals(3, products.size(), "Phải trả về đúng số lượng sản phẩm");
            assertTrue(products.contains(laptop), "Danh sách phải chứa laptop");
            assertTrue(products.contains(phone), "Danh sách phải chứa phone");
            assertTrue(products.contains(book), "Danh sách phải chứa book");
        }

        @Test
        @DisplayName("Kiểm tra phương thức findByCategory()")
        void testFindByCategory() {
            // Act
            List<Product> electronicsProducts = repository.findByCategory(Category.ELECTRONICS);
            List<Product> bookProducts = repository.findByCategory(Category.BOOKS);
            List<Product> foodProducts = repository.findByCategory(Category.FOOD);

            // Assert
            assertEquals(2, electronicsProducts.size(), "Phải có 2 sản phẩm thuộc danh mục ELECTRONICS");
            assertTrue(electronicsProducts.contains(laptop), "Danh sách phải chứa laptop");
            assertTrue(electronicsProducts.contains(phone), "Danh sách phải chứa phone");

            assertEquals(1, bookProducts.size(), "Phải có 1 sản phẩm thuộc danh mục BOOKS");
            assertTrue(bookProducts.contains(book), "Danh sách phải chứa book");

            assertEquals(0, foodProducts.size(), "Không có sản phẩm thuộc danh mục FOOD");
        }

        @Test
        @DisplayName("Kiểm tra phương thức findByPriceRange()")
        void testFindByPriceRange() {
            // Act
            List<Product> expensiveProducts = repository.findByPriceRange(
                    new BigDecimal("500"), new BigDecimal("2000"));
            List<Product> cheapProducts = repository.findByPriceRange(
                    new BigDecimal("0"), new BigDecimal("100"));

            // Assert
            assertEquals(2, expensiveProducts.size(), "Phải có 2 sản phẩm trong khoảng giá đắt");
            assertTrue(expensiveProducts.contains(laptop), "Danh sách phải chứa laptop");
            assertTrue(expensiveProducts.contains(phone), "Danh sách phải chứa phone");

            assertEquals(1, cheapProducts.size(), "Phải có 1 sản phẩm trong khoảng giá rẻ");
            assertTrue(cheapProducts.contains(book), "Danh sách phải chứa book");
        }
    }

    @Nested
    @DisplayName("Tests for delete methods")
    class DeleteTests {
        @Test
        @DisplayName("Kiểm tra phương thức delete()")
        void testDelete() {
            // Act
            repository.delete(laptop.getId());
            Optional<Product> found = repository.findById(laptop.getId());

            // Assert
            assertFalse(found.isPresent(), "Sản phẩm không nên tồn tại sau khi xóa");
            assertEquals(2, repository.count(), "Kho phải chứa 2 sản phẩm sau khi xóa 1");
        }

        @Test
        @DisplayName("Kiểm tra phương thức deleteAll()")
        void testDeleteAll() {
            // Act
            repository.deleteAll();

            // Assert
            assertEquals(0, repository.count(), "Kho phải rỗng sau khi xóa tất cả");
            assertTrue(repository.findAll().isEmpty(), "findAll() phải trả về danh sách rỗng");
        }
    }
}
