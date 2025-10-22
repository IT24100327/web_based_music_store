<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Summary & Checkout</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order-details.css">
</head>
<body>

<jsp:include page="includes/navbar.jsp"/>

<div class="container order-container animate-fade-in-up">
    <div class="order-details-section">
        <div class="order-details-header">
            <h1>Review Your Order</h1>
            <p>Please review your items and complete the payment below.</p>
        </div>

        <div class="cart-items-list">
            <c:forEach var="item" items="${cartItems}">
                <div class="cart-item">
                    <img src="${pageContext.request.contextPath}/cover-art?trackId=${item.trackId}" alt="${item.title}" class="item-image">
                    <div class="item-details">
                        <h5><c:out value="${item.title}"/></h5>
                        <p>by <c:out value="${item.artistName}"/></p>
                    </div>
                    <div class="item-price">
                        Rs. <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${item.price}"/>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div class="payment-form-section">
            <h2>Payment Details</h2>
            <form id="paymentForm" action="${pageContext.request.contextPath}/process-payment" method="POST">
                <input type="hidden" id="promoCodeInput" name="promoCode" value="">

                <div class="payment-method-options">
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="paymentMethod" id="cardPayment" value="CARD" checked onchange="togglePaymentDetails()">
                        <label class="form-check-label" for="cardPayment">
                            <i class="fas fa-credit-card"></i> Credit / Debit Card
                        </label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="paymentMethod" id="onlinePayment" value="ONLINE" onchange="togglePaymentDetails()">
                        <label class="form-check-label" for="onlinePayment">
                            <i class="fas fa-university"></i> Online Banking
                        </label>
                    </div>
                </div>

                <div id="cardDetails">
                    <div class="mb-3">
                        <label for="cardholderName" class="form-label">Cardholder Name</label>
                        <input type="text" class="form-control" id="cardholderName" name="cardholderName" placeholder="John Doe">
                    </div>
                    <div class="mb-3">
                        <label for="cardNumber" class="form-label">Card Number</label>
                        <input type="text" class="form-control" id="cardNumber" name="cardNumber" placeholder="0000 0000 0000 0000">
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="expiryDate" class="form-label">Expiry Date</label>
                            <input type="text" class="form-control" id="expiryDate" name="expiryDate" placeholder="MM/YY">
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="cvv" class="form-label">CVV</label>
                            <input type="text" class="form-control" id="cvv" name="cvv" placeholder="123">
                        </div>
                    </div>
                </div>

                <div id="onlineBankingDetails" style="display: none;">
                    <div class="mb-3">
                        <label for="bankName" class="form-label">Bank Name</label>
                        <input type="text" class="form-control" id="bankName" name="bankName" placeholder="e.g., Commercial Bank">
                    </div>
                    <div class="mb-3">
                        <label for="accountHolderName" class="form-label">Account Holder Name</label>
                        <input type="text" class="form-control" id="accountHolderName" name="accountHolderName" placeholder="John Doe">
                    </div>
                    <div class="mb-3">
                        <label for="accountNumber" class="form-label">Account Number</label>
                        <input type="text" class="form-control" id="accountNumber" name="accountNumber" placeholder="e.g., 1234567890">
                    </div>
                    <div class="mb-3">
                        <label for="transferReference" class="form-label">Transfer Reference</label>
                        <input type="text" class="form-control" id="transferReference" name="transferReference" placeholder="e.g., Order Payment">
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="order-summary-card" data-subtotal="${cartTotal}">
        <h3>Order Summary</h3>

        <div class="promo-section">
            <div class="promo-input-group">
                <input type="text" class="form-control" id="promoCodeField" placeholder="Enter Promo Code">
                <button class="btn btn-secondary" id="applyPromoBtn">Apply</button>
            </div>
            <div id="promoFeedback" class="promo-feedback"></div>
        </div>

        <div class="summary-details">
            <div class="summary-row">
                <span>Subtotal</span>
                <span>Rs. <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${cartTotal}"/></span>
            </div>
            <div class="summary-row" id="summaryDiscountRow" style="display: none;">
                <span>Discount</span>
                <span id="summaryDiscount">- Rs. 0.00</span>
            </div>
            <div class="summary-row total">
                <span>Total</span>
                <span id="summaryTotal">Rs. <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${cartTotal}"/></span>
            </div>
        </div>

        <button type="submit" form="paymentForm" class="btn btn-primary btn-checkout">
            <i class="fas fa-lock"></i> Place Order
        </button>
    </div>
</div>

<jsp:include page="includes/footer.jsp"/>

<script>
    // Pass context path to JS files
    window.contextPath = "${pageContext.request.contextPath}";

    function togglePaymentDetails() {
        const cardDetails = document.getElementById('cardDetails');
        const onlineDetails = document.getElementById('onlineBankingDetails');
        if (document.getElementById('cardPayment').checked) {
            cardDetails.style.display = 'block';
            onlineDetails.style.display = 'none';
        } else {
            cardDetails.style.display = 'none';
            onlineDetails.style.display = 'block';
        }
    }
</script>
<script src="${pageContext.request.contextPath}/js/order-details.js"></script>

</body>
</html>