package com.atk.mail.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import com.atk.mail.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Abdulkerim.Atik
 *
 */
@Entity
@Table(name = "CF_ROLE")
@Immutable
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@EqualsAndHashCode(callSuper=false)
public @Data class Role extends BaseEntity {

	private static final long serialVersionUID = -8062130900332381399L;

	@Id
	@GeneratedValue(generator = "role_gen")
	@SequenceGenerator(name = "role_gen", sequenceName = "SEQ_ROLE")
	private Long id;
	
	private String code;

	private String name;

	private String description;
	
	@Column(name = "ACTIVE")
	@Type(type = "org.hibernate.type.YesNoType")
	private Boolean active;

	@ManyToMany(cascade = { CascadeType.ALL })
	@Fetch(FetchMode.JOIN)
	@JoinTable(name = "CF_ROLE_PRIVILEGES", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "PRIVILEGE_ID") })
	private List<Privilege> privileges = new ArrayList<Privilege>();
	
	public Role() {
	}
	
	public Role(String code) {
		this.code = code;
	}

	public static String[] getAllRoles() {
		return new String[] { "accountAdmin", "customerAdmin"};
	}

}
