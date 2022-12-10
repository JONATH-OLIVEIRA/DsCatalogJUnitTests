package com.estudosjonath.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.estudosjonath.dscatalog.entities.Product;
import com.estudosjonath.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	private long exintingId;
	private long nonExistId;
	private long countTotalProducts;

	@BeforeEach
	void setUp() throws Exception {
		exintingId = 1L;
		nonExistId = 1000L;
		countTotalProducts = 25L;
	}

	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdisNull() {
		Product product = Factory.createProduct();
		product.setId(0);
		product = repository.save(product);

		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {

		repository.deleteById(exintingId);

		Optional<Product> result = repository.findById(exintingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void deleteShouldEmptyResultDataAccessExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistId);
		});
	}

}
