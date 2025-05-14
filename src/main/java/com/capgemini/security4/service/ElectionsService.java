package com.capgemini.security4.service;

import java.util.List;

import com.capgemini.security4.entity.Elections;

public interface ElectionsService {
	Elections createElection(Elections election);
	Elections getElectionById(Long id);
	List<Elections> getAllElections();
	Elections updateElection(Long id,Elections election);
	void deleteElection(Long id);
	List<Elections> getElectionsByStatus(Boolean status);

	
}

