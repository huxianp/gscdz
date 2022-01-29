package com.gscdz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gscdz.model.SolvedProblem;

public interface SolvedProblemRepository extends JpaRepository<SolvedProblem, Long>{

}
