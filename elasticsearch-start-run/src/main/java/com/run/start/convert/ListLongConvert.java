package com.run.start.convert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.run.start.tools.JacksonUtil;
import java.util.List;
import java.util.Optional;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ListLongConvert implements AttributeConverter<List<Long>, String> {
	
	@Override
	public String convertToDatabaseColumn(List<Long> attribute) {
		return Optional.ofNullable(attribute)
				.map(JacksonUtil::bean2Json)
				.orElse(null);
	}
	
	@Override
	public List<Long> convertToEntityAttribute(String dbData) {
		return Optional.ofNullable(dbData)
				.map(data->JacksonUtil.json2LongByTypeReference(data, new TypeReference<List<Long>>() {
				}))
				.orElse(Lists.newArrayList());
	}
}
