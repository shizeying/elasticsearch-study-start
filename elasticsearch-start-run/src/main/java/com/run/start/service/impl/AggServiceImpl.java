package com.run.start.service.impl;

import com.run.start.bean.dto.AggDto;
import com.run.start.service.AggService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AggServiceImpl implements AggService {
	
	@Override
	public AggregationBuilder convertAggregationBuilder(AggDto dto) {
		
		return null;
	}
	
	

	
	
}
