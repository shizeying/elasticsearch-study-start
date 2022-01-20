package com.run.start.bean.pojo;

import com.run.start.base.BaseEntity;
import com.run.start.constant.AggEnum;
import com.run.start.convert.AggEnumConvert;
import com.run.start.convert.ListLongConvert;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
	
	@OneToOne(targetEntity = )
	private ScriptPojo script;
	
}
