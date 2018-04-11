package patterns.experiment.webshop.marketing.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import patterns.experiment.webshop.marketing.api.MarketingMailRequest;

public class MailRepository {

	private List<MarketingMailRequest> mails;
	private AtomicLong mailIdCounter;

	public MailRepository() {
		this.mailIdCounter = new AtomicLong();
		this.mails = new ArrayList<MarketingMailRequest>();
	}

	public List<MarketingMailRequest> search(int limit) {
		if (limit > 0) {
			return mails.subList(0, Math.min(mails.size(), limit));
		}
		return mails;
	}

	public MarketingMailRequest getById(long mailId) {
		MarketingMailRequest foundMail = null;

		for (MarketingMailRequest mail : mails) {
			if (mail.getId() == mailId) {
				foundMail = mail;
				break;
			}
		}

		return foundMail;
	}

	public MarketingMailRequest store(MarketingMailRequest mail) {
		final MarketingMailRequest createdMail = new MarketingMailRequest(mailIdCounter.incrementAndGet(),
				mail.getType(), mail.getOrder());
		this.mails.add(createdMail);

		return createdMail;
	}

	public MarketingMailRequest update(long mailId, MarketingMailRequest mail) {
		final MarketingMailRequest updatedOrder = mail;

		return updatedOrder;
	}

	public boolean deleteById(long mailId) {
		return true;
	}

}
