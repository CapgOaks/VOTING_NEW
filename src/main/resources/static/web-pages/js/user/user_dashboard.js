import config from "../config/config.js";
import { getToken } from "../utils/jwtUtils.js";

console.log("user_dashboard.js loaded");

function decodeJWT(token) {
  if (!token) return null;
  const base64Url = token.split('.')[1];
  if (!base64Url) return null;
  const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  try {
    return JSON.parse(decodeURIComponent(escape(window.atob(base64))));
  } catch (e) {
    console.error('Failed to decode JWT', e);
    return null;
  }
}

async function fetchDashboardData() {
  try {
    console.log('Fetching dashboard data...');
    const token = getToken();
    if (!token) throw new Error('No token found');

    const electionsResponse = await fetch(`${config.API_BASE_URL}/elections`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    if (!electionsResponse.ok) throw new Error('Failed to fetch elections');
    const elections = await electionsResponse.json();
    console.log('Elections data:', elections);
    const totalElections = elections.length;
    document.getElementById('total-elections-count').textContent = totalElections + " total elections";

    const candidatesResponse = await fetch(`${config.API_BASE_URL}/candidates`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    if (!candidatesResponse.ok) throw new Error('Failed to fetch candidates');
    const candidates = await candidatesResponse.json();
    console.log('Candidates data:', candidates);
    const totalCandidates = candidates.length;
    document.getElementById('total-candidates-count').textContent = totalCandidates + " total candidates";

    const decoded = decodeJWT(token);
    console.log('Decoded token:', decoded);
    const userId = decoded ? decoded.userid : null;
    if (!userId) throw new Error('User ID not found in token');

    const userProfileResponse = await fetch(`${config.API_BASE_URL}/users/${userId}`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    if (!userProfileResponse.ok) throw new Error('Failed to fetch user profile');
    const userProfile = await userProfileResponse.json();
    console.log('User profile:', userProfile);
    document.getElementById('user-profile-summary').textContent = "Welcome, " + userProfile.userName;

    renderCharts(totalElections, totalCandidates);

  } catch (error) {
    console.error('Error fetching dashboard data:', error);
    document.getElementById('total-elections-count').textContent = "Failed to load";
    document.getElementById('total-candidates-count').textContent = "Failed to load";
    document.getElementById('user-profile-summary').textContent = "Failed to load";
  }
}

function renderCharts(totalElections, totalCandidates) {
  const electionsCtx = document.getElementById('electionsChart').getContext('2d');
  const candidatesCtx = document.getElementById('candidatesChart').getContext('2d');

  new Chart(electionsCtx, {
    type: 'bar',
    data: {
      labels: ['Total Elections'],
      datasets: [{
        label: 'Elections',
        data: [totalElections],
        backgroundColor: 'rgba(54, 162, 235, 0.7)',
      }]
    },
    options: {
      responsive: true,
      scales: {
        y: { beginAtZero: true, precision: 0 }
      }
    }
  });

  new Chart(candidatesCtx, {
    type: 'bar',
    data: {
      labels: ['Total Candidates'],
      datasets: [{
        label: 'Candidates',
        data: [totalCandidates],
        backgroundColor: 'rgba(75, 192, 192, 0.7)',
      }]
    },
    options: {
      responsive: true,
      scales: {
        y: { beginAtZero: true, precision: 0 }
      }
    }
  });
}

fetchDashboardData();
