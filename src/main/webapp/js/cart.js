// Cart functionality for RhythmWave Music Store
document.addEventListener('DOMContentLoaded', function() {
    // Play button functionality
    const playButtons = document.querySelectorAll('.play-btn');
    playButtons.forEach(button => {
        button.addEventListener('click', function() {
            this.classList.toggle('playing');
            const icon = this.querySelector('i');
            if (icon.classList.contains('fa-play')) {
                icon.classList.replace('fa-play', 'fa-pause');
            } else {
                icon.classList.replace('fa-pause', 'fa-play');
            }
        });
    });

    // Initialize cart buttons
    initializeCartButtons();

    // Initialize cart modal
    initializeCartModal();
});

// Initialize cart buttons
function initializeCartButtons() {
    const cartButtons = document.querySelectorAll('.cart-btn');
    cartButtons.forEach(button => {
        button.addEventListener('click', function() {
            const trackId = this.getAttribute('data-track-id');
            const isAdded = this.classList.contains('added');
            const action = isAdded ? 'remove' : 'add';

            updateCart(action, trackId, this);
        });
    });
}

// Initialize cart modal
function initializeCartModal() {
    // Add event listeners to remove buttons in modal
    document.addEventListener('click', function(e) {
        if (e.target.closest('.cart-item-remove')) {
            const button = e.target.closest('.cart-item-remove');
            const trackId = button.getAttribute('data-track-id');
            updateCart('remove', trackId, null);
        }
    });

    // Load initial cart state
    loadCartState();
}

// Load initial cart state from server
async function loadCartState() {
    try {
        const response = await fetch('CartServlet?action=getState', {
            method: 'GET',
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Failed to load cart state');
        }

        const data = await response.json();
        updateCartUI(data);
    } catch (error) {
        console.error('Error loading cart state:', error);
    }
}

// AJAX call to update cart
async function updateCart(action, trackId, button) {
    try {
        const response = await fetch(`CartServlet?action=${action}&trackId=${trackId}`, {
            method: 'GET',
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || 'Failed to update cart');
        }

        const data = await response.json();

        // Update button state if provided
        if (button) {
            if (action === 'add') {
                button.classList.add('added');
                button.querySelector('i').classList.replace('fa-cart-plus', 'fa-check');

                // Revert after 2 seconds
                setTimeout(() => {
                    if (button.classList.contains('added')) {
                        button.querySelector('i').classList.replace('fa-check', 'fa-cart-plus');
                    }
                }, 2000);
            } else {
                button.classList.remove('added');
                button.querySelector('i').classList.replace('fa-check', 'fa-cart-plus');
            }
        }

        // Update UI
        updateCartUI(data);
    } catch (error) {
        console.error('Error updating cart:', error);
        alert('Error: ' + error.message);
    }
}

// Update cart UI elements
function updateCartUI(data) {
    // Update cart badge
    const cartBadge = document.querySelector('.cart-badge');
    if (cartBadge) {
        cartBadge.textContent = data.itemCount;
    }

    // Update cart modal content
    updateCartModal(data);
}

// Update cart modal content
function updateCartModal(data) {
    const modalBody = document.querySelector('#shoppingCartModal .modal-body');
    const cartTotal = document.querySelector('.cart-total');

    if (!modalBody || !cartTotal) return;

    // Update total
    cartTotal.textContent = 'Rs. ' + data.cartTotal.toFixed(2);

    // Update cart items
    if (data.itemCount === 0) {
        modalBody.innerHTML = `
            <div class="cart-empty">
                <i class="fas fa-shopping-cart"></i>
                <p>Your cart is empty</p>
            </div>
        `;
    } else {
        let cartItemsHtml = '';
        data.cartItems.forEach(item => {
            cartItemsHtml += `
                <div class="cart-item">
                    <img src="https://images.unsplash.com/photo-1571330735066-03aaa9429d89?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80"
                         alt="Album Cover" class="cart-item-img">
                    <div class="cart-item-details">
                        <div class="cart-item-title">${escapeHtml(item.title)}</div>
                        <div class="cart-item-artist">by ${escapeHtml(item.artist)}</div>
                        <div class="cart-item-price">Rs. ${item.price.toFixed(2)}</div>
                    </div>
                    <button class="cart-item-remove" data-track-id="${item.trackId}">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            `;
        });
        modalBody.innerHTML = cartItemsHtml;
    }
}

// Helper function to escape HTML
function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.replace(/[&<>"']/g, function(m) { return map[m]; });
}