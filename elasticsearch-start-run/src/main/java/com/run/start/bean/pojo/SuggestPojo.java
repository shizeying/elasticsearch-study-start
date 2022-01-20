package com.run.start.bean.pojo;

import com.run.start.base.BaseEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "suggest")
@EntityListeners({AuditingEntityListener.class})
public class SuggestPojo extends BaseEntity {
	@Column(nullable = false)
	private String name;
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
