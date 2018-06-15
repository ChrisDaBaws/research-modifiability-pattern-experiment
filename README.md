# Service-Oriented Modifiability Experiment

To provide empirical support for the modifiability of service-based sytems this repo contains two functionally equivalent WebShop systems. The system is implemented in very basic fashion and provides CRUD operations for e.g. `customers`, `products`, or `orders`. For simplicity, no persistence of data is implemented, i.e. after restarting a service all changes to data will be reset.

Several exercises have to be performed on the systems within a certain timeframe. Both effectiveness and efficiency should be measured for each version.

Prerequisites (if you use the provided Ubuntu VM, everything is already installed and ready for use):

- Make sure a [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) >=1.8 is installed and that the `JAVA_HOME` variable is set accordingly.
- Make sure [Maven](https://maven.apache.org/download.cgi) >=3.5.0 is installed and `mvn` is available from the command line.
- Make sure [Node.js](https://nodejs.org/en/download) >=8.0.0 is installed and that `npm` is available from the command line.
- For Version 2 of the system: make sure [Apache Kafka](https://kafka.apache.org/downloads) >=1.1.0 is installed. The Windows build scripts for Kafka and Zookeeper currently expect all related files to reside in `C:\dev\apache-kafka`. If you installed Kafka somewhere else, be sure to adjust `_scripts\win\1_start-zookeeper.bat` and `_scripts\win\2_start-kafka.bat` with your custom path. In the provided Ubuntu VM, both Zookeeper and Kafka are already installed and run as services. So you won't need start scripts.
- Install a Java IDE (recommended: [Eclipse](https://www.eclipse.org/downloads))
- Install a Web IDE (recommended: [Visual Studio Code](https://code.visualstudio.com/download))
- Install a modern web browser (recommended: [Mozilla Firefox](https://www.mozilla.org/en-US/firefox))

Please choose a version and refer to the README in the respective workspace folder. The exercise evaluation web UI that checks if the exercise has been successfully finished is the same for both versions (`./exercise-validation`).