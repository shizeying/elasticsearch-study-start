package com.run.start.convert;

import com.run.start.constant.AggEnum;
import com.run.start.constant.DateHistogramIntervalEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

@Converter(autoApply = true)
public class DateHistogramIntervalConvert implements AttributeConverter<DateHistogramIntervalEnum, String> {
	
	@Override
	public String convertToDatabaseColumn(DateHistogramIntervalEnum attribute) {
		return Optional.ofNullable(attribute.getName()).map(String::toLowerCase).orElse(null);
	}
	
	@Override
	public DateHistogramIntervalEnum convertToEntityAttribute(String dbData) {
		return DateHistogramIntervalEnum.getTypeFromName(dbData);
	}
}