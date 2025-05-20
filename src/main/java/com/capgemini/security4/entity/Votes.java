package com.capgemini.security4.entity;

import jakarta.persistence.Entity;

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

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id", updatable = false)
	private Users user;

	@ManyToOne
	@JoinColumn(name = "candidate_id", nullable = false, referencedColumnName = "candidate_id", updatable = false)
	private Candidates candidate;

	@ManyToOne
	@JoinColumn(name = "election_id", nullable = false, referencedColumnName = "election_id", updatable = false)
	private Elections election;

	@Column(name="time_stamp" ,nullable = false)
	private LocalDateTime timeStamp;

	
	

}
