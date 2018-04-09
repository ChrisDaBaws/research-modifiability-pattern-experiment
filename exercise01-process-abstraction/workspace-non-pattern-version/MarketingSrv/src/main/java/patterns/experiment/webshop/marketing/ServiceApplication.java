package patterns.experiment.webshop.marketing;

import javax.ws.rs.client.Client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import patterns.experiment.webshop.marketing.health.StandardHealthCheck;
import patterns.experiment.webshop.marketing.resources.MarketingResource;

public class ServiceApplication extends Application<ServiceConfiguration> {

	public static void main(final String[] args) throws Exception {
		new ServiceApplication().run(args);
	}

	@Override
	public String getName() {
		return "OrderSrv";
	}

	@Override
	public void initialize(final Bootstrap<ServiceConfiguration> bootstrap) {

	}

	@Override
	public void run(final ServiceConfiguration configuration, final Environment environment) {
		final EmailClient mailClient = new EmailClient();
		final Client restClient = new JerseyClientBuilder(environment)
				.using(configuration.getJerseyClientConfiguration()).build(getName());
		final MarketingResource orderResource = new MarketingResource(restClient, mailClient);

		final StandardHealthCheck healthCheck = new StandardHealthCheck();

		ObjectMapper mapper = environment.getObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		environment.healthChecks().register("template", healthCheck);
		environment.jersey().register(orderResource);
	}

}
