package com.capgemini.security4.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
	
	@NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
	@Column(name = "title")
	private String title;
	
	@Size(max = 1000, message = "Description must be less than 1000 characters")
	@Column(name = "description")
	private String description;
	
	@NotNull(message = "Start date is required")
	@Column(name = "start_date")
	private LocalDateTime startDate;
	
	@NotNull(message = "End date is required")
	@Column(name = "end_date")
	private LocalDateTime endDate;
	
	@NotNull(message = "Election status must be specified")
	@Column(name = "election_status")
	private Boolean electionStatus;
	
}
