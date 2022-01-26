package com.run.start.bean.dto;

import com.run.start.constant.AggEnum;
import com.run.start.constant.DateHistogramIntervalEnum;
import com.run.start.constant.ValueTypeEnum;
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
public class AggDto implements Serializable {
	
	private Long id;
	private String aggName;
	private AggEnum type;
	private String fieldName;
	private Integer size;
	private List<Long> aggIds;
	private IndexDto indexPojo;
	private ValueTypeEnum ValueType;
	private String childType;
	private Integer interval=1;
	private DateHistogramIntervalEnum dateHistogramInterval;
}