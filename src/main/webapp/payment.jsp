<%-- /webapp/payment.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment | RhythmWave</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <style>
        /* === Dark Mode Background === */
        body {
            background: linear-gradient(135deg, #0a0a0a, #1a1a1a);
            color: #fff;
            font-family: 'Poppins', sans-serif;
            min-height: 100vh;
        }

        /* Container */
        .payment-container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
            background: rgba(255, 255, 255, 0.05);
            border-radius: 15px;
            backdrop-filter: blur(10px);
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
        }

        .payment-header {
            text-align: center;
            margin-bottom: 3rem;
        }

        .payment-header h1 {
            color: #fff;
            font-size: 2.5rem;
            margin-bottom: 0.5rem;
        }

        .payment-header p {
            color: #ccc;
            font-size: 1.1rem;
        }

        .payment-content {
            display: grid;
            grid-template-columns: 1fr 400px;
            gap: 2rem;
        }

        .order-summary, .payment-section {
            background: rgba(255, 255, 255, 0.08);
            border-radius: 15px;
            padding: 2rem;
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.2);
        }

        .order-summary h3, .payment-section h3 {
            color: #fff;
            margin-bottom: 1.5rem;
            font-size: 1.5rem;
        }

        .cart-item {
            display: flex;
            align-items: center;
            padding: 1rem 0;
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }

        .cart-item:last-child {
            border-bottom: none;
        }

        .item-image {
            width: 60px;
            height: 60px;
            border-radius: 8px;
            margin-right: 1rem;
            object-fit: cover;
        }

        .item-details h6 {
            color: #fff;
            margin: 0;
            font-size: 1rem;
        }

        .item-details p {
            color: #aaa;
            margin: 0;
            font-size: 0.9rem;
        }

        .item-price {
            color: #fff;
            font-weight: 600;
        }

        .payment-method label {
            color: #fff;
        }

        .payment-option label {
            display: block;
            padding: 1rem;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 10px;
            border: 2px solid transparent;
            text-align: center;
            cursor: pointer;
            color: #fff;
            transition: all 0.3s ease;
        }

        .payment-option input[type="radio"]:checked + label {
            background: rgba(138, 43, 226, 0.3);
            border-color: #8a2be2;
        }

        .payment-option label:hover {
            background: rgba(255, 255, 255, 0.2);
        }

        .payment-details {
            background: rgba(255, 255, 255, 0.05);
            border-radius: 10px;
            padding: 1.5rem;
            border: 1px solid rgba(255, 255, 255, 0.1);
        }

        .form-control {
            background: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.2);
            border-radius: 8px;
            color: #fff;
        }

        .form-control::placeholder {
            color: rgba(255, 255, 255, 0.6);
        }

        .total-section {
            background: rgba(138, 43, 226, 0.2);
            border-radius: 10px;
            padding: 1.5rem;
        }

        .total-row span {
            color: #fff;
        }

        .btn-pay {
            width: 100%;
            padding: 1rem;
            background: linear-gradient(135deg, #8a2be2, #9932cc);
            border: none;
            border-radius: 10px;
            color: #fff;
            font-size: 1.1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .btn-pay:hover {
            background: linear-gradient(135deg, #9932cc, #8a2be2);
            transform: translateY(-2px);
        }

        .error-message {
            background: rgba(220, 53, 69, 0.2);
            border: 1px solid #dc3545;
            color: #fff;
            padding: 1rem;
            border-radius: 10px;
            margin-bottom: 1rem;
        }

        @media (max-width: 768px) {
            .payment-content {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>

<jsp:include page="/includes/navbar.jsp"/>

<main class="payment-container">
    <div class="payment-header">
        <h1><i class="fas fa-credit-card me-2"></i>Select Payment Method</h1>
        <p>Choose your preferred payment method to complete your purchase</p>
    </div>

    <c:if test="${not empty error}">
        <div class="error-message">
            <i class="fas fa-exclamation-triangle me-2"></i>
            <c:out value="${error}"/>
        </div>
    </c:if>

    <div class="payment-content">
        <!-- Order Summary -->
        <div class="order-summary">
            <h3><i class="fas fa-shopping-cart me-2"></i>Order Summary</h3>
            <c:forEach var="item" items="${cartItems}">
                <div class="cart-item">
                    <img src="${pageContext.request.contextPath}/cover-art?trackId=${item.trackId}"
                         alt="Cover for <c:out value='${item.title}'/>" class="item-image">
                    <div class="item-details">
                        <h6><c:out value="${item.title}"/></h6>
                        <p>by <c:out value="${item.artistName}"/></p>
                    </div>
                    <div class="item-price">
                        Rs. <fmt:formatNumber value="${item.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Payment Section -->
        <div class="payment-section">
            <h3><i class="fas fa-lock me-2"></i>Payment Details</h3>
            <form id="paymentForm" action="${pageContext.request.contextPath}/process-payment" method="POST">
                <div class="payment-method">
                    <label>Select Payment Method</label>
                    <div class="payment-options">
                        <div class="payment-option">
                            <input type="radio" id="cardPayment" name="paymentMethod" value="CARD" required>
                            <label for="cardPayment"><i class="fas fa-credit-card"></i>Card Payment</label>
                        </div>
                        <div class="payment-option">
                            <input type="radio" id="onlinePayment" name="paymentMethod" value="ONLINE" required>
                            <label for="onlinePayment"><i class="fas fa-university"></i>Online Banking</label>
                        </div>
                    </div>
                </div>

                <!-- Card Details -->
                <div id="cardDetails" class="payment-details" style="display: none;">
                    <div class="form-group">
                        <label for="cardNumber">Card Number</label>
                        <input type="text" id="cardNumber" name="cardNumber" class="form-control" placeholder="1234 5678 9012 3456" maxlength="19">
                    </div>
                    <div class="form-row d-flex gap-3">
                        <div class="form-group flex-fill">
                            <label for="expiryDate">Expiry Date</label>
                            <input type="text" id="expiryDate" name="expiryDate" class="form-control" placeholder="MM/YY" maxlength="5">
                        </div>
                        <div class="form-group flex-fill">
                            <label for="cvv">CVV</label>
                            <input type="text" id="cvv" name="cvv" class="form-control" placeholder="123" maxlength="4">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="cardholderName">Cardholder Name</label>
                        <input type="text" id="cardholderName" name="cardholderName" class="form-control" placeholder="John Doe">
                    </div>
                </div>

                <!-- Online Banking -->
                <div id="onlineDetails" class="payment-details" style="display: none;">
                    <div class="form-group">
                        <label for="bankName">Bank Name</label>
                        <select id="bankName" name="bankName" class="form-control">
                            <option value="">Select Bank</option>
                            <option value="Commercial Bank">Commercial Bank</option>
                            <option value="People's Bank">People's Bank</option>
                            <option value="Sampath Bank">Sampath Bank</option>
                            <option value="Hatton National Bank">Hatton National Bank</option>
                            <option value="DFCC Bank">DFCC Bank</option>
                            <option value="NTB">NTB</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="accountNumber">Account Number</label>
                        <input type="text" id="accountNumber" name="accountNumber" class="form-control" placeholder="Enter your account number">
                    </div>
                    <div class="form-group">
                        <label for="accountHolderName">Account Holder Name</label>
                        <input type="text" id="accountHolderName" name="accountHolderName" class="form-control" placeholder="Account holder name">
                    </div>
                    <div class="form-group">
                        <label for="transferReference">Transfer Reference</label>
                        <input type="text" id="transferReference" name="transferReference" class="form-control" placeholder="Enter transfer reference">
                    </div>
                </div>

                <!-- Total Section -->
                <div class="total-section">
                    <div class="total-row d-flex justify-content-between">
                        <span>Subtotal</span>
                        <span>Rs. <fmt:formatNumber value="${cartTotal}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span>
                    </div>
                    <div class="total-row total d-flex justify-content-between">
                        <span>Total</span>
                        <span>Rs. <fmt:formatNumber value="${cartTotal}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span>
                    </div>
                </div>

                <button type="submit" class="btn-pay" id="payButton">
                    <i class="fas fa-lock me-2"></i>
                    Pay Rs. <fmt:formatNumber value="${cartTotal}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                </button>
            </form>
        </div>
    </div>
</main>

<jsp:include page="/includes/footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const cardPayment = document.getElementById('cardPayment');
        const onlinePayment = document.getElementById('onlinePayment');
        const cardDetails = document.getElementById('cardDetails');
        const onlineDetails = document.getElementById('onlineDetails');

        function togglePaymentDetails() {
            cardDetails.style.display = cardPayment.checked ? 'block' : 'none';
            onlineDetails.style.display = onlinePayment.checked ? 'block' : 'none';
        }

        cardPayment.addEventListener('change', togglePaymentDetails);
        onlinePayment.addEventListener('change', togglePaymentDetails);
    });
</script>

</body>
</html>
