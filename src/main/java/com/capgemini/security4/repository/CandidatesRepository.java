package com.capgemini.security4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.security4.entity.Candidates;

public interface CandidatesRepository extends JpaRepository<Candidates, Long> {
	
}
