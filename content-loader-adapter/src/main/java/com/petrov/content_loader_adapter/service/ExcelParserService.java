package com.petrov.content_loader_adapter.service;

import com.petrov.content_loader_adapter.dto.EstateDataDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ExcelParserService {

	public List<EstateDataDto> parseExelFile(MultipartFile file) throws IOException {
		List<EstateDataDto> estateDataDtoList = new ArrayList<>();

		try (InputStream inputStream = file.getInputStream();
			 Workbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream))) {

			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			if (rowIterator.hasNext()) {
				rowIterator.next();
			}

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				EstateDataDto dto = parseRowToDto(row);
				if (Objects.nonNull(dto)) {
					estateDataDtoList.add(dto);
				}
			}
		}
		return estateDataDtoList;


	}

	private EstateDataDto parseRowToDto(Row row) {
		if (Objects.isNull(row)) {
			return null;
		}

		EstateDataDto dto = new EstateDataDto();

		try {
			dto.setCadastrNumber(getStringCellValue(row.getCell(0)));
			dto.setSource(getStringCellValue(row.getCell(1)));
			dto.setPrice(getBigDecimalCellValue(row.getCell(2)));
			dto.setType(getStringCellValue(row.getCell(3)));
			dto.setSquare(getBigDecimalCellValue(row.getCell(4)));
			dto.setRoomCount(getIntegerCellValue(row.getCell(5)));
			dto.setFloor(getIntegerCellValue(row.getCell(6)));
			dto.setTotalFloors(getIntegerCellValue(row.getCell(7)));
			dto.setAddress(getStringCellValue(row.getCell(8)));
			dto.setUpdatedAt(LocalDateTime.now());

		} catch (Exception e) {
			log.error("Error parsing row: {}", e.getMessage());
			return null;
		}

		return dto;
	}

	private String getStringCellValue(Cell cell) {
		if (Objects.isNull(cell)) {
			return null;
		}
		return switch (cell.getCellType()) {
			case STRING -> cell.getStringCellValue().trim();
			case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
			default -> null;
		};
	}

	private BigDecimal getBigDecimalCellValue(Cell cell) {
		if (Objects.isNull(cell)) {
			return null;
		}
		return switch (cell.getCellType()) {
			case NUMERIC -> BigDecimal.valueOf(cell.getNumericCellValue());
			case STRING -> new BigDecimal(cell.getStringCellValue());
			default -> null;
		};
	}

	private Integer getIntegerCellValue(Cell cell) {
		if (Objects.isNull(cell)) {
			return null;
		}
		return switch (cell.getCellType()) {
			case NUMERIC -> (int) cell.getNumericCellValue();
			case STRING -> Integer.parseInt(cell.getStringCellValue().trim());
			default -> null;
		};
	}

}
