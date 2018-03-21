package jreb.research.patterns.experiment.webshop.api;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductAvailabilityCheckResponse {
    @NotNull
    @Min(1)
    private long productId;
    @NotNull
    private boolean available;
    @NotNull
    @Min(0)
    private int amount;

    public ProductAvailabilityCheckResponse() {
        // Jackson deserialization
    }

    public ProductAvailabilityCheckResponse(long productId, boolean available, int amount) {
        this.productId = productId;
        this.available = available;
        this.amount = amount;
    }

    @JsonProperty
    public long getProductId() {
        return productId;
    }

    @JsonProperty
    public boolean getAvailable() {
        return available;
    }

    @JsonProperty
    public int getAmount() {
        return amount;
    }

}