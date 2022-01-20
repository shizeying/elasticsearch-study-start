package com.run.start.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
public class ScriptDto implements Serializable {
	
	private Long id;
	private String script;
	private String name;
	private String type;
	private Long indexId;
	private IndexDto indexPojo;
}
