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
@Table(name = "suggest")
public class SuggestPojo extends BaseEntity {
	private String name;
}
