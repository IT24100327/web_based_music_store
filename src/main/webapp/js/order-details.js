// /webapp/js/order-details.js
document.addEventListener('DOMContentLoaded', () => {
    const applyPromoBtn = document.getElementById('applyPromoBtn');
    if (applyPromoBtn) {
        applyPromoBtn.addEventListener('click', handlePromoValidation);
    }
});

/**
 * Handles the AJAX call to validate the promo code.
 */
async function handlePromoValidation() {
    const promoInput = document.getElementById('promoCodeField');
    const summaryCard = document.querySelector('.order-summary-card');
    if (!promoInput || !summaryCard) return;

    const code = promoInput.value.trim();
    const subtotal = summaryCard.dataset.subtotal;

    if (!code) {
        updatePromoFeedback('Please enter a promo code.', 'error');
        return;
    }

    // Show loading state
    const applyBtn = document.getElementById('applyPromoBtn');
    applyBtn.disabled = true;
    applyBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Applying...';

    try {
        const url = `${window.contextPath}/validate-promo?code=${encodeURIComponent(code)}&total=${subtotal}`;
        const response = await fetch(url);
        const data = await response.json();

        if (data.success) {
            updateOrderSummary(data);
            updatePromoFeedback(`Success! ${data.discount}% discount applied.`, 'success');
            document.getElementById('promoCodeInput').value = data.promoCode; // Set hidden input for form submission
        } else {
            resetOrderSummary();
            updatePromoFeedback(data.message || 'Invalid promo code.', 'error');
            document.getElementById('promoCodeInput').value = ''; // Clear hidden input
        }
    } catch (error) {
        console.error('Promo validation error:', error);
        resetOrderSummary();
        updatePromoFeedback('Could not validate promo code. Please try again.', 'error');
    } finally {
        // Restore button state
        applyBtn.disabled = false;
        applyBtn.innerHTML = 'Apply';
    }
}

/**
 * Updates the UI with the calculated discount and final total.
 * @param {object} promoData - The successful response from the validation servlet.
 */
function updateOrderSummary(promoData) {
    document.getElementById('summaryDiscount').textContent = `- Rs. ${promoData.discountAmount.toFixed(2)}`;
    document.getElementById('summaryDiscountRow').style.display = 'flex';
    document.getElementById('summaryTotal').textContent = `Rs. ${promoData.finalAmount.toFixed(2)}`;
}

/**
 * Resets the summary to its original state if promo is invalid.
 */
function resetOrderSummary() {
    const summaryCard = document.querySelector('.order-summary-card');
    const subtotal = parseFloat(summaryCard.dataset.subtotal).toFixed(2);

    document.getElementById('summaryDiscountRow').style.display = 'none';
    document.getElementById('summaryTotal').textContent = `Rs. ${subtotal}`;
}

/**
 * Displays success or error messages to the user.
 * @param {string} message - The message to display.
 * @param {'success'|'error'} type - The type of message.
 */
function updatePromoFeedback(message, type) {
    const feedbackEl = document.getElementById('promoFeedback');
    feedbackEl.textContent = message;
    feedbackEl.className = `promo-feedback ${type}`;
    feedbackEl.style.display = 'block';
}