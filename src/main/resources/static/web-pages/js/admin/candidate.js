import { api } from '../services/api.js';

document.addEventListener("DOMContentLoaded", function () {
    const candidateForm = document.getElementById("candidateForm");
    const candidateTableBody = document.getElementById("candidateTableBody");

    const userSelect = document.getElementById("userId");
    const partySelect = document.getElementById("partyId");
    const electionSelect = document.getElementById("electionId");
    const manifestoInput = document.getElementById("manifesto");

    // Load dropdowns
    async function populateDropdown(endpoint, selectElement, labelKey = 'name') {
        try {
            const items = await api.get(endpoint);
            selectElement.innerHTML = `<option value="">Select...</option>`;
            items.forEach(item => {
                const option = document.createElement("option");
                option.value = item.id;
                option.textContent = item[labelKey];
                selectElement.appendChild(option);
            });
        } catch (error) {
            console.error(`Error loading ${endpoint}:`, error);
        }
    }

    populateDropdown('users', userSelect, 'username');
    populateDropdown('parties', partySelect);
    populateDropdown('elections', electionSelect);

    // Form submission
    candidateForm.addEventListener("submit", async function (e) {
        e.preventDefault();

        const userId = userSelect.value;
        const partyId = partySelect.value;
        const electionId = electionSelect.value;
        const manifesto = manifestoInput.value.trim();

        let isValid = true;

        if (!userId) {
            userSelect.classList.add("is-invalid");
            isValid = false;
        } else {
            userSelect.classList.remove("is-invalid");
        }

        if (!partyId) {
            partySelect.classList.add("is-invalid");
            isValid = false;
        } else {
            partySelect.classList.remove("is-invalid");
        }

        if (!electionId) {
            electionSelect.classList.add("is-invalid");
            isValid = false;
        } else {
            electionSelect.classList.remove("is-invalid");
        }

        if (manifesto.length > 1000) {
            manifestoInput.classList.add("is-invalid");
            isValid = false;
        } else {
            manifestoInput.classList.remove("is-invalid");
        }

        if (!isValid) return;

        try {
            const candidateData = { userId, partyId, electionId, manifesto };
            const response = await api.post('candidates', candidateData);
            appendCandidateToTable(response);
            candidateForm.reset();
        } catch (error) {
            console.error('Error adding candidate:', error);
            alert('Failed to add candidate.');
        }
    });

    function appendCandidateToTable(candidate) {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${candidate.user?.username || 'N/A'}</td>
            <td>${candidate.party?.name || 'N/A'}</td>
            <td>${candidate.election?.name || 'N/A'}</td>
            <td>${candidate.manifesto || ''}</td>
            <td>
                <button class="btn btn-danger btn-sm delete-btn" data-id="${candidate.id}">Delete</button>
            </td>
        `;
        candidateTableBody.appendChild(row);
    }

    // Load all candidates
    async function loadCandidates() {
        try {
            const candidates = await api.get('candidates');
            candidates.forEach(appendCandidateToTable);
        } catch (error) {
            console.error('Error loading candidates:', error);
        }
    }

    loadCandidates();

    // Handle deletion
    candidateTableBody.addEventListener("click", async function (e) {
        if (e.target.classList.contains("delete-btn")) {
            const row = e.target.closest("tr");
            const id = e.target.dataset.id;

            try {
                await api.delete(`candidates/${id}`);
                row.remove();
            } catch (error) {
                console.error('Error deleting candidate:', error);
                alert('Failed to delete candidate.');
            }
        }
    });
});
