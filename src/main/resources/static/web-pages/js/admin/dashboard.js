console.log('admin\'s dashboard.js loaded');
console.log("dashboard.js loaded");

import { api } from "../services/api.js";

async function loadDashboardData() {
  try {
    const [
      candidatesRes,
      votersRes,
      electionsRes,
      votesRes,
      recentElectionsRes
    ] = await Promise.all([
      api.get("dashboard/total-candidates"),
      api.get("dashboard/registered-voters"),
      api.get("dashboard/ongoing-elections"),
      api.get("dashboard/votes-today"),
      api.get("dashboard/recent-elections")
    ]);

    updateSummaryCards({
      candidates: candidatesRes.total || 0,
      voters: votersRes.total || 0,
      elections: electionsRes.total || 0,
      votes: votesRes.total || 0
    });

    displayRecentElections(recentElectionsRes.elections || []);
  } catch (error) {
    console.error("Error loading dashboard data:", error);
    // Show fallback values
    updateSummaryCards({ candidates: "--", voters: "--", elections: "--", votes: "--" });
    document.getElementById("recent-elections-body").innerHTML = `
      <tr><td colspan="4" class="text-danger text-center">Failed to load recent elections.</td></tr>
    `;
  }
}

function updateSummaryCards({ candidates, voters, elections, votes }) {
  document.getElementById("total-candidates").textContent = candidates;
  document.getElementById("registered-voters").textContent = voters;
  document.getElementById("ongoing-elections").textContent = elections;
  document.getElementById("votes-today").textContent = votes;
}

function displayRecentElections(elections) {
  const tbody = document.getElementById("recent-elections-body");
  tbody.innerHTML = "";

  if (elections.length === 0) {
    tbody.innerHTML = `
      <tr><td colspan="4" class="text-center">No recent elections found.</td></tr>
    `;
    return;
  }

  elections.forEach(election => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${election.name}</td>
      <td>${election.startDate} to ${election.endDate}</td>
      <td>${election.status}</td>
      <td>${election.totalVotes || 0}</td>
    `;
    tbody.appendChild(row);
  });
}

loadDashboardData();
