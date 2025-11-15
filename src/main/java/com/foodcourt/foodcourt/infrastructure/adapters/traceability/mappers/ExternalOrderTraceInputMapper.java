package com.foodcourt.foodcourt.infrastructure.adapters.traceability.mappers;

import com.foodcourt.foodcourt.domain.model.order.OrderTrace;
import com.foodcourt.foodcourt.infrastructure.adapters.traceability.dto.ExternalOrderTraceInput;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExternalOrderTraceInputMapper {
	
	ExternalOrderTraceInputMapper INSTANCE = Mappers.getMapper(ExternalOrderTraceInputMapper.class);
	
	ExternalOrderTraceInput toDto(OrderTrace orderTrace);
	
}
