package com.run.start.utils;

import com.google.common.collect.Lists;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.join.aggregations.Children;
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
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class AggUtils {
	
	
	public static List<AggBean> getAllAggs(Aggregations aggregations) {
		
		if (Objects.isNull(aggregations) || CollectionUtils.isEmpty(aggregations.asMap())) {
			return Lists.newArrayList();
			
		}
		
		for (Entry<String, Aggregation> entry : aggregations.getAsMap().entrySet()) {
			final AggBean aggBean = new AggBean();
			final String key = entry.getKey();
			final Aggregation value = entry.getValue();
			final Object values = matchAggGetValues(value);
			aggBean.setKeyAsString(key);
			aggBean.setCount(values);
			
		}
		
		
	}
	
	private static <R> R matchAggGetValues(Aggregation aggregation) {
		if (aggregation instanceof Terms) {
			final AggBean termsValue = getTermsValue((Terms) aggregation);
		}
		
		if (aggregation instanceof Histogram) {
			getHistogramBucketValue((Histogram) aggregation);
		}
		if (aggregation instanceof CompositeAggregation) {
			getCompositeAggregationValue((CompositeAggregation) aggregation);
		}
		if (aggregation instanceof Filters) {
			getFiltersValue((Filters) aggregation);
		}
		
		if (aggregation instanceof Nested) {
			getNestedValue((Nested) aggregation);
		}
		if (aggregation instanceof Missing) {
			getMissingValue((Missing) aggregation);
		}
		if (aggregation instanceof Children) {
			getChildrenValue((Children) aggregation);
		}
		if (aggregation instanceof SignificantTerms) {
			getSignificantTermsValue((SignificantTerms) aggregation);
		}
		if (aggregation instanceof Range) {
			getRangeValue((Range) aggregation);
		}
		if (aggregation instanceof Global) {
			getGlobalValue((Global) aggregation);
		}
		//if (aggregation instanceof GeoHashGrid){
		//
		//}
		
		return null;
	}
	
	private static void getGlobalValue(Global aggregation) {
		final String globalKey = aggregation.getName();
		final long docCount = aggregation.getDocCount();
		aggregation.getAggregations().asList().stream().map(AggUtils::matchAggGetValues)
				.collect(Collectors.toSet());
		
	}
	
	private static void getChildrenValue(Children aggregation) {
		final String childrenKey = aggregation.getName();
		final long docCount = aggregation.getDocCount();
	}
	
	private static void getNestedValue(Nested aggregation) {
		final String nestedKey = aggregation.getName();
		final long docCount = aggregation.getDocCount();
	}
	
	private static void getMissingValue(Missing aggregation) {
		final String missingKey = aggregation.getName();
		final long docCount = aggregation.getDocCount();
	}
	
	private static void getRangeValue(Range aggregation) {
		final String rangeKey = aggregation.getName();
		final Set<AggBean> aggBeans = aggregation
				.getBuckets()
				.stream()
				.filter(Objects::nonNull)
				.map(AggUtils::getBucket)
				.collect(Collectors.toSet());
		
	}
	
	private static void getSignificantTermsValue(SignificantTerms aggregation) {
		final String significantTermsKey = aggregation.getName();
		aggregation.getBuckets().stream().filter(Objects::nonNull).map(AggUtils::getBucket)
	}
	
	
	private static void getFiltersValue(Filters aggregation) {
		final String filtersKey = aggregation.getName();
		aggregation.getBuckets().stream().filter(Objects::nonNull).map(AggUtils::getBucket)
	}
	
	private static AggBean getCompositeAggregationValue(CompositeAggregation aggregation) {
		final String compositeAggregationKey = aggregation.getName();
		final String type = aggregation.getType();
		
		final Set<AggBean> aggBeans = aggregation.getBuckets().stream().filter(Objects::nonNull)
				.map(AggUtils::getBucket)
				
				.collect(Collectors.toSet());
		final AggBean aggBean = AggBean.builder().keyAsString(compositeAggregationKey).type(type).build();
		aggBean.getAggs().addAll(aggBeans);
		return aggBean;
		
		
	}
	
	private static AggBean getHistogramBucketValue(Histogram aggregation) {
		final String histogramKey = aggregation.getName();
		final String type = aggregation.getType();
		
		final Set<AggBean> aggBeans = aggregation.getBuckets().stream().filter(Objects::nonNull)
				.map(AggUtils::getBucket)
				
				.collect(Collectors.toSet());
		final AggBean aggBean = AggBean.builder().keyAsString(histogramKey).type(type).build();
		aggBean.getAggs().addAll(aggBeans);
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
		
		final Set<AggBean> aggBeans = aggregation.getBuckets().stream().filter(Objects::nonNull)
				.map(AggUtils::getBucket)
				
				.collect(Collectors.toSet());
		final AggBean aggBean = AggBean.builder().keyAsString(termKey).type(type).count(docCountError)
				.build();
		aggBean.getAggs().addAll(aggBeans);
		return aggBean;
		
	}
	
	@Builder
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	public static class AggBean {
		
		private String keyAsString;
		private Object count;
		private String type;
		private List<AggBean> aggs = Lists.newArrayList();
		
	}
}
