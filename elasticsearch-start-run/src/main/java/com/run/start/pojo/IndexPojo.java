package com.run.start.pojo;


import com.run.start.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"indexAlias"})
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
	@org.hibernate.annotations.ForeignKey(name ="none")
	@JoinColumn(name = "id", nullable = false, insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT),
			referencedColumnName =
					"id")
	private IndexAliasPojo indexAlias;
	//@OneToMany
	//private List<IndexFieldPojo> fields;
	
}
