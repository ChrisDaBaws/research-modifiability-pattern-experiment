package patterns.experiment.webshop.notifications;

import patterns.experiment.webshop.notifications.api.Customer;
import patterns.experiment.webshop.notifications.api.Order;
import patterns.experiment.webshop.notifications.api.Product;

public class EmailClient {

	public EmailClient() {
		// MAGIC CONFIGURATION
	}

	public boolean sendMarketingMail(String type, Customer customer, Order order) {
		// SEND MAGIC MARKETING MAIL
		return type.equalsIgnoreCase("SIMILAR_PRODUCTS_MAIL");
	}

	public boolean sendNewProductMail(String type, Product product) {
		// SEND MAGIC NEW PRODCUT MAIL
		// type needs to be `NEW_PRODUCT_MAIL` for the EmailClient to send the mail
		return type.equalsIgnoreCase("NEW_PRODUCT_MAIL");
	}

}
