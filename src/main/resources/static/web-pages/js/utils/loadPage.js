export default async function loadPage(role, page) {
    const hmtlRes = await fetch(`/web-pages/pages/${role}/${page}.html`);
    document.getElementById("main-content").innerHTML = await hmtlRes.text();
    const jsModule = await import(`/web-pages/js/${role}/${page}.js`);
    if (jsModule && jsModule.init) {
        jsModule.init();
    }
}