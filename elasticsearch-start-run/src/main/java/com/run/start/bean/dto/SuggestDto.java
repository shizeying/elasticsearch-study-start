package com.run.start.bean.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class SuggestDto implements Serializable {
	
	private Long id;
	private String name;
	private Long indexId;
	private IndexDto indexPojo;
}
