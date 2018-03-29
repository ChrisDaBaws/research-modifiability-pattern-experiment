package patterns.experiment.webshop.marketing.resources;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import patterns.experiment.webshop.marketing.EmailClient;
import patterns.experiment.webshop.marketing.api.BaseResponse;
import patterns.experiment.webshop.marketing.api.MarketingMailRequest;
import patterns.experiment.webshop.marketing.api.Order;

@Path("/marketing-mails")
@Produces(MediaType.APPLICATION_JSON)
public class MarketingResource {
	private Client restClient;
	private EmailClient mailClient;
	private Logger log;

	public MarketingResource(Client restClient, EmailClient mailClient) {
		this.restClient = restClient;
		this.mailClient = mailClient;
		this.log = LoggerFactory.getLogger(MarketingResource.class);
		log.info("OrderResource instantiated...");
	}

	@POST
	@Timed
	public BaseResponse sendMarketingMail(@NotNull @Valid MarketingMailRequest request) {
		BaseResponse response;
		final String orderUrl = "http://localhost:8020/orders/" + request.getOrderId();

		// Retrieve order from OrderSrv
		final Invocation.Builder orderRequest = restClient.target(orderUrl).request();
		final Order order = orderRequest.get(Order.class);

		log.info("Trying to send marketing mail for order with ID " + order.getId() + "...");
		if (mailClient.sendMarketingMail(request.getCustomerId(), order)) {
			response = new BaseResponse("OK", 201, "Marketing mail successfully sent.");
			log.info("Marketing mail successfully sent.");
		} else {
			response = new BaseResponse("FAILURE", 400, "Marketing mail could not be sent.");
			log.info("Marketing mail could not be sent.");
		}

		return response;
	}
}