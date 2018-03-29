package jreb.research.patterns.experiment.webshop;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import jreb.research.patterns.experiment.webshop.db.CustomerRepository;
import jreb.research.patterns.experiment.webshop.health.StandardHealthCheck;
import jreb.research.patterns.experiment.webshop.resources.CustomerResource;

public class CustomerSrvApplication extends Application<CustomerSrvConfiguration> {

	public static void main(final String[] args) throws Exception {
		new CustomerSrvApplication().run(args);
	}

	private CustomerRepository customerRepository;

	@Override
	public String getName() {
		return "CustomerSrv";
	}

	@Override
	public void initialize(final Bootstrap<CustomerSrvConfiguration> bootstrap) {
		this.customerRepository = new CustomerRepository();
	}

	@Override
	public void run(final CustomerSrvConfiguration configuration, final Environment environment) {

		final CustomerResource customerResource = new CustomerResource(configuration.getDefaultCreditRating(),
				customerRepository);

		final StandardHealthCheck healthCheck = new StandardHealthCheck(configuration.getDefaultCreditRating());

		environment.healthChecks().register("template", healthCheck);
		environment.jersey().register(customerResource);
	}

}
