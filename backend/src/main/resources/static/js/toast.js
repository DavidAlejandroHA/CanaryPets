// Se ejecuta automáticamente en redirecciones toast
document.addEventListener("DOMContentLoaded", () => {
    const toastElList = document.querySelectorAll('.toast.show');
    // Se usa la clase .show para que solo se muestren por defecto los mensajes
    // que vienen del backend en las redirecciones con RedirectAttributes
    // (en este caso hay que inicializar solo .toast.show)
    toastElList.forEach(toastEl => {
        new bootstrap.Toast(toastEl, { delay: 3000 }).show();
    });
});

// Se ejecuta manualmente al presionar un botón en el carrito
function showToast(message, type = "success") {
    if (!message || message.trim() === "") return; // Ignorar mensajes vacíos

    const toastEl = document.getElementById("live-toast");
    const toastBody = document.getElementById("toast-body");

    if (!toastEl || !toastBody) return;

    // Reset clases
    toastEl.classList.remove("text-bg-success", "text-bg-danger");

    // Tipo
    if (type === "error") {
        toastEl.classList.add("text-bg-danger");
    } else {
        toastEl.classList.add("text-bg-success");
    }

    // Mensaje
    toastBody.textContent = message;

    // Mostrar
    const toast = new bootstrap.Toast(toastEl, {
        delay: 2000
    });
    toast.show();
}