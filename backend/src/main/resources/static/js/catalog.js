function addToCart(productId) {
    const qty = document.getElementById("qty__" + productId).value;

    //console.log("🔥 ADD TO CART CLICK", productId);
    fetch("/cart/add-ajax", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": document.querySelector('input[name="_csrf"]').value,
            "X-Requested-With": "XMLHttpRequest" // 👈 IMPORTANTE
        },
        body: JSON.stringify({
            productId: productId,
            quantity: qty
        })
    })
    .then(res => {
        //if (!res.ok) throw new Error();
        if (res.status === 401) { // En caso de que el usuario no esté logeado
            window.location.href = "/auth/login";
            return;
        }
        res.text().then(text => {
            //console.log("Texto:", text);
            console.log("Status:", res.status);
        });

        if (!res.ok) {
            return res.text().then(text => {
                console.error("Error backend:", text);
                //throw new Error("Error HTTP " + res.status);
                showToast("Error al añadir carrito ", "error");
            });
        }

        // Toast
        showToast("Producto añadido al carrito 🛒", "success");

        // Animación carrito
        animateCart();

        // Cerrar modal
        const modal = bootstrap.Modal.getInstance(
            document.getElementById("addToCartModal__" + productId)
        );
        modal.hide();
    })
    .catch( err => {
        // fallback
        //window.location.href = "/cart";
        console.error("Error en addToCart:", err);
        //alert("Error al añadir al carrito");
    });
}

// Para abrir dialog de "Añadir al carrito" js con fallback en caso de que no haya js
document.querySelectorAll(".add-to-cart-btn").forEach(btn => {
    btn.addEventListener("click", function (e) {
        e.preventDefault(); // Evita navegación

        const productId = this.dataset.id;

        const modalEl = document.getElementById("addToCartModal__" + productId);
        const modal = new bootstrap.Modal(modalEl);
        modal.show();
    });
});