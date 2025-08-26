package com.petrov.content_loader_adapter.service;

import com.petrov.content_loader_adapter.dto.EstateDataDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExcelParserServiceImplTest {

	@InjectMocks
	private ExcelParserServiceImpl exelParserService;

	private Workbook workbook;
	private Sheet sheet;

	@BeforeEach
	void setUp() {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Test Sheet");
	}

	@Test
	void testParseExelFile_Success() throws IOException {
		// Создаем тестовые данные в Excel
		createTestRow(sheet, "12345", "Source1", "1000000", "Apartment", "50.5", "2", "3", "5", "Test Address");

		// Преобразуем Workbook в MultipartFile
		MultipartFile file = createMultipartFile(workbook);

		// Вызываем метод для тестирования
		List<EstateDataDto> result = exelParserService.parseExelFile(file);

		// Проверяем результаты
		assertNotNull(result);
		assertEquals(1, result.size());

		EstateDataDto dto = result.get(0);
		assertEquals("12345", dto.getCadastrNumber());
		assertEquals("Source1", dto.getSource());
		assertEquals(new BigDecimal("1000000"), dto.getPrice());
		assertEquals("Apartment", dto.getType());
		assertEquals(new BigDecimal("50.5"), dto.getSquare());
		assertEquals(2, dto.getRoomCount());
		assertEquals(3, dto.getFloor());
		assertEquals(5, dto.getTotalFloors());
		assertEquals("Test Address", dto.getAddress());
		assertNotNull(dto.getUpdatedAt());
	}

	@Test
	void testParseExelFile_EmptyFile() throws IOException {
		// Создаем пустой файл
		MultipartFile file = createMultipartFile(workbook);

		// Вызываем метод для тестирования
		List<EstateDataDto> result = exelParserService.parseExelFile(file);

		// Проверяем результаты
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	void testParseExelFile_InvalidData() throws IOException {
		// Создаем строку с некорректными данными
		createTestRow(sheet, "12345", "Source1", "InvalidPrice", "Apartment", "InvalidSquare", "InvalidRoomCount", "3", "5", "Test Address");

		// Преобразуем Workbook в MultipartFile
		MultipartFile file = createMultipartFile(workbook);

		// Вызываем метод для тестирования
		List<EstateDataDto> result = exelParserService.parseExelFile(file);

		// Проверяем результаты
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	void testParseRowToDto_NullRow() {
		// Вызываем метод для тестирования
		EstateDataDto result = exelParserService.parseRowToDto(null);

		// Проверяем результаты
		assertNull(result);
	}

	@Test
	void testParseRowToDto_ValidRow() {
		// Создаем тестовую строку
		Row row = sheet.createRow(0);
		createCell(row, 0, "12345");
		createCell(row, 1, "Source1");
		createCell(row, 2, "1000000");
		createCell(row, 3, "Apartment");
		createCell(row, 4, "50.5");
		createCell(row, 5, "2");
		createCell(row, 6, "3");
		createCell(row, 7, "5");
		createCell(row, 8, "Test Address");

		// Вызываем метод для тестирования
		EstateDataDto result = exelParserService.parseRowToDto(row);

		// Проверяем результаты
		assertNotNull(result);
		assertEquals("12345", result.getCadastrNumber());
		assertEquals("Source1", result.getSource());
		assertEquals(new BigDecimal("1000000"), result.getPrice());
		assertEquals("Apartment", result.getType());
		assertEquals(new BigDecimal("50.5"), result.getSquare());
		assertEquals(2, result.getRoomCount());
		assertEquals(3, result.getFloor());
		assertEquals(5, result.getTotalFloors());
		assertEquals("Test Address", result.getAddress());
		assertNotNull(result.getUpdatedAt());
	}

	@Test
	void testParseRowToDto_InvalidRow() {
		// Создаем строку с некорректными данными
		Row row = sheet.createRow(0);
		createCell(row, 0, "12345");
		createCell(row, 1, "Source1");
		createCell(row, 2, "InvalidPrice"); // Некорректное значение
		createCell(row, 3, "Apartment");
		createCell(row, 4, "InvalidSquare"); // Некорректное значение
		createCell(row, 5, "InvalidRoomCount"); // Некорректное значение
		createCell(row, 6, "3");
		createCell(row, 7, "5");
		createCell(row, 8, "Test Address");

		// Вызываем метод для тестирования
		EstateDataDto result = exelParserService.parseRowToDto(row);

		// Проверяем результаты
		assertNull(result);
	}

	// Вспомогательные методы

	private void createTestRow(Sheet sheet, String cadastrNumber, String source, String price, String type, String square, String roomCount, String floor, String totalFloors, String address) {
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		createCell(row, 0, cadastrNumber);
		createCell(row, 1, source);
		createCell(row, 2, price);
		createCell(row, 3, type);
		createCell(row, 4, square);
		createCell(row, 5, roomCount);
		createCell(row, 6, floor);
		createCell(row, 7, totalFloors);
		createCell(row, 8, address);
	}

	private void createCell(Row row, int columnIndex, String value) {
		Cell cell = row.createCell(columnIndex);
		cell.setCellValue(value);
	}

	private MultipartFile createMultipartFile(Workbook workbook) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		return new MockMultipartFile("test.xlsx", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", outputStream.toByteArray());
	}
}

