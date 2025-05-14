package com.capgemini.security4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.security4.entity.Instructor;
import com.capgemini.security4.repository.InstructorRepository;

@Service
public class InstructorServiceImpl implements InstructorService {

	private final InstructorRepository repository;

	@Autowired
	public InstructorServiceImpl(InstructorRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Instructor> getAllInstructors() {
		return repository.findAll();
	}

	@Override
	public Instructor getInstructorById(Long id) {
		return repository.findById(id).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
	}

	@Override
	public Instructor createInstructor(Instructor instructor) {
		return repository.save(instructor);
	}

	@Override
	public Instructor updateInstructor(Long id, Instructor updated) {
		Instructor existing = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Instructor not found with ID: " + id));
		existing.setExpertise(updated.getExpertise());
		existing.setInstructorName(updated.getInstructorName());
		return repository.save(existing);
	}

	@Override
	public void deleteInstructor(Long id) {
		if (!repository.existsById(id)) {
			throw new RuntimeException("Cannot delete. Instructor not found with ID: " + id);
		}
		repository.deleteById(id);
	}

}
