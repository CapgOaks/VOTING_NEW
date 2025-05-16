package com.capgemini.security4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidates {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "candidate_id")
	private Long candidateId;

	@ManyToOne
	@JoinColumn(name = "party_id", referencedColumnName = "party_id", insertable = false, updatable = false)
	private Party party;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
	private Users user;

	@Size(max = 1000, message = "Manifesto must be less than 1000 characters")
	@Column(name = "manifesto", nullable = true)
	private String manifesto;

	@ManyToOne
	@JoinColumn(name = "election_id", referencedColumnName = "election_id", insertable = false, updatable = false)
	private Elections election;
}
