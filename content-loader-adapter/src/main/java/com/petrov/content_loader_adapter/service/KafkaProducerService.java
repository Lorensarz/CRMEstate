package com.petrov.content_loader_adapter.service;

import com.petrov.content_loader_adapter.dto.EstateDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

	@Value("${spring.kafka.producer.topic}")
	private String topic;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public void sendEstateDataBatch(List<EstateDataDto> estateDataList) {
		for (EstateDataDto dto : estateDataList) {
			sendEstateData(dto);
		}
		kafkaTemplate.flush();
	}

	private CompletableFuture<SendResult<String, Object>> sendEstateData(EstateDataDto estateData) {
		String key = estateData.getCadastrNumber();
		return kafkaTemplate.send(topic, key, estateData)
				.whenComplete((result, ex) -> {
					if (Objects.isNull(ex)) {
						log.info("Sent message: {}", estateData.getCadastrNumber());
					} else {
						log.error("Failed to send message", ex);
					}
				});
	}
}
