// Alternativa: mostrar toast al actualizar desde la página de listado de pedidos
// usando un select + form
/*document.addEventListener("DOMContentLoaded", () => {

    const toastEl = document.getElementById("statusToast");
    const toast = new bootstrap.Toast(toastEl);

    document.querySelectorAll(".status-form").forEach(form => {

        form.addEventListener("submit", async (e) => {
            e.preventDefault();

            try {
                const res = await fetch(form.action, {
                    method: "POST",
                    body: new FormData(form)
                });

                if (res.status === 401) {
                    window.location.href = "/auth/login";
                    return;
                }

                if (!res.ok) throw new Error();

                // Mostrar toast
                toast.show();

            } catch (err) {
                console.error(err);
                form.submit(); // fallback sin JS
            }
        });

    });

});*/