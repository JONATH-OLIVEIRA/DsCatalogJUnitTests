package com.estudosjonath.dscatalog.tests;

import java.time.Instant;

import com.estudosjonath.dscatalog.dto.ProductDTO;
import com.estudosjonath.dscatalog.entities.Category;
import com.estudosjonath.dscatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L, "PHONE", "PC Gamer", 1200.0, "https://img.cood phone",
				Instant.parse("2020-10-20T03:00:00Z"));
		product.getCategories().add(new Category(2L, "Eletronics"));
		return product;
	}

	public static ProductDTO createProductDTO() {
		Product product = createProduct();

		return new ProductDTO(product, product.getCategories());
	}

}
