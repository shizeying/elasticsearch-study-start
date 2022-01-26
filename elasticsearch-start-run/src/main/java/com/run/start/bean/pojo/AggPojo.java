package com.run.start.bean.pojo;

import com.run.start.base.BaseEntity;
import com.run.start.constant.AggEnum;
import com.run.start.constant.DateHistogramIntervalEnum;
import com.run.start.constant.ValueTypeEnum;
import com.run.start.convert.AggEnumConvert;
import com.run.start.convert.ListLongConvert;
import com.run.start.convert.ValueTypeEnumConvert;
import lombok.*;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "index_agg")
@EntityListeners({AuditingEntityListener.class})
public class AggPojo extends BaseEntity implements Serializable {
	
	@Column(nullable = false)
	private String aggName;
	@Column(nullable = false)
	@Convert(converter = AggEnumConvert.class)
	private AggEnum type;
	@Column(nullable = false)
	private String fieldName;
	private Integer size;
	@Convert(converter = ValueTypeEnumConvert.class)
	private ValueTypeEnum ValueType;
	@Convert(converter = ListLongConvert.class)
	private List<Long> aggIds;
	@Column(nullable = false)
	private Long indexId;
	@ManyToOne(targetEntity = IndexPojo.class, fetch = FetchType.LAZY)
	@JoinColumn(
			//@ManyToOne:当前表中的关联字段
			name = "indexId", updatable = false, insertable = false, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT),
			//@ManyToOne:关联表中的关联字段
			referencedColumnName = "id")
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private IndexPojo indexPojo;
	private String childType;
	private Integer interval=1;
	private DateHistogramIntervalEnum dateHistogramInterval;
	//
	// @OneToOne(targetEntity = )
	// private ScriptPojo script;
	
}