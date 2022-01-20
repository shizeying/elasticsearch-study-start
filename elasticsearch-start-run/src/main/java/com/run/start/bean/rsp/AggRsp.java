package com.run.start.bean.rsp;

import com.google.common.collect.Lists;
import com.run.start.bean.dto.AggDto;
import lombok.Data;

import java.util.List;


@Data

public class AggRsp extends AggDto {
	
	private List<Double>  values= Lists.newArrayList();
	private boolean keyed=false;
	private Object missing;
	private String script;
	private String format;
	private boolean explain=false;
	private int size=1;
	private int from=10;
	private 
}
