package com.run.start.bean.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
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
