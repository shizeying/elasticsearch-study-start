package com.run.start.pojo;


import com.run.start.pojo.base.BaseEntity;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(catalog = "index")
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
	@ManyToOne
	private IndexAliasPojo indexAlias;
	@OneToMany
	private List<IndexFieldPojo> fields;
	
}
