package jreb.research.patterns.experiment.webshop.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import jreb.research.patterns.experiment.webshop.api.Product;

public class ProductRepository {

	private AtomicLong productIdCounter;

	public ProductRepository() {
		this.productIdCounter = new AtomicLong();
	}

	public List<Product> search(int limit) {
		final List<Product> products = new ArrayList<Product>();
		final Product product = new Product(1, "TestProduct", 1, 0.5);
		products.add(product);
		return products;
	}

	public Product getById(long productId) {
		final Product product = new Product(productId, "TestProduct", 1, 0.5);

		return product;
	}

	public Product store(Product product) {
		final Product createdProduct = new Product(productIdCounter.incrementAndGet(), product.getName(),
				product.getCategoryId(), product.getPrice());

		return createdProduct;
	}

	public Product update(long long1, Product product) {
		final Product updatedProduct = product;

		return updatedProduct;
	}

	public boolean deleteById(long productId) {

		return true;
	}

}
