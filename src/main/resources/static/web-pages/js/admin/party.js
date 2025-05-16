import { api } from '../services/api.js';

document.addEventListener("DOMContentLoaded", function () {
    const partyForm = document.getElementById("partyForm");
    const partyTableBody = document.getElementById("partyTableBody");

    partyForm.addEventListener("submit", function (e) {
        e.preventDefault();

        const nameInput = document.getElementById("partyName");
        const statusInput = document.getElementById("partyStatus");
        const symbolInput = document.getElementById("partySymbol");

        const name = nameInput.value.trim();
        const status = statusInput.value.trim();
        const file = symbolInput.files[0];

        let isValid = true;

        if (name.length < 5) {
            nameInput.classList.add("is-invalid");
            isValid = false;
        } else {
            nameInput.classList.remove("is-invalid");
        }

        if (status.length < 5) {
            statusInput.classList.add("is-invalid");
            isValid = false;
        } else {
            statusInput.classList.remove("is-invalid");
        }

        if (!file) {
            symbolInput.classList.add("is-invalid");
            isValid = false;
        } else {
            symbolInput.classList.remove("is-invalid");
        }

        if (!isValid) return;

        const reader = new FileReader();
        reader.onload = async function (event) {
            const base64Symbol = event.target.result;

            try {
                const partyData = { name, status, symbol: base64Symbol };
                const response = await api.post('parties', partyData);
                appendPartyToTable(response); // assuming response is saved party
                partyForm.reset();
            } catch (error) {
                console.error('Error saving party:', error);
                alert('Failed to add party.');
            }
        };

        reader.readAsDataURL(file);
    });

    function appendPartyToTable(party) {
        const newRow = document.createElement("tr");
        newRow.innerHTML = `
            <td>${party.name}</td>
            <td>${party.status}</td>
            <td><img src="${party.symbol}" style="width: 50px; height: 50px;" /></td>
            <td><button class="btn btn-danger btn-sm delete-btn" data-id="${party.id}">Delete</button></td>
        `;
        partyTableBody.appendChild(newRow);
    }

    // Load existing parties on page load
    async function loadParties() {
        try {
            const parties = await api.get('parties');
            parties.forEach(appendPartyToTable);
        } catch (error) {
            console.error('Error loading parties:', error);
        }
    }

    loadParties();

    // Handle delete
    partyTableBody.addEventListener("click", async function (e) {
        if (e.target.classList.contains("delete-btn")) {
            const row = e.target.closest("tr");
            const id = e.target.getAttribute("data-id");

            try {
                await api.delete(`parties/${id}`);
                row.remove();
            } catch (error) {
                console.error('Error deleting party:', error);
                alert('Failed to delete party.');
            }
        }
    });
});
