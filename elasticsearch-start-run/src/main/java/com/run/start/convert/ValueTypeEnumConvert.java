package com.run.start.convert;

import com.run.start.constant.ValueTypeEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

@Converter(autoApply = true)
public class ValueTypeEnumConvert  implements AttributeConverter<ValueTypeEnum, String> {
	
	@Override
	public String convertToDatabaseColumn(ValueTypeEnum attribute) {
		return Optional.ofNullable(attribute.getName()).map(String::toLowerCase).orElse(null);
	}
	
	@Override
	public ValueTypeEnum convertToEntityAttribute(String dbData) {
		return ValueTypeEnum.getTypeFromName(dbData);
	}
}