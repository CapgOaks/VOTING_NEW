package com.capgemini.security4.service;

import java.util.List;

import com.capgemini.security4.entity.Instructor;

public interface InstructorService {

	List<Instructor> getAllInstructors();

	Instructor getInstructorById(Long id);

	Instructor createInstructor(Instructor instructor);

	Instructor updateInstructor(Long id, Instructor instructor);

	void deleteInstructor(Long id);

}
