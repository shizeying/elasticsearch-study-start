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
public class IndexFieldDto implements Serializable {
	
	private Long id;
	private String fieldName;
	private String type;
	private IndexDto indexPojo;
}
