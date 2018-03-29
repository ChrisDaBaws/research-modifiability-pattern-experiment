package jreb.research.patterns.experiment.webshop;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import jreb.research.patterns.experiment.webshop.db.ProductRepository;
import jreb.research.patterns.experiment.webshop.health.StandardHealthCheck;
import jreb.research.patterns.experiment.webshop.resources.ProductResource;

public class ProductSrvApplication extends Application<ProductSrvConfiguration> {

	public static void main(final String[] args) throws Exception {
		new ProductSrvApplication().run(args);
	}

	private ProductRepository productRepository;

	@Override
	public String getName() {
		return "ProductSrv";
	}

	@Override
	public void initialize(final Bootstrap<ProductSrvConfiguration> bootstrap) {
		this.productRepository = new ProductRepository();
	}

	@Override
	public void run(final ProductSrvConfiguration configuration, final Environment environment) {

		final ProductResource productResource = new ProductResource(configuration.getDefaultCategoryId(),
				productRepository);

		final StandardHealthCheck healthCheck = new StandardHealthCheck(configuration.getDefaultCategoryId());

		environment.healthChecks().register("template", healthCheck);
		environment.jersey().register(productResource);
	}

}
