package com.run.start.pojo;


import com.run.start.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "es_index")
public class IndexPojo extends BaseEntity {
	
	/**
	 * 索引名称
	 */
	private String name;
	/**
	 * 索引别名
	 */
	private String alias;
	/**
	 * 对应的索引权重
	 */
	private Double boots;
	
	@ManyToOne(targetEntity = IndexAliasPojo.class)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(
			 //@ManyToOne:当前表中的关联字段
			name = "id", nullable = false, insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT),
			//@ManyToOne:关联表中的关联字段
			referencedColumnName = "indexId"
	)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private IndexAliasPojo indexAlias;
	//@OneToMany
	//private List<IndexFieldPojo> fields;
	
}
