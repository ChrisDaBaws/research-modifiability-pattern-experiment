package patterns.experiment.webshop.marketing;

import patterns.experiment.webshop.marketing.api.Customer;
import patterns.experiment.webshop.marketing.api.Order;

public class EmailClient {

	public EmailClient() {
		// MAGIC CONFIGURATION
	}

	public boolean sendMarketingMail(String type, Customer customer, Order order) {
		// SEND MAGIC MARKETING MAIL
		return type.equalsIgnoreCase("SIMILAR_PRODUCTS_MAIL");
	}

}
