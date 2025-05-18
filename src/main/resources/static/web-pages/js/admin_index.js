import loadPage from "./utils/loadPage.js";
import { api } from './services/api.js';
import { logout } from './utils/auth.js';


document.querySelectorAll(".nav-link[data-page]").forEach((link) => {
    link.addEventListener("click", (event) => {
        event.preventDefault();
        const role = event.target.dataset.role;
        const page = event.target.dataset.page;
        loadPage(role, page);
    });
});

document.addEventListener("DOMContentLoaded", async () => {
    const dashBoardDto = await api.get('admin/dashboard-stats');

    console.log(dashBoardDto);


    const totalCandidates = document.getElementById("total-candidates");
    const registeredVoters = document.getElementById("registered-voters");
    const ongoingElections = document.getElementById("ongoing-elections");
    const votesToday = document.getElementById("votes-today");

    totalCandidates.innerText = dashBoardDto.totalCandidates;
    registeredVoters.innerText = dashBoardDto.registeredVoters;
    ongoingElections.innerText = dashBoardDto.ongoingElections;
    votesToday.innerText = dashBoardDto.votesToday;

    document.querySelector('#logout').addEventListener('click', logout);
});