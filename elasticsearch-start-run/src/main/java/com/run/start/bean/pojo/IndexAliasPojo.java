package com.run.start.bean.pojo;

import com.google.common.collect.Lists;
import com.run.start.base.BaseEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
@Table(name = "index_alias")
@EntityListeners({AuditingEntityListener.class})
public class IndexAliasPojo extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * index 所属搜索别名
	 */
	@NotNull
	@Column(nullable = false)
	private String indexAlias;

	@OneToMany(targetEntity = IndexPojo.class,fetch=FetchType.LAZY)
	//如果不需要索引,那么此注解是不去能去掉的:	@org.springframework.data.annotation.Transient @org.hibernate.annotations.ForeignKey(name = "none")
	@org.hibernate.annotations.ForeignKey(name = "none")
	@org.springframework.data.annotation.Transient
	@JoinColumn(
			//@OneToMany 关联表中的关联字段
			name = "indexAliasId",
			updatable = false,insertable = false,
			//@OneToMany 当前表中的关联字段
			referencedColumnName = "id"
			, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)
	)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<IndexPojo> indexPojoList = Lists.newLinkedList();
	
}
