import { api } from '../services/api.js';


document.addEventListener("DOMContentLoaded", async () => {
    const mainContent = document.getElementById("main-content");
    const htmlRes = await fetch('pages/admin/dashboard.html');
    const html = await htmlRes.text();
    mainContent.innerHTML = (html);
    const dashboardJs = await import('./dashboard.js');
});

document.getElementById('add-party-link').addEventListener('click', async function () {
    const htmlRes = await fetch('pages/admin/party.html');
    const html = await htmlRes.text();
    document.getElementById('main-content').innerHTML = html;
    const addPartyJs = await import('./party.js');
})