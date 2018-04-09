package patterns.experiment.webshop.products.db;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import patterns.experiment.webshop.products.api.Product;

public class ProductRepository {

	private AtomicLong productIdCounter;
	private List<Product> products;

	public ProductRepository() {
		this.productIdCounter = new AtomicLong();
		this.products = Arrays.asList(new Product(productIdCounter.incrementAndGet(), "TestProduct1", 1, 12.5, 5),
				new Product(productIdCounter.incrementAndGet(), "TestProduct2", 1, 13, 4),
				new Product(productIdCounter.incrementAndGet(), "TestProduct3", 2, 15, 3),
				new Product(productIdCounter.incrementAndGet(), "TestProduct4", 2, 3.99, 2),
				new Product(productIdCounter.incrementAndGet(), "TestProduct5", 3, 7.20, 1));
	}

	public List<Product> search(int limit) {
		if (limit > 0) {
			return products.subList(0, Math.min(products.size(), limit));
		}
		return products;
	}

	public Product getById(long productId) {
		Product foundProduct = null;

		for (Product product : products) {
			if (product.getId() == productId) {
				foundProduct = product;
				break;
			}
		}

		return foundProduct;
	}

	public Product store(Product product) {
		final Product createdProduct = new Product(productIdCounter.incrementAndGet(), product.getName(),
				product.getCategoryId(), product.getPrice(), product.getAvailableAmount());
		this.products.add(createdProduct);

		return createdProduct;
	}

	public Product update(long long1, Product product) {
		final Product updatedProduct = product;

		return updatedProduct;
	}

	public boolean deleteById(long productId) {

		return true;
	}

	public int getAvailableProductAmount(long productId) {
		int availableAmount = -1;

		// Simulate a complex availability check
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (Product product : products) {
			if (product.getId() == productId) {
				availableAmount = product.getAvailableAmount();
				break;
			}
		}

		return availableAmount;
	}

}
