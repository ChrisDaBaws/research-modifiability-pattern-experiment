# OrderSrv

How to start the OrderSrv application
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/OrderSrv-1.0.0.jar server config.yml`
3. To check that your application is running enter url `http://localhost:8030` (port can be adjusted in `config.yml`)

Health Check
---

To see your applications health enter url `http://localhost:8031/healthcheck` (port can be adjusted in `config.yml`)
