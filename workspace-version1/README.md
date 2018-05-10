# Service-Based WebShop Version 1

This is the workspace for version 1 of the web shop. It consists of several RESTful Java services that communicate via HTTP requests.

## Directory and Folder Structure

There are three special folders in this directory:

- `_docs`: This folder holds general documentation about the system, namely an architecture diagram of the initial state, another one for the final state, and a document that briefly describes each component in the system.
- `_exercises`: This folder holds the 3 concrete exercise descriptions that have to be performed on the system.
- `_scripts`: This folder holds scripts to build and run the components in the system, one `.bat` and one `.sh` script for every component.

Moreover, every component in the system has a separate folder in this directory. Apart from the `WebUI`, they are all RESTful services, which is indicated by the `Srv` part of their folder name. Every Java service in this workspace is a Maven project and is defined via its `pom.xml` file. Maven commands like `mvn clean install` are used in the various files in the `_scripts` folder to build the executable `.jar` file for each service. The details for this are documented in the `README.md` file of each service.

## RESTful Services Implementation

The services use the [Dropwizard](http://www.dropwizard.io/1.3.1/docs/) Java framework to provide their RESTful HTTP APIs. They have two configuration files (`pom.xml` and `config.yml`) and all follow the same package structure:

- `experiment.webshop.{serviceDomain}`  
The top-level package of the service that holds the `ServiceApplication` class which is the entrypoint and the `ServiceConfiguration` class that provides getters and setters for all custom parameters in the `config.yml` file. These two classes will not be modified during the exercises.
- `experiment.webshop.{serviceDomain}.api`  
The `api` package holds all model classes of the service, i.e. domain entities as well as request and response classes. Some of these classes may also be present in other services, because they operate on the same entities.
- `experiment.webshop.{serviceDomain}.db`  
The `db` package holds a repository class that provides operations to retrieve, store, update, or delete the domain entities the service is responsible for. It acts as the sole access point to a persistent database, although none is present in this prototype implementation.
- `experiment.webshop.{serviceDomain}.health`  
The `health` package holds a health check class for operational/administrative purposes. It will not be modified during the exercises.
- `experiment.webshop.{serviceDomain}.resources`  
The `resources` package holds a resource class that specifies all provided REST operations of the service, i.e. its interface. It uses annotations to indicate the path like `@Path("/products")` and the HTTP method, e.g. `@GET`. Most changes will have to be performed in these resource classes.
