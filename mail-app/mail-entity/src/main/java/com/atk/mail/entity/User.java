package com.atk.mail.entity;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;

import com.atk.mail.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author Abdulkerim.Atik
 *
 */

@Entity
@Table(name = "USERS")
@EqualsAndHashCode(callSuper=false)
public @Data class User extends BaseEntity {

	private static final long serialVersionUID = 8785534197851028173L;

	@Id
	@GeneratedValue(generator = "user_gen")
	@SequenceGenerator(name = "user_gen", sequenceName = "SEQ_USERS", allocationSize = 1, initialValue = 1)
	private Long id;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@Email
	private String email;

	@Column(name = "ACTIVE")
	@Type(type = "org.hibernate.type.YesNoType")
	private Boolean active = Boolean.FALSE;

	@Column(name = "ACTIVATION_KEY")
	private String activationKey;

	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID")
	private Account account;

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "USER_ROLES", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "ROLE_ID") })
	private List<Role> roles = new ArrayList<Role>();

}






	

