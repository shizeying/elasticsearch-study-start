package com.run.start.utils;

import com.google.common.collect.Lists;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.join.aggregations.Children;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.adjacency.AdjacencyMatrix;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregation;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.geogrid.GeoGrid;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalAutoDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalVariableWidthHistogram;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.range.InternalDateRange;
import org.elasticsearch.search.aggregations.bucket.range.InternalRange;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.*;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.*;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Slf4j
public class AggUtils {
	
	
	public static List<AggBean> getAllAggs(Aggregations aggregations) {
		
		if (Objects.isNull(aggregations) || CollectionUtils.isEmpty(aggregations.asMap())) {
			return Lists.newArrayList();
			
		}
		List<AggBean> aggBeans = Lists.newArrayList();
		for (Entry<String, Aggregation> entry : aggregations.getAsMap().entrySet()) {
			final AggBean aggBean = new AggBean();
			final String key = entry.getKey();
			final Aggregation value = entry.getValue();
			final AggBean bean = matchAggGetValues(value);
			
			aggBean.setKeyAsString(key);
			aggBean.getAggs().add(bean);
			aggBeans.add(aggBean);
			
		}
		return aggBeans;
		
	}
	
	private static AggBean matchAggGetValues(Aggregation aggregation) {
		if (aggregation instanceof Terms) {
			return getTermsValue((Terms) aggregation);
		}
		
		if (aggregation instanceof Histogram) {
			return getHistogramBucketValue((Histogram) aggregation);
		}
		if (aggregation instanceof CompositeAggregation) {
			return getCompositeAggregationValue((CompositeAggregation) aggregation);
		}
		if (aggregation instanceof Filters) {
			return getFiltersValue((Filters) aggregation);
		}
		
		if (aggregation instanceof Nested) {
			return getNestedValue((Nested) aggregation);
		}
		if (aggregation instanceof Missing) {
			return getMissingValue((Missing) aggregation);
		}
		if (aggregation instanceof Children) {
			return getChildrenValue((Children) aggregation);
		}
		if (aggregation instanceof SignificantTerms) {
			return getSignificantTermsValue((SignificantTerms) aggregation);
		}
		if (aggregation instanceof Range) {
			return getRangeValue((Range) aggregation);
		}
		if (aggregation instanceof Global) {
			return getGlobalValue((Global) aggregation);
		}
		if (aggregation instanceof ValueCount) {
			final String name = ((ValueCount) aggregation).getName();
			final long value = ((ValueCount) aggregation).getValue();
			final String type = aggregation.getType();
			return AggBean.builder().keyAsString(name)
					.type(type)
					.count(value).build();
		}
		if (aggregation instanceof Min) {
			final double value = ((Min) aggregation).getValue();
			final String name = aggregation.getName();
			final String type = aggregation.getType();
			return AggBean.builder().keyAsString(name)
					.type(type)
					.count(value).build();
		}
		if (aggregation instanceof Max) {
			
			final double value = ((Max) aggregation).getValue();
			final String name = aggregation.getName();
			final String type = aggregation.getType();
			return AggBean.builder().keyAsString(name)
					.type(type)
					.count(value).build();
		}
		if (aggregation instanceof Sum) {
			final double value = ((Sum) aggregation).getValue();
			final String name = aggregation.getName();
			final String type = aggregation.getType();
			return AggBean.builder().keyAsString(name)
					.type(type)
					.count(value).build();
			
		}
		if (aggregation instanceof Avg) {
			final double value = ((Avg) aggregation).getValue();
			final String name = aggregation.getName();
			final String type = aggregation.getType();
			return AggBean.builder().keyAsString(name)
					.type(type)
					.count(value).build();
			
		}
		if (aggregation instanceof Stats) {
			final String name = aggregation.getName();
			final String avgAsString = ((Stats) aggregation).getAvgAsString();
			final long count = ((Stats) aggregation).getCount();
			final double max = ((Stats) aggregation).getMax();
			final double sum = ((Stats) aggregation).getSum();
			final double min = ((Stats) aggregation).getMin();
			final String type = aggregation.getType();
			return AggBean.builder()
					.keyAsString(name)
					.type(type)
					.avgAsString(avgAsString)
					.max(max)
					.sum(sum)
					.min(min)
					.count(count).build();
		}
		if (aggregation instanceof ExtendedStats) {
			final String name = aggregation.getName();
			final String avgAsString = ((ExtendedStats) aggregation).getAvgAsString();
			final long count = ((ExtendedStats) aggregation).getCount();
			final double max = ((ExtendedStats) aggregation).getMax();
			final double sum = ((ExtendedStats) aggregation).getSum();
			final double min = ((ExtendedStats) aggregation).getMin();
			double stdDeviation = ((ExtendedStats) aggregation).getStdDeviation();
			double sumOfSquares = ((ExtendedStats) aggregation).getSumOfSquares();
			double variance = ((ExtendedStats) aggregation).getVariance();
			final String type = aggregation.getType();
			return AggBean.builder()
					.keyAsString(name)
					.type(type)
					.avgAsString(avgAsString)
					.max(max)
					.sum(sum)
					.min(min)
					.stdDeviation(stdDeviation)
					.sumOfSquares(sumOfSquares)
					.variance(variance)
					.count(count).build();
		}
		if (aggregation instanceof Percentiles) {
			return getPercentiles(aggregation);
			
			
		}
		if (aggregation instanceof PercentileRanks) {
			return getPercentileRanks((PercentileRanks) aggregation);
		}
		if (aggregation instanceof Cardinality) {
			final String name = aggregation.getName();
			final String type = aggregation.getType();
			final long value = ((Cardinality) aggregation).getValue();
			return AggBean.builder().keyAsString(name)
					.type(type)
					.count(value).build();
			
		}
		if (aggregation instanceof GeoBounds) {
			final String name = aggregation.getName();
			final String type = aggregation.getType();
			final GeoPoint right = ((GeoBounds) aggregation).bottomRight();
			final GeoPoint left = ((GeoBounds) aggregation).topLeft();
			return AggBean.builder().keyAsString(name)
					.type(type)
					.right(GeoPintBean
							.builder()
							.geohash(right.getGeohash())
							.lat(right.getLat())
							.lon(right.lon())
							.build())
					.left(GeoPintBean
							.builder()
							.geohash(left.getGeohash())
							.lat(left.getLat())
							.lon(left.lon())
							.build())
					
					.build();
			
			
		}
		if (aggregation instanceof ScriptedMetric) {
			final String type = aggregation.getType();
			final String name = aggregation.getName();
			Object scriptedResult = ((ScriptedMetric) aggregation).aggregation();
			return AggBean.builder().keyAsString(name)
					.type(type)
					.value(scriptedResult).build();
			
		}
		
		// if (aggregation instanceof InternalGeoHashGridBucket){
		//
		// }
		
		return null;
	}
	
	private static AggBean getPercentileRanks(final PercentileRanks agg) {
		final String name = agg.getName();
		final String type = agg.getType();
		final AggBean aggBean = AggBean.builder()
				.keyAsString(name)
				.type(type)
				.build();
		// For each entry
		for (Percentile entry : agg) {
			double percent = entry.getPercent();    // Percent
			double value = entry.getValue();        // Value
			
			log.info("percent [{}], value [{}]", percent, value);
			aggBean.getAggs().add(AggBean.builder()
					
					
					.keyAsString(percent + "")
					.count(value)
					.build());
		}
		return aggBean;
	}
	
	private static AggBean getPercentiles(final Aggregation aggregation) {
		final String name = aggregation.getName();
		final String type = aggregation.getType();
		final AggBean aggBean = AggBean.builder()
				.keyAsString(name)
				.type(type)
				.build();
		for (Percentile entry : (Percentiles) aggregation) {
			//百分比
			double percent = entry.getPercent();    // Percent
			double value = entry.getValue();        // Value
			
			aggBean.getAggs().add(AggBean.builder()
					
					
					.keyAsString(percent + "")
					.count(value)
					.build());
			log.info("percent [{}], value [{}]", percent, value);
		}
		return aggBean;
	}
	
	private static AggBean getGlobalValue(Global aggregation) {
		final String globalKey = aggregation.getName();
		final long docCount = aggregation.getDocCount();
		final String type = aggregation.getType();
		
		return AggBean.builder().keyAsString(globalKey)
				.type(type)
				.count(docCount)
				.build();
		
	}
	
	private static AggBean getChildrenValue(Children aggregation) {
		final String childrenKey = aggregation.getName();
		final String type = aggregation.getType();
		final long docCount = aggregation.getDocCount();
		
		return AggBean.builder().keyAsString(childrenKey)
				.type(type)
				.count(docCount)
				.build();
	}
	
	private static AggBean getNestedValue(Nested aggregation) {
		final String nestedKey = aggregation.getName();
		final String type = aggregation.getType();
		final long docCount = aggregation.getDocCount();
		
		return AggBean.builder().keyAsString(nestedKey)
				.type(type)
				.count(docCount)
				.build();
	}
	
	private static AggBean getMissingValue(Missing aggregation) {
		final String missingKey = aggregation.getName();
		final long docCount = aggregation.getDocCount();
		final String type = aggregation.getType();
		
		return AggBean.builder().keyAsString(missingKey)
				.type(type)
				.count(docCount)
				.build();
	}
	
	private static AggBean getRangeValue(Range aggregation) {
		final String rangeKey = aggregation.getName();
		final String type = aggregation.getType();
		
		
	
		final AggBean aggBean = AggBean.builder().keyAsString(rangeKey).type(type).build();
		
		if (Objects.nonNull(aggregation.getBuckets())&&!aggregation.getBuckets().isEmpty()) {
			final Set<AggBean> aggBeans = aggregation.getBuckets().stream().filter(Objects::nonNull)
					.map(AggUtils::getBucket)
					
					.collect(Collectors.toSet());
			aggBean.getAggs().addAll(aggBeans);
		}
		return aggBean;
		
	}
	
	private static AggBean getSignificantTermsValue(SignificantTerms aggregation) {
		final String significantTermsKey = aggregation.getName();
		final String type = aggregation.getType();
		
	
		final AggBean aggBean = AggBean.builder().keyAsString(significantTermsKey).type(type).build();
		
		if (Objects.nonNull(aggregation.getBuckets())&&!aggregation.getBuckets().isEmpty()) {
			final Set<AggBean> aggBeans = aggregation.getBuckets().stream().filter(Objects::nonNull)
					.map(AggUtils::getBucket)
					
					.collect(Collectors.toSet());
			aggBean.getAggs().addAll(aggBeans);
		}
		return aggBean;
	}
	
	
	private static AggBean getFiltersValue(Filters aggregation) {
		final String filtersKey = aggregation.getName();
		final String type = aggregation.getType();
		
		
	
		final AggBean aggBean = AggBean.builder().keyAsString(filtersKey).type(type).build();
		
		if (Objects.nonNull(aggregation.getBuckets())&&!aggregation.getBuckets().isEmpty()) {
			final Set<AggBean> aggBeans = aggregation.getBuckets().stream().filter(Objects::nonNull)
					.map(AggUtils::getBucket)
					
					.collect(Collectors.toSet());
			aggBean.getAggs().addAll(aggBeans);
		}
		return aggBean;
		
	}
	
	private static AggBean getCompositeAggregationValue(CompositeAggregation aggregation) {
		final String compositeAggregationKey = aggregation.getName();
		final String type = aggregation.getType();
		
	
		final AggBean aggBean = AggBean.builder().keyAsString(compositeAggregationKey).type(type).build();
		
		if (Objects.nonNull(aggregation.getBuckets())&&!aggregation.getBuckets().isEmpty()) {
			final Set<AggBean> aggBeans = aggregation.getBuckets().stream().filter(Objects::nonNull)
					.map(AggUtils::getBucket)
					
					.collect(Collectors.toSet());
			aggBean.getAggs().addAll(aggBeans);
		}
	
		return aggBean;
		
		
	}
	
	private static AggBean getHistogramBucketValue(Histogram aggregation) {
		final String histogramKey = aggregation.getName();
		final String type = aggregation.getType();
		final AggBean aggBean = AggBean.builder().keyAsString(histogramKey).type(type).build();
		
		if (Objects.nonNull(aggregation.getBuckets())&&!aggregation.getBuckets().isEmpty()) {
			final Set<AggBean> aggBeans = aggregation.getBuckets().stream().filter(Objects::nonNull)
					.map(AggUtils::getBucket)
					
					.collect(Collectors.toSet());
			aggBean.getAggs().addAll(aggBeans);
		}
	
		return aggBean;
		
		
	}
	
	
	private static AggBean getBucket(Object bucket) {
		if (bucket instanceof Terms.Bucket) {
			final String keyAsString = ((Terms.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
			
		} else if (bucket instanceof Histogram.Bucket) {
			final String keyAsString = ((Histogram.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((Histogram.Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((Histogram.Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
		} else if (bucket instanceof Range.Bucket) {
			final String keyAsString = ((Range.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((Range.Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((Range.Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
		} else if (bucket instanceof Filters.Bucket) {
			final String keyAsString = ((Filters.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((Filters.Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((Filters.Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
		} else if (bucket instanceof CompositeAggregation.Bucket) {
			final String keyAsString = ((CompositeAggregation.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((CompositeAggregation.Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((CompositeAggregation.Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
		} else if (bucket instanceof GeoGrid.Bucket) {
			final String keyAsString = ((GeoGrid.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((GeoGrid.Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((GeoGrid.Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
		} else if (bucket instanceof AdjacencyMatrix.Bucket) {
			final String keyAsString = ((AdjacencyMatrix.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((AdjacencyMatrix.Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((AdjacencyMatrix.Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
		} else if (bucket instanceof InternalRareTerms.Bucket) {
			final String keyAsString = ((InternalRareTerms.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((InternalRareTerms.Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((InternalRareTerms.Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
		} else if (bucket instanceof SignificantTerms.Bucket) {
			final String keyAsString = ((SignificantTerms.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((SignificantTerms.Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((SignificantTerms.Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
		} else if (bucket instanceof LongTerms.Bucket) {
			final String keyAsString = ((LongTerms.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((LongTerms.Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((LongTerms.Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
		} else if (bucket instanceof StringTerms.Bucket) {
			final String keyAsString = ((StringTerms.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((StringTerms.Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((StringTerms.Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
		} else if (bucket instanceof InternalRange.Bucket) {
			final String keyAsString = ((InternalRange.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((InternalRange.Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((InternalRange.Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
		} else if (bucket instanceof InternalDateRange.Bucket) {
			final String keyAsString = ((InternalDateRange.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((InternalDateRange.Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((InternalDateRange.Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
		} else if (bucket instanceof InternalAutoDateHistogram.Bucket) {
			final String keyAsString = ((InternalAutoDateHistogram.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((InternalAutoDateHistogram.Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((InternalAutoDateHistogram.Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
		} else if (bucket instanceof InternalDateHistogram.Bucket) {
			final String keyAsString = ((InternalDateHistogram.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((InternalDateHistogram.Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((InternalDateHistogram.Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
		} else if (bucket instanceof InternalVariableWidthHistogram.Bucket) {
			final String keyAsString = ((InternalVariableWidthHistogram.Bucket) bucket).getKeyAsString();
			
			final long docCount = ((InternalVariableWidthHistogram.Bucket) bucket).getDocCount();
			final Aggregations aggregations = ((InternalVariableWidthHistogram.Bucket) bucket).getAggregations();
			final AggBean aggBean = AggBean.builder().keyAsString(keyAsString).count(docCount).build();
			if (Objects.nonNull(aggregations)) {
				aggBean.getAggs().addAll(getAllAggs(aggregations));
				
			}
			return aggBean;
		} else if (bucket instanceof TopHits) {
			final String name = ((TopHits) bucket).getName();
			final String type = ((TopHits) bucket).getType();
			final SearchHits hits = ((TopHits) bucket).getHits();
			final AggBean aggBean = AggBean.builder().keyAsString(name).count(hits.getTotalHits().value)
					.type(type).build();
			if (hits.getHits().length > 0) {
				final List<Map<String, Object>> maps = Arrays.stream(hits.getHits())
						.map(HitsUtils::formatValues)
						.collect(Collectors.toList());
				aggBean.getHits().addAll(maps);
				
				
			}
			return aggBean;
			
		}
		
		throw new RuntimeException();
	}
	
	private static AggBean getTermsValue(Terms aggregation) {
		final String termKey = aggregation.getName();
		final long docCountError = aggregation.getDocCountError();
		final String type = aggregation.getType();
		final AggBean aggBean = AggBean.builder().keyAsString(termKey).type(type).count(docCountError)
				.build();
		if (Objects.nonNull(aggregation.getBuckets()) && !aggregation.getBuckets().isEmpty()) {
			final Set<AggBean> aggBeans = aggregation.getBuckets().stream().filter(Objects::nonNull)
					.map(AggUtils::getBucket)
					
					.collect(Collectors.toSet());
			
			aggBean.getAggs().addAll(aggBeans);
		}
		
		return aggBean;
		
	}
	
	@Builder
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	public static class AggBean {
		private Object avgAsString;
		private Object max;
		private Object sum;
		private Object min;
		private Object stdDeviation;
		private Object sumOfSquares;
		private Object variance;
		private String keyAsString;
		private Object count;
		private Object value;
		private String type;
		private GeoPintBean right;
		private GeoPintBean left;
		private List<AggBean> aggs = Lists.newArrayList();
		private List<Map<String, Object>> hits = Lists.newArrayList();
		
	}
	
	@Builder
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	public static class GeoPintBean {
		private double lat;
		private double lon;
		private String geohash;
	}
	
}
