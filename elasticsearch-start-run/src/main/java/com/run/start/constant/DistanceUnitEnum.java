package com.run.start.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum DistanceUnitEnum {
	INCH,
	YARD,
	FEET,
	KILOMETERS,
	NAUTICALMILE,
	MILLIMETERS,
	CENTIMETERS,
	
	MILES,
	METERS;
	private String name;
	
	public static DistanceUnitEnum getTypeFromName(String name) {
		if (StringUtils.isAllBlank(name)) {
			return null;
		}
		for (DistanceUnitEnum type : DistanceUnitEnum.values()) {
			if (type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
	
	public static DistanceUnit getType(DistanceUnitEnum type) {
		if (Objects.isNull(type)) {
			return null;
		}
		switch (type) {
			case INCH:
				return DistanceUnit.INCH;
			case YARD:
				return DistanceUnit.YARD;
			case FEET:
				return DistanceUnit.FEET;
			case KILOMETERS:
				return DistanceUnit.KILOMETERS;
			case NAUTICALMILE:
				return DistanceUnit.NAUTICALMILES;
			case MILLIMETERS:
				return DistanceUnit.MILLIMETERS;
			case CENTIMETERS:
				return DistanceUnit.CENTIMETERS;
			case METERS:
				return DistanceUnit.METERS;
			default:
				return null;
			
		}
	}
}