import config from "../config/config.js";
import { getToken } from "../utils/jwtUtils.js";

export function init() {
  const API_BASE_URL = config.API_BASE_URL;

  const form = document.getElementById("electionForm");
  const electionIdInput = document.getElementById("electionId");
  const titleInput = document.getElementById("title");
  const descInput = document.getElementById("description");
  const startDateInput = document.getElementById("startDate");
  const endDateInput = document.getElementById("endDate");
  const statusInput = document.getElementById("electionStatus");
  const modalTitle = document.getElementById("modalTitle");
  const modalElement = document.getElementById("electionModal");
  const confirmModal = document.getElementById("confirmModal");
  const confirmDeleteBtn = document.getElementById("confirmDeleteBtn");

  let allElections = [];
  let deleteId = null;

  function getHeaders() {
    const headers = { "Content-Type": "application/json" };
    const token = getToken();
    if (token) headers["Authorization"] = `Bearer ${token}`;
    return headers;
  }

  function resetForm() {
    form.reset();
    electionIdInput.value = "";
    modalTitle.textContent = "Add New Election";
  }

  function openModal() {
    bootstrap.Modal.getOrCreateInstance(modalElement).show();
  }

  function closeModal() {
    bootstrap.Modal.getInstance(modalElement)?.hide();
  }

  function openConfirmModal(id) {
    deleteId = id;
    bootstrap.Modal.getOrCreateInstance(confirmModal).show();
  }

  function closeConfirmModal() {
    deleteId = null;
    bootstrap.Modal.getInstance(confirmModal)?.hide();
  }

  function validateForm() {
    let valid = true;

    if (!titleInput.value.trim()) {
      titleInput.classList.add("is-invalid");
      valid = false;
    } else {
      titleInput.classList.remove("is-invalid");
    }

    const start = new Date(startDateInput.value);
    const end = new Date(endDateInput.value);
    if (!startDateInput.value || !endDateInput.value || start >= end) {
      startDateInput.classList.add("is-invalid");
      endDateInput.classList.add("is-invalid");
      valid = false;
    } else {
      startDateInput.classList.remove("is-invalid");
      endDateInput.classList.remove("is-invalid");
    }

    return valid;
  }

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    const election = {
      title: titleInput.value.trim(),
      description: descInput.value.trim(),
      startDate: new Date(startDateInput.value).toISOString(),
      endDate: new Date(endDateInput.value).toISOString(),
      electionStatus: statusInput.value === "Active",
    };

    const id = electionIdInput.value;
    const url = id
      ? `${API_BASE_URL}/elections/${id}`
      : `${API_BASE_URL}/elections`;
    const method = id ? "PUT" : "POST";

    try {
      await fetch(url, {
        method,
        headers: getHeaders(),
        body: JSON.stringify(election),
      });
      closeModal();
      await fetchElections();
    } catch (err) {
      alert(`Save failed: ${err.message}`);
    }
  });

  async function fetchElections() {
    try {
      const response = await fetch(`${API_BASE_URL}/elections`, {
        headers: getHeaders(),
      });
      if (!response.ok) throw new Error("Failed to fetch elections");
      const data = await response.json();
      allElections = Array.isArray(data) ? data : [];
      renderElections(allElections);
    } catch (err) {
      console.error("Fetch failed:", err);
    }
  }

  function renderElections(elections) {
    const tbody = document.getElementById("electionsTableBody");
    tbody.innerHTML = "";

    elections.forEach((elec) => {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td>${elec.electionId}</td>
        <td>${elec.title}</td>
        <td>${elec.description || ""}</td>
        <td>${new Date(elec.startDate).toLocaleString()}</td>
        <td>${new Date(elec.endDate).toLocaleString()}</td>
        <td>${elec.electionStatus ? "Active" : "Inactive"}</td>
        <td class="d-flex justify-content-center">
          <button class="btn btn-sm btn-outline-primary edit-btn" data-id="${elec.electionId}">
            <i class="bi bi-pencil-square"></i>
          </button>
          
        </td>
      `;
      tbody.appendChild(row);
    });

    document.querySelectorAll(".edit-btn").forEach((btn) =>
      btn.addEventListener("click", () =>
        loadElectionForEdit(btn.dataset.id)
      )
    );

    document.querySelectorAll(".delete-btn").forEach((btn) =>
      btn.addEventListener("click", () =>
        openConfirmModal(btn.dataset.id)
      )
    );
  }

  async function loadElectionForEdit(id) {
    try {
      const response = await fetch(`${API_BASE_URL}/elections/${id}`, {
        headers: getHeaders(),
      });
      if (!response.ok) throw new Error("Failed to load election");
      const data = await response.json();

      electionIdInput.value = data.electionId;
      titleInput.value = data.title;
      descInput.value = data.description;
      startDateInput.value = data.startDate.slice(0, 16);
      endDateInput.value = data.endDate.slice(0, 16);
      statusInput.value = data.electionStatus ? "Active" : "Inactive";

      modalTitle.textContent = "Edit Election";
      openModal();
    } catch (err) {
      alert(`Error: ${err.message}`);
    }
  }

  confirmDeleteBtn.addEventListener("click", async () => {
    if (!deleteId) return;
    try {
      const response = await fetch(`${API_BASE_URL}/elections/${deleteId}`, {
        method: "DELETE",
        headers: getHeaders(),
      });
      if (!response.ok) throw new Error("Delete failed");
      await fetchElections();
      closeConfirmModal();
    } catch (err) {
      alert(`Delete failed: ${err.message}`);
    }
  });

  // Search filter
  const searchInput = document.createElement("input");
  searchInput.type = "text";
  searchInput.placeholder = "Search by Title...";
  searchInput.classList.add("form-control", "mb-3");
  document
    .querySelector(".table")
    .parentNode.insertBefore(searchInput, document.querySelector(".table"));

  searchInput.addEventListener("input", () => {
    const term = searchInput.value.toLowerCase();
    const filtered = allElections.filter((e) =>
      e.title.toLowerCase().includes(term)
    );
    renderElections(filtered);
  });

  // Initial render
  fetchElections();
}
