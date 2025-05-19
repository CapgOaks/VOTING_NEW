import { api } from '../services/api.js';

const allElectionsList = document.getElementById("allElectionsList");

async function loadAllElections() {
	try {
		/*const electionsRes = await fetch('/api/elections', {
		  headers: {
			'Authorization': getAuthorization()
		  }
		});
	
		if (!electionsRes.ok) {
		  throw new Error("Failed to fetch elections");
		}*/

		const elections = await api.get('elections');
		console.log(elections);

		if (elections.length === 0) {
			allElectionsList.innerHTML = "<p>No elections found.</p>";
			return;
		}

		const candidatesRes = await fetch('/api/candidates', {
			headers: {
				'Authorization': getAuthorization()
			}
		});

		if (!candidatesRes.ok) {
			throw new Error("Failed to fetch candidates");
		}

		const candidates = await candidatesRes.json();

		const table = document.createElement("table");
		table.classList.add("table", "table-striped");

		const thead = document.createElement("thead");
		thead.innerHTML = `
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Status</th>
        <th>Days Left</th>
      </tr>
    `;
		table.appendChild(thead);

		const tbody = document.createElement("tbody");

		elections.forEach(election => {
			const row = document.createElement("tr");

			const today = new Date();
			const endDate = new Date(election.endDate);
			const timeDiff = endDate - today;
			const daysLeft = timeDiff > 0 ? Math.ceil(timeDiff / (1000 * 3600 * 24)) : 0;

			const electionCandidates = candidates.filter(c => c.election && c.election.electionId === election.electionId);
			const candidateNames = electionCandidates.map(c => c.candidateName).join(", ") || "None";

			row.innerHTML = `
        <td>${election.electionId}</td>
        <td>${election.title}</td>
        <td>${election.electionStatus ? "Active" : "Inactive"}</td>
        <td>${daysLeft}</td>
       
      `;
			tbody.appendChild(row);
		});

		table.appendChild(tbody);
		allElectionsList.appendChild(table);

	} catch (error) {
		console.error("Error loading elections:", error);
		allElectionsList.innerHTML = "<p class='text-danger'>Failed to load elections.</p>";
	}
}

export function init() {
	loadAllElections();
}
