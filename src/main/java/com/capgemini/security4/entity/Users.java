package com.capgemini.voting.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
    private Long userId;
    
	@Column(name="user_name")
    private String userName;
    
    @Column(name="user_email" ,unique = true) 
    private String userEmail;
    
    @Column(name="password_hash")
    private String passwordHash;
    
    @Column(name="created_at")
    private LocalDate createdAt;
    
    @Column(name="dob")
    private LocalDate dob;
    
    @Column(name="role")
    private String role; 
}

