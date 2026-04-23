/* Variables de pago */
let selectedPayment = "card";
let paymentDone = false;

/**/
let currentStep = 1;
const steps = document.querySelectorAll(".checkout-step");

function showStep(step) {
    steps.forEach(s => s.style.display = "none");

    const current = document.getElementById(`step-${step}`);
    if (current) {
        current.style.display = "block";
    }
    //document.querySelector(`[data-step="${step}"]`).style.display = "block"; // En base a data-step



    const cartBtn = document.querySelector(".no-js-only");

    // Botones
    if (cartBtn) { // Botón "Volver al carrito"
        cartBtn.classList.toggle("d-none", step !== 1);
    }
    document.getElementById("back-btn").classList.toggle("d-none", step === 1); // Atrás
    document.getElementById("next-btn").classList.toggle("d-none", step === 3); // Siguiente
    document.getElementById("finish-btn").classList.toggle("d-none", step !== 3); // Terminar
}

// validar form envío
function validateShipping() {
    let valid = true;

    document.querySelectorAll("#step-1 [required]").forEach(input => {
        const group = input.closest(".inputGroup");
        if (!group) return; // Evitar errores con checkboxs

        const value = input.value.trim();

        // Si el input es de tipo email
        if (input.type === "email") {
            if (!input.value.includes("@")) {
                //valid = input.value.includes("@"); // Esto sobreescribe valid global
                group.classList.add("invalid");
                valid = false;
                return; // Importante
            }/* else {
                group.classList.remove("invalid");
            }*/
        }

        // Manejar checkboxs required
        /*if (input.type === "checkbox") {
            if (!input.checked) {
                group.classList.add("invalid");
                valid = false;
            } else {
                group.classList.remove("invalid");
            }
            return;
        }*/

        // General
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
    showStep(currentStep);
    updateProgressBar();

    // VALIDACIÓN AL SALIR DEL INPUT
    document.querySelectorAll("#step-1 input, #step-1 select").forEach(input => {
        input.addEventListener("blur", () => {
            const group = input.closest(".inputGroup");
            if (!group) return; // Evitar error en caso de checkbox

            if (input.hasAttribute("required") && input.value.trim().length < 2) {
                group.classList.add("invalid");
            } else {
                group.classList.remove("invalid");
            }
        });
    });

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

            // Activar / desactivar required dinámicamente según método_ de pago
            const cardInputs = document.querySelectorAll("#payment-card [required]"); // Todos los inputs de pago
            cardInputs.forEach(input => {
                if (selectedPayment === "card") {
                    input.disabled = false;
                    input.setAttribute("required", "required");
                } else {
                    input.disabled = true;
                    input.removeAttribute("required");
                }
            });
        });
    });

    // BOTONES DE PAGO
    document.querySelectorAll(".pay-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            const method = btn.dataset.method;
            paymentDone = true;
            alert("Pago simulado correcto ✅" + "(" + (method === "bank" ?
             "Transferencia bancaria" :
             "Paypal"
             ) + ")");

            //currentStep = 3;
            //showStep(3);
            //updateProgressBar();
        });
    });




    // activar modo JS
    //document.querySelectorAll(".no-js-only").forEach(el => el.style.display = "none");
    document.getElementById("checkout-steps").classList.remove("d-none");
    document.getElementById("next-btn").classList.remove("d-none");

    document.getElementById("next-btn").addEventListener("click", () => {
        //e.preventDefault(); // Evitar submit automático (útil en caso de que el botón sea tipo "submit")
        if (currentStep === 1 && !validateShipping()) {
            console.log("test: ", validateShipping())
            return;
        }
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
//const stepNodes = document.querySelectorAll(".step-node");
// progressBar = document.getElementById("progress-bar");

function updateProgressBar() {
    const nodes = document.querySelectorAll(".step-node");
    const bar = document.getElementById("progress-bar");

    // Cambiar color de nodo
    nodes.forEach((node, index) => {
        node.classList.remove("active", "done");

        if (index < currentStep - 1) {
            node.classList.add("done");
            node.classList.remove("active");
        } else if (index === currentStep - 1) {
            node.classList.add("active");
            node.classList.remove("done");
        } else {
            node.classList.remove("active", "done");
        }

        // Mejora:
        const step = Number(node.dataset.step);

        if (step < currentStep) {
            node.classList.add("done");
        } else if (step === currentStep) {
            node.classList.add("active");
        }
    });

    // 🔹 Calcular ancho barra
    /*const progressPercent = (currentStep - 1) / (stepNodes.length - 1) * 100;
    progressBar.style.width = progressPercent + "%";*/ // En base a % no funciona del todo_ bien

    // Cambiar barra de progreso
    const currentNode = nodes[currentStep - 1];
    const firstNode = nodes[0];

    const firstRect = firstNode.getBoundingClientRect();
    const currentRect = currentNode.getBoundingClientRect();

    const containerRect = firstNode.parentElement.getBoundingClientRect();

    const start = firstRect.left + firstRect.width / 2;
    const current = currentRect.left + currentRect.width / 2;

    const percent = ((current - start) / containerRect.width) * 100;

    bar.style.width = percent + "%";
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