package com.run.start;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.common.collect.Maps;
import com.run.start.tools.JacksonUtil;
import com.run.start.utils.AggUtils;
import com.run.start.utils.AggUtils.AggBean;
import com.run.start.utils.HitsUtils;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.control.Try;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.global.GlobalAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

@SpringBootTest
class AppTests {
	
	@Test
	void contextLoads() {
	}
	
	@Autowired
	private RestHighLevelClient restHighLevelClient;
	
	@Test
	void setRestHighLevelClient() throws IOException {
		final GlobalAggregationBuilder aggregation = AggregationBuilders
				.global("agg")
				.subAggregation(AggregationBuilders.terms("genders").field("title.raw"));
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.matchQuery("committeeNames", "Senate"));
		
		sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
		sourceBuilder.size(10);
		sourceBuilder.aggregation(aggregation);
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.field("committeeNames", 10);
		sourceBuilder.highlighter(highlightBuilder);
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("congress-legislation3");
		//sourceBuilder.fetchSource("committeeNames", "");
		searchRequest.source(sourceBuilder);
		final SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		
		for (SearchHit hit : search.getHits().getHits()) {
			HitsUtils.formatValues(hit);
		}
		
		final Suggest suggest = search.getSuggest();
		SearchTemplateRequest searchTemplateRequest = new SearchTemplateRequest();
		
		String indexName = "congress";
		
		final String scrip = getScript("tou_piao.json");
		
		searchTemplateRequest.setScriptType(ScriptType.INLINE);
		searchTemplateRequest.setScript(scrip);
		searchTemplateRequest.setScriptParams(new HashMap<>());
		searchTemplateRequest.setRequest(new SearchRequest(indexName));
		searchTemplateRequest.setExplain(true);
		searchTemplateRequest.setProfile(true);
		Aggregations aggregations = Try.of(
						() -> restHighLevelClient.searchTemplate(searchTemplateRequest, RequestOptions.DEFAULT))
				.mapTry(SearchTemplateResponse::getResponse)
				.mapTry(SearchResponse::getAggregations)
				
				.onFailure(Throwable::printStackTrace)
				.get();
		final List<AggBean> allAggs = AggUtils.getAllAggs(aggregations);
		
	}
	
	private String getScript(String fileName) {
		
		return Try.of(() -> new ClassPathResource(String.format("search-template/%s", fileName)))
				.mapTry(ClassPathResource::getInputStream)
				.mapTry(inputStream -> IOUtils.toString(inputStream, StandardCharsets.UTF_8))
				.get();
	}
	
	
	@Test
	void setMapping() throws IOException {
		GetMappingsRequest request = new GetMappingsRequest();
		request.indices("congress-legislation3");
		request.indicesOptions(IndicesOptions.lenientExpandOpen());
		final GetMappingsResponse mapping = restHighLevelClient.indices().getMapping(request,
				RequestOptions.DEFAULT);
		final Map<String, Object> properties = (Map<String, Object>) mapping.mappings()
				.get("congress-legislation3")
				.getSourceAsMap().get("properties");
		final Map<String, Object> map = JsonFlattener.flattenAsMap(
				JacksonUtil.bean2Json(properties));
		final Map<String, Object> mapTmp = Maps.newHashMap(map);
		map.keySet()
				.stream()
				.filter(key -> StringUtils.containsAny(key, "copy_to", "analyzer", "similarity", "id",
						"ignore_above"))
				.forEach(mapTmp::remove);
		final Map<String, Set<String>> collect = mapTmp
				.entrySet()
				.stream()
				.filter(Objects::nonNull)
				.map(entry -> {
					final String sourceKey = entry.getKey().replaceAll("\\.fields", "").replaceAll("\\"
							+ ".type", "").replaceAll("\\.properties", "");
					final String dealKey = sourceKey.replaceAll("\\.raw", "");
					return Tuple.of(dealKey, sourceKey, (String) entry.getValue());
				})
				.filter(tuplue3 -> StringUtils.equalsAny(tuplue3._3, "text", "keyword"))
				//1.现根据type聚合 2.根据dealKey聚合 3.获取集合
				.collect(Collectors.groupingBy(
						Tuple3::_1, Collectors.mapping(Tuple3::_2, Collectors.toSet())
				
				));
		final Map<String, Set<String>> notKeywordAndText = mapTmp
				.entrySet()
				.stream()
				.map(entry -> {
					final String sourceKey = entry.getKey().replaceAll("\\.fields", "").replaceAll("\\"
							+ ".type", "").replaceAll("\\.properties", "");
					final String dealKey = sourceKey.replaceAll("\\.raw", "");
					return Tuple.of(dealKey, sourceKey, (String) entry.getValue());
				})
				.filter(tuple3 -> !StringUtils.equalsAny(tuple3._3, "text", "keyword"))
				.collect(Collectors.groupingBy(
						Tuple3::_3, Collectors.mapping(Tuple3::_2, Collectors.toSet())
				
				));
		
		System.out.println(JacksonUtil.bean2Json(collect));
		//System.out.println(JacksonUtil.bean2Json(stringSetMap));
		
	}
	@Test
	void setRestHighLevelClient2(){
		BulkRequest bulkRequest = new BulkRequest("congress-test");
		
		final IndexRequest indexRequest = new IndexRequest();
		indexRequest.id("1");
		Map<String,Object> data=new HashMap<String,Object>(){{
			 put("aa","1aaa");
			
		}};
		indexRequest.source(JacksonUtil.bean2Json(data), XContentType.JSON);
		bulkRequest.add(indexRequest);
		
		
		String path = System.getProperty("user.dir");
		System.out.println(path);
		
		
	}
	
	public static void main(String[] args) {
		
		System.out.println();
	}
	
	
}
