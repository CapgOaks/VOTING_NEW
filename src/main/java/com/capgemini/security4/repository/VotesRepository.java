package com.capgemini.security4.repository;


import com.capgemini.security4.entity.Votes;
import com.capgemini.security4.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotesRepository extends JpaRepository<Votes, Long> {
    boolean existsByUser_UserId(Long userId);
    Votes findByUser(Users user);
}
