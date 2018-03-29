package patterns.experiment.webshop.marketing.api;

import javax.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MarketingMailRequest {
	@Min(1)
	private long customerId;
	@Min(1)
	private long orderId;

	public MarketingMailRequest() {
		// Jackson deserialization
	}

	public MarketingMailRequest(long customerId, long orderId) {
		this.customerId = customerId;
		this.orderId = orderId;
	}

	@JsonProperty
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	@JsonProperty
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

}