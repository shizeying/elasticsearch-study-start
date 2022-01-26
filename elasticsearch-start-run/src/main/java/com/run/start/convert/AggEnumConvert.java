package com.run.start.convert;

import com.run.start.constant.AggEnum;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

@Converter(autoApply = true)
public class AggEnumConvert implements AttributeConverter<AggEnum, String> {
	
	@Override
	public String convertToDatabaseColumn(AggEnum attribute) {
		return Optional.ofNullable(attribute.getName()).map(String::toLowerCase).orElse(null);
	}
	
	@Override
	public AggEnum convertToEntityAttribute(String dbData) {
		return AggEnum.getTypeFromName(dbData);
	}
}