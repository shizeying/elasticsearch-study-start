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
@Table(name = "script")
public class ScriptPojo extends BaseEntity {
	private String script;
	private String name;
	private String type;
}
