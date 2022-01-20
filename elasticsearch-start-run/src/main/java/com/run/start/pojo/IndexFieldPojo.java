package com.run.start.pojo;


import com.run.start.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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
public class IndexFieldPojo extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String fieldName;
	private String type;
	private Long indexId;
	@ManyToOne(targetEntity = IndexPojo.class)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(
			//@ManyToOne:当前表中的关联字段
			name = "indexId", updatable = false, insertable = false, foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT),
			//@ManyToOne:关联表中的关联字段
			referencedColumnName = "id")
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private IndexPojo indexPojo;
	
}
