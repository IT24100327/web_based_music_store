// Cart Main Entry Point
document.addEventListener('DOMContentLoaded', function() {
    initializeCartFromServerState();
    initializeCartButtons();
    initializeCartModal();
});

// Expose reInitCart globally for other scripts (e.g., music.js)
window.reInitCart = reInitCart;