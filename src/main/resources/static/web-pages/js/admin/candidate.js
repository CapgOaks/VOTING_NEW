import { api } from '../services/api.js';

    const candidateForm = document.getElementById("candidateForm");
    const candidateTableBody = document.getElementById("candidateTableBody");

    const userSelect = document.getElementById("userId");
    const partySelect = document.getElementById("partyId");
    const electionSelect = document.getElementById("electionId");
    const manifestoInput = document.getElementById("manifesto");

    // Show loading state
    function showLoading(show) {
        if (show) {
            candidateTableBody.innerHTML = `
                <tr>
                    <td colspan="5" class="text-center">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div>
                        <p>Loading candidates...</p>
                    </td>
                </tr>
            `;
        }
    }

    // ✅ Load dropdowns
    async function populateDropdown(endpoint, selectElement, labelKey = 'name') {
        try {
            selectElement.disabled = true;
            const items = await api.get(endpoint);
            selectElement.innerHTML = `<option value="">Select...</option>`;
            items.forEach(item => {
                const option = document.createElement("option");
                option.value = item.id;
                option.textContent = item[labelKey];
                selectElement.appendChild(option);
            });
            selectElement.disabled = false;
        } catch (error) {
            console.error(`Error loading ${endpoint}:`, error);
            selectElement.innerHTML = `<option value="">Error loading data</option>`;
            selectElement.disabled = false;
        }
    }

    // ✅ Add one candidate to table
    function appendCandidateToTable(candidate) {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${candidate.user?.username || 'N/A'}</td>
            <td>${candidate.party?.name || 'N/A'}</td>
            <td>${candidate.election?.name || 'N/A'}</td>
            <td>${candidate.manifesto ? candidate.manifesto.substring(0, 50) + (candidate.manifesto.length > 50 ? '...' : '') : ''}</td>
            <td>
                <button class="btn btn-danger btn-sm delete-btn" data-id="${candidate.id}">
                    <i class="fas fa-trash-alt"></i> Delete
                </button>
            </td>
        `;
        candidateTableBody.appendChild(row);
    }

    // ✅ Load all candidates
    async function loadCandidates() {
        try {
            showLoading(true);
            const candidates = await api.get('candidates');
            candidateTableBody.innerHTML = ''; // Clear before reload
            
            if (candidates.length === 0) {
                candidateTableBody.innerHTML = `
                    <tr>
                        <td colspan="5" class="text-center text-muted">No candidates found</td>
                    </tr>
                `;
                return;
            }
            
            candidates.forEach(appendCandidateToTable);
        } catch (error) {
            console.error('Error loading candidates:', error);
            candidateTableBody.innerHTML = `
                <tr>
                    <td colspan="5" class="text-center text-danger">
                        Failed to load candidates. Please try again later.
                    </td>
                </tr>
            `;
        }
    }

    // ✅ Form submission
    candidateForm.addEventListener("submit", async function (e) {
        e.preventDefault();

        const userId = userSelect.value;
        const partyId = partySelect.value;
        const electionId = electionSelect.value;
        const manifesto = manifestoInput.value.trim();

        let isValid = true;

        // Reset validation
        [userSelect, partySelect, electionSelect, manifestoInput].forEach(el => {
            el.classList.remove("is-invalid");
        });

        // Validate inputs
        if (!userId) {
            userSelect.classList.add("is-invalid");
            isValid = false;
        }

        if (!partyId) {
            partySelect.classList.add("is-invalid");
            isValid = false;
        }

        if (!electionId) {
            electionSelect.classList.add("is-invalid");
            isValid = false;
        }

        if (manifesto.length > 1000) {
            manifestoInput.classList.add("is-invalid");
            isValid = false;
        }

        if (!isValid) return;

        try {
            const submitBtn = candidateForm.querySelector('button[type="submit"]');
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Adding...';

            const candidateData = { userId, partyId, electionId, manifesto };
            const response = await api.post('candidates', candidateData);
            
            appendCandidateToTable(response);
            candidateForm.reset();
        } catch (error) {
            console.error('Error adding candidate:', error);
            alert('Failed to add candidate: ' + (error.message || 'Please try again later.'));
        } finally {
            const submitBtn = candidateForm.querySelector('button[type="submit"]');
            submitBtn.disabled = false;
            submitBtn.textContent = 'Add Candidate';
        }
    });

    // ✅ Delete candidate
    candidateTableBody.addEventListener("click", async function (e) {
        if (e.target.classList.contains("delete-btn") || e.target.closest(".delete-btn")) {
            const deleteBtn = e.target.classList.contains("delete-btn") ? e.target : e.target.closest(".delete-btn");
            const row = deleteBtn.closest("tr");
            const id = deleteBtn.dataset.id;

            if (!confirm('Are you sure you want to delete this candidate?')) {
                return;
            }

            try {
                deleteBtn.disabled = true;
                deleteBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>';

                await api.delete(`candidates/${id}`);
                row.remove();
                
                // If table is empty now, show message
                if (candidateTableBody.children.length === 0) {
                    candidateTableBody.innerHTML = `
                        <tr>
                            <td colspan="5" class="text-center text-muted">No candidates found</td>
                        </tr>
                    `;
                }
            } catch (error) {
                console.error('Error deleting candidate:', error);
                alert('Failed to delete candidate: ' + (error.message || 'Please try again later.'));
                deleteBtn.disabled = false;
                deleteBtn.innerHTML = '<i class="fas fa-trash-alt"></i> Delete';
            }
        }
    });

    // Initialize the page
    populateDropdown('users', userSelect, 'username');
    populateDropdown('parties', partySelect);
    populateDropdown('elections', electionSelect);
    loadCandidates();
