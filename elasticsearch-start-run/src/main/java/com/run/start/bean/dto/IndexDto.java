package com.run.start.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
public class IndexDto implements Serializable {
	
	private Long id;
	private String name;
	private String mappingByName;
	private Double boots;
	private IndexAliasDto indexAlias;
	private List<IndexFieldDto> fields;
	private List<ScriptDto> scripts;
	private List<SuggestDto> suggests;
}
