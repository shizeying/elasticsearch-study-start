package com.run.start.pojo;


import com.run.start.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "index_field")
public class IndexFieldPojo extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String fieldName;
	private String type;
	private Long indexId;
	//@ManyToOne
	//private IndexPojo indexPojo;
	@ManyToMany(targetEntity = IndexPojo.class)
	@JoinColumn(
			//@ManyToOne:当前表中的关联字段
			name = "indexId",updatable = false,insertable = false,
			foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT),
			//@ManyToOne:关联表中的关联字段
			referencedColumnName = "id"
	)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<IndexPojo> indexPojoList;
}
