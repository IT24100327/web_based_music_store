// Cart Handlers: Buttons and Modal
// cart-handlers.js
function initializeCartButtons() {
    document.removeEventListener('click', handleCartButtonClick); // Remove any existing listener
    document.addEventListener('click', handleCartButtonClick);

    function handleCartButtonClick(e) {
        if (e.target.closest('.cart-btn, .cart-btn-sm')) {
            const button = e.target.closest('.cart-btn, .cart-btn-sm');
            const trackId = button.getAttribute('data-track-id');
            if (!trackId) {
                console.warn('Cart button missing track-id');
                return;
            }
            const isAdded = button.classList.contains('added');
            const action = isAdded ? 'remove' : 'add';
            updateCart(action, trackId, button);
        }
    }
}

function initializeCartModal() {
    document.addEventListener('click', function (e) {
        if (e.target.closest('.cart-item-remove')) {
            const button = e.target.closest('.cart-item-remove');
            const trackId = button.getAttribute('data-track-id');
            if (trackId) {
                updateCart('remove', trackId, null);
            }
        }
    });

    loadCartState();
}