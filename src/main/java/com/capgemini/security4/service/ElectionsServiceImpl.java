package com.capgemini.security4.service;

import com.capgemini.security4.entity.Elections;

import com.capgemini.security4.exception.ElectionNotFoundException;
import com.capgemini.security4.repository.ElectionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ElectionsServiceImpl implements ElectionsService {

    
    private ElectionsRepository electionsRepository;

    @Autowired
    public ElectionsServiceImpl(ElectionsRepository electionsRepository) {
		super();
		this.electionsRepository = electionsRepository;
	}
    
    @Override
    public Elections createElection(Elections election) {
        return electionsRepository.save(election);
    }

    

    
	@Override
    public Elections getElectionById(Long id) {
        return electionsRepository.findById(id).orElseThrow(()-> new ElectionNotFoundException("Election not Found with id:"+ id));
    }

    @Override
    public List<Elections> getAllElections() {
        return electionsRepository.findAll();
    }

    @Override
    public Elections updateElection(Long id, Elections updatedElection) {
        Optional<Elections> existing = electionsRepository.findById(id);
        if (existing.isPresent()) {
            Elections election = existing.get();
            election.setTitle(updatedElection.getTitle());
            election.setDescription(updatedElection.getDescription());
            election.setStartDate(updatedElection.getStartDate());
            election.setEndDate(updatedElection.getEndDate());
            election.setElectionStatus(updatedElection.getElectionStatus());
            return electionsRepository.save(election);
        }
        else
        	throw new ElectionNotFoundException("Election not Found with id: " + id);
    }

    @Override
    public void deleteElection(Long id) {
    	if(!electionsRepository.existsById(id)) {
        	throw new ElectionNotFoundException("Election not Found with id:"+ id);
        	}   
    	electionsRepository.deleteById(id);
    }

	@Override
    public List<Elections> getElectionsByStatus(Boolean status) {
        return electionsRepository.findByElectionStatus(status);
    }

    @Override
    public List<Elections> getUpcomingElections() {
        return electionsRepository.findUpcomingElections();
    }
}
