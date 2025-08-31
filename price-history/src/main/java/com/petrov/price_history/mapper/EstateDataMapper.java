package com.petrov.price_history.mapper;


import com.petrov.price_history.dto.EstateDataDto;
import com.petrov.price_history.entity.PriceHistoryRecord;
import org.mapstruct.Mapper;

@Mapper
public interface EstateDataMapper {


	EstateDataDto priceHistoryRecordToEstateDataDto(PriceHistoryRecord priceHistoryRecord);

	PriceHistoryRecord estateDataDtoToPriceHistoryRecord(EstateDataDto estateDataDto);

}
