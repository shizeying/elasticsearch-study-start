package com.run.start.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum AggEnum {
	global,
	terms,
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
		for (AggEnum type : AggEnum.values()) {
			if (type.getName().equals(name)) {
				return type;
			}
		}
		return null;
	}
}
