package patterns.experiment.webshop.orders.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import patterns.experiment.webshop.orders.api.Order;
import patterns.experiment.webshop.orders.api.OrderItem;

public class OrderRepository {

	private AtomicLong orderIdCounter;
	private Random randomizer;

	public OrderRepository() {
		this.orderIdCounter = new AtomicLong();
		this.randomizer = new Random();
	}

	public List<Order> search(int limit) {
		final List<Order> orders = new ArrayList<Order>();
		final List<OrderItem> items = new ArrayList<OrderItem>();
		final OrderItem item1 = new OrderItem(1, 10);
		final OrderItem item2 = new OrderItem(2, 5);
		items.add(item1);
		items.add(item2);
		final Order order = new Order(1, 1, items);
		orders.add(order);
		return orders;
	}

	public Order getById(long orderId) {
		final List<OrderItem> items = new ArrayList<OrderItem>();
		final OrderItem item1 = new OrderItem(1, 10);
		final OrderItem item2 = new OrderItem(2, 5);
		items.add(item1);
		items.add(item2);
		final Order order = new Order(1, 1, items);

		return order;
	}

	public Order store(Order order) {
		final Order createdOrder = new Order(orderIdCounter.incrementAndGet(), order.getCustomerId(), order.getItems());
		return createdOrder;
	}

	public Order update(long orderId, Order order) {
		final Order updatedOrder = order;

		return updatedOrder;
	}

	public boolean deleteById(long orderId) {
		return true;
	}

	public int updateAndGetRating(long orderId) {
		final int max = 6;
		final int min = 1;

		return this.randomizer.nextInt(max + 1 - min) + min;
	}

}
