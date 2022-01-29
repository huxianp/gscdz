package com.gscdz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gscdz.model.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long>{
	
}
