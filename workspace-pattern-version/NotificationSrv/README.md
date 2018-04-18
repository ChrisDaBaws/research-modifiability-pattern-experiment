# NotificationSrv

How to start the NotificationSrv application
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/NotificationSrv-1.0.0.jar server config.yml`
3. To check that your application is running enter url `http://localhost:8010` (port can be adjusted in `config.yml`)

Health Check
---

To see your applications health enter url `http://localhost:8011/healthcheck` (port can be adjusted in `config.yml`)
