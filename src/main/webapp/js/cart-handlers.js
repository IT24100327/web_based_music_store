// Cart Handlers: Buttons and Modal
function initializeCartButtons() {
    const cartButtons = document.querySelectorAll('.cart-btn, .cart-btn-sm');
    cartButtons.forEach(button => {
        button.replaceWith(button.cloneNode(true));
    });

    document.querySelectorAll('.cart-btn, .cart-btn-sm').forEach(button => {
        button.addEventListener('click', function() {
            const trackId = this.getAttribute('data-track-id');
            if (!trackId) {
                console.warn('Cart button missing track-id');
                return;
            }

            const isAdded = this.classList.contains('added');
            const action = isAdded ? 'remove' : 'add';
            updateCart(action, trackId, this);
        });
    });
}

function initializeCartModal() {
    document.addEventListener('click', function(e) {
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