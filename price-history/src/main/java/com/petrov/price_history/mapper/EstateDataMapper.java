package com.petrov.price_history.mapper;


import com.petrov.price_history.dto.EstateDataDto;
import com.petrov.price_history.entity.PriceHistoryRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EstateDataMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "recordedAt", expression = "java(java.time.LocalDateTime.now())")
	@Mapping(target = "version", constant = "0L")
	PriceHistoryRecord estateDataDtoToPriceHistoryRecord(EstateDataDto estateDataDto);

}
