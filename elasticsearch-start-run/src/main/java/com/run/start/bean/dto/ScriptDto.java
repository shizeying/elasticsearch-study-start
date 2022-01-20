package com.run.start.bean.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class ScriptDto implements Serializable {
	
	private Long id;
	private String script;
	private String name;
	private String type;
	private Long indexId;
	private IndexDto indexPojo;
}
