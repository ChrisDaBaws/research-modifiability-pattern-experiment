# Service-Oriented Modifiability Experiment

To provide empirical support for the modifiability of service-based sytems this repo contains two functionally equivalent WebShop systems. The system is implemented in very basic fashion and provides CRUD operations for e.g. `customers`, `products`, or `orders`. For simplicity, no persistence of data is implemented, i.e. after restarting a service all changes to data will be reset.

Several exercises have to be performed on the systems within a certain timeframe. Both effectiveness and efficiency should be measured for each version.

Required:

- [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) >=1.8
- [Maven](https://maven.apache.org/download.cgi) >=3.5.0
- [Node.js](https://nodejs.org/en/download) >=8.0.0
- A Java IDE (recommended: [Eclipse](https://www.eclipse.org/downloads))
- A Web IDE (recommended: [Visual Studio Code](https://code.visualstudio.com/download))
- For version 2 of the system: [Apache Kafka](https://kafka.apache.org/downloads) >=1.1.0
- A modern web browser (recommended: [Chrome](https://www.google.com/chrome))

Please choose a version and refer to the README in the respective workspace folder. The exercise evaluation web UI that checks if the exercise has been successfully finished is the same for both versions (`./exercise-validation`).