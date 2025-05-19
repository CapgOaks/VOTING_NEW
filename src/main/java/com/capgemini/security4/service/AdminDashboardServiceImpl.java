package com.capgemini.security4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.security4.dto.AdminDashboardStatsDto;
import com.capgemini.security4.repository.AdminDashboardRepository;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

	private AdminDashboardRepository adminDashboardRepository;

	@Autowired
	public AdminDashboardServiceImpl(AdminDashboardRepository adminDashboardRepository) {
		this.adminDashboardRepository = adminDashboardRepository;
	}

	@Override
	public AdminDashboardStatsDto getDashboardStats() {
		return adminDashboardRepository.getDashboardStats();
	}

}
