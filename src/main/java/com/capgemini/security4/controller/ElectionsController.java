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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.security4.entity.Elections;
import com.capgemini.security4.service.ElectionsService;

@RestController
@RequestMapping("/api/elections")
public class ElectionsController {
		
		private ElectionsService electionsService;
		
		@Autowired
		public ElectionsController(ElectionsService electionsService) {
			super();
			this.electionsService = electionsService;
		}
		
		
		@GetMapping
		public ResponseEntity<List<Elections>> getAllElections(){
			return ResponseEntity.status(HttpStatus.OK).body(electionsService.getAllElections());
		}
		
		@GetMapping("/{id}")
		public ResponseEntity<Elections> getElectionById(@PathVariable Long id){
			Elections election = electionsService.getElectionById(id);
			 return ResponseEntity.status(HttpStatus.OK).body(election);
		}
		
		@GetMapping("/status")
	    public ResponseEntity<List<Elections>> getElectionsByStatus(@RequestParam Boolean status) {
	        return ResponseEntity.status(HttpStatus.OK).body(electionsService.getElectionsByStatus(status));
	    }
		
		@PostMapping
		 public ResponseEntity<Elections> createElection(@RequestBody Elections election) {
	        Elections created = electionsService.createElection(election);
	        return ResponseEntity.status(HttpStatus.CREATED).body(created);
	    }
		
		@PutMapping("/{id}")
	    public ResponseEntity<Elections> updateElection(@PathVariable Long id, @RequestBody Elections updatedElection) {
	        Elections updated = electionsService.updateElection(id, updatedElection);
	        return ResponseEntity.status(HttpStatus.OK).body(updated);
	    }
		
		 @DeleteMapping("/{id}")
		    public ResponseEntity<Void> deleteElection(@PathVariable Long id) {
		        electionsService.deleteElection(id);
		        return ResponseEntity.status(HttpStatus.OK).build();
		    }
		
}
