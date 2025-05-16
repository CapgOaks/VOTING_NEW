import { api } from '../services/api.js';


document.addEventListener("DOMContentLoaded", async () => {
    const mainContent = document.getElementById("main-content");
    const htmlRes = await fetch('pages/admin/dashboard.html');
    const html = await htmlRes.text();
    mainContent.innerHTML = (html);
    const dashboardJs = await import('./dashboard.js');
});