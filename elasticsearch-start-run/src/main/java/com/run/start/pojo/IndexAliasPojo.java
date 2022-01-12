package com.run.start.pojo;

import com.run.start.pojo.base.BaseEntity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.w3c.dom.stylesheets.LinkStyle;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(catalog = "index_alias")
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
	@OneToMany
	private List<IndexPojo> indexPojoList;
	
}
