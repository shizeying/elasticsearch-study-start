package com.run.start.utils;

import cn.hutool.core.codec.Caesar;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import com.run.start.bean.rsp.AggRsp;
import com.run.start.constant.ValueTypeEnum;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.join.aggregations.Children;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.adjacency.AdjacencyMatrix;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregation;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.geogrid.GeoGrid;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalAutoDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalVariableWidthHistogram;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.missing.MissingAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.InternalDateRange;
import org.elasticsearch.search.aggregations.bucket.range.InternalRange;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.*;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.*;
import org.elasticsearch.search.aggregations.support.ValueType;
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
			//final AggBean aggBean = new AggBean();
			final Aggregation value = entry.getValue();
			final AggBean bean = matchAggGetValues(value);
			if (Objects.nonNull(bean)) {
				aggBeans.add(bean);
				
			}
			
			
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
		if (aggregation instanceof TopHits) {
			final String name = ((TopHits) aggregation).getName();
			final String type = ((TopHits) aggregation).getType();
			final SearchHits hits = ((TopHits) aggregation).getHits();
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
		
		if (aggregation instanceof ParsedFilter) {
			return getParsedFilterValue((ParsedFilter) aggregation);
		}
		
		return null;
	}
	
	private static AggBean getParsedFilterValue(ParsedFilter aggregation) {
		final String parsedFilterKey = aggregation.getName();
		final long docCount = aggregation.getDocCount();
		final String type = aggregation.getType();
		
		final Aggregations aggregations = ((ParsedFilter) aggregation).getAggregations();
		final AggBean aggBean = AggBean.builder().keyAsString(parsedFilterKey).type(type)
		                               .count(docCount)
		                               .build();
		if (Objects.nonNull(aggregations)) {
			aggBean.getAggs().addAll(getAllAggs(aggregations));
			
		}
		return aggBean;
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
		
		if (Objects.nonNull(aggregation.getBuckets()) && !aggregation.getBuckets().isEmpty()) {
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
		
		if (Objects.nonNull(aggregation.getBuckets()) && !aggregation.getBuckets().isEmpty()) {
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
		
		if (Objects.nonNull(aggregation.getBuckets()) && !aggregation.getBuckets().isEmpty()) {
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
		
		final AggBean aggBean = AggBean.builder().keyAsString(compositeAggregationKey).type(type)
		                               .build();
		
		if (Objects.nonNull(aggregation.getBuckets()) && !aggregation.getBuckets().isEmpty()) {
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
		
		if (Objects.nonNull(aggregation.getBuckets()) && !aggregation.getBuckets().isEmpty()) {
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
	@JsonInclude(JsonInclude.Include.NON_NULL)
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
		@Builder.Default
		private List<AggBean> aggs = Lists.newArrayList();
		@Builder.Default
		private List<Map<String, Object>> hits = Lists.newArrayList();
		
	}
	
	@Builder
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class GeoPintBean {
		
		private double lat;
		private double lon;
		private String geohash;
	}
	
	public static ValueType getValueTypeEnum(ValueTypeEnum typeEnum) {
		
		switch (typeEnum) {
			case STRING:
				return ValueType.STRING;
			case LONG:
				return ValueType.LONG;
			case DOUBLE:
				return ValueType.DOUBLE;
			case NUMBER:
				return ValueType.NUMBER;
			case DATE:
				return ValueType.DATE;
			case IP:
				return ValueType.IP;
			case NUMERIC:
				return ValueType.NUMERIC;
			case GEOPOINT:
				return ValueType.GEOPOINT;
			case BOOLEAN:
				return ValueType.BOOLEAN;
			case RANGE:
				return ValueType.RANGE;
			default:
				throw new RuntimeException();
		}
	}
	
	private AggregationBuilder get(AggRsp dto) {
		switch (dto.getType()) {
			case global:
				return AggregationBuilders
						.global(dto.getAggName());
			case terms:
				final TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(dto.getAggName()).field(dto.getFieldName());
				if (StringUtils.isNoneBlank(dto.getScript())) {
					termsAggregationBuilder.script(new Script(dto.getScript()));
				}
				if (Objects.nonNull(dto.getMissing())) {
					termsAggregationBuilder.missing(dto.getMissing());
				}
				if (StringUtils.isNotBlank(dto.getFormat())) {
					termsAggregationBuilder.format(dto.getFormat());
				}
				if (Objects.nonNull(dto.getValueType())) {
					termsAggregationBuilder.userValueTypeHint(getValueTypeEnum(dto.getValueType()));
				}
				return termsAggregationBuilder;
			case filter:
				if (CollectionUtils.isEmpty(dto.getTermQueries())) {
					throw new RuntimeException();
				}
				if (dto.getTermQueries().size() == 1) {
					return AggregationBuilders.filter(dto.getAggName(), QueryBuilders.termQuery(dto.getTermQueries().get(0).getField(),
							dto.getTermQueries().get(0).getKw()));
					
				} else {
					throw new RuntimeException();
				}
			
			case missing:
				final MissingAggregationBuilder missingAggregationBuilder = AggregationBuilders.missing(dto.getAggName()).field(dto.getFieldName());
				if (StringUtils.isNoneBlank(dto.getScript())) {
					missingAggregationBuilder.script(new Script(dto.getScript()));
				}
				if (Objects.nonNull(dto.getMissing())) {
					missingAggregationBuilder.missing(dto.getMissing());
				}
				if (StringUtils.isNotBlank(dto.getFormat())) {
					missingAggregationBuilder.format(dto.getFormat());
				}
				if (Objects.nonNull(dto.getValueType())) {
					missingAggregationBuilder.userValueTypeHint(getValueTypeEnum(dto.getValueType()));
				}
				return missingAggregationBuilder;
			case Nested:
				return AggregationBuilders
						.nested(dto.getAggName(), dto.getFieldName());
			
			
			case children:
			case significantTerms:
			case Range:
			case histogram:
			case dateHistogram:
			case geoDistance:
			case min:
				final MinAggregationBuilder min = AggregationBuilders.min(dto.getAggName()).field(dto.getFieldName());
				if (StringUtils.isNoneBlank(dto.getScript())) {
					min.script(new Script(dto.getScript()));
				}
				if (Objects.nonNull(dto.getMissing())) {
					min.missing(dto.getMissing());
				}
				if (StringUtils.isNotBlank(dto.getFormat())) {
					min.format(dto.getFormat());
				}
				if (Objects.nonNull(dto.getValueType())) {
					min.userValueTypeHint(getValueTypeEnum(dto.getValueType()));
				}
				return min;
			
			case max:
				final MaxAggregationBuilder max = AggregationBuilders.max(dto.getAggName()).field(dto.getFieldName());
				if (StringUtils.isNotBlank(dto.getFormat())) {
					max.format(dto.getFormat());
				}
				if (Objects.nonNull(dto.getMissing())) {
					max.missing(dto.getMissing());
				}
				if (StringUtils.isNoneBlank(dto.getScript())) {
					max.script(new Script(dto.getScript()));
				}
				if (Objects.nonNull(dto.getValueType())) {
					max.userValueTypeHint(getValueTypeEnum(dto.getValueType()));
				}
				return max;
			case sum:
				final SumAggregationBuilder sum = AggregationBuilders
						.sum(dto.getAggName()).field(dto.getFieldName());
				if (StringUtils.isNotBlank(dto.getFormat())) {
					sum.format(dto.getFormat());
				}
				if (Objects.nonNull(dto.getMissing())) {
					sum.missing(dto.getMissing());
				}
				if (StringUtils.isNoneBlank(dto.getScript())) {
					sum.script(new Script(dto.getScript()));
				}
				if (Objects.nonNull(dto.getValueType())) {
					sum.userValueTypeHint(getValueTypeEnum(dto.getValueType()));
				}
				return max;
			case avg:
				final AvgAggregationBuilder avg = AggregationBuilders
						.avg(dto.getAggName()).field(dto.getFieldName());
				if (StringUtils.isNotBlank(dto.getFormat())) {
					max.format(dto.getFormat());
				}
				if (Objects.nonNull(dto.getMissing())) {
					max.missing(dto.getMissing());
				}
				if (StringUtils.isNoneBlank(dto.getScript())) {
					max.script(new Script(dto.getScript()));
				}
				if (Objects.nonNull(dto.getValueType())) {
					avg.userValueTypeHint(getValueTypeEnum(dto.getValueType()));
				}
				if (Objects.nonNull(dto.getValueType())) {
					avg.userValueTypeHint(getValueTypeEnum(dto.getValueType()));
				}
				return max;
			case stats:
				final StatsAggregationBuilder stats = AggregationBuilders
						.stats(dto.getAggName()).field(dto.getFieldName());
				if (StringUtils.isNotBlank(dto.getFormat())) {
					max.format(dto.getFormat());
				}
				if (Objects.nonNull(dto.getMissing())) {
					max.missing(dto.getMissing());
				}
				if (StringUtils.isNoneBlank(dto.getScript())) {
					max.script(new Script(dto.getScript()));
				}
				if (Objects.nonNull(dto.getValueType())) {
					max.userValueTypeHint(getValueTypeEnum(dto.getValueType()));
				}
				if (Objects.nonNull(dto.getValueType())) {
					max.userValueTypeHint(getValueTypeEnum(dto.getValueType()));
				}
				return max;
			case extendedStats:
				final ExtendedStatsAggregationBuilder extendedStatsAggregationBuilder = AggregationBuilders
						.extendedStats(dto.getAggName()).field(dto.getFieldName());
				if (StringUtils.isNotBlank(dto.getFormat())) {
					extendedStatsAggregationBuilder.format(dto.getFormat());
				}
				if (Objects.nonNull(dto.getMissing())) {
					extendedStatsAggregationBuilder.missing(dto.getMissing());
				}
				if (StringUtils.isNoneBlank(dto.getScript())) {
					extendedStatsAggregationBuilder.script(new Script(dto.getScript()));
				}
				if (Objects.nonNull(dto.getValueType())) {
					extendedStatsAggregationBuilder.userValueTypeHint(getValueTypeEnum(dto.getValueType()));
				}
				return extendedStatsAggregationBuilder;
			case count:
				final ValueCountAggregationBuilder count = AggregationBuilders.count(dto.getAggName()).field(dto.getFieldName());
				if (StringUtils.isNotBlank(dto.getFormat())) {
					count.format(dto.getFormat());
				}
				if (Objects.nonNull(dto.getMissing())) {
					count.missing(dto.getMissing());
				}
				if (StringUtils.isNoneBlank(dto.getScript())) {
					count.script(new Script(dto.getScript()));
				}
				if (Objects.nonNull(dto.getValueType())) {
					count.userValueTypeHint(getValueTypeEnum(dto.getValueType()));
				}
				return count;
			
			case percentiles:
				return AggregationBuilders.percentiles(dto.getAggName()).field(dto.getFieldName());
			case percentileRanks:
				final double[] values = dto.getValues().stream().filter(Objects::nonNull).mapToDouble(Double::doubleValue).toArray();
				if (values.length > 0) {
					final PercentileRanksAggregationBuilder ranksAggregationBuilder = AggregationBuilders.percentileRanks(dto.getAggName(), values)
					                                                                                     .field(dto.getFieldName())
					                                                                                     .keyed(dto.isKeyed());
					if (Objects.nonNull(dto.getMissing())) {
						ranksAggregationBuilder.missing(dto.getMissing());
					}
					if (StringUtils.isNoneBlank(dto.getScript())) {
						ranksAggregationBuilder.script(new Script(dto.getScript()));
						
					}
					if (StringUtils.isNotBlank(dto.getFormat())) {
						ranksAggregationBuilder.format(dto.getFormat());
					}
					if (Objects.nonNull(dto.getValueType())) {
						ranksAggregationBuilder.userValueTypeHint(getValueTypeEnum(dto.getValueType()));
					}
					return ranksAggregationBuilder;
				} else {
					throw new RuntimeException("values不能为空");
				}
			
			case cardinality:
				final CardinalityAggregationBuilder cardinalityAggregationBuilder = AggregationBuilders.cardinality(dto.getAggName())
				                                                                                       .field(dto.getFieldName());
				if (Objects.nonNull(dto.getMissing())) {
					cardinalityAggregationBuilder.missing(dto.getMissing());
				}
				if (StringUtils.isNoneBlank(dto.getScript())) {
					cardinalityAggregationBuilder.script(new Script(dto.getScript()));
				}
				if (StringUtils.isNotBlank(dto.getFormat())) {
					cardinalityAggregationBuilder.format(dto.getFormat());
				}
				if (Objects.nonNull(dto.getValueType())) {
					cardinalityAggregationBuilder.userValueTypeHint(getValueTypeEnum(dto.getValueType()));
				}
				return cardinalityAggregationBuilder;
			case geoBounds:
				final GeoBoundsAggregationBuilder aggregationBuilder = new GeoBoundsAggregationBuilder(dto.getAggName());
				aggregationBuilder.field(dto.getFieldName());
				aggregationBuilder.wrapLongitude(true);
				if (Objects.nonNull(dto.getMissing())) {
					aggregationBuilder.missing(dto.getMissing());
				}
				if (StringUtils.isNoneBlank(dto.getScript())) {
					aggregationBuilder.script(new Script(dto.getScript()));
				}
				if (StringUtils.isNotBlank(dto.getFormat())) {
					aggregationBuilder.format(dto.getFormat());
				}
				if (Objects.nonNull(dto.getValueType())) {
					aggregationBuilder.userValueTypeHint(getValueTypeEnum(dto.getValueType()));
				}
				
				return aggregationBuilder;
			case topHits:
				return AggregationBuilders
						.topHits(dto.getAggName())
						.explain(dto.isExplain())
						.size(dto.getSize())
						.from(dto.getFrom())
						
						;
			case scriptedMetric:
				// return  AggregationBuilders
				// 		.scriptedMetric("agg")
				// 		.initScript(new Script("state.heights = []"))
				// 		.mapScript(new Script("state.heights.add(doc.gender.value == 'male' ? doc.height.value : -1.0 * doc.height.value)"))
				// 		.combineScript(new Script("double heights_sum = 0.0; for (t in state.heights) { heights_sum += t } return heights_sum"))
				// 		.reduceScript(new Script("double heights_sum = 0.0; for (a in states) { heights_sum += a } return heights_sum"));
			
			default:
				throw new RuntimeException();
			
		}
		
		
	}
}