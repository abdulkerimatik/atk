package com.atk.mail.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.atk.mail.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>,UserRepoCustom {

	@Query("select user from User user where user.username=:username")
	User findByUsername(@Param("username") String username);

}
