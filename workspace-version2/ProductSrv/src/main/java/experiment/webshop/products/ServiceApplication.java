package experiment.webshop.products;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import experiment.webshop.products.db.ProductCategoryRepository;
import experiment.webshop.products.db.ProductRepository;
import experiment.webshop.products.db.WarehouseRepository;
import experiment.webshop.products.health.StandardHealthCheck;
import experiment.webshop.products.resources.ProductCategoryResource;
import experiment.webshop.products.resources.ProductResource;
import experiment.webshop.products.resources.WarehouseResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ServiceApplication extends Application<ServiceConfiguration> {

	public static void main(final String[] args) throws Exception {
		new ServiceApplication().run(args);
	}

	@Override
	public String getName() {
		return "ProductSrv";
	}

	@Override
	public void initialize(final Bootstrap<ServiceConfiguration> bootstrap) {
	}

	@Override
	public void run(final ServiceConfiguration configuration, final Environment environment) {
		
		final StandardHealthCheck healthCheck = new StandardHealthCheck(configuration.getDefaultCategoryId());

		// JSON pretty print
		ObjectMapper mapper = environment.getObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		// CORS filter
		final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
		cors.setInitParameter("allowedOrigins", "*");
		cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
		cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
		cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

		environment.healthChecks().register("template", healthCheck);
		
		// Product resource
		final ProductRepository productRepository = new ProductRepository();
		final ProductResource productResource = new ProductResource(configuration.getDefaultCategoryId(),
				productRepository);
		environment.jersey().register(productResource);
		
		// Category resource
		final ProductCategoryRepository categoryRepository = new ProductCategoryRepository();
		final ProductCategoryResource categoryResource = new ProductCategoryResource(categoryRepository);
		environment.jersey().register(categoryResource);
		
		// Warehouse resource
		final WarehouseRepository warehouseRepository = new WarehouseRepository();
		final WarehouseResource warehouseResource = new WarehouseResource(warehouseRepository);
		environment.jersey().register(warehouseResource);
	}

}
