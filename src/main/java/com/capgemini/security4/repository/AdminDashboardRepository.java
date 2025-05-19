package com.capgemini.security4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.capgemini.security4.dto.AdminDashboardStatsDto;
import com.capgemini.security4.entity.Users;

@Repository
public interface AdminDashboardRepository extends JpaRepository<Users, Long>{
	@Query("""
		    SELECT new com.capgemini.security4.dto.AdminDashboardStatsDto(
		        (SELECT COUNT(c) FROM Candidates c),
		        (SELECT COUNT(u) FROM Users u WHERE LOWER(u.role) = 'user'),
		        (SELECT COUNT(e) FROM Elections e WHERE e.startDate <= CURRENT_TIMESTAMP AND e.endDate >= CURRENT_TIMESTAMP),
		        (SELECT COUNT(v) FROM Votes v WHERE FUNCTION('DATE', v.timeStamp) = CURRENT_DATE)
		    )
		""")
	    AdminDashboardStatsDto getDashboardStats();
}
