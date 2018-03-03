package com.atk.mail.entity.base;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.atk.mail.entity.User;

import lombok.Data;


/**
 * @author Abdulkerim.Atik
 *
 */
@MappedSuperclass
public @Data  class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CDATE")
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UDATE")
	private Date modifyDate;
	

	@ManyToOne
	@JoinColumn(name = "CUSER_ID")
	private User createUser;
	
	@ManyToOne
	@JoinColumn(name="UUSER_ID")
	private User modifyUser;

}
