package patterns.experiment.webshop.orderprocess.resources;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import patterns.experiment.webshop.orderprocess.api.BaseResponse;
import patterns.experiment.webshop.orderprocess.api.CreditRatingCheckResponse;
import patterns.experiment.webshop.orderprocess.api.Order;
import patterns.experiment.webshop.orderprocess.api.OrderItem;
import patterns.experiment.webshop.orderprocess.api.ProductAvailabilityCheckResponse;

@Path("/order-process")
@Produces(MediaType.APPLICATION_JSON)
public class OrderProcessResource {
	private Client restClient;
	private Logger log;

	public OrderProcessResource(Client restClient) {
		this.restClient = restClient;
		this.log = LoggerFactory.getLogger(OrderProcessResource.class);
		log.info("OrderResource instantiated...");
	}

	@POST
	@Timed
	public BaseResponse createOrderProcess(@NotNull @Valid Order order) {
		final int WORST_ALLOWED_CREDIT_RATING = 3; // 1 --> best rating, 6 --> worst rating
		final int MINIMAL_REMAINING_PRODUCT_AMOUNT_NECESSARY = 3;
		BaseResponse response;
		final long customerId = order.getCustomerId();
		final String creditRatingUrl = "http://localhost:8010/customers/" + customerId + "/credit-rating-check";
		final String orderCreationUrl = "http://localhost:8020/orders";
		final List<OrderItem> items = order.getItems();

		// Check credit rating of customer
		log.info("Checking credit rating of customer with ID " + customerId + "...");
		final Invocation.Builder creditRatingRequest = restClient.target(creditRatingUrl).request();
		final CreditRatingCheckResponse creditRatingResponse = creditRatingRequest.get(CreditRatingCheckResponse.class);

		if (creditRatingResponse.getRating() <= WORST_ALLOWED_CREDIT_RATING) {
			// Permitted --> credit rating sufficient
			log.info("Credit rating of customer with ID " + order.getCustomerId()
					+ " acceptable! Checking product availability...");

			// Check availability of all order items
			boolean itemsAvailable = true;
			String productAvailabilityUrl;
			Invocation.Builder productAvailabilityRequest;
			ProductAvailabilityCheckResponse productAvailabilityResponse;
			for (OrderItem item : items) {
				productAvailabilityUrl = "http://localhost:8000/products/" + item.getProductId()
						+ "/availability?amount=" + item.getAmount();
				productAvailabilityRequest = restClient.target(productAvailabilityUrl).request();
				productAvailabilityResponse = productAvailabilityRequest.get(ProductAvailabilityCheckResponse.class);

				if (productAvailabilityResponse.getAvailableAmount()
						- item.getAmount() < MINIMAL_REMAINING_PRODUCT_AMOUNT_NECESSARY) {
					log.info(
							"Product with ID " + item.getProductId() + " has not enough capacity to fullfil the order ("
									+ productAvailabilityResponse.getRequestedAmount() + " requested).");
					itemsAvailable = false;
					break;
				}
			}

			if (itemsAvailable) {
				// Items available --> create order via OrderSrv
				log.info("Creating order for customer with ID " + customerId + "...");
				final Invocation.Builder orderRequest = restClient.target(orderCreationUrl).request();
				final Order createdOrder = orderRequest.post(Entity.json(order), Order.class);
				log.info("Order with ID " + createdOrder.getId() + " successfully created.");
				response = new BaseResponse("OK", 201,
						"Order with ID " + createdOrder.getId() + " successfully created.");
			} else {
				// Items not available --> decline order
				throw new WebApplicationException("Order declined for customer with ID " + order.getCustomerId()
						+ ": items not available in required capacity.", Status.BAD_REQUEST);
			}

		} else {
			// Declined --> credit rating too low
			log.info("Order declined for customer with ID " + order.getCustomerId() + ": credit rating too low.");
			throw new WebApplicationException(
					"Order declined for customer with ID " + order.getCustomerId() + ": credit rating too low.",
					Status.BAD_REQUEST);
		}

		return response;
	}
}