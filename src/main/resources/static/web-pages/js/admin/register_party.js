import { api } from "../services/api.js";
import { getToken } from "../utils/jwtUtils.js";
import config from "../config/config.js";

const apiUrl = `${config.API_BASE_URL}/parties`;

const form = document.getElementById("partyForm");
const partyNameInput = document.getElementById("partyName");
const partyStatusInput = document.getElementById("partyStatus");
const partyLogoInput = document.getElementById("partyLogo");
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
  const isEdit = !!editId;

  formData.append("partyName", partyNameInput.value.trim());
  formData.append("partyStatus", partyStatusInput.value.trim());

  if (!isEdit&&partyLogoInput.files[0]) {
    formData.append("partyLogo", partyLogoInput.files[0]);
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

  // Basic validation
  if (!partyNameInput.value.trim()) {
    partyNameInput.classList.add("is-invalid");
    alert("Party name is required.");
    submitBtn.disabled = false;
    return;
  }

  if (!partyStatusInput.value.trim()) {
    partyStatusInput.classList.add("is-invalid");
    alert("Party status is required.");
    submitBtn.disabled = false;
    return;
  }

  const formData = createFormData();

  try {
    if (isEdit) {
      const response = await fetch(`${apiUrl}/${editId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${getToken()}`,
        },
        body: JSON.stringify({
          partyName: partyNameInput.value.trim(),
          partyStatus: partyStatusInput.value.trim(),
          // Exclude file
        }),
      });
    } else {
      const response = await fetch(apiUrl, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${getToken()}`,
        },
        body: formData,
      });
    }

    clearForm();
    await fetchAndRenderParties();
  } catch (err) {
    console.error(err);
    alert("An error occurred. Please try again.");
  }

  submitBtn.disabled = false;
});

async function loadPartyForEdit(id) {
  const party = await api.get(`parties/${id}`);

  editModeElem.value = "true";
  currentLogoSection.classList.remove("d-none");
  partyLogoUploadSection.classList.add("d-none");

  if (party.partyLogo) {
    currentPosterImage.src = `${config.API_BASE_URL}/parties/logo/${party.partyLogo}`;
  } else {
    currentPosterImage.src = "";
  }

  partyNameInput.value = party.partyName;
  partyStatusInput.value = party.partyStatus;

  editId = id;
  submitBtn.textContent = "Update Party";
  submitBtn.classList.remove("btn-primary");
  submitBtn.classList.add("btn-warning");

  partyNameInput.scrollIntoView({ behavior: "smooth" });
  partyNameInput.focus();
}

async function deleteParty(id) {
  if (!confirm("Are you sure you want to delete this party?")) return;

  await api.delete(`parties/${id}`);
  fetchAndRenderParties();
}

function clearForm() {
  form.reset();
  editId = null;
  editModeElem.value = "false";
  submitBtn.textContent = "Add Party";
  submitBtn.classList.remove("btn-warning");
  submitBtn.classList.add("btn-primary");

  partyLogoUploadSection.classList.remove("d-none");
  currentLogoSection.classList.add("d-none");

  document.querySelectorAll(".is-valid, .is-invalid").forEach((el) => {
    el.classList.remove("is-valid", "is-invalid");
  });
}

// Sorting by Party Name
const nameHeader = document.querySelector("th:nth-child(2)");
nameHeader.style.cursor = "pointer";

nameHeader.addEventListener("click", () => {
  const sorted = [...allParties].sort(
    (a, b) => a.partyName.localeCompare(b.partyName) * sortDirection
  );
  sortDirection *= -1;

  renderParties(sorted);

  // Update sort icon
  nameHeader.textContent = `Party Name ${sortDirection === 1 ? "↑" : "↓"}`;
});

// Initial fetch
fetchAndRenderParties();
