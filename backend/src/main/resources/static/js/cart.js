document.addEventListener("DOMContentLoaded", () => {

    const csrfToken = document.querySelector('input[name="_csrf"]')?.value;

    // =========================
    // 🗑️ ELIMINAR ITEM (AJAX + fallback)
    // =========================
    document.querySelectorAll(".remove-btn").forEach(btn => {
        btn.addEventListener("click", function () {
            e.preventDefault(); // Importante

            const item = this.closest(".cart-item");
            const productId = item.dataset.id;

            fetch(`/cart/remove/${productId}`, {
                method: "POST",
                headers: {
                    'X-CSRF-TOKEN': csrfToken,
                    "X-Requested-With": "XMLHttpRequest" // No es necesario aquí, pero si recomendado
                }
            })
            .then(response => {
                if (response.status === 401) { // En caso de que el usuario no esté logeado
                    window.location.href = "/auth/login";
                    return;
                }

                if (!response.ok) {
                    return response.text().then(text => {
                        console.error("Error backend:", text);
                        // Si falla -> fallback (recarga clásica)
                        window.location.href = `/cart`;
                        //throw new Error("Error HTTP " + response.status);
                    });
                }

                // Animación y eliminación
                /*item.classList.add("fade-out");*/
                item.classList.add("slide-out");

                setTimeout(() => {
                    item.remove();
                    checkEmptyCart();
                    updateTotals();
                }, 300); //  Coincide con el transition: 0.3s
            })
            .catch( err => {
                // Error de red -> fallback
                console.error("Error fetch:", err);
                window.location.href = `/cart`;
            });
        });
    });


    // =========================
    // 🔢 ACTUALIZAR CANTIDAD
    // =========================
    document.querySelectorAll(".quantity-input").forEach(input => {
        input.addEventListener("change", function () {

            const item = this.closest(".cart-item");
            const productId = item.dataset.id;
            const quantity = this.value;

            fetch(`/cart/update-ajax`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    'X-CSRF-TOKEN': csrfToken,
                    "X-Requested-With": "XMLHttpRequest" // No es necesario aquí, pero si recomendado
                },
                body: JSON.stringify({ id: productId, quantity })
            })
            .then(response => {

                if (response.status === 401) { // En caso de que el usuario no esté logeado
                    window.location.href = "/auth/login";
                    return;
                }

                if (!response.ok) {
                    // fallback
                    window.location.reload();
                    return;
                }

                // Recalcular totales
                // ACTUALIZAR TOTAL DEL ITEM
                updateItemTotal(item);

                // ACTUALIZAR RESUMEN (subtotal + total)
                updateTotals();

                // Animación visual
                item.classList.add("updated");

                setTimeout(() => {
                    item.classList.remove("updated");
                }, 300);
            })
            .catch(() => {
                window.location.reload();
            });
        });
    });

    // Evitar que el form haga submit al hacer update
    document.querySelectorAll(".update-form").forEach(form => {
        form.addEventListener("submit", e => {
            e.preventDefault(); // Evita recarga
        });
    });


    // =========================
    // 🧮 ACTUALIZAR TOTAL
    // =========================

    function updateItemTotal(item) {
        const price = isPremium
            ? parseFloat(item.dataset.pricePremium)
            : parseFloat(item.dataset.price);

        const quantity = parseInt(item.querySelector(".quantity-input").value);

        /*if (!price || !quantity) return;*/
        if (isNaN(price) || isNaN(quantity)) return;

        const total = (price * quantity).toFixed(2);

        // Actualizar TODOS los .item-total
        item.querySelectorAll(".item-total").forEach(el => {
            el.textContent = total + " €";
        });
    }


    // =========================
    // 🧾 ACTUALIZAR RESUMEN (subtotal, total)
    // =========================
    function updateTotals() {
        let subtotal = 0;

        document.querySelectorAll(".cart-item").forEach(item => {
            const price = isPremium
                ? parseFloat(item.dataset.pricePremium)
                : parseFloat(item.dataset.price);
            const quantity = parseInt(item.querySelector(".quantity-input").value);

            if (price && quantity) {
                subtotal += price * quantity;
            }
        });

        /*const shipping = subtotal > 0 ? 3.99 : 0;*/
        const shipping = isPremium ? 0 : (subtotal > 0 ? 3.99 : 0);
        const total = subtotal + shipping;

        // Elementos
        const subtotalEl = document.getElementById("cart-subtotal");
        const subtotalPremiumEl = document.getElementById("cart-subtotal-premium");
        const totalEl = document.getElementById("cart-total");

        // Actualizar SUBTOTAL correctamente
        if (isPremium) {
            if (subtotalPremiumEl) {
                subtotalPremiumEl.textContent = subtotal.toFixed(2) + " €";
            }
        } else {
            if (subtotalEl) {
                subtotalEl.textContent = subtotal.toFixed(2) + " €";
            }
        }

        // Actualizar TOTAL
        if (totalEl) {
            totalEl.textContent = total.toFixed(2) + " €";
        }
    }


    // =========================
    // 🛒 CARRITO VACÍO
    // =========================
    function checkEmptyCart() {
        const items = document.querySelectorAll(".cart-item");

        if (items.length === 0) {
            document.getElementById("empty-cart")?.classList.remove("d-none");
            document.getElementById("cart-content")?.classList.add("d-none");
        }
    }

    // Recalcular TODO_ al cargar para evitar inconsistencias si algo cambia en backend
    document.querySelectorAll(".cart-item").forEach(item => {
        updateItemTotal(item);
    });
    updateTotals();

});