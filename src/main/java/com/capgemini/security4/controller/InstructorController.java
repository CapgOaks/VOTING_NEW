package com.capgemini.security4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.security4.entity.Instructor;
import com.capgemini.security4.service.InstructorService;

@RestController
@RequestMapping("/api/instructor")
public class InstructorController {

	private final InstructorService instructorService;

	@Autowired
	public InstructorController(InstructorService instructorService) {
		this.instructorService = instructorService;
	}

	@GetMapping
	public ResponseEntity<List<Instructor>> getAllInstructors() {
		List<Instructor> instructors = instructorService.getAllInstructors();
		return ResponseEntity.status(HttpStatus.OK).body(instructors);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Instructor> getInstructorById(@PathVariable Long id) {
		Instructor instructor = instructorService.getInstructorById(id);
		return ResponseEntity.status(HttpStatus.OK).body(instructor);
	}

	@PostMapping
	public ResponseEntity<Instructor> createInstructor(@RequestBody Instructor instructor) {
		Instructor saved = instructorService.createInstructor(instructor);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Instructor> updateInstructor(@PathVariable Long id, @RequestBody Instructor newInstructor) {
		Instructor updated = instructorService.updateInstructor(id, newInstructor);
		return ResponseEntity.status(HttpStatus.OK).body(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
		instructorService.deleteInstructor(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
