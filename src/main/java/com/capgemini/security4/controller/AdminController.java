package com.capgemini.security4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.security4.dto.AdminDashboardStatsDto;
import com.capgemini.security4.service.AdminDashboardService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {
	private AdminDashboardService adminDashboardService;

	@Autowired
	public AdminController(AdminDashboardService adminDashboardService) {
		this.adminDashboardService = adminDashboardService;
	}

	@GetMapping("/dashboard-stats")
	@PreAuthorize("hasRole('admin')")
	public ResponseEntity<AdminDashboardStatsDto> getDashboardStats() {
		AdminDashboardStatsDto dto = adminDashboardService.getDashboardStats();
		log.info("AdminDashboardStatsDto: " + dto.toString());
		return ResponseEntity.ok(dto);
	}
}
