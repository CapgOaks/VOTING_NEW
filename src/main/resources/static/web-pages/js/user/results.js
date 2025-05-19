// results_page.js
import { api } from "../services/api.js";
import config from "../config/config.js";

async function displayElectionResults() {
  const electionDropdown = document.getElementById("electionDropdown");
  const resultsContainer = document.getElementById("resultsContainer");
  const resultsTable = document.getElementById("resultsTable");

  // Load elections
  try {
    const elections = await api.get("elections/status?status=true");
    electionDropdown.innerHTML =
      `<option disabled selected>Select Election</option>` +
      elections
        .map(
          (e) => `<option value="${e.electionId}">${e.title}</option>`
        )
        .join("");
  } catch (err) {
    resultsContainer.innerHTML =
      `<p class="text-danger">Failed to load elections.</p>`;
    return;
  }

  // On election change, fetch and show results
  electionDropdown.addEventListener("change", async () => {
    const electionId = electionDropdown.value;
    resultsContainer.innerHTML = "";
    resultsTable.innerHTML = "";

    try {
      const results = await api.get(`votes/election/${electionId}`);

      if (!results.length) {
        resultsContainer.innerHTML =
          `<p class="text-warning">No results found for this election.</p>`;
        return;
      }

      // Build table headers
      const tableHeader = `
        <thead class="table-dark">
          <tr>
            <th>Candidate Name</th>
            <th>Party Name</th>
            <th>Votes</th>
            <th>Percentage</th>
          </tr>
        </thead>
        <tbody>
          ${results
            .map(
              (r) => `
            <tr>
              <td>${r.userName}</td>
              <td>${r.partyName}</td>
              <td>${r.voteCount}</td>
              <td>${r.percentage}%</td>
            </tr>
          `
            )
            .join("")}
        </tbody>
      `;
      resultsTable.innerHTML = tableHeader;
    } catch (err) {
      resultsContainer.innerHTML =
        `<p class="text-danger">Error loading results.</p>`;
    }
  });
}

export function init() {
  displayElectionResults();
}
