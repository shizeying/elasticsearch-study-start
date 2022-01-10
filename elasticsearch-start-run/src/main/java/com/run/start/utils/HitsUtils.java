package com.run.start.utils;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.util.Map;

public class HitsUtils {
	public static Map<String,Object> formatValues(final SearchHit searchHit) {
		final Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
		return null;
	}
}
