import loadPage from "./utils/loadPage.js";

document.querySelectorAll(".nav-link[data-page]").forEach((link) => {
    link.addEventListener("click", (event) => {
        event.preventDefault();
        const role = event.target.dataset.role;
        const page = event.target.dataset.page;
        loadPage(role, page);
    });
});
