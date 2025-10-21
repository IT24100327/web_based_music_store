// Cart Utilities: API, UI Updates, Errors
async function initializeCartFromServerState() {
    await loadCartState();
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
            throw new Error(`Failed to load cart state: ${response.status}`);
        }

        const data = await response.json();
        updateCartUI(data);
    } catch (error) {
        handleCartError(error, 'Failed to load cart');
    }
}

async function updateCart(action, trackId, button) {
    if (!trackId) {
        handleCartError(new Error('Missing track ID'), 'Invalid track');
        return;
    }

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
            throw new Error(error.error || `Failed to update cart: ${response.status}`);
        }

        const data = await response.json();

        if (button) {
            updateCartButtonState(button, action === 'add');
        }

        updateCartUI(data);

    } catch (error) {
        handleCartError(error, 'Failed to update cart');
    }
}

function updateCartUI(data) {
    // Update badges
    const cartBadges = document.querySelectorAll('.cart-badge');
    cartBadges.forEach(badge => {
        badge.textContent = data.itemCount || 0;
        badge.style.display = data.itemCount > 0 ? 'flex' : 'none';
        if (data.itemCount > 0) {
            badge.classList.add('updated');
            setTimeout(() => badge.classList.remove('updated'), 600);
        }
    });

    // Update buttons
    if (data.cartItems) {
        const cartSet = new Set(data.cartItems.map(item => item.trackId));
        document.querySelectorAll('.cart-btn, .cart-btn-sm').forEach(btn => {
            const trackId = parseInt(btn.getAttribute('data-track-id'));
            const isAdded = cartSet.has(trackId);
            btn.classList.toggle('added', isAdded);
            const icon = btn.querySelector('i');
            if (icon) {
                icon.classList.toggle('fa-cart-plus', !isAdded);
                icon.classList.toggle('fa-check', isAdded);
            }
        });
    }

    // Update modal
    if (data.cartItems) {
        updateCartModal(data);
    }
}

function updateCartModal(data) {
    const modalBody = document.querySelector('#shoppingCartModal .modal-body');
    const cartTotal = document.querySelector('.cart-total');
    const modalHeaderBadge = document.querySelector('#shoppingCartModal .modal-title .cart-badge');

    if (!modalBody || !cartTotal || !modalHeaderBadge) return;

    cartTotal.textContent = `Rs. ${(data.cartTotal || 0).toFixed(2)}`;
    modalHeaderBadge.textContent = data.itemCount || 0;
    modalHeaderBadge.style.display = data.itemCount > 0 ? 'flex' : 'none';

    modalBody.innerHTML = data.itemCount === 0
        ? `
            <div class="cart-empty">
                <i class="fas fa-shopping-cart"></i>
                <p>Your cart is empty</p>
            </div>
        `
        : data.cartItems.map(item => `
            <div class="cart-item">
                <img src="${item.coverArtData ? `${window.contextPath}/cover-art?trackId=${item.trackId}` : 'https://images.unsplash.com/photo-1571330735066-03aaa9429d89?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80'}"
                     alt="Cover for ${escapeHtml(item.title)}" class="cart-item-img">
                <div class="cart-item-details">
                    <div class="cart-item-title">${escapeHtml(item.title)}</div>
                    <div class="cart-item-artist">by ${escapeHtml(item.artistName)}</div>
                    <div class="cart-item-price">Rs. ${item.price.toFixed(2)}</div>
                </div>
                <button class="cart-item-remove" data-track-id="${item.trackId}">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        `).join('');
}

function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.replace(/[&<>"']/g, function (m) {
        return map[m];
    });
}

function handleCartError(error, userMessage = 'Cart operation failed') {
    console.error('Cart Error:', error);
    showUserNotification(`${userMessage}: ${error.message}. <button onclick="window.reInitCart()">Retry</button>`, 'error');
}

function showUserNotification(message, type = 'error') {
    const notification = document.createElement('div');
    notification.className = `user-notification ${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 12px 16px;
        background: ${type === 'error' ? '#f8d7da' : '#d1ecf1'};
        color: ${type === 'error' ? '#721c24' : '#0c5460'};
        border: 1px solid ${type === 'error' ? '#f5c6cb' : '#bee5eb'};
        border-radius: 4px;
        z-index: 10000;
        max-width: 300px;
    `;

    document.body.appendChild(notification);

    setTimeout(() => {
        if (notification.parentNode) {
            notification.parentNode.removeChild(notification);
        }
    }, 5000);
}

function reInitCart() {
    initializeCartButtons();
    loadCartState();
}

function updateCartButtonState(button, isAdded) {
    if (isAdded) {
        button.classList.add('added');
        const icon = button.querySelector('i');
        if (icon) {
            icon.classList.replace('fa-cart-plus', 'fa-check');
        }
    } else {
        button.classList.remove('added');
        const icon = button.querySelector('i');
        if (icon) {
            if (icon.classList.contains('fa-check')) {
                icon.classList.replace('fa-check', 'fa-cart-plus');
            }
        }
    }
}
