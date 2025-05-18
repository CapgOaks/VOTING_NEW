package com.capgemini.security4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capgemini.security4.dto.CandidateDto;
import com.capgemini.security4.entity.Candidates;

public interface CandidatesRepository extends JpaRepository<Candidates, Long> {
	@Query("""
			SELECT new com.capgemini.security4.dto.CandidateDto(
			  c.candidateId,
			  u.userName,
			  c.manifesto,
			  p.partyName,
			  p.partyLogo
			)
			FROM Candidates c
			JOIN c.user u
			JOIN c.party p
			WHERE c.election.electionId = :electionId
			""")
	List<CandidateDto> findCandidateDtosByElectionId(@Param("electionId") Long electionId);
}
