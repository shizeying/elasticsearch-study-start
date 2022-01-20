package com.run.start.pojo;


import com.google.common.collect.Lists;
import com.run.start.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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
@EntityListeners({AuditingEntityListener.class})
public class IndexPojo extends BaseEntity implements Serializable {
	
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
	
	@ManyToOne(targetEntity = IndexAliasPojo.class,fetch=FetchType.LAZY)
	//@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(
			//@ManyToOne:当前表中的关联字段
			name = "id", updatable = false, insertable = false,
			foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT),
			//@ManyToOne:关联表中的关联字段
			referencedColumnName = "indexId"
	)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private IndexAliasPojo indexAlias;
	
	@OneToMany(targetEntity = IndexFieldPojo.class,fetch=FetchType.LAZY)
	//如果不需要索引,那么此注解是不去能去掉的:	@org.springframework.data.annotation.Transient @org.hibernate.annotations.ForeignKey(name = "none")
	@org.hibernate.annotations.ForeignKey(name = "none")
	@org.springframework.data.annotation.Transient
	@JoinColumn(
			//@OneToMany 关联表中的关联字段
			name = "indexId",
			updatable = false, insertable = false,
			//@OneToMany 当前表中的关联字段
			referencedColumnName = "id"
			, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)
	)
	private List<IndexFieldPojo> fields = Lists.newLinkedList();
	
	@OneToMany(targetEntity = ScriptPojo.class,fetch=FetchType.LAZY)
	//如果不需要索引,那么此注解是不去能去掉的:	@org.springframework.data.annotation.Transient @org.hibernate.annotations.ForeignKey(name = "none")
	@org.hibernate.annotations.ForeignKey(name = "none")
	@org.springframework.data.annotation.Transient
	@JoinColumn(
			//@OneToMany 关联表中的关联字段
			name = "indexId",
			updatable = false, insertable = false,
			//@OneToMany 当前表中的关联字段
			referencedColumnName = "id"
			, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)
	)
	private List<ScriptPojo> scripts = Lists.newLinkedList();
	@OneToMany(targetEntity = SuggestPojo.class,fetch=FetchType.LAZY)
	//如果不需要索引,那么此注解是不去能去掉的:	@org.springframework.data.annotation.Transient @org.hibernate.annotations.ForeignKey(name = "none")
	@org.hibernate.annotations.ForeignKey(name = "none")
	@org.springframework.data.annotation.Transient
	@JoinColumn(
			//@OneToMany 关联表中的关联字段
			name = "indexId",
			updatable = false, insertable = false,
			//@OneToMany 当前表中的关联字段
			referencedColumnName = "id"
			, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)
	)
	private List<SuggestPojo> suggests = Lists.newLinkedList();
	;
	
}
