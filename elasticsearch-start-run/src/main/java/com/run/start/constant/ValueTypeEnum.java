package com.run.start.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
		for (ValueTypeEnum type : ValueTypeEnum.values()) {
			if (type.getName().equals(name)) {
				return type;
			}
		}
		return null;
	}
	
}
