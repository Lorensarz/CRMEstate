package com.petrov.content_loader_adapter.controller;

import com.petrov.content_loader_adapter.dto.EstateDataDto;
import com.petrov.content_loader_adapter.service.ExcelParserService;
import com.petrov.content_loader_adapter.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileUploadController {

	private final ExcelParserService exelParserService;
	private final KafkaProducerService kafkaProducerService;

	@PostMapping("/load")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("Please select a file to upload");
		}
		if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xlsx")) {
			return ResponseEntity.badRequest().body("Only Exel files (\".xlsx\") are supported");
		}

		try {
			List<EstateDataDto> estateDataList = exelParserService.parseExelFile(file);
			if (estateDataList.isEmpty()) {
				return ResponseEntity.badRequest().body("No valid data found in the file");
			}

			kafkaProducerService.sendEstateDataBatch(estateDataList);
			return ResponseEntity.ok()
					.body("Successfully processed " + estateDataList.size() + " records and sent to Kafka");

		} catch (IOException e) {
			return ResponseEntity.internalServerError()
					.body("Failed to process file: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body("Unexpected error " + e.getMessage());
		}
	}
}
