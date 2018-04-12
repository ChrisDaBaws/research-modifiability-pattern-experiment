package patterns.experiment.webshop.marketing.resources;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import patterns.experiment.webshop.marketing.EmailClient;
import patterns.experiment.webshop.marketing.api.BaseResponse;
import patterns.experiment.webshop.marketing.api.Customer;
import patterns.experiment.webshop.marketing.api.MarketingMailRequest;
import patterns.experiment.webshop.marketing.api.Order;
import patterns.experiment.webshop.marketing.db.MailRepository;

@Path("/marketing-mails")
@Produces(MediaType.APPLICATION_JSON)
public class MarketingResource {
	private Client restClient;
	private EmailClient mailClient;
	private MailRepository mailRepository;
	private Logger log;

	public MarketingResource(Client restClient, EmailClient mailClient, MailRepository repository) {
		this.restClient = restClient;
		this.mailClient = mailClient;
		this.mailRepository = repository;
		this.log = LoggerFactory.getLogger(MarketingResource.class);
		log.info("OrderResource instantiated...");
	}

	@GET
	@Timed
	public List<MarketingMailRequest> getMails(@QueryParam("limit") @DefaultValue("20") IntParam limit) {
		final List<MarketingMailRequest> mails = mailRepository.search(limit.get());

		return mails;
	}

	@Path("/{id}")
	@GET
	@Timed
	public MarketingMailRequest getMailById(@PathParam("id") LongParam mailId) {
		final MarketingMailRequest mail = mailRepository.getById(mailId.get());

		if (mail == null) {
			final String msg = String.format("Mail with ID %d does not exist...", mailId.get());
			throw new WebApplicationException(msg, Status.NOT_FOUND);
		}

		return mail;
	}

	@POST
	@Timed
	public BaseResponse sendMarketingMail(@NotNull @Valid MarketingMailRequest request) {
		BaseResponse response;
		final Order order = request.getOrder();
		final String marketingMailType = request.getType();

		// Retrieve customer from CustomerSrv
		final String customerUrl = "http://localhost:8010/customers/" + order.getCustomerId();
		final Invocation.Builder customerRequest = restClient.target(customerUrl).request();
		final Customer customer = customerRequest.get(Customer.class);

		log.info("Trying to send marketing mail to customer " + customer.getName() + " for order with ID "
				+ order.getId() + "...");
		if (mailClient.sendMarketingMail(marketingMailType, customer, order)) {
			response = new BaseResponse("OK", 201, "Marketing mail successfully sent.");
			log.info("Marketing mail successfully sent.");
			mailRepository.store(request);
		} else {
			response = new BaseResponse("FAILURE", 400, "Unknown mail type. Marketing mail could not be sent.");
			log.info("Unknown mail type. Marketing mail could not be sent.");
		}

		return response;
	}
}