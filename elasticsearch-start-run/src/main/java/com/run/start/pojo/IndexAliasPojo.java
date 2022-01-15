package com.run.start.pojo;

import com.run.start.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"indexPojoList"})
@ToString
@Entity
@Table(name = "index_alias")
public class IndexAliasPojo extends BaseEntity {
	
	/**
	 * index 所属搜索别名
	 */
	@NotNull
	private String indexAlias;
	
	/**
	 * index id
	 */
	private Long indexId;
	@OneToMany(targetEntity = IndexPojo.class)
	@Transient
	@JoinColumn(name = "id", nullable = false, insertable = false, updatable = false,
			referencedColumnName =
					"indexId")
	private List<IndexPojo> indexPojoList;
	
}
