package com.capgemini.security4.entity;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
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
	
	
	@NotBlank(message = "Username is required")
    @Length(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
	@Column(name="user_name")
    private String userName;
    
	@NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name="user_email" ,unique = true) 
    private String userEmail;
    
	@NotBlank(message = "Password is required")
    @Length(min = 8, message = "Password must be at least 8 characters long")
    @Column(name="password_hash")
    private String passwordHash;
    
	@PastOrPresent(message = "Creation date cannot be in the future")
    @Column(name="created_at")
    private LocalDate createdAt;
    
	@Past(message = "Date of birth must be in the past")
    @Column(name="dob")
    private LocalDate dob;
    
	@NotBlank(message = "Role is required")
    @Pattern(regexp = "admin|user", message = "Role must be ADMIN, USER ")
    @Column(name="role")
    private String role; 
}

