
    const allElectionsList = document.getElementById("allElectionsList");

    fetch('/api/elections', {
        headers: {
            'Authorization': getAuthorization()
        }
    })
    .then(response => response.json())
    .then(elections => {
        if (elections.length === 0) {
            allElectionsList.innerHTML = "<p>No elections found.</p>";
            return;
        }

        fetch('/api/candidates', {
            headers: {
                'Authorization': getAuthorization()
            }
        })
        .then(response => response.json())
        .then(candidates => {
            const table = document.createElement("table");
            table.classList.add("table", "table-striped");
            const thead = document.createElement("thead");
            thead.innerHTML = `
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Status</th>
                    <th>Days Left</th>
                    <th>Candidates</th>
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
                    <td>${election.electionName}</td>
                    <td>${election.status ? "Active" : "Inactive"}</td>
                    <td>${daysLeft}</td>
                    <td>${candidateNames}</td>
                `;
                tbody.appendChild(row);
            });
            table.appendChild(tbody);
            allElectionsList.appendChild(table);
        });
    })
    .catch(error => {
        allElectionsList.innerHTML = "<p class='text-danger'>Failed to load elections.</p>";
    });

