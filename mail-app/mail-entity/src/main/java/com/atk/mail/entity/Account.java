package com.atk.mail.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;

import com.atk.mail.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Abdulkerim.Atik
 *
 */

@Entity
@Table(name = "ACCOUNT")
@EqualsAndHashCode(callSuper = false)
public @Data class Account extends BaseEntity {

	private static final long serialVersionUID = -4612994571839714476L;

	@Id
	@GeneratedValue(generator = "account_gen")
	@SequenceGenerator(name = "account_gen", sequenceName = "SEQ_ACCOUNT", allocationSize = 1, initialValue = 1)
	private Long id;

	@Column(name = "COMMERCIAL_NAME")
	private String commercialName;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "VKN_TCKNO")
	private String vknTckno;

	@Email
	private String email;

	private String website;

	@Column(name = "TAX_OFFICE")
	private String taxOffice;

	private String status;

	@Column(name = "SUB_STATUS")
	private String subStatus;
	
	@OneToMany(mappedBy = "account", cascade = { CascadeType.ALL })
	private List<User> userList = new ArrayList<User>();


}
