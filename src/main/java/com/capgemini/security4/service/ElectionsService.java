package com.capgemini.security4.service;

import java.util.List;

import com.capgemini.security4.entity.Elections;


import com.capgemini.security4.dto.VotersCountDto;

public interface ElectionsService {
	Elections createElection(Elections election);
	Elections getElectionById(Long id);
	List<Elections> getAllElections();
	Elections updateElection(Long id,Elections election);
	void deleteElection(Long id);
	List<Elections> getElectionsByStatus(Boolean status);

	List<Elections> getUpcomingElections();

	List<VotersCountDto> getVotersCountPerElection();
}

