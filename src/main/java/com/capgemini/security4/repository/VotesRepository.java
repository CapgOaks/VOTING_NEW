package com.capgemini.security4.repository;

import com.capgemini.security4.entity.Votes;
import com.capgemini.security4.entity.Users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VotesRepository extends JpaRepository<Votes, Long> {

	interface WinnerProjection {
		Long getCandidateId();

		Long getTotalVotes();
	}

	boolean existsByUser_UserId(Long userId);

	Votes findByUser(Users user);

	@Query(value = """
			SELECT v.candidate_id AS candidateId,
			       COUNT(*)       AS totalVotes
			  FROM votes v
			 WHERE v.election_id = :electionId
			 GROUP BY v.candidate_id
			 ORDER BY totalVotes DESC
			 LIMIT 1
			""", nativeQuery = true)
	WinnerProjection findTopCandidateIdAndVotes(@Param("electionId") Long electionId);
	
	@Query("SELECT v.candidate, COUNT(v) FROM Votes v WHERE v.election.electionId = :electionId GROUP BY v.candidate")
	List<Object[]> countVotesByCandidateInElection(@Param("electionId") Long electionId);

	@Query("SELECT COUNT(v) FROM Votes v WHERE v.election.electionId = :electionId")
	Long countTotalVotesInElection(@Param("electionId") Long electionId);
	
	


}
