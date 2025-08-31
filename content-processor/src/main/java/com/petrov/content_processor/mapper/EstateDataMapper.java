package com.petrov.content_processor.mapper;

import com.petrov.content_processor.dto.EstateDataDto;
import com.petrov.content_processor.entity.Estate;
import org.mapstruct.Mapper;

@Mapper
public interface EstateDataMapper {


	EstateDataDto estateToEstateDataDto(Estate estate);

	Estate estateDataDtoToEstate(EstateDataDto estateDataDto);

}
