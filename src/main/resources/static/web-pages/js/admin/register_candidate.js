import { api } from "../services/api.js";
import config from "../config/config.js";
import { getToken } from "../utils/jwtUtils.js";

export function init() {
  console.log("register_candidate.js loaded");

  const form = document.getElementById("candidateForm");
  const userSelect = document.getElementById("userSelect");
  const partySelect = document.getElementById("partySelect");
  const electionSelect = document.getElementById("electionSelect");
  const manifestoInput = document.getElementById("manifestoInput");
  const submitBtn = document.getElementById("submitBtn");
  const editModeElem = document.getElementById("editMode");

  let editId = null;
  let allCandidates = [];
  let allUsers = [];
  let allParties = [];
  let allElections = [];

  // Load all dropdown data
  async function loadDropdowns() {
    try {
      [allUsers, allParties, allElections] = await Promise.all([
        api.get("users?role=user"),
        api.get("parties"),
        api.get("elections"),
      ]);

      populateSelect(userSelect, allUsers, "userId", "userName");
      populateSelect(partySelect, allParties, "partyId", "partyName");
      populateSelect(electionSelect, allElections, "electionId", "title");

      await fetchAndRenderCandidates();
    } catch (err) {
      console.error("Initialization error", err);
      showError("Failed to load initial data");
    }
  }

  function populateSelect(selectElem, items, valueKey, textKey) {
    selectElem.innerHTML = `<option value="">-- Select --</option>`;
    items.forEach((item) => {
      selectElem.innerHTML += `<option value="${item[valueKey]}">${item[textKey]}</option>`;
    });
  }

  async function fetchAndRenderCandidates() {
    try {
      allCandidates = await api.get("candidates");
      renderCandidates(allCandidates);
    } catch (err) {
      console.error("Error fetching candidates", err);
      showError("Failed to load candidates");
    }
  }

  function renderCandidates(candidates) {
    const tbody = document.getElementById("candidateTableBody");
    tbody.innerHTML = "";

    candidates.forEach((c) => {
      const user = allUsers.find((u) => u.userId === c.userId);
      const party = allParties.find((p) => p.partyId === c.partyId);
      const election = allElections.find((e) => e.electionId === c.electionId);

      const row = document.createElement("tr");
      row.innerHTML = `
                <td>${c.candidateId}</td>
                <td>${user?.userName || "N/A"}</td>
                <td>${party?.partyName || "N/A"}</td>
                <td>${election?.title || "N/A"}</td>
                <td>${c.manifesto || "-"}</td>
                <td>
                    <button class="btn btn-outline-primary btn-sm edit-btn" data-id="${
                      c.candidateId
                    }">Edit</button>
                </td>
            `;
      tbody.appendChild(row);
    });

    // Add event listeners
    document.querySelectorAll(".edit-btn").forEach((btn) => {
      btn.addEventListener("click", () => loadCandidateForEdit(btn.dataset.id));
    });
  }

  // Form submission handler
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    if (!validateForm()) return;

    submitBtn.disabled = true;
    submitBtn.innerHTML =
      '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Processing...';

    const candidateData = {
      userId: parseInt(userSelect.value),
      partyId: parseInt(partySelect.value),
      electionId: parseInt(electionSelect.value),
      manifesto: manifestoInput.value.trim(),
    };

    try {
      // Check if user is already a candidate in this election
      if (
        !editId &&
        isUserAlreadyCandidate(candidateData.userId, candidateData.electionId)
      ) {
        showError("This user is already a candidate in this election");
        return;
      }

      if (editId) {
        const response =await api.put(`candidates/${editId}`, candidateData);
        console.log('Update response:', response);
        showSuccess("Candidate updated successfully");
      } else {
        await api.post("candidates", candidateData);
        showSuccess("Candidate registered successfully");
      }

      clearForm();
      await fetchAndRenderCandidates();
    } catch (err) {
      console.error("Submit error", err);
      showError(err.response?.data?.message || "Error processing request");
    } finally {
      submitBtn.disabled = false;
      submitBtn.innerHTML = editId ? "Update Candidate" : "Add Candidate";
    }
  });

  function isUserAlreadyCandidate(userId, electionId) {
    return allCandidates.some(
      (c) =>
        c.userId == userId &&
        c.electionId == electionId &&
        c.candidateId != editId
    );
  }

  function validateForm() {
    if (!userSelect.value) {
      showError("Please select a user");
      return false;
    }
    if (!partySelect.value) {
      showError("Please select a party");
      return false;
    }
    if (!electionSelect.value) {
      showError("Please select an election");
      return false;
    }
    return true;
  }

  async function loadCandidateForEdit(id) {
    try {
      const candidate = await api.get(`candidates/${id}`);

      userSelect.value = candidate.userId;
      partySelect.value = candidate.partyId;
      electionSelect.value = candidate.electionId;
      manifestoInput.value = candidate.manifesto || "";

      editId = id;
      editModeElem.value = "true";
      submitBtn.textContent = "Update Candidate";
      submitBtn.classList.replace("btn-primary", "btn-warning");
    } catch (err) {
      console.error("Error loading candidate", err);
      showError("Failed to load candidate data");
    }
  }

//   async function deleteCandidate(id) {
//     if (!confirm("Are you sure you want to delete this candidate?")) return;

//     try {
//       await api.delete(`candidates/${id}`);
//       showSuccess("Candidate deleted successfully");
//       await fetchAndRenderCandidates();
//     } catch (err) {
//       console.error("Delete error", err);
//       showError("Failed to delete candidate");
//     }
//   }

  function clearForm() {
    form.reset();
    editId = null;
    editModeElem.value = "false";
    submitBtn.textContent = "Add Candidate";
    submitBtn.classList.replace("btn-warning", "btn-primary");
  }

  function showError(message) {
    // Implement your error display (toast, alert, etc.)
    alert(`Error: ${message}`);
  }

  function showSuccess(message) {
    // Implement your success display
    alert(`Success: ${message}`);
  }

  // Initialize the page
  loadDropdowns();
}
