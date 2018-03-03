package com.atk.mail.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.atk.mail.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Abdulkerim.Atik
 *
 */
@Entity
@Table(name = "CF_PRIVILEGE")
@Immutable
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@EqualsAndHashCode(callSuper = false)
public @Data class Privilege extends BaseEntity {

	private static final long serialVersionUID = 4535072681279576640L;

	@Id
	@GeneratedValue(generator = "privilege_gen")
	@SequenceGenerator(name = "privilege_gen", sequenceName = "SEQ_PRIVILEGE")
	private Long id;

	@Column(name = "CODE")
	private String code;

	@Column(name = "DESCRIPTION")
	private String description;

	@ManyToMany
	private List<Role> roles = new ArrayList<Role>();

}
