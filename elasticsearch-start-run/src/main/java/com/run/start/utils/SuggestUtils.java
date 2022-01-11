package com.run.start.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.suggest.Suggest;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class SuggestUtils {
	public static List<SuggBean> formatValue(Suggest suggest) {
		List<SuggBean> suggBeans= Lists.newArrayList();
				suggest.iterator().forEachRemaining(next->{
			final String name = next.getName();
			final List<Options> optionsList = next.getEntries()
					.stream()
					.filter(Objects::nonNull)
					.map(SuggestUtils::getSuggest)
					.flatMap(Collection::stream)
					.distinct()
					.collect(Collectors.toList());
					suggBeans.add(	SuggBean.builder().name(name).texts(optionsList).build());
		});
	     return  suggBeans;
		
	}
	
	private static List<Options> getSuggest(final Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> entry) {
		
		return entry.getOptions()
				.stream()
				.map(option -> Options.builder().
						text(option.getHighlighted().string())
						.highlightedText(option.getText().string())
						.score(option.getScore())
						.build())
				.collect(Collectors.toList());
		
		
	}
	
	
	@Builder
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Options {
		private String text;
		private String highlightedText;
		private float score;
	}
	@Builder
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class SuggBean {
		private String name;
		private List<Options> texts;
	}
}
