package com.run.start;

import com.fasterxml.jackson.core.type.TypeReference;
import com.run.start.tools.JacksonUtil;
import com.run.start.utils.AggUtils;
import com.run.start.utils.AggUtils.AggBean;
import com.run.start.utils.HitsUtils;
import io.vavr.control.Try;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.swing.text.Highlighter.Highlight;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.global.GlobalAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
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
	void  setRestHighLevelClient() throws IOException {
		final GlobalAggregationBuilder aggregation  = AggregationBuilders
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
	
		
		SearchTemplateRequest searchTemplateRequest = new SearchTemplateRequest();
		
	
		String indexName = "congress";
		
		final String scrip = getScript("tou_piao.json");
		
		searchTemplateRequest.setScriptType(ScriptType.INLINE);
		searchTemplateRequest.setScript(scrip);
		searchTemplateRequest.setScriptParams(new HashMap<>());
		searchTemplateRequest.setRequest(new SearchRequest(indexName));
		searchTemplateRequest.setExplain(true);
		searchTemplateRequest.setProfile(true);
		Aggregations aggregations = Try.of(() -> restHighLevelClient.searchTemplate(searchTemplateRequest, RequestOptions.DEFAULT))
				.mapTry(SearchTemplateResponse::getResponse)
				.mapTry(SearchResponse::getAggregations)
				
				.onFailure(Throwable::printStackTrace)
				.get();
		final List<AggBean> allAggs = AggUtils.getAllAggs(aggregations);
		System.out.println(JacksonUtil.bean2Json(allAggs));
		
	}
	private String getScript(String fileName) {
		
		return Try.of(() -> new ClassPathResource(String.format("search-template/%s", fileName)))
				.mapTry(ClassPathResource::getInputStream)
				.mapTry(inputStream -> IOUtils.toString(inputStream, StandardCharsets.UTF_8))
				.get();
	}
}
