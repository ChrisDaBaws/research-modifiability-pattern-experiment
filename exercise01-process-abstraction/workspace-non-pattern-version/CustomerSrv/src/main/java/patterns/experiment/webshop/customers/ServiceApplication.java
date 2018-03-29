package patterns.experiment.webshop.customers;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import patterns.experiment.webshop.customers.db.CustomerRepository;
import patterns.experiment.webshop.customers.health.StandardHealthCheck;
import patterns.experiment.webshop.customers.resources.CustomerResource;

public class ServiceApplication extends Application<ServiceConfiguration> {

	public static void main(final String[] args) throws Exception {
		new ServiceApplication().run(args);
	}

	private CustomerRepository customerRepository;

	@Override
	public String getName() {
		return "CustomerSrv";
	}

	@Override
	public void initialize(final Bootstrap<ServiceConfiguration> bootstrap) {
		this.customerRepository = new CustomerRepository();
	}

	@Override
	public void run(final ServiceConfiguration configuration, final Environment environment) {

		final CustomerResource customerResource = new CustomerResource(configuration.getDefaultCreditRating(),
				customerRepository);

		final StandardHealthCheck healthCheck = new StandardHealthCheck(configuration.getDefaultCreditRating());

		environment.healthChecks().register("template", healthCheck);
		environment.jersey().register(customerResource);
	}

}
