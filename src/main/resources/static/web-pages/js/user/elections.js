import { api } from '../services/api.js';

const electionsList = document.getElementById("electionsList");


async function loadElections() {
  try {
   /* const response = await fetch('/api/elections', {
      headers: {
        'Authorization': getAuthorization()
      }
    });

    if (!response.ok) {
      throw new Error('Failed to fetch elections');
    }*/

    const elections = await api.get('elections');
	console.log(elections);

    if (elections.length === 0) {
      electionsList.innerHTML = "<p>No elections found.</p>";
      return;
    }

    const list = document.createElement("ul");
    list.classList.add("list-group");

    elections.forEach(election => {
      const item = document.createElement("li");
      item.classList.add("list-group-item");
      item.textContent = `${election.title} (${election.status ? "Active" : "Inactive"})`;
      list.appendChild(item);
    });

    electionsList.appendChild(list);
  } catch (error) {
    console.error('Error loading elections:', error);
    electionsList.innerHTML = "<p class='text-danger'>Failed to load elections.</p>";
  }
}

// Automatically load elections on script load
loadElections();
