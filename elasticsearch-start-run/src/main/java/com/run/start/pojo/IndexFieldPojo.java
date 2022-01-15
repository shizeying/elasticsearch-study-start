package com.run.start.pojo;


import com.run.start.base.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "index_field")
public class IndexFieldPojo extends BaseEntity {
	   private String fieldName;
		 private String type;
		 private Long indexId;
		 //@ManyToOne
		 //private IndexPojo indexPojo;
}
