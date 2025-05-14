package com.capgemini.security4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.security4.entity.Instructor;



@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long>{


}
