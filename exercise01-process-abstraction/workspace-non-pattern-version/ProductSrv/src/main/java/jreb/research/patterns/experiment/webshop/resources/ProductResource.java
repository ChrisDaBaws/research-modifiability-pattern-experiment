package jreb.research.patterns.experiment.webshop.resources;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;
import jreb.research.patterns.experiment.webshop.api.Product;
import jreb.research.patterns.experiment.webshop.db.ProductRepository;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
	private final long defaultCategoryId;
	private ProductRepository productRepository;

	public ProductResource(long defaultCategoryId, ProductRepository repository) {
		this.defaultCategoryId = defaultCategoryId;
		this.productRepository = repository;
	}

	@GET
	@Timed
	public List<Product> getProducts(@QueryParam("limit") @DefaultValue("20") IntParam limit) {
		final List<Product> products = productRepository.search(limit.get());

		return products;
	}

	@Path("/{id}")
	@GET
	@Timed
	public Product getProductById(@PathParam("id") LongParam productId) {
		final Product product = productRepository.getById(productId.get());

		return product;
	}

	@POST
	@Timed
	public Response createProduct(@NotNull @Valid Product product) {
		if (product.getCategoryId() == 0) {
			product.setCategoryId(defaultCategoryId);
		}
		final Product createdProduct = productRepository.store(product);

		return Response.created(UriBuilder.fromResource(ProductResource.class).build(createdProduct)).build();

	}

	@Path("/{id}")
	@PUT
	@Timed
	public Response updateProduct(@PathParam("id") LongParam productId, @NotNull @Valid Product product) {
		final Product updatedProduct = productRepository.update(productId.get(), product);

		return Response.created(UriBuilder.fromResource(ProductResource.class).build(updatedProduct)).build();

	}
	
	@Path("/{id}")
	@DELETE
	@Timed
	public Response deleteProduct(@PathParam("id") LongParam productId) {
		final boolean deleted = productRepository.deleteById(productId.get());

		return Response.created(UriBuilder.fromResource(ProductResource.class).build(deleted)).build();

	}
}