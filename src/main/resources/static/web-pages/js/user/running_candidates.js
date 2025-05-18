console.log("running_candidates.js loaded");

import { api } from "../services/api.js";
import config from "../config/config.js";

async function fetchRunningCandidates() {
  try {
    const candidates = await api.get('candidates/running');
    console.log('Fetched candidates:', candidates);
    displayCandidates(candidates);
  } catch (error) {
    console.error('Error fetching running candidates:', error);
    document.getElementById('candidatesContainer').innerHTML = '<p class="text-danger">Failed to load running candidates.</p>';
  }
}

function displayCandidates(candidates) {
  console.log('Displaying candidates:', candidates);
  const container = document.getElementById('candidatesContainer');
  container.innerHTML = '';
  if (candidates.length === 0) {
    container.innerHTML = '<p>No running candidates found.</p>';
    return;
  }
  candidates.forEach(candidate => {
    const card = document.createElement('div');
    card.className = 'col-md-4';

    card.innerHTML = `
      <div class="card h-100">
        <img src="${candidate.partyLogo ? `${config.API_BASE_URL}/parties/logo/${candidate.partyLogo}` : ''}" class="card-img-top" alt="${candidate.partyName} Logo" />
        <div class="card-body">
          <h5 class="card-title">${candidate.candidateName}</h5>
          <h6 class="card-subtitle mb-2 text-muted">${candidate.electionName}</h6>
          <p class="card-text">${candidate.manifesto || 'No manifesto available.'}</p>
        </div>
      </div>
    `;
    container.appendChild(card);
  });
}

fetchRunningCandidates();

export async function init() {
  await fetchRunningCandidates();
}
