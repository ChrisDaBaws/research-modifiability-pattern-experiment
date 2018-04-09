package patterns.experiment.webshop.orders;

import javax.ws.rs.client.Client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import patterns.experiment.webshop.orders.db.OrderRepository;
import patterns.experiment.webshop.orders.health.StandardHealthCheck;
import patterns.experiment.webshop.orders.resources.OrderResource;

public class ServiceApplication extends Application<ServiceConfiguration> {

	public static void main(final String[] args) throws Exception {
		new ServiceApplication().run(args);
	}

	private OrderRepository orderRepository;

	@Override
	public String getName() {
		return "OrderSrv";
	}

	@Override
	public void initialize(final Bootstrap<ServiceConfiguration> bootstrap) {
		this.orderRepository = new OrderRepository();
	}

	@Override
	public void run(final ServiceConfiguration configuration, final Environment environment) {
		final Client restClient = new JerseyClientBuilder(environment)
				.using(configuration.getJerseyClientConfiguration()).build(getName());
		final OrderResource orderResource = new OrderResource(orderRepository, restClient);

		final StandardHealthCheck healthCheck = new StandardHealthCheck();

		ObjectMapper mapper = environment.getObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		environment.healthChecks().register("template", healthCheck);
		environment.jersey().register(orderResource);
	}

}
