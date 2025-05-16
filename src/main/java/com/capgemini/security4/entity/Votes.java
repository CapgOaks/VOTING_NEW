package com.capgemini.security4.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "votes")
@NoArgsConstructor
@AllArgsConstructor
public class Votes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long voteId;

	@NotNull(message = "User is required")
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id", insertable = false, updatable = false)
	private Users user;

	@NotNull(message = "Candidate is required")
	@ManyToOne
	@JoinColumn(name = "candidate_id", nullable = false, referencedColumnName = "candidate_id", insertable = false, updatable = false)
	private Candidates candidate;

	@NotNull(message = "Election is required")
	@ManyToOne
	@JoinColumn(name = "election_id", nullable = false, referencedColumnName = "election_id", insertable = false, updatable = false)
	private Elections election;

	@Column(nullable = false)
	private LocalDateTime timeStamp;

	
	

}
