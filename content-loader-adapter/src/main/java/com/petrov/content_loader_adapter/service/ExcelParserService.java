package com.petrov.content_loader_adapter.service;

import com.petrov.content_loader_adapter.dto.EstateDataDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ExcelParserService {

	List<EstateDataDto> parseExelFile(MultipartFile file) throws IOException;
}
