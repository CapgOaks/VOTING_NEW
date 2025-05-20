package com.capgemini.security4;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.capgemini.security4.entity.Candidates;
import com.capgemini.security4.entity.Party;
import com.capgemini.security4.entity.Users;
import com.capgemini.security4.entity.Votes;
import com.capgemini.security4.repository.VotesRepository;
import com.capgemini.security4.service.VotesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.web.server.ResponseStatusException;

import java.util.*;

class VotesServiceImplTest {

	@Mock
	private VotesRepository votesRepository;

	@InjectMocks
	private VotesServiceImpl votesService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testHasUserVoted_True() {
		when(votesRepository.existsByUser_UserId(1L)).thenReturn(true);
		assertTrue(votesService.hasUserVoted(1L));
	}

	@Test
	void testHasUserVoted_False() {
		when(votesRepository.existsByUser_UserId(2L)).thenReturn(false);
		assertFalse(votesService.hasUserVoted(2L));
	}

	@Test
	void testCastVote_Success() {
		Users user = new Users();
		user.setUserId(1L);
		Candidates candidate = new Candidates();
		candidate.setCandidateId(2L);
		Votes vote = new Votes();
		vote.setUser(user);
		vote.setCandidate(candidate);
		vote.setElection(new com.capgemini.security4.entity.Elections());

		when(votesRepository.existsByUser_UserId(1L)).thenReturn(false);
		when(votesRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		Votes saved = votesService.castVote(vote);
		assertNotNull(saved.getTimeStamp());
	}

	@Test
	void testCastVote_MissingUser() {
		Votes vote = new Votes();
		vote.setCandidate(new Candidates());
		vote.setElection(new com.capgemini.security4.entity.Elections());
		ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> votesService.castVote(vote));
		assertEquals("400 BAD_REQUEST \"User, candidate, and election must be specified\"", ex.getMessage());
	}

	@Test
	void testCastVote_MissingCandidate() {
		Votes vote = new Votes();
		vote.setUser(new Users());
		vote.setElection(new com.capgemini.security4.entity.Elections());
		ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> votesService.castVote(vote));
		assertEquals("400 BAD_REQUEST \"User, candidate, and election must be specified\"", ex.getMessage());
	}

	@Test
	void testCastVote_MissingElection() {
		Votes vote = new Votes();
		vote.setUser(new Users());
		vote.setCandidate(new Candidates());
		ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> votesService.castVote(vote));
		assertEquals("400 BAD_REQUEST \"User, candidate, and election must be specified\"", ex.getMessage());
	}

	@Test
	void testCastVote_UserAlreadyVoted() {
		Users user = new Users();
		user.setUserId(1L);
		Candidates candidate = new Candidates();
		Votes vote = new Votes();
		vote.setUser(user);
		vote.setCandidate(candidate);
		vote.setElection(new com.capgemini.security4.entity.Elections());

		when(votesRepository.existsByUser_UserId(1L)).thenReturn(true);

		ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> votesService.castVote(vote));
		assertEquals("409 CONFLICT \"User has already voted\"", ex.getMessage());
	}

	@Test
	void testGetElectionResults() {
		// Mock candidate, user, and party
		Users user = new Users();
		user.setUserName("testuser");
		Party party = new Party();
		party.setPartyName("testparty");
		Candidates candidate = new Candidates();
		candidate.setUser(user);
		candidate.setParty(party);

		// Mock repository return: List<Object[]>
		Object[] row = new Object[] { candidate, 10L };
		List<Object[]> repoResult = new ArrayList<>();
		repoResult.add(row);

		when(votesRepository.countVotesByCandidateInElection(1L)).thenReturn(repoResult);
		when(votesRepository.countTotalVotesInElection(1L)).thenReturn(10L);

		List<Map<String, Object>> results = votesService.getElectionResults(1L);

		assertEquals(1, results.size());
		assertEquals("testuser", results.get(0).get("userName"));
		assertEquals("testparty", results.get(0).get("partyName"));
		assertEquals(10L, results.get(0).get("voteCount"));
		assertEquals(100.0, results.get(0).get("percentage"));
	}

	@Test
	void testGetElectionResults_ZeroVotes() {
		// Setup candidate, user, and party
		Users user = new Users();
		user.setUserName("testuser");
		Party party = new Party();
		party.setPartyName("testparty");
		Candidates candidate = new Candidates();
		candidate.setUser(user);
		candidate.setParty(party);

		// Mock repository return: List<Object[]>
		Object[] row = new Object[] { candidate, 0L };
		List<Object[]> repoResult = new ArrayList<>();
		repoResult.add(row);

		when(votesRepository.countVotesByCandidateInElection(1L)).thenReturn(repoResult);
		when(votesRepository.countTotalVotesInElection(1L)).thenReturn(0L);

		List<Map<String, Object>> results = votesService.getElectionResults(1L);

		assertEquals(1, results.size());
		assertEquals("testuser", results.get(0).get("userName"));
		assertEquals("testparty", results.get(0).get("partyName"));
		assertEquals(0L, results.get(0).get("voteCount"));
		assertEquals(0.0, results.get(0).get("percentage"));
	}
}