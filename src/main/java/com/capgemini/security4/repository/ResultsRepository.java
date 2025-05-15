package com.capgemini.security4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.security4.entity.Results;

@Repository
public interface ResultsRepository extends JpaRepository<Results, Long> {
	List<Results> findAllByElectionId(Long electionId);
}
