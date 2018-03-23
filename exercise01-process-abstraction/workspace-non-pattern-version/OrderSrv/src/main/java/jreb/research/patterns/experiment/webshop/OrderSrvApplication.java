package jreb.research.patterns.experiment.webshop;

import javax.ws.rs.client.Client;

import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import jreb.research.patterns.experiment.webshop.db.OrderRepository;
import jreb.research.patterns.experiment.webshop.health.StandardHealthCheck;
import jreb.research.patterns.experiment.webshop.resources.OrderResource;

public class OrderSrvApplication extends Application<OrderSrvConfiguration> {

	public static void main(final String[] args) throws Exception {
		new OrderSrvApplication().run(args);
	}

	private OrderRepository orderRepository;

	@Override
	public String getName() {
		return "OrderSrv";
	}

	@Override
	public void initialize(final Bootstrap<OrderSrvConfiguration> bootstrap) {
		this.orderRepository = new OrderRepository();
	}

	@Override
	public void run(final OrderSrvConfiguration configuration, final Environment environment) {
		final Client restClient = new JerseyClientBuilder(environment)
				.using(configuration.getJerseyClientConfiguration()).build(getName());
		final OrderResource orderResource = new OrderResource(orderRepository, restClient);

		final StandardHealthCheck healthCheck = new StandardHealthCheck();

		environment.healthChecks().register("template", healthCheck);
		environment.jersey().register(orderResource);
	}

}
