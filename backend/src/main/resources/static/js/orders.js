// Interceptar clicks y evitar recarga completa
document.querySelectorAll(".pagination a").forEach(link => {
    link.addEventListener("click", e => {
        e.preventDefault();
        window.location.href = link.href; // fallback limpio
    });
});