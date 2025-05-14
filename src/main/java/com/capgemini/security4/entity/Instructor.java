package com.capgemini.security4.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Instructor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String instructorName;
	private String expertise;

	public Instructor() {
	}

	public Instructor(Long id, String instructorName, String expertise) {
		this.id = id;
		this.instructorName = instructorName;
		this.expertise = expertise;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInstructorName() {
		return instructorName;
	}

	public void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
	}

	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}

	@Override
	public String toString() {
		return "Instructors [id=" + id + ", instructorName=" + instructorName + ", expertise=" + expertise + "]";
	}

}
