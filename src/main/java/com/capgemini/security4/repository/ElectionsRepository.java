package com.capgemini.security4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.security4.entity.Elections;

public interface ElectionsRepository extends JpaRepository<Elections, Long> {

	List<Elections> findByElectionStatus(Boolean status);

}
