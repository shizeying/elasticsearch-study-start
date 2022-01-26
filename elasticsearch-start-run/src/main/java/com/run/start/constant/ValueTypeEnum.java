package com.run.start.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ValueTypeEnum {
	STRING,
	LONG,
	DOUBLE,
	NUMBER,
	DATE,
	IP,
	NUMERIC,
	GEOPOINT,
	BOOLEAN,
	RANGE;
	private String name;
	
	public static ValueTypeEnum getTypeFromName(String name) {
		if (StringUtils.isAllBlank(name)){
			return null;
		}
		for (ValueTypeEnum type : ValueTypeEnum.values()) {
			if (type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
	
}