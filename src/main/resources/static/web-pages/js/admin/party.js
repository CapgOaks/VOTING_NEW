import { api } from "../services/api.js";
import { getToken } from "../utils/jwtUtils.js";
import config from "../config/config.js";

const apiUrl = `${config.API_BASE_URL}/parties`;

const form = document.getElementById("partyForm");
const partyNameInput = document.getElementById("partyName");
const partyStatusInput = document.getElementById("partyStatus");
const partyLogoInput = document.getElementById("partyLogo"); // file input
const submitBtn = document.getElementById("submitBtn");
const currentPosterImage = document.getElementById("currentPosterImage");
const currentLogoSection = document.getElementById("currentLogoSection");
const partyLogoUploadSection = document.getElementById(
  "partyLogoUploadSection"
);
const editModeElem = document.getElementById("editMode");

let editId = null;
let allParties = [];
let sortDirection = 1;

function createFormData() {
  const formData = new FormData();
  const editModeElem = document.getElementById("editMode");
  const isEdit = editModeElem && editModeElem.value === "true";

  formData.append("partyName", partyNameInput.value.trim());
  formData.append("partyStatus", partyStatusInput.value.trim());

  if (partyLogoInput.files[0]) {
    formData.append("partyLogo", partyLogoInput.files[0]);
  } else if (isEdit && !partyLogoInput.files[0]) {
    formData.append("partyLogo", partyLogoInput.value);
  }

  return formData;
}

function renderParties(parties) {
  const tbody = document.getElementById("partyTableBody");
  tbody.innerHTML = "";

  parties.forEach((party) => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${party.partyId}</td>
      <td>${party.partyName}</td>
      <td>${party.partyStatus}</td>
      <td>
        ${
          party.partyLogo
            ? `<img src="${config.API_BASE_URL}/parties/logo/${party.partyLogo}" alt="Logo" height="40">`
            : "No Logo"
        }
      </td>
      <td>
        <button class="btn btn-outline-primary edit-btn" data-id="${
          party.partyId
        }">Edit</button>
        <button class="btn btn-outline-danger delete-btn" data-id="${
          party.partyId
        }">Delete</button>
      </td>
    `;
    tbody.appendChild(row);
  });

  // Add event listeners to the new buttons
  document.querySelectorAll(".edit-btn").forEach((btn) => {
    btn.addEventListener("click", () => loadPartyForEdit(btn.dataset.id));
  });

  document.querySelectorAll(".delete-btn").forEach((btn) => {
    btn.addEventListener("click", () => deleteParty(btn.dataset.id));
  });
}

async function fetchAndRenderParties() {
  try {
    allParties = await api.get("parties");
    renderParties(allParties);
  } catch (err) {
    console.error("Failed to fetch parties", err);
    alert("Failed to load parties. Please try again.");
  }
}

form.addEventListener("submit", async (e) => {
  e.preventDefault();
  submitBtn.disabled = true;

  const isEdit = !!editId;

  const formData = createFormData();

  if (isEdit) {
    // For editing, we need to use fetch directly since our api.js doesn't handle FormData
    await fetch(`${apiUrl}/${editId}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${getToken()}`,
      },
      body: formData,
    });
  } else {
    // For new party, use fetch directly for FormData
    await fetch(apiUrl, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${getToken()}`,
      },
      body: formData,
    });
  }

  clearForm();
  fetchAndRenderParties();

  submitBtn.disabled = false;
});

async function loadPartyForEdit(id) {
  try {
    const party = await api.get(`parties/${id}`);

    document.getElementById("editMode").value = "true";
    currentLogoSection.classList.remove("d-none");
    partyLogoUploadSection.classList.add("d-none");

    if (party.partyLogo) {
      currentPosterImage.src = `${config.API_BASE_URL}/parties/logo/${party.partyLogo}`;
    } else {
      currentPosterImage.src = "";
    }

    partyNameInput.value = party.partyName;
    partyStatusInput.value = party.partyStatus;
    partyLogoInput.value = party.partyLogo; // Store current logo filename

    editId = id;
    submitBtn.textContent = "Update Party";
    submitBtn.classList.remove("btn-primary");
    submitBtn.classList.add("btn-warning");
  } catch (err) {
    console.error("Error loading party for edit", err);
    alert("Failed to load party for editing. Please try again.");
  }
}


async function deleteParty(id) {
  if (!confirm("Are you sure you want to delete this party?")) return;

  try {
    await api.delete(`parties/${id}`);
    fetchAndRenderParties();
  } catch (err) {
    console.error("Failed to delete party", err);
    alert("Failed to delete party. Please try again.");
  }
}

function clearForm() {
  form.reset();
  editId = null;
  document.getElementById("editMode").value = "false";
  submitBtn.textContent = "Add Party";
  submitBtn.classList.remove("btn-warning");
  submitBtn.classList.add("btn-primary");

  partyLogoUploadSection.classList.remove("d-none");
  currentPartyLogoSection.classList.add("d-none");

  // Clear validation classes
  document.querySelectorAll(".is-valid, .is-invalid").forEach((el) => {
    el.classList.remove("is-valid", "is-invalid");
  });
}

// Add sorting functionality
document.querySelector("th:nth-child(2)").style.cursor = "pointer";
document.querySelector("th:nth-child(2)").addEventListener("click", () => {
  const sorted = [...allParties].sort(
    (a, b) => a.partyName.localeCompare(b.partyName) * sortDirection
  );
  sortDirection *= -1;
  renderParties(sorted);
});

// Initialize
fetchAndRenderParties();
