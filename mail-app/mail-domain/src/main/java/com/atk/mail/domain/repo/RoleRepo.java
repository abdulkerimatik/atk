package com.atk.mail.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atk.mail.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Long>{

	Role findByCode(String code);
}
