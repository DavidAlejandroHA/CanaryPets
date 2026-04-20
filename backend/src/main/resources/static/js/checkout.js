let currentStep = 1;
const steps = document.querySelectorAll(".checkout-step");

function showStep(step) {
    steps.forEach(s => s.style.display = "none");
    document.querySelector(`[data-step="${step}"]`).style.display = "block";

    // botones
    document.getElementById("back-btn").classList.toggle("d-none", step === 1);
    document.getElementById("next-btn").classList.toggle("d-none", step === 3);
    document.getElementById("finish-btn").classList.toggle("d-none", step !== 3);
}

// validar form envío
function validateShipping() {
    let valid = true;

    document.querySelectorAll("#step-1 [required]").forEach(input => {
        const group = input.closest(".inputGroup");
        const value = input.value.trim();

        // Si el input es de tipo email
        if (input.type === "email") {
            valid = input.value.includes("@");
        }

        if (value.length < 2) {
            group.classList.add("invalid");
            valid = false;
        } else {
            group.classList.remove("invalid");
        }
    });

    return valid;
}

// init
document.addEventListener("DOMContentLoaded", () => {

    // activar modo JS
    document.querySelectorAll(".no-js-only").forEach(el => el.style.display = "none");
    document.getElementById("checkout-steps").classList.remove("d-none");
    document.getElementById("next-btn").classList.remove("d-none");

    showStep(1);
    updateProgressBar();

    document.getElementById("next-btn").addEventListener("click", () => {

        if (currentStep === 1 && !validateShipping()) return;

        if (currentStep === 2 && !validatePayment()) return;

        currentStep++;
        showStep(currentStep);
        updateProgressBar();
    });

    document.getElementById("back-btn").addEventListener("click", () => {
        currentStep--;
        showStep(currentStep);
        updateProgressBar();
    });

    document.getElementById("pay-btn").addEventListener("click", () => {
        currentStep = 3;
        showStep(3);
        updateProgressBar();
    });

});

// VALIDACIÓN AL SALIR DEL INPUT
document.querySelectorAll("#step-1 input, #step-1 select").forEach(input => {
    input.addEventListener("blur", () => {
        const group = input.closest(".inputGroup");

        if (input.hasAttribute("required") && input.value.trim().length < 2) {
            group.classList.add("invalid");
        } else {
            group.classList.remove("invalid");
        }
    });
});




/* Pago */
let selectedPayment = "card";
let paymentDone = false;

// CAMBIO DE MÉTODO_
document.querySelectorAll(".payment-option").forEach(btn => {
    btn.addEventListener("click", () => {

        document.querySelectorAll(".payment-option")
            .forEach(b => b.classList.remove("active"));

        btn.classList.add("active");

        selectedPayment = btn.dataset.method;
        paymentDone = false;

        document.querySelectorAll(".paymentBlock")
            .forEach(b => b.classList.add("d-none"));

        document.getElementById("payment-" + selectedPayment)
            .classList.remove("d-none");
    });
});

// BOTONES DE PAGO
document.querySelectorAll(".pay-btn").forEach(btn => {
    btn.addEventListener("click", () => {
        paymentDone = true;
        alert("Pago simulado correcto ✅");
    });
});

function validatePayment() {
    let valid = true;

    // CHECKBOX términos
    const terms = document.getElementById("terms");
    const group = terms.closest(".inputGroup");

    if (!terms.checked) {
        group.classList.add("invalid");
        valid = false;
    } else {
        group.classList.remove("invalid");
    }

    // TARJETA
    if (selectedPayment === "card") {
        document.querySelectorAll("#payment-card [required]").forEach(input => {
            const g = input.closest(".inputGroup");

            if (!input.value.trim()) {
                g.classList.add("invalid");
                valid = false;
            } else {
                g.classList.remove("invalid");
            }
        });
    }

    // PAYPAL / BANK
    if (selectedPayment !== "card" && !paymentDone) {
        alert("Debes realizar el pago primero");
        valid = false;
    }

    return valid;
}

/* Checkout progress */
const stepNodes = document.querySelectorAll(".step-node");
const progressBar = document.getElementById("progress-bar");

function updateProgressBar() {

    stepNodes.forEach((node, index) => {
        node.classList.remove("active", "done");

        if (index < currentStep) {
            node.classList.add("done");
        } else if (index === currentStep) {
            node.classList.add("active");
        }
    });

    // 🔹 Calcular ancho barra
    const progressPercent = (currentStep) / (stepNodes.length - 1) * 100;
    progressBar.style.width = progressPercent + "%";
}


/* Mandar petición de crear pedido al controller */
// A futuro
/*function submitOrder() {

    const data = {
        email: document.getElementById("correo").value,
        nombre: document.getElementById("nombre").value,
        apellidos: document.getElementById("apellidos").value,
        telefono: document.getElementById("telefono").value,
        direccionEnvio: document.getElementById("direccionEnv").value,
        direccionEnvio2: document.getElementById("direccionEnv2").value,
        municipio: document.getElementById("municipio").value,
        provincia: document.getElementById("select-provincia").value,
        codigoPostal: document.getElementById("codPostal").value,
        sameAsShipping: document.getElementById("checkbox-domiciliar").checked,

        paymentMethod: selectedPayment,

        //items: getCartItems()
        // El backend al final es el que se encarga de gestionar los items
    };

    fetch("/orders/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": document.querySelector('[name="_csrf"]').value
        },
        body: JSON.stringify(data)
    })
    .then(res => {
        if (res.status === 401) {
            window.location.href = "/auth/login";
            return;
        }
        if (!res.ok) throw new Error();
        return res.text();
    })
    //.then(orderId => {
        //window.location.href = `/order/success/${orderId}`;
    //    window.location.href = `/checkout-completed`;
    //})/
    .catch(() => {
        alert("Error al procesar pedido");
    });
}*/

function getCartItems() {
    const items = [];

    document.querySelectorAll(".cart-item").forEach(item => {
        const productId = item.dataset.id;
        const quantity = item.dataset.qty;

        if (productId && quantity) {
            items.push({
                productId: parseInt(productId),
                quantity: parseInt(quantity)
            });
        }
    });

    return items;
}