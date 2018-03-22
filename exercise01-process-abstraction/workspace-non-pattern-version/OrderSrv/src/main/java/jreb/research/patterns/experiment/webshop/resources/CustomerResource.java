package jreb.research.patterns.experiment.webshop.resources;

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
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;
import jreb.research.patterns.experiment.webshop.api.BaseResponse;
import jreb.research.patterns.experiment.webshop.api.CreditRatingCheckResponse;
import jreb.research.patterns.experiment.webshop.api.Customer;
import jreb.research.patterns.experiment.webshop.db.CustomerRepository;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {
	private final int defaultCreditRating;
	private CustomerRepository customerRepository;
	private Logger log;

	public CustomerResource(int defaultCreditRating, CustomerRepository repository) {
		this.defaultCreditRating = defaultCreditRating;
		this.customerRepository = repository;
		this.log = LoggerFactory.getLogger(CustomerResource.class);
	}

	@GET
	@Timed
	public List<Customer> getCustomers(@QueryParam("limit") @DefaultValue("20") IntParam limit) {
		final List<Customer> customers = customerRepository.search(limit.get());

		return customers;
	}

	@Path("/{id}")
	@GET
	@Timed
	public Customer getCustomerById(@PathParam("id") LongParam customerId) {
		final Customer customer = customerRepository.getById(customerId.get());

		return customer;
	}

	@POST
	@Timed
	public BaseResponse createCustomer(@NotNull @Valid Customer customer) {
		if (customer.getCreditRating() == 0) {
			customer.setCreditRating(this.defaultCreditRating);
		}
		final Customer createdCustomer = customerRepository.store(customer);

		return new BaseResponse("OK", 201, "Customer with ID " + createdCustomer.getId() + " successfully created.");
	}

	@Path("/{id}")
	@PUT
	@Timed
	public BaseResponse updateCustomer(@PathParam("id") LongParam customerId, @NotNull @Valid Customer customer) {
		final Customer updatedCustomer = customerRepository.update(customerId.get(), customer);

		return new BaseResponse("OK", 204, "Customer with ID " + updatedCustomer.getId() + " successfully updated.");
	}

	@Path("/{id}")
	@DELETE
	@Timed
	public BaseResponse deleteCustomer(@PathParam("id") LongParam customerId) {
		final boolean deleted = customerRepository.deleteById(customerId.get());

		return new BaseResponse(deleted ? "OK" : "FAILED", deleted ? 202 : 400,
				deleted ? "Customer with ID " + customerId.get() + " successfully deleted."
						: "Failed to delete customer with ID " + customerId.get() + ".");
	}

	@Path("/{id}/credit-rating")
	@GET
	@Timed
	public CreditRatingCheckResponse updateAndCheckCreditRating(@PathParam("id") LongParam customerId) {
		log.info("Updating credit rating for customer with ID " + customerId.get() + "...");

		final int rating = customerRepository.updateAndGetRating(customerId.get());
		return new CreditRatingCheckResponse(customerId.get(), (rating < 4), rating);
	}
}