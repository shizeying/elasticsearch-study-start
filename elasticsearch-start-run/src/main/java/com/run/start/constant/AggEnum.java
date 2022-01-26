package com.run.start.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum AggEnum {
	global,
	terms,
	filter,
	//不支持
	filters,
	missing,
	Nested,
	children,
	significantTerms,
	Range,
	histogram,
	dateHistogram,
	geoDistance,
	min,
	max,
	sum,
	avg,
	stats,
	extendedStats,
	count,
	percentiles,
	percentileRanks,
	cardinality,
	geoBounds,
	topHits,
	scriptedMetric;
	
	private String name;
	
	
	public static AggEnum getTypeFromName(String name) {
		if (StringUtils.isAllBlank(name)){
			return null;
		}
		for (AggEnum type : AggEnum.values()) {
			if (type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
}