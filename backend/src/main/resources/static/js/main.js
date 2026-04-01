function goToPage(page) {
    const url = new URL(window.location.href);
    url.searchParams.set('page', page);
    window.location.href = url.toString();
}

function changeCategory(select) {
    // cambiar path
    const url = new URL(window.location.href);
    url.pathname = "/categoria/" + select.value;

    // mantener búsqueda si existe
    const search = url.searchParams.get("search");
    url.searchParams = new URLSearchParams(); // limpiar params
    
    if (search) {
        url.searchParams.set("search", search);
    }

    window.location.href = url.toString();
}