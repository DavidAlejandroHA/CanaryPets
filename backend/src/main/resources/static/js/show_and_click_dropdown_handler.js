// Unused
/*document.addEventListener("DOMContentLoaded", function () {

    const dropdowns = document.querySelectorAll(".nav-item.dropdown");

    dropdowns.forEach(dropdown => {
        const link = dropdown.querySelector("a");

        link.addEventListener("click", function (e) {
            e.preventDefault();

            const isActive = dropdown.classList.contains("active");

            // cerrar todos
            dropdowns.forEach(d => d.classList.remove("active"));

            // toggle
            if (!isActive) {
                dropdown.classList.add("active");
            }
        });
    });

    // click fuera → cerrar todo_
    document.addEventListener("click", function (e) {
        dropdowns.forEach(dropdown => {
            if (!dropdown.contains(e.target)) {
                dropdown.classList.remove("active");
            }
        });
    });

});*/