package com.run.start.pojo;


import com.run.start.base.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
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
		 //@ManyToOne
		 //private IndexPojo indexPojo;
}
