package com.gscdz.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gscdz.model.CustomUser;
import com.gscdz.model.User;

public interface CustomUserRepository extends JpaRepository<CustomUser, Long>{
	Optional<CustomUser> findByUsername(String username);

}
