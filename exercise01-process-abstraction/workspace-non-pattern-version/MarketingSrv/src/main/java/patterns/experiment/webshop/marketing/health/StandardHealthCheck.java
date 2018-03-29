package patterns.experiment.webshop.marketing.health;

import com.codahale.metrics.health.HealthCheck;

public class StandardHealthCheck extends HealthCheck {
	

	public StandardHealthCheck() {
		
	}

	@Override
	protected Result check() throws Exception {
	
		return Result.healthy();
	}
}