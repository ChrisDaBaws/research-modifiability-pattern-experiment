package experiment.webshop.notifications.messaging;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import experiment.webshop.notifications.api.NewProductMailRequest;
import experiment.webshop.notifications.api.Product;
import experiment.webshop.notifications.resources.NotificationResource;

public class KafkaListener implements Runnable {
	private final String KAFKA_TOPIC_NAME = "new-products";
	private Properties kafkaProps;
	private Logger log;
	private NotificationResource notificationResource;

	public KafkaListener(NotificationResource notificationResource) {
		this.notificationResource = notificationResource;

		// Set Kafka connection properties
		kafkaProps = new Properties();
		kafkaProps.put("bootstrap.servers", "localhost:9092");
		kafkaProps.put("enable.auto.commit", "true");
		kafkaProps.put("group.id", "NotificationSrv");
		kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		kafkaProps.put("value.deserializer", ProductJsonDeserializer.class);
		this.log = LoggerFactory.getLogger(KafkaListener.class);
		log.info("KafkaNotifier instantiated...");
	}

	@Override
	public void run() {
		final Consumer<String, Product> consumer = new KafkaConsumer<String, Product>(kafkaProps);
		consumer.subscribe(Arrays.asList(KAFKA_TOPIC_NAME));
		while (true) {
			ConsumerRecords<String, Product> messages = consumer.poll(1000);
			for (ConsumerRecord<String, Product> message : messages) {
				Product createdProduct = message.value();

				// TODO: Add the new product to the internal DB

				// TODO: Notify sales department mailing list of the new product

			}
		}
	}
}
