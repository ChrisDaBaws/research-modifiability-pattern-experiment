package jreb.research.patterns.experiment.webshop.api;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreditRatingCheckResponse {
    @NotNull
    @Min(1)
    private long customerId;
    @NotNull
    private boolean acceptable;
    @NotNull
    @Min(0)
    private int creditRating;

    public CreditRatingCheckResponse() {
        // Jackson deserialization
    }

    public CreditRatingCheckResponse(long customerId, boolean acceptable, int creditRating) {
        this.customerId = customerId;
        this.acceptable = acceptable;
        this.creditRating = creditRating;
    }

    @JsonProperty
    public long getCustomerId() {
        return customerId;
    }

    @JsonProperty
    public boolean getAcceptable() {
        return acceptable;
    }

    @JsonProperty
    public int getCreditRating() {
        return creditRating;
    }

}