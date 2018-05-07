# Service Descriptions

This document provides a brief overview for every service.

## CustomerSrv

Endpoint: `http://localhost:8000`

This service provides CRUD operations for customer entities as well as an operation to retrieve a refreshed credit rating for a single customer.

```bash
    GET     /customers (experiment.webshop.customers.resources.CustomerResource)
    POST    /customers (experiment.webshop.customers.resources.CustomerResource)
    DELETE  /customers/{id} (experiment.webshop.customers.resources.CustomerResource)
    GET     /customers/{id} (experiment.webshop.customers.resources.CustomerResource)
    PUT     /customers/{id} (experiment.webshop.customers.resources.CustomerResource)

    GET     /customers/{id}/credit-rating-check (experiment.webshop.customers.resources.CustomerResource)
```

## NotificationSrv

Endpoint: `http://localhost:8010`

This service provides operations to send a "marketing mail" with similar product info to a customer or a "new product mail" to the sales department. It also provides operations to retrieve the previously sent mails and to add products to as well as retrieve products from its "new product" database.

```bash
    GET     /marketing-mails (experiment.webshop.notifications.resources.NotificationResource)
    POST    /marketing-mails (experiment.webshop.notifications.resources.NotificationResource)
    GET     /marketing-mails/{id} (experiment.webshop.notifications.resources.NotificationResource)


    GET     /product-mails (experiment.webshop.notifications.resources.NotificationResource)
    POST    /product-mails (experiment.webshop.notifications.resources.NotificationResource)
    GET     /product-mails/{id} (experiment.webshop.notifications.resources.NotificationResource)(experiment.webshop.customers.resources.CustomerResource)

    GET     /new-products (experiment.webshop.notifications.resources.NotificationResource)
    POST    /new-products (experiment.webshop.notifications.resources.NotificationResource)
    GET     /new-products/{id} (experiment.webshop.notifications.resources.NotificationResource)
```

## OrderSrv

Endpoint: `http://localhost:8030`

This service provides CRUD operations for order entities including the complicated process to create a new order.

```bash
    GET     /orders (experiment.webshop.orders.resources.OrderResource)
    POST    /orders (experiment.webshop.orders.resources.OrderResource)
    DELETE  /orders/{id} (experiment.webshop.orders.resources.OrderResource)
    GET     /orders/{id} (experiment.webshop.orders.resources.OrderResource)
    PUT     /orders/{id} (experiment.webshop.orders.resources.OrderResource)
```

## ProductSrv

Endpoint: `http://localhost:8050`

This service provides CRUD operations for product and product category entities. It also provides operations to get and set the currently available amount of a certain product.

```bash
    GET     /categories (experiment.webshop.products.resources.ProductResource)
    POST    /categories (experiment.webshop.products.resources.ProductResource)
    DELETE  /categories/{id} (experiment.webshop.products.resources.ProductResource)
    GET     /categories/{id} (experiment.webshop.products.resources.ProductResource)
    PUT     /categories/{id} (experiment.webshop.products.resources.ProductResource)

    GET     /products (experiment.webshop.products.resources.ProductResource)
    POST    /products (experiment.webshop.products.resources.ProductResource)
    DELETE  /products/{id} (experiment.webshop.products.resources.ProductResource)
    GET     /products/{id} (experiment.webshop.products.resources.ProductResource)
    PUT     /products/{id} (experiment.webshop.products.resources.ProductResource)

    GET     /products/{id}/availability (experiment.webshop.products.resources.ProductResource)
    PUT     /products/{id}/availability (experiment.webshop.products.resources.ProductResource)
```

## Web UI

Endpoint: `http://localhost:5000`

The Web UI provides easy read access to the resources of most services.
