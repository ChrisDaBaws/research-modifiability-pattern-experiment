package experiment.webshop.products.resources;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.codahale.metrics.annotation.Timed;

import experiment.webshop.products.api.BaseResponse;
import experiment.webshop.products.api.Product;
import experiment.webshop.products.api.ProductAvailabilityCheckResponse;
import experiment.webshop.products.api.ProductCategory;
import experiment.webshop.products.db.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
	private Client restClient;
	private final long defaultCategoryId;
	private ProductRepository productRepository;
	private Logger log;

	public ProductResource(Client restClient, long defaultCategoryId, ProductRepository repository) {
		this.restClient = restClient;
		this.defaultCategoryId = defaultCategoryId;
		this.productRepository = repository;
		this.log = LoggerFactory.getLogger(ProductResource.class);
		log.info("ProductResource instantiated...");
	}

	// Product resources

	@Path("/products")
	@GET
	@Timed
	public List<Product> getProducts(@QueryParam("limit") @DefaultValue("20") IntParam limit) {
		final List<Product> products = productRepository.searchProducts(limit.get());

		return products;
	}

	@Path("/products/{id}")
	@GET
	@Timed
	public Product getProductById(@PathParam("id") LongParam productId) {
		final Product product = productRepository.getProductById(productId.get());

		if (product == null) {
			final String msg = String.format("Product with ID %d does not exist...", productId.get());
			throw new WebApplicationException(msg, Status.NOT_FOUND);
		}

		return product;
	}

	@Path("/products")
	@POST
	@Timed
	public BaseResponse createProduct(@NotNull @Valid Product product) {
		if (product.getCategoryId() == 0) {
			product.setCategoryId(defaultCategoryId);
		}
		final Product createdProduct = productRepository.storeProduct(product);

		return new BaseResponse("OK", 201, "Product with ID " + createdProduct.getId() + " successfully created.");
	}

	@Path("/products/{id}")
	@PUT
	@Timed
	public BaseResponse updateProduct(@PathParam("id") LongParam productId, @NotNull @Valid Product product) {
		final Product updatedProduct = productRepository.updateProduct(productId.get(), product);

		return new BaseResponse("OK", 204, "Product with ID " + updatedProduct.getId() + " successfully updated.");
	}

	@Path("/products/{id}")
	@DELETE
	@Timed
	public BaseResponse deleteProduct(@PathParam("id") LongParam productId) {
		final boolean deleted = productRepository.deleteProductById(productId.get());

		return new BaseResponse(deleted ? "OK" : "FAILED", deleted ? 202 : 400,
				deleted ? "Product with ID " + productId.get() + " successfully deleted."
						: "Failed to delete product with ID " + productId.get() + ".");
	}

	// Warehouse resources

	@Path("/products/{id}/availability")
	@GET
	@Timed
	public ProductAvailabilityCheckResponse checkProductAvailability(@PathParam("id") LongParam productId,
			@QueryParam("amount") @DefaultValue("1") IntParam requestedAmount) {

		log.info("Checking availability for product with ID " + productId.get() + " for the amount of "
				+ requestedAmount.get() + "...");
		final int availableAmount = productRepository.getAvailableProductAmount(productId.get());

		if (availableAmount == -1) {
			final String msg = String.format("Product with ID %d does not exist...", productId.get());
			throw new WebApplicationException(msg, Status.NOT_FOUND);
		}

		final int MINIMAL_REMAINING_AMOUNT_NECESSARY = 3;
		return new ProductAvailabilityCheckResponse(productId.get(),
				(availableAmount - requestedAmount.get() >= MINIMAL_REMAINING_AMOUNT_NECESSARY), requestedAmount.get());
	}

	@Path("/products/{id}/availability")
	@PUT
	@Timed
	public BaseResponse updateProductAvailability(@PathParam("id") LongParam productId,
			@QueryParam("amount") @DefaultValue("1") IntParam amount) {

		log.info("Setting available amount for product with ID " + productId.get() + " to " + amount.get() + "...");
		productRepository.setAvailableProductAmount(productId.get(), amount.get());

		return new BaseResponse("OK", 200,
				"Available amount for product with ID " + productId.get() + " successfully updated.");
	}

	// Product category resources

	@Path("/categories")
	@GET
	@Timed
	public List<ProductCategory> getCategories(@QueryParam("limit") @DefaultValue("20") IntParam limit) {
		final List<ProductCategory> categories = productRepository.searchCategories(limit.get());

		return categories;
	}

	@Path("/categories/{id}")
	@GET
	@Timed
	public ProductCategory getCategoryById(@PathParam("id") LongParam categoryId) {
		final ProductCategory category = productRepository.getCategoryById(categoryId.get());

		if (category == null) {
			final String msg = String.format("Category with ID %d does not exist...", categoryId.get());
			throw new WebApplicationException(msg, Status.NOT_FOUND);
		}

		return category;
	}

	@Path("/categories")
	@POST
	@Timed
	public BaseResponse createCategory(@NotNull @Valid ProductCategory category) {
		final ProductCategory createdCategory = productRepository.storeCategory(category);

		return new BaseResponse("OK", 201, "Category with ID " + createdCategory.getId() + " successfully created.");
	}

	@Path("/categories/{id}")
	@PUT
	@Timed
	public BaseResponse updateCategory(@PathParam("id") LongParam categoryId,
			@NotNull @Valid ProductCategory category) {
		final ProductCategory updatedCategory = productRepository.updateCategory(categoryId.get(), category);

		return new BaseResponse("OK", 204, "Category with ID " + updatedCategory.getId() + " successfully updated.");
	}

	@Path("/categories/{id}")
	@DELETE
	@Timed
	public BaseResponse deleteCategory(@PathParam("id") LongParam categoryId) {
		final boolean deleted = productRepository.deleteCategoryById(categoryId.get());

		return new BaseResponse(deleted ? "OK" : "FAILED", deleted ? 202 : 400,
				deleted ? "Category with ID " + categoryId.get() + " successfully deleted."
						: "Failed to delete category with ID " + categoryId.get() + ".");
	}

}