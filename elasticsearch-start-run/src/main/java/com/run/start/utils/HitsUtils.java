package com.run.start.utils;

import com.alibaba.fastjson.JSON;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

@Slf4j
public class HitsUtils {
	
	public static Map<String, Object> formatValues(final SearchHit searchHit) {
		final String id = searchHit.getId();
		//获取高亮
		final Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
		//获取keys 便于获取路径
		final Map<String, Set<String>> setMap = JsonFlattener.flattenAsMap(
						searchHit.getSourceAsString()).entrySet()
				.stream().filter(Objects::nonNull).map(Entry::getKey)
				.map(key -> Tuple.of(key.replaceAll("(\\[)\\d*(\\])", ""), key))
				.collect(Collectors.groupingBy(Tuple2::_1,
						Collectors.collectingAndThen(Collectors.toList(),
								tuple2s -> tuple2s.stream().map(Tuple2::_2).collect(Collectors.toSet()))));
		
		final Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
		final DocumentContext parse = JsonPath.parse(sourceAsMap);
		for (Entry<String, Set<String>> entry : setMap.entrySet()) {
			final String highlightKey = entry.getKey();
			final HighlightField highlightField = highlightFields.get(highlightKey);
			
			if (Objects.isNull(highlightField)){
				continue;
			}
			for (String key : entry.getValue()) {
					final Text[] fragments = highlightField.getFragments();
					if (Objects.nonNull(fragments) && fragments.length > 0) {
						final Object read = parse.read(key);
						if (Objects.nonNull(read) && read instanceof String) {
							final String content = (String) read;
							for (Text fragment : fragments) {
								final String text = StringUtils.replaceAll()
										
										fragment.string().replaceAll("<em>", "").replaceAll("</em>", "");
								if (StringUtils.contains(content, text)) {
									log.info("进来了");
									final String[] split = StringUtils.split(content, text);
									if (split.length == 0) {
										parse.set(key, fragment.string());
									} else {
										final StringBuilder stringBuilder = new StringBuilder();
										for (int i = 0; i < split.length; i++) {
											stringBuilder.append(split[i]);
											stringBuilder.append(fragment.string());
										}
										parse.set(key, stringBuilder.toString());
									}
								}
							}
						}
					}
			}
		}
		
		
		
		sourceAsMap.put("_id", id);
		sourceAsMap.put("indexName", searchHit.getIndex());
		
		return sourceAsMap;
	}
	
	
}