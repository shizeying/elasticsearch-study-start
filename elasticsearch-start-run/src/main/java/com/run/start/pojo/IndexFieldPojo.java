package com.run.start.pojo;


import com.run.start.pojo.base.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
@Table(name = "index_field")
public class IndexFieldPojo extends BaseEntity {
	   private String fieldName;
		 private String type;
		 private Long indexId;
		 //@ManyToOne
		 //private IndexPojo indexPojo;
}
