
// Headers manuales
const csrfToken = document.querySelector('meta[name="_csrf"]').content;
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

// Hacer cambios en vivo
document.addEventListener("DOMContentLoaded", () => {

    // ❤️ TOGGLE FAVORITOS (catálogo + detail)
    document.querySelectorAll(".favorite-form").forEach(form => {

        form.addEventListener("submit", async (e) => {

            e.preventDefault(); // Solo si hay JS

            const button = form.querySelector(".favorite-btn");
            const icon = form.querySelector("span, .fav-icon, .favorite-text");
            const productId = button.dataset.id;

            try {
                const response = await fetch("/profile/favorites/toggle-ajax", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                        "X-Requested-With": "XMLHttpRequest",
                        [csrfHeader]: csrfToken
                        /* Los headers manuales suelen ser necesarios si se utiliza
                        new URLSearchParams(...) o JSON.stringify(...) en el body.
                         En los casos donde se usa body: new FormData(form), se usan
                         los input type="hidden" del propio form que ya vienen con th:value="${_csrf.token}"*/
                    },
                    body: new URLSearchParams({
                        productId: productId
                    })
                });

                if (response.status === 401) { // En caso de que el usuario no esté logeado
                    window.location.href = "/auth/login";
                    return;
                }

                if (!response.ok) throw new Error();

                const data = await response.json();

                // CATÁLOGO (icono ❤️🤍)
                if (icon && !icon.classList.contains("favorite-text")) {
                    icon.textContent = data.favorite ? "❤️" : "🤍";
                }

                // PRODUCT DETAIL (texto completo)
                if (icon && icon.classList.contains("favorite-text")) {
                    icon.textContent = data.favorite
                        ? "❤️ Eliminar de favoritos"
                        : "🤍 Añadir a favoritos";
                }

                // Clases botón
                button.classList.toggle("active", data.favorite);

                // (solo detail)
                button.classList.toggle("btn-danger", data.favorite);
                button.classList.toggle("btn-outline-danger", !data.favorite);

                button.dataset.favorite = data.favorite;

            } catch (err) {
                console.error(err);

                // Fallback automático (sin romper UX)
                form.submit();
            }

        });

    });

    // 🗑️ REMOVE FAVORITOS (fade-out en /favorites)
    document.querySelectorAll(".remove-fav-form").forEach(form => {

        form.addEventListener("submit", async (e) => {

            e.preventDefault();

            const card = form.closest(".col-6, .col-md-4, .col-lg-3");

            try {
                const res = await fetch(form.action, {
                    method: "POST",
                    body: new FormData(form)
                });

                if (res.status === 401) { // En caso de que el usuario no esté logeado
                    window.location.href = "/auth/login";
                    return;
                }

                if (!res.ok) throw new Error();

                // Animación
                card.style.transition = "opacity 0.3s ease, transform 0.3s ease";
                card.style.opacity = "0";
                card.style.transform = "scale(0.95)";

                //setTimeout(() => card.remove(), 300);
                setTimeout(() => {
                    card.remove();

                    // Comprobar si no quedan favoritos
                    const remainingCards = document.querySelectorAll(".remove-fav-form");

                    if (remainingCards.length === 0) {
                        const container = document.querySelector(".row.g-3");

                        container.innerHTML = `
                            <div class="col-12 text-center py-5">
                                <p class="text-muted">No tienes productos favoritos aún</p>
                                <a href="/" class="btn btn-primary">Explorar productos</a>
                            </div>
                        `;
                    }

                }, 300);

            } catch (err) {
                console.error(err);
                form.submit(); // fallback
            }
        });
    });

    /*  Efecto "pop" típico de favoritos: */
    document.querySelectorAll(".favorite-form").forEach(form => {
      form.addEventListener("submit", (e) => {
        const btn = form.querySelector(".favorite-btn");
        const icon = form.querySelector(".fav-icon");

        // Animación inmediata (antes del fetch)
        //icon.style.transform = "scale(1.4)";

        /*setTimeout(() => {
          icon.style.transform = "scale(1)";
        }, 150);*/

        /* Añadir animación pop */
        btn.classList.add("clicked");

        setTimeout(() => {
          btn.classList.remove("clicked");
        }, 300);
      });

      // Eliminar la card si pasa de ❤️ -> 🤍
//      form.addEventListener("submit", async (e) => {
//      const isFavoritesPage = window.location.pathname.includes("/profile/favorites");
//
//          if (isActive && isFavoritesPage) {
//             // eliminar card
//             e.preventDefault();
//
//                       const btn = form.querySelector(".favorite-btn");
//                       const card = form.closest(".col-6, .col-md-4, .col-lg-3");
//                       const isActive = btn.classList.contains("active"); // ❤️ antes del click
//
//                       try {
//                         const res = await fetch(form.action.replace("toggle", "toggle-ajax"), {
//                           method: "POST",
//                           body: new FormData(form)
//                         });
//
//                         if (res.status === 401) {
//                           window.location.href = "/auth/login";
//                           return;
//                         }
//
//                         if (!res.ok) throw new Error();
//
//                         // Si estaba activo -> se está quitando -> eliminar card
//                         if (isActive) {
//                           card.style.transition = "opacity 0.3s ease, transform 0.3s ease";
//                           card.style.opacity = "0";
//                           card.style.transform = "scale(0.95)";
//
//                           setTimeout(() => {
//                             card.remove();
//
//                             const remainingCards = document.querySelectorAll(".favorite-form");
//
//                             if (remainingCards.length === 0) {
//                               const container = document.querySelector(".row.g-3");
//                               container.innerHTML = `
//                                 <div class="col-12 text-center py-5">
//                                   <p class="text-muted">No tienes productos favoritos aún</p>
//                                   <a href="/" class="btn btn-primary">Explorar productos</a>
//                                 </div>
//                               `;
//                             }
//
//                           }, 300);
//
//                         } else {
//                           // Si NO estaba activo (añadir favorito), solo toggle visual
//                           btn.classList.add("active");
//                           form.querySelector(".fav-icon").textContent = "❤️";
//                         }
//
//                       } catch (err) {
//                         console.error(err);
//                         form.submit(); // fallback
//                       }
//          }
//        });
    });
});