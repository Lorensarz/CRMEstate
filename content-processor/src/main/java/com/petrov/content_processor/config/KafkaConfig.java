package com.petrov.content_processor.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Value("${spring.kafka.consumer.trusted-package}")
	private String trustedPackage;

	@Value("${spring.kafka.consumer.auto-commit}")
	private String autoCommit;

	@Value("${spring.kafka.consumer.auto-offset-reset}")
	private String autoOffsetReset;

	@Value("${spring.kafka.consumer.max-poll-records}")
	private String maxPollRecords;

	@Value("${spring.kafka.consumer.fetch-max-bytes}")
	private String fetchMaxBytes;


	@Bean
	public ConsumerFactory<String, Object> consumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		configProps.put(JsonDeserializer.TRUSTED_PACKAGES, trustedPackage);
		configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
		configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
		configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
		configProps.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, fetchMaxBytes);

		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setBatchListener(true);
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
		factory.setConcurrency(3);

		return factory;
	}

}
