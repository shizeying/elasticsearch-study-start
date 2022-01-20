package com.run.start.service.impl;

import com.run.start.bean.dto.AggDto;
import com.run.start.service.AggService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.GeoBoundsAggregationBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AggServiceImpl implements AggService {
	
	@Override
	public AggregationBuilder convertAggregationBuilder(AggDto dto) {
		
		return null;
	}
	
	
	private AggregationBuilder get(AggDto dto) {
		switch (dto.getType()) {
			case terms:
			
			case filters:
			case missing:
			case Nested:
			case children:
			case significantTerms:
			case Range:
			case histogram:
			case dateHistogram:
			case geoDistance:
			case min:
				return AggregationBuilders.min(dto.getAggName()).field(dto.getFieldName());
			case max:
				return AggregationBuilders.max(dto.getAggName()).field(dto.getFieldName());
			case sum:
				return   AggregationBuilders
						.sum("agg")
						.field("height");
			case avg:
				return   AggregationBuilders
						.avg("agg")
						.field("height");
			case stats:
				return   AggregationBuilders
						.stats("agg")
						.field("height");
			case extendedStats:
				return  AggregationBuilders
						.extendedStats("agg")
						.field("height");
			case count:
				return  AggregationBuilders
						.count("agg")
						.field("height");
			case percentiles:
				return AggregationBuilders
						.percentiles("agg")
						.field("height");
			case percentileRanks:
				return  AggregationBuilders
						.percentileRanks("agg")
						.field("height")
						.values(1.24, 1.91, 2.22);
			case cardinality:
				return   AggregationBuilders
						.cardinality("agg")
						.field("tags");
			case geoBounds:
				return  GeoBoundsAggregationBuilder
						.geoBounds("agg")
						.field("address.location")
						.wrapLongitude(true);
			case topHits:
				return AggregationBuilders.topHits("top")
						.explain(true)
						.size(1)
						.from(10);
			case scriptedMetric:
				return  AggregationBuilders
						.scriptedMetric("agg")
						.initScript(new Script("state.heights = []"))
						.mapScript(new Script("state.heights.add(doc.gender.value == 'male' ? doc.height.value : -1.0 * doc.height.value)"))
						.combineScript(new Script("double heights_sum = 0.0; for (t in state.heights) { heights_sum += t } return heights_sum"))
						.reduceScript(new Script("double heights_sum = 0.0; for (a in states) { heights_sum += a } return heights_sum"));
			
			
		}
		
		
	}
}
