document.addEventListener("DOMContentLoaded", () => {
    function autoFadeOut(element) {
        if (!element) return;
        setTimeout(() => {
            element.style.transition = 'opacity 0.5s ease';
            element.style.opacity = '0';
            setTimeout(() => element.remove(), 500);
        }, 2500);
    }

    autoFadeOut(document.getElementById('flashMessageSuccess'));
    autoFadeOut(document.getElementById('flashMessageError'));
});
