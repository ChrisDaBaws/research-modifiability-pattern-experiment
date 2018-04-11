package patterns.experiment.webshop.products;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import patterns.experiment.webshop.products.db.ProductRepository;
import patterns.experiment.webshop.products.health.StandardHealthCheck;
import patterns.experiment.webshop.products.resources.ProductResource;

public class ServiceApplication extends Application<ServiceConfiguration> {

	public static void main(final String[] args) throws Exception {
		new ServiceApplication().run(args);
	}

	private ProductRepository productRepository;

	@Override
	public String getName() {
		return "ProductSrv";
	}

	@Override
	public void initialize(final Bootstrap<ServiceConfiguration> bootstrap) {
		this.productRepository = new ProductRepository();
	}

	@Override
	public void run(final ServiceConfiguration configuration, final Environment environment) {

		final ProductResource productResource = new ProductResource(configuration.getDefaultCategoryId(),
				productRepository);

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
		environment.jersey().register(productResource);
	}

}
