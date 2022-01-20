package com.run.start.bean.dto;

import com.run.start.bean.pojo.IndexPojo;
import java.io.Serializable;
import lombok.Data;

@Data
public class IndexFieldDto implements Serializable {
	
	private Long id;
	private String fieldName;
	private String type;
	private IndexDto indexPojo;
}
