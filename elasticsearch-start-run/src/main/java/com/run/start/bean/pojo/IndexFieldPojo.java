package com.run.start.bean.pojo;


import com.run.start.base.BaseEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "index_field")
@EntityListeners({AuditingEntityListener.class})
public class IndexFieldPojo extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Column(nullable = false)
	private String fieldName;
	@Column(nullable = false)
	private String type;
	@Column(nullable = false)
	private Long indexId;
	@ManyToOne(targetEntity = IndexPojo.class,fetch=FetchType.LAZY)
	@JoinColumn(
			//@ManyToOne:当前表中的关联字段
			name = "indexId", updatable = false, insertable = false, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT),
			//@ManyToOne:关联表中的关联字段
			referencedColumnName = "id")
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private IndexPojo indexPojo;
	
}
