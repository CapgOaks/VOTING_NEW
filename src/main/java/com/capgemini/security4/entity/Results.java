package com.capgemini.voting.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "Results")
public class Results {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "result_id")
	private int resultId;

	@ManyToOne
	@JoinColumn(name = "candidate_id", referencedColumnName = "candidate_id", insertable = false, updatable = false)
	private Candidates candidate;

	@ManyToOne
	@JoinColumn(name = "election_id", referencedColumnName = "election_id", insertable = false, updatable = false)
	private Elections election;

	@Column(name = "total_votes", nullable = false)
	private int totalVotes;

	@Column(name = "declared_at")
	private LocalDateTime declaredAt;

	public Results() {
		super();
	}

}
