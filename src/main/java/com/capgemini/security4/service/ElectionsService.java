package com.capgemini.security4.service;

import java.util.List;

import com.capgemini.security4.dto.VotersCountDto;
import com.capgemini.security4.entity.Elections;



public interface ElectionsService {
	Elections createElection(Elections election);
	Elections getElectionById(Long id);
	List<Elections> getAllElections();
	boolean existsByTitle(String title);
	List<VotersCountDto> getVotersCountPerElection();
	Elections updateElection(Long id,Elections election);
	
	List<Elections> getElectionsByStatus(Boolean status);

	List<Elections> getUpcomingElections();

	
}

