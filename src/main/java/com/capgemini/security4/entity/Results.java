package com.capgemini.security4.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
	private Long resultId;

	@Column(name = "candidate_id")
	private Long candidateId;

	@ManyToOne
	@JoinColumn(name = "candidate_id", referencedColumnName = "candidate_id", insertable = false, updatable = false)
	private Candidates candidate;

	@Column(name = "election_id")
	private Long electionId;

	@ManyToOne
	@JoinColumn(name = "election_id", referencedColumnName = "election_id", insertable = false, updatable = false)
	private Elections election;

	@NotNull(message = "Total votes must be specified")
    @Min(value = 0, message = "Total votes cannot be negative")
	@Column(name = "total_votes", nullable = false)
	private Long totalVotes;

	@PastOrPresent(message = "Declared date must be in the past or present")
	@Column(name = "declared_at")
	private LocalDateTime declaredAt;

	public Results() {
		super();
	}

}
