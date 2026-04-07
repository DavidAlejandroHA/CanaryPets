document.addEventListener("DOMContentLoaded", () => {
    const toastElList = document.querySelectorAll('.toast');
    toastElList.forEach(toastEl => {
        new bootstrap.Toast(toastEl, { delay: 3000 }).show();
    });
});