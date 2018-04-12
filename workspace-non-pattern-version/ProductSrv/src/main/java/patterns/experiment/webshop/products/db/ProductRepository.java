package patterns.experiment.webshop.products.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import patterns.experiment.webshop.products.api.Product;
import patterns.experiment.webshop.products.api.ProductCategory;

public class ProductRepository {

	private AtomicLong productIdCounter;
	private AtomicLong categoryIdCounter;
	private List<Product> products;
	private List<ProductCategory> categories;

	public ProductRepository() {
		this.productIdCounter = new AtomicLong();
		this.categoryIdCounter = new AtomicLong();
		this.products = new ArrayList<Product>(
				Arrays.asList(new Product(productIdCounter.incrementAndGet(), "TestProduct1", 1, 12.5, 5),
						new Product(productIdCounter.incrementAndGet(), "TestProduct2", 1, 13, 4),
						new Product(productIdCounter.incrementAndGet(), "TestProduct3", 2, 15, 3),
						new Product(productIdCounter.incrementAndGet(), "TestProduct4", 2, 3.99, 2),
						new Product(productIdCounter.incrementAndGet(), "TestProduct5", 3, 7.20, 1)));
		this.categories = new ArrayList<ProductCategory>(Arrays.asList(
				new ProductCategory(categoryIdCounter.incrementAndGet(), "TestCategory1", 0,
						new ArrayList<String>(Arrays.asList("tag1"))),
				new ProductCategory(categoryIdCounter.incrementAndGet(), "TestCategory2", 0,
						new ArrayList<String>(Arrays.asList("tag2"))),
				new ProductCategory(categoryIdCounter.incrementAndGet(), "TestCategory3", 0,
						new ArrayList<String>(Arrays.asList("tag3")))));
	}

	// Product methods

	public List<Product> searchProducts(int limit) {
		if (limit > 0) {
			return products.subList(0, Math.min(products.size(), limit));
		}
		return products;
	}

	public Product getProductById(long productId) {
		Product foundProduct = null;

		for (Product product : products) {
			if (product.getId() == productId) {
				foundProduct = product;
				break;
			}
		}

		return foundProduct;
	}

	public Product storeProduct(Product product) {
		final Product createdProduct = new Product(productIdCounter.incrementAndGet(), product.getName(),
				product.getCategoryId(), product.getPrice(), product.getAvailableAmount());
		this.products.add(createdProduct);

		return createdProduct;
	}

	public Product updateProduct(long productId, Product product) {
		final Product updatedProduct = product;

		return updatedProduct;
	}

	public boolean deleteProductById(long productId) {

		return true;
	}

	public int getAvailableProductAmount(long productId) {
		int availableAmount = -1;

		// Simulate a complex availability check
		try {
			TimeUnit.SECONDS.sleep(1);
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

	// Product category methods

	public List<ProductCategory> searchCategories(int limit) {
		if (limit > 0) {
			return categories.subList(0, Math.min(categories.size(), limit));
		}
		return categories;
	}

	public ProductCategory getCategoryById(long categoryId) {
		ProductCategory foundCategory = null;

		for (ProductCategory category : categories) {
			if (category.getId() == categoryId) {
				foundCategory = category;
				break;
			}
		}

		return foundCategory;
	}

	public ProductCategory storeCategory(ProductCategory category) {
		final ProductCategory createdCategory = new ProductCategory(categoryIdCounter.incrementAndGet(),
				category.getName(), category.getParentCategoryId(), category.getTags());
		this.categories.add(createdCategory);

		return createdCategory;
	}

	public ProductCategory updateCategory(long categoryId, ProductCategory category) {
		final ProductCategory updatedCategory = category;

		return updatedCategory;
	}

	public boolean deleteCategoryById(long categoryId) {

		return true;
	}

}
