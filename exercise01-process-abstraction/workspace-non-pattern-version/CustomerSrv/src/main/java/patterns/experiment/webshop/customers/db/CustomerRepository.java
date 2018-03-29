package patterns.experiment.webshop.customers.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import patterns.experiment.webshop.customers.api.Customer;

public class CustomerRepository {

	private AtomicLong customerIdCounter;
	private Random randomizer;

	public CustomerRepository() {
		this.customerIdCounter = new AtomicLong();
		this.randomizer = new Random();
	}

	public List<Customer> search(int limit) {
		final List<Customer> customers = new ArrayList<Customer>();
		final Customer customer = new Customer(1, "TestCustomer", "customer@test.com", 3);
		customers.add(customer);
		return customers;
	}

	public Customer getById(long customerId) {
		final Customer customer = new Customer(customerId, "TestCustomer", "customer@test.com", 3);

		return customer;
	}

	public Customer store(Customer customer) {
		final Customer createdCustomer = new Customer(customerIdCounter.incrementAndGet(), customer.getName(),
				customer.getEmail(), customer.getCreditRating());

		return createdCustomer;
	}

	public Customer update(long customerId, Customer customer) {
		final Customer updatedCustomer = customer;

		return updatedCustomer;
	}

	public boolean deleteById(long customerId) {

		return true;
	}

	public int updateAndGetRating(long customerId) {
		final int max = 6;
		final int min = 1;

		return this.randomizer.nextInt(max + 1 - min) + min;
	}

}
