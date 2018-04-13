package patterns.experiment.webshop.notifications.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import patterns.experiment.webshop.notifications.api.MarketingMailRequest;
import patterns.experiment.webshop.notifications.api.NewProductMailRequest;

public class MailRepository {

	private List<MarketingMailRequest> marketingMails;
	private AtomicLong marketingMailIdCounter;
	private List<NewProductMailRequest> productMails;
	private AtomicLong productMailIdCounter;

	public MailRepository() {
		this.marketingMailIdCounter = new AtomicLong();
		this.marketingMails = new ArrayList<MarketingMailRequest>();
		this.productMailIdCounter = new AtomicLong();
		this.productMails = new ArrayList<NewProductMailRequest>();
	}

	// Marketing mails

	public List<MarketingMailRequest> searchMarketingMails(int limit) {
		if (limit > 0) {
			return marketingMails.subList(0, Math.min(marketingMails.size(), limit));
		}
		return marketingMails;
	}

	public MarketingMailRequest getMarketingMailById(long mailId) {
		MarketingMailRequest foundMail = null;

		for (MarketingMailRequest mail : marketingMails) {
			if (mail.getId() == mailId) {
				foundMail = mail;
				break;
			}
		}

		return foundMail;
	}

	public MarketingMailRequest storeMarketingMail(MarketingMailRequest mail) {
		final MarketingMailRequest createdMail = new MarketingMailRequest(marketingMailIdCounter.incrementAndGet(),
				mail.getType(), mail.getOrder());
		this.marketingMails.add(createdMail);

		return createdMail;
	}

	// Product mails

	public List<NewProductMailRequest> searchProductMails(int limit) {
		if (limit > 0) {
			return productMails.subList(0, Math.min(productMails.size(), limit));
		}
		return productMails;
	}

	public NewProductMailRequest getProductMailById(long mailId) {
		NewProductMailRequest foundMail = null;

		for (NewProductMailRequest mail : productMails) {
			if (mail.getId() == mailId) {
				foundMail = mail;
				break;
			}
		}

		return foundMail;
	}

	public NewProductMailRequest storeProductMail(NewProductMailRequest mail) {
		final NewProductMailRequest createdMail = new NewProductMailRequest(productMailIdCounter.incrementAndGet(),
				mail.getType(), mail.getProduct());
		this.productMails.add(createdMail);

		return createdMail;
	}
}
