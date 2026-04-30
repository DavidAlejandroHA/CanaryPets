document.addEventListener("DOMContentLoaded", () => {

    /* Añadir al carrito */
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


    /* Recargar cambios en vivo del botón de favoritos */
    document.querySelectorAll(".favorite-form").forEach(form => {
      form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const btn = form.querySelector("button");
        const icon = form.querySelector(".fav-icon, .favorite-text");

        try {
          const res = await fetch("/profile/favorites/toggle-ajax", {
            method: "POST",
            body: new FormData(form)
          });

          if (res.status === 401) {
            window.location.href = "/auth/login";
            return;
          }

          if (!res.ok) throw new Error();

          // Toggle visual
          const isActive = btn.classList.contains("active");

          if (isActive) {
            btn.classList.remove("active");

            if (icon) {
              if (icon.classList.contains("fav-icon")) {
                icon.textContent = "🤍";
              } else {
                icon.textContent = "🤍 Añadir a favoritos";
              }
            }

          } else {
            btn.classList.add("active");

            if (icon) {
              if (icon.classList.contains("fav-icon")) {
                icon.textContent = "❤️";
              } else {
                icon.textContent = "❤️ Eliminar de favoritos";
              }
            }
          }

          // Animación click
          /*btn.style.transform = "scale(0.9)";
          setTimeout(() => {
            btn.style.transform = "scale(1)";
          }, 150);*/

          // Animación latido mediante js + css
          btn.classList.remove("animate");

          // Se fuerza reinicio/reflow (importante)
          void btn.offsetWidth;
          btn.classList.add("animate");

          setTimeout(() => {
            btn.classList.remove("animate");
          }, 400);

        } catch (err) {
          console.error(err);
          form.submit(); // fallback sin JS
        }
      });
    });
});