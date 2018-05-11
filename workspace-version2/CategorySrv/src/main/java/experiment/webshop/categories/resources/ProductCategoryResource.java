package experiment.webshop.categories.resources;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import experiment.webshop.categories.db.ProductCategoryRepository;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ProductCategoryResource {
	private ProductCategoryRepository categoryRepository;
	private Logger log;

	public ProductCategoryResource(ProductCategoryRepository repository) {
		this.categoryRepository = repository;
		this.log = LoggerFactory.getLogger(ProductCategoryResource.class);
		log.info("ProductCategoryResource instantiated...");
	}

}