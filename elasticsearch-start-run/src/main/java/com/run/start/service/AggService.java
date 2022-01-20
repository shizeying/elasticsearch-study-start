package com.run.start.service;

import com.run.start.bean.dto.AggDto;
import org.elasticsearch.search.aggregations.AggregationBuilder;

public interface AggService {
	
	
	
	AggregationBuilder convertAggregationBuilder(AggDto dto);
	
}
