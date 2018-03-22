package jreb.research.patterns.experiment.webshop.api;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class Order {
	private long id;
	@NotNull
	private Date created;
	@NotEmpty
	private String status;
	@Min(1)
	private long customerId;
	@NotNull
	private List<OrderItem> items;

	public Order() {
		// Jackson deserialization
	}

	public Order(long id, long customerId, List<OrderItem> items) {
		this.id = id;
		this.customerId = customerId;
		this.items = items;
		this.created = new Date();
		this.status = "NEW";
	}

	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public Date getCreated() {
		return created;
	}

	@JsonProperty
	public String getStatus() {
		return status;
	}

	@JsonProperty
	public long getCustomerId() {
		return customerId;
	}

	@JsonProperty
	public List<OrderItem> getItems() {
		return items;
	}

	public class OrderItem {
		private long productId;
		private int amount;

		public OrderItem() {
			// Jackson deserialization
		}

		public OrderItem(long productId, int amount) {
			this.productId = productId;
			this.amount = amount;
		}

		@JsonProperty
		public long getProductId() {
			return productId;
		}

		@JsonProperty
		public int getAmount() {
			return amount;
		}
	}

}