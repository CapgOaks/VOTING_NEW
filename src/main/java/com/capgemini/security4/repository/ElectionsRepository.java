package com.capgemini.security4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.security4.entity.Elections;

import org.springframework.data.jpa.repository.Query;

public interface ElectionsRepository extends JpaRepository<Elections, Long> {

    @Query("SELECT e FROM Elections e WHERE e.startDate > CURRENT_TIMESTAMP")
    List<Elections> findUpcomingElections();

    boolean existsByTitle(String title);


	List<Elections> findByElectionStatus(Boolean status);

}
