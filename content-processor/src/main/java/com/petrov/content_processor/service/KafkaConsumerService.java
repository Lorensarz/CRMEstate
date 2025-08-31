package com.petrov.content_processor.service;

import com.petrov.content_processor.dto.EstateDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {


	private final EstateService estateService;

	@KafkaListener(
			topics = "${spring.kafka.topic}",
			containerFactory = "kafkaListenerContainerFactory",
			groupId = "${spring.kafka.consumer.group-id}"
	)
	public void consumeButch(@Payload List<EstateDataDto> messages, Acknowledgment ack) {
		try {
			log.info("Receive batch of {} messages", messages.size());

			if (!messages.isEmpty()) {
				estateService.processEstateBatch(messages);
				log.info("Successfully processed butch with {} records", messages.size());
			}

			ack.acknowledge();
			log.info("Acknowledged batch processing");
		} catch (Exception e) {
			log.error("Failed to process batch: {}", e.getMessage(), e);
			throw new RuntimeException("Butch processing failed", e);
		}


	}

}
