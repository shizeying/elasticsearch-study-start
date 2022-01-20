package com.run.start.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
public class IndexAliasDto implements Serializable {
	
	private  Long id;
	@NotNull
	private  String indexAlias;
	private  List<IndexDto> indexPojoList;
}
