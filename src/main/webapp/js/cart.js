document.addEventListener('DOMContentLoaded', function() {
    initializeCartFromServerState();
    initializeCartButtons();
    initializeCartModal();
});

function initializeCartFromServerState() {
    if (window.initialCartState) {
        updateCartUI({
            cartItems: [],
            cartTotal: window.initialCartState.cartTotal,
            itemCount: window.initialCartState.itemCount
        });
    }
    loadCartState();
}

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

function initializeCartModal() {
    document.addEventListener('click', function(e) {
        if (e.target.closest('.cart-item-remove')) {
            const button = e.target.closest('.cart-item-remove');
            const trackId = button.getAttribute('data-track-id');
            updateCart('remove', trackId, null);
        }
    });

    loadCartState();
}

async function loadCartState() {
    try {
        const response = await fetch(`${window.contextPath}/CartServlet?action=getState`, {
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

async function updateCart(action, trackId, button) {
    try {
        const response = await fetch(`${window.contextPath}/CartServlet?action=${action}&trackId=${trackId}`, {
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

        if (button) {
            if (action === 'add') {
                button.classList.add('added');
                const icon = button.querySelector('i');
                if (icon) {
                    icon.classList.replace('fa-cart-plus', 'fa-check');
                }
            } else if (action === 'remove') {
                button.classList.remove('added');
                const icon = button.querySelector('i');
                if (icon) {
                    icon.classList.replace('fa-check', 'fa-cart-plus');
                }
            }
        }

        updateCartUI(data);

    } catch (error) {
        console.error('Error updating cart:', error);
        alert('Error: ' + error.message);
    }
}

function updateCartUI(data) {
    const cartBadges = document.querySelectorAll('.cart-badge');
    cartBadges.forEach(badge => {
        badge.textContent = data.itemCount;
        badge.style.display = data.itemCount > 0 ? 'flex' : 'none';

        if (data.itemCount > 0) {
            badge.classList.add('updated');
            setTimeout(() => {
                badge.classList.remove('updated');
            }, 600);
        }
    });

    if (data.cartItems) {
        const cartSet = new Set(data.cartItems.map(item => item.trackId));
        document.querySelectorAll('.cart-btn').forEach(btn => {
            const trackId = parseInt(btn.getAttribute('data-track-id'));
            const isAdded = cartSet.has(trackId);
            btn.classList.toggle('added', isAdded);
            const icon = btn.querySelector('i');
            if (icon) {
                if (isAdded) {
                    if (icon.classList.contains('fa-cart-plus')) {
                        icon.classList.replace('fa-cart-plus', 'fa-check');
                    }
                } else {
                    if (icon.classList.contains('fa-check')) {
                        icon.classList.replace('fa-check', 'fa-cart-plus');
                    }
                }
            }
        });
    }

    if (data.cartItems) {
        updateCartModal(data);
    }
}

function updateCartModal(data) {
    const modalBody = document.querySelector('#shoppingCartModal .modal-body');
    const cartTotal = document.querySelector('.cart-total');

    if (!modalBody || !cartTotal) return;

    cartTotal.textContent = 'Rs. ' + data.cartTotal.toFixed(2);

    const modalHeaderBadge = document.querySelector('#shoppingCartModal .modal-title .cart-badge');
    if (modalHeaderBadge) {
        modalHeaderBadge.textContent = data.itemCount;
        modalHeaderBadge.style.display = data.itemCount > 0 ? 'flex' : 'none';
    }

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

// Expose for dynamic re-init (called after AJAX updates)
window.reInitCart = function() {
    initializeCartButtons();  // Re-attach click listeners to new .cart-btn
    loadCartState();         // Refresh cart data from server (updates classes/icons)
};