package patterns.experiment.webshop.orders.resources;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;
import patterns.experiment.webshop.orders.api.BaseResponse;
import patterns.experiment.webshop.orders.api.CreditRatingCheckResponse;
import patterns.experiment.webshop.orders.api.Order;
import patterns.experiment.webshop.orders.api.OrderItem;
import patterns.experiment.webshop.orders.api.ProductAvailabilityCheckResponse;
import patterns.experiment.webshop.orders.db.OrderRepository;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
	private OrderRepository orderRepository;
	private Client restClient;
	private Logger log;

	public OrderResource(OrderRepository repository, Client restClient) {
		this.orderRepository = repository;
		this.restClient = restClient;
		this.log = LoggerFactory.getLogger(OrderResource.class);
		log.info("OrderResource instantiated...");
	}

	@GET
	@Timed
	public List<Order> getOrders(@QueryParam("limit") @DefaultValue("20") IntParam limit) {
		final List<Order> orders = orderRepository.search(limit.get());

		return orders;
	}

	@Path("/{id}")
	@GET
	@Timed
	public Order getOrderById(@PathParam("id") LongParam orderId) {
		final Order order = orderRepository.getById(orderId.get());

		return order;
	}

	@POST
	@Timed
	public BaseResponse createOrder(@NotNull @Valid Order order) {
		BaseResponse response;
		final long customerId = order.getCustomerId();
		final String creditRatingUrl = "http://localhost:8010/customers/" + customerId + "/credit-rating-check";
		final List<OrderItem> items = order.getItems();

		// Check credit rating of customer

		final Invocation.Builder creditRatingRequest = restClient.target(creditRatingUrl).request();
		final CreditRatingCheckResponse creditRatingResponse = creditRatingRequest.get(CreditRatingCheckResponse.class);

		if (creditRatingResponse.getAcceptable()) {
			// Permitted --> credit rating sufficient

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
				if (!productAvailabilityResponse.getAvailable()) {
					log.info(
							"Product with ID " + item.getProductId() + " has not enough capacity to fullfil the order ("
									+ productAvailabilityResponse.getRequestedAmount() + " requested).");
					itemsAvailable = false;
					break;
				}
			}

			if (itemsAvailable) {
				// Items available --> create order
				final Order createdOrder = orderRepository.store(order);
				log.info("Order with ID " + createdOrder.getId() + " successfully created.");
				response = new BaseResponse("OK", 201,
						"Order with ID " + createdOrder.getId() + " successfully created.");
			} else {
				// Items not available --> decline order
				response = new BaseResponse("DECLINED", 400,
						"Order declined: items not available in required capacity.");
			}

		} else {
			// Declined --> credit rating too low
			log.info("Order declined because credit rating too low.");
			response = new BaseResponse("DECLINED", 400, "Order declined: credit rating too low.");
		}

		return response;

	}

	@Path("/{id}")
	@PUT
	@Timed
	public BaseResponse updateOrder(@PathParam("id") LongParam orderId, @NotNull @Valid Order order) {
		final Order updatedOrder = orderRepository.update(orderId.get(), order);

		return new BaseResponse("OK", 204, "Order with ID " + updatedOrder.getId() + " successfully updated.");
	}

	@Path("/{id}")
	@DELETE
	@Timed
	public BaseResponse deleteOrder(@PathParam("id") LongParam orderId) {
		final boolean deleted = orderRepository.deleteById(orderId.get());

		return new BaseResponse(deleted ? "OK" : "FAILED", deleted ? 202 : 400,
				deleted ? "Order with ID " + orderId.get() + " successfully deleted."
						: "Failed to delete order with ID " + orderId.get() + ".");
	}
}