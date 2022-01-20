package com.run.start.bean.dto;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IndexAliasDto implements Serializable {
	
	private  Long id;
	@NotNull
	private  String indexAlias;
	private  List<IndexDto> indexPojoList;
}
