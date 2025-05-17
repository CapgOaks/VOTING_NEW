export default async function loadPage(role, page) {
    console.log('load page called', role, page);
    
    const hmtlRes = await fetch(`/web-pages/pages/${role}/${page}.html`);
    const html = await hmtlRes.text();
    // console.log('html loaded', html);
    const mainContainer = document.getElementById("main-content");
    console.log(mainContainer);
    mainContainer.innerHTML = html;
    
    const jsModule = await import(`/web-pages/js/${role}/${page}.js`);
    if (jsModule && jsModule.init) {
        jsModule.init();
    }
}