package patterns.experiment.webshop.warehouse.resources;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import patterns.experiment.webshop.warehouse.db.WarehouseRepository;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class WarehouseResource {
	private WarehouseRepository warehouseRepository;
	private Logger log;

	public WarehouseResource(WarehouseRepository repository) {
		this.warehouseRepository = repository;
		this.log = LoggerFactory.getLogger(WarehouseResource.class);
		log.info("WarehouseResource instantiated...");
	}

}