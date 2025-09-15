package com.petrov.notification.config;

import com.petrov.notification.dto.TaskNotificationEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Value("${spring.kafka.consumer.trusted-package}")
	private String trustedPackages;

	@Value("${spring.kafka.consumer.auto-offset-reset}")
	private String autoOffsetReset;

	@Value("${spring.kafka.consumer.auto-commit}")
	private String autoCommit;

	@Bean
	public ConsumerFactory<String, TaskNotificationEvent> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, trustedPackages);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.parseBoolean(autoCommit));
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
		props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 52428800);

		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
				new JsonDeserializer<>(TaskNotificationEvent.class, false));
	}

	@Bean
	public ProducerFactory<String, Object> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		configProps.put(ProducerConfig.ACKS_CONFIG, "all");
		configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
		configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	// Container Factory with DLT support
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, TaskNotificationEvent> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, TaskNotificationEvent> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());

		// Настройка DLT
		DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate());

		DefaultErrorHandler errorHandler = new DefaultErrorHandler(
				recoverer,
				new FixedBackOff(1000L, 2) // 2 попытки с интервалом 1 секунда
		);

		errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);
		errorHandler.addNotRetryableExceptions(NullPointerException.class);

		factory.setCommonErrorHandler(errorHandler);
		factory.getContainerProperties().setAckMode(org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE);
		return factory;
	}
}