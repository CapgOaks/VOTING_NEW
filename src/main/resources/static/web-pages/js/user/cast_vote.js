// Utility function to fetch data with authorization
async function fetchData(url) {
    const response = await fetch(url, {
        headers: {
            'Authorization': getAuthorization()
        }
    });
    if (!response.ok) {
        throw new Error(`Failed to fetch data from ${url}`);
    }
    return response.json();
}

async function displayVoteCards() {
    try {
        const voteCards = document.getElementById("voteCards");
        const voteMessage = document.getElementById("voteMessage");
        const userId = getUserId();
        console.log(userId);

        const parties = await fetchData('/api/parties');

        if (parties.length === 0) {
            voteCards.innerHTML = "<p>No parties found.</p>";
            return;
        }

        const candidates = await fetchData('/api/candidates');
        const hasVoted = await fetchData(`/api/votes/user/${userId}`);

        parties.forEach(party => {
            const partyCandidates = candidates.filter(c => c.party && c.party.partyId === party.partyId);

            partyCandidates.forEach(candidate => {
                const col = document.createElement("div");
                col.classList.add("col");

                const card = document.createElement("div");
                card.classList.add("card", "h-100");

                const img = document.createElement("img");
                img.src = `/api/parties/logo/${party.partyLogo}`;
                img.classList.add("card-img-top");
                img.alt = party.partyName;

                const cardBody = document.createElement("div");
                cardBody.classList.add("card-body");

                const title = document.createElement("h5");
                title.classList.add("card-title");
                title.textContent = party.partyName;

                const candidateName = document.createElement("p");
                candidateName.classList.add("card-text");
                candidateName.textContent = `Candidate: ${candidate.candidateName}`;

                const voteBtn = document.createElement("button");
                voteBtn.classList.add("btn", "btn-primary");

                if (hasVoted) {
                    voteBtn.textContent = "Already Voted";
                    voteBtn.disabled = true;
                } else {
                    voteBtn.textContent = "Vote";

                    voteBtn.addEventListener("click", async function () {
                        if (voteBtn.disabled) return;

                        const voteData = {
                            candidate: { candidateId: candidate.candidateId },
                            election: { electionId: candidate.election ? candidate.election.electionId : null }
                        };

                        try {
                            const response = await fetch(`/api/votes/user/${userId}`, {
                                method: "POST",
                                headers: {
                                    "Content-Type": "application/json",
                                    "Authorization": getAuthorization()
                                },
                                body: JSON.stringify(voteData)
                            });

                            if (!response.ok) {
                                throw new Error("Failed to cast vote.");
                            }

                            const data = await response.json();
                            voteMessage.textContent = "Vote cast successfully!";
                            voteMessage.classList.add("text-success");
                            voteBtn.textContent = "Already Voted";
                            voteBtn.disabled = true;
                        } catch (error) {
                            voteMessage.textContent = error.message;
                            voteMessage.classList.add("text-danger");
                        }
                    });
                }

                cardBody.appendChild(title);
                cardBody.appendChild(candidateName);
                cardBody.appendChild(voteBtn);

                card.appendChild(img);
                card.appendChild(cardBody);

                col.appendChild(card);
                voteCards.appendChild(col);
            });
        });
    } catch (error) {
        const voteCards = document.getElementById("voteCards");
        voteCards.innerHTML = "<p class='text-danger'>Failed to load parties and candidates.</p>";
        console.error(error);
    }
}

export function init() {
    displayVoteCards();
}
