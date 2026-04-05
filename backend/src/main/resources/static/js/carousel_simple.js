let currentIndex = 0;

function selectImage(img) {
    const thumbs = document.querySelectorAll(".thumb");

    // Actualizar índice
    currentIndex = Array.from(thumbs).indexOf(img);
    // Convierte NodeList -> array
    // y encuentra el índice real del elemento clicado

    // Cambiar imagen principal
    document.getElementById("mainImage").src = img.src;

    // Actualizar borde
    thumbs.forEach(t => t.classList.remove("border-primary"));
    img.classList.add("border-primary");
}

function nextImage() {
    const thumbs = document.querySelectorAll(".thumb");
    currentIndex = (currentIndex + 1) % thumbs.length;
    selectImage(thumbs[currentIndex]);
}

function prevImage() {
    const thumbs = document.querySelectorAll(".thumb");
    currentIndex = (currentIndex - 1 + thumbs.length) % thumbs.length;
    selectImage(thumbs[currentIndex]);
}

// Seleccionar internamente en js la primera imagen del carousel
document.addEventListener("DOMContentLoaded", () => {
    const first = document.querySelector(".thumb");
    if (first) {
        selectImage(first);
    }
});