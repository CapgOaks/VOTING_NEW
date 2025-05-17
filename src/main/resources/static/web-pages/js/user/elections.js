document.addEventListener("DOMContentLoaded", function() {
	const electionsList = document.getElementById("electionsList");

	fetch('/api/elections', {
		headers: {
			'Authorization': getAuthorization()
		}
	})
		.then(response => response.json())
		.then(elections => {
			if (elections.length === 0) {
				electionsList.innerHTML = "<p>No elections found.</p>";
				return;
			}
			const list = document.createElement("ul");
			list.classList.add("list-group");
			elections.forEach(election => {
				const item = document.createElement("li");
				item.classList.add("list-group-item");
				item.textContent = `${election.electionName} (${election.status ? "Active" : "Inactive"})`;
				list.appendChild(item);
			});
			electionsList.appendChild(list);
		})
		.catch(error => {
			electionsList.innerHTML = "<p class='text-danger'>Failed to load elections.</p>";
		});
});
