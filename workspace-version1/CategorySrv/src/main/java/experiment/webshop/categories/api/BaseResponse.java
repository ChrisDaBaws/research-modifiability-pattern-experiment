package experiment.webshop.categories.api;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;

public class BaseResponse {
    @NotNull
    @NotEmpty
    private String status;
    @NotNull
    private int statusCode;
    @NotNull
    @NotEmpty
    private String message;

    public BaseResponse() {
        // Jackson deserialization
    }

    public BaseResponse(String status, int statusCode, String message) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
    }

    @JsonProperty
    public String getStatus() {
        return status;
    }

    @JsonProperty
    public int getStatusCode() {
        return statusCode;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

}