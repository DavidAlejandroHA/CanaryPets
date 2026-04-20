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
                    //window.location.reload();
                    response.json().then(errors => {

                            errors.forEach(err => {

                                // marcar producto
                                const el = document.querySelector(
                                    `.cart-item[data-id="${err.productId}"]`
                                );
                                if (el) el.classList.add("border-danger");

                                // toast
                                showToast(err.message, "error");
                            });

                        });

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

/* Validar carrito */
document.getElementById("checkout-btn").addEventListener("click", function () {
    if (this.disabled) return; // evitar spam
        this.disabled = true;
    const csrfToken = document.querySelector('input[name="_csrf"]').value;

    fetch("/cart/validate-ajax", {
        method: "POST",
        headers: {
            "X-CSRF-TOKEN": csrfToken,
            "X-Requested-With": "XMLHttpRequest"
        }
    })
    .then(async res => {

        // No logeado
        if (res.status === 401) {
            window.location.href = "/auth/login";
            return;
        }

        // Errores de validación (ahora se usa json en vez de .text())
        if (!res.ok) {
            this.disabled = false;
            const errors = await res.json();

            // Limpiar estados previos
            document.querySelectorAll(".cart-item").forEach(el => {
                el.classList.remove("border-danger");
            });

            // errors[0]
            errors.forEach(err => {

                // Marcar producto visualmente
                if (err.productId) {
                    const el = document.querySelector(`.cart-item[data-id="${err.productId}"]`);
                    if (el) el.classList.add("border-danger");
                }

                // En caso de ser necesario marcar el stock en exceso
                if (err.productId && err.availableStock) {
                    const input = document.querySelector(
                        `.cart-item[data-id="${err.productId}"] .quantity-input`
                    );
                    //input.classList.add("is-invalid"); // No hace falta
                    if (input) input.value = err.availableStock;
                }

                // Toast
                showToast(err.message, "error");
            });

            // Si hay un error, se hace scroll hacia el primer error
            if (errors.length > 0) {
                const firstError = errors[0];
                if (firstError.productId) {
                    const el = document.querySelector(`.cart-item[data-id="${firstError.productId}"]`);
                    if (el) el.scrollIntoView({ behavior: "smooth", block: "center" });
                }
            }

            return;
        }

        // Todo_ OK
        window.location.href = "/checkout";
    })
    .catch(() => {
        this.disabled = false;
        showToast("Error de conexión", "error");
    });
});