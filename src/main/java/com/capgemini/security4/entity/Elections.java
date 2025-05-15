package com.capgemini.security4.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Entity
@Data
@Table(name = "elections")
@NoArgsConstructor
@AllArgsConstructor

public class Elections {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
 	@Column(name = "election_id")
	private Long electionId;

	@Column(name = "title")
	private String title;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "start_date")
	private LocalDateTime startDate;
	
	@Column(name = "end_date")
	private LocalDateTime endDate;
	
	@Column(name = "election_status")
	private Boolean electionStatus;
	
}
