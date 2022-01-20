package com.run.start.convert;

import com.run.start.constant.ValueTypeEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ValueTypeEnumConvert  implements AttributeConverter<ValueTypeEnum, String> {
	
	@Override
	public String convertToDatabaseColumn(ValueTypeEnum attribute) {
		return attribute.getName();
	}
	
	@Override
	public ValueTypeEnum convertToEntityAttribute(String dbData) {
		return ValueTypeEnum.getTypeFromName(dbData);
	}
}
