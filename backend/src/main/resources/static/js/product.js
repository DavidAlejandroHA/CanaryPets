document.addEventListener("DOMContentLoaded", () => {

    const form = document.getElementById("add-to-cart-form");
    if (!form) return;

    form.addEventListener("submit", function (e) {
        e.preventDefault();

        const btn = form.querySelector("button");
        btn.disabled = true;

        const productId = form.querySelector("[name='productId']").value;
        const quantity = form.querySelector("[name='quantity']").value;
        const csrf = form.querySelector("[name='_csrf']").value;

        fetch("/cart/add-ajax", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "X-CSRF-TOKEN": csrf
            },
            body: JSON.stringify({
                productId: productId,
                quantity: quantity
            })
        })
        .then(res => {

            if (res.status === 401) {
                window.location.href = "/auth/login";
                return;
            }

            // Feedback UX
            showToast("Producto añadido al carrito 🛒");
            animateCart();
        })
        .catch(() => {
            // Fallback por si falla JS
            form.submit();
            //showToast("Error al añadir producto", "error");
        })
        .finally(() => btn.disabled = false);
    });

});