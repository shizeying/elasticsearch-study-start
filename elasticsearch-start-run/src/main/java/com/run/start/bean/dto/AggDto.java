package com.run.start.bean.dto;

import com.run.start.constant.AggEnum;
import com.run.start.convert.AggEnumConvert;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class AggDto implements Serializable {
	
	private Long id;
	private String aggName;
	private AggEnum type;
	private String fieldName;
	private Integer size;
	private List<Long> aggIds;
	private IndexDto indexPojo;
}
