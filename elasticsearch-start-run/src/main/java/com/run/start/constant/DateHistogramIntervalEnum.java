package com.run.start.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum DateHistogramIntervalEnum {
	MINUTE,
	HOUR,
	DAY,
	WEEK,
	MONTH,
	QUARTER,
	YEAR;
	private String name;
	public static DateHistogramIntervalEnum getTypeFromName(String name) {
		if (StringUtils.isAllBlank(name)){
			return null;
		}
		for (DateHistogramIntervalEnum type : DateHistogramIntervalEnum.values()) {
			if (type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
	
	public  static DateHistogramInterval getType(DateHistogramIntervalEnum type){
		if (Objects.isNull(type)){
			return null;
		}
		switch (type){
			case DAY:
				return DateHistogramInterval.DAY;
			case HOUR:
				return DateHistogramInterval.HOUR;
			case WEEK:
				return DateHistogramInterval.WEEK;
			case YEAR:
				return DateHistogramInterval.YEAR;
			case MONTH:
				return DateHistogramInterval.MONTH;
			case MINUTE:
				return DateHistogramInterval.MINUTE;
			case QUARTER:
				return DateHistogramInterval.QUARTER;
			default:
				return null;
			
		}
	}
	
}