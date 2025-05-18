package com.capgemini.security4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capgemini.security4.dto.RunningCandidateDto;
import com.capgemini.security4.entity.Candidates;

public interface CandidatesRepository extends JpaRepository<Candidates, Long> {

@Query("SELECT new com.capgemini.security4.dto.RunningCandidateDto(p.partyName, p.partyLogo, c.manifesto, c.user.userName, c.election.title) FROM Candidates c JOIN c.party p WHERE p.partyStatus = :partyStatus")
List<RunningCandidateDto> findRunningCandidates(@Param("partyStatus") String partyStatus);

  
}
