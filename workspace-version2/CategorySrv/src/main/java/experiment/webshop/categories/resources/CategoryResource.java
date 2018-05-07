package experiment.webshop.categories.resources;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import experiment.webshop.categories.db.CategoryRepository;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {
	private CategoryRepository categoryRepository;
	private Logger log;

	public CategoryResource(CategoryRepository repository) {
		this.categoryRepository = repository;
		this.log = LoggerFactory.getLogger(CategoryResource.class);
		log.info("CategoryResource instantiated...");
	}

}