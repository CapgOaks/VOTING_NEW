// vote_page.js (updated)
import { getStoredPayload } from "../utils/jwtUtils.js";
import { api } from "../services/api.js";
import config from "../config/config.js";

async function displayVoteCards() {
  const voteCards = document.getElementById("voteCards");
  const voteMessage = document.getElementById("voteMessage");
  const electionDd = document.getElementById("electionDropdown");

  // extract userId from JWT payload
  const payload = getStoredPayload();
  const userId = payload?.userid || payload?.sub;
  if (!userId) {
    voteCards.innerHTML = `<p class="text-danger">User not authenticated.</p>`;
    return;
  }

  // 1️⃣ Load elections
  try {
    const elections = await api.get("elections/by-status?status=true");
    electionDd.innerHTML =
      `<option disabled selected>Select Election</option>` +
      elections.map(
        (e) => `<option value="${e.electionId}">${e.title}</option>`
      ).join("");
  } catch (err) {
    voteCards.innerHTML = `<p class="text-danger">Cannot load elections.</p>`;
    return;
  }

  // 2️⃣ On election change, fetch candidates + vote status
  electionDd.addEventListener("change", async () => {
    voteCards.innerHTML = "";
    voteMessage.textContent = "";
    const electionId = electionDd.value;

    let candidates, hasVoted;
    try {
      [candidates, hasVoted] = await Promise.all([
        api.get(`candidates/election/${electionId}`),
        api.get(`votes/user/${userId}`)
      ]);
    } catch (err) {
      voteCards.innerHTML =
        `<p class="text-danger">Failed to load candidates or vote status.</p>`;
      return;
    }

    if (!candidates.length) {
      voteCards.innerHTML = `<p>No candidates found.</p>`;
      return;
    }

    // 3️⃣ Render cards
    candidates.forEach((c) => {
      const col = document.createElement("div");
      col.className = "col";

      col.innerHTML = `
        <div class="card card-custom h-100 text-center">
          <div class="pt-4">
            <img
              src="${config.API_BASE_URL}/parties/logo/${c.partyLogo}"
              alt="${c.partyName} logo"
              class="logo-circle rounded-circle mx-auto d-block"
            />
          </div>
          <div class="card-body d-flex flex-column">
            <h5 class="card-title mb-1">${c.candidateName}</h5>
            <h6 class="card-subtitle mb-3 text-muted">${c.partyName}</h6>
            <p class="card-text flex-grow-1">
              ${c.manifesto || "No manifesto provided."}
            </p>
            <button class="btn btn-primary vote-btn mt-3" ${hasVoted ? "disabled" : ""}>
              ${hasVoted ? "Already Voted" : "Vote"}
            </button>
          </div>
        </div>
      `;



      const btn = col.querySelector(".vote-btn");
      if (!hasVoted) {
        btn.addEventListener("click", async () => {
          const payload = {
            candidateId: c.candidateId,
            electionId
          };
          try {
            await api.post(`votes`, payload);
            voteMessage.textContent = "Vote cast successfully!";
            voteMessage.className = "text-success";

            voteCards.querySelectorAll('.vote-btn').forEach((button) => {
              button.disabled = true;
              button.textContent = "Already Voted";
            });
            
          } catch {
            voteMessage.textContent = "Failed to cast vote.";
            voteMessage.className = "text-danger";
          }
        });
      }

      voteCards.appendChild(col);
    });
  });
}

export function init() {
  displayVoteCards();
}
