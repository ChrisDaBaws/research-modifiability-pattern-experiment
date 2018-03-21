package jreb.research.patterns.experiment.webshop;

import javax.validation.Valid;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class ProductSrvConfiguration extends Configuration {

	@NotNull
	@Min(1)
	private long defaultCategoryId = 1;

	@JsonProperty
	public void setDefaultCategoryId(long id) {
		this.defaultCategoryId = id;
	}

	@JsonProperty
	public long getDefaultCategoryId() {
		return this.defaultCategoryId;
	}
	
	@Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

}
