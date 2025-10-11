// Music Main Entry Point
document.addEventListener('DOMContentLoaded', function() {
    initializeViewControls();
    initializePagination();
    initializePlayButtons();

    // Initial cart init
    if (typeof window.reInitCart === 'function') {
        window.reInitCart();
    }
});