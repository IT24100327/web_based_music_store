<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RhythmWave - Order Details</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <style>
        body {
            background-color: var(--dark-bg);
            color: var(--text-primary);
        }
        .order-container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 2rem 1rem;
        }
        .order-header {
            background-color: var(--card-bg);
            border-radius: var(--border-radius);
            padding: 2rem;
            margin-bottom: 2rem;
            border: 1px solid #333;
            text-align: center;
        }
        .order-header h1 {
            color: var(--text-primary);
            margin-bottom: 0.5rem;
        }
        .order-header p {
            color: var(--text-secondary);
        }
        .cart-items-section, .order-summary {
            background-color: var(--card-bg);
            border-radius: var(--border-radius);
            padding: 2rem;
            margin-bottom: 2rem;
            border: 1px solid #333;
        }
        .cart-item {
            display: flex;
            align-items: center;
            padding: 1rem 0;
            border-bottom: 1px solid #333;
        }
        .cart-item:last-child {
            border-bottom: none;
        }
        .item-image {
            width: 60px;
            height: 60px;
            border-radius: 4px;
            background-color: #333;
            margin-right: 1rem;
            display: flex;
            align-items: center;
            justify-content: center;
            color: var(--primary);
        }
        .item-details h5 {
            margin-bottom: 0.25rem;
            color: var(--text-primary);
        }
        .item-details p {
            margin-bottom: 0;
            color: var(--text-secondary);
            font-size: var(--font-size-small);
        }
        .item-price {
            margin-left: auto;
            font-weight: bold;
            color: var(--primary);
        }
        .summary-row {
            display: flex;
            justify-content: space-between;
            padding: 0.5rem 0;
        }
        .summary-row.total {
            border-top: 1px solid #333;
            font-weight: bold;
            font-size: var(--font-size-subheading);
        }
        .btn-checkout {
            background-color: var(--primary);
            border-color: var(--primary);
            color: #000;
            font-weight: 500;
        }
        .btn-checkout:hover {
            background-color: var(--secondary);
            border-color: var(--secondary);
            color: #000;
        }
        .empty-cart {
            text-align: center;
            padding: 3rem;
            color: var(--text-secondary);
        }
        .empty-cart i {
            font-size: 4rem;
            margin-bottom: 1rem;
            color: #333;
        }
    </style>
</head>
<body>
<jsp:include page="/includes/navbar.jsp" />

<div class="order-container">
    <div class="order-header">
        <h1><i class="fas fa-shopping-cart me-2"></i>Order Details</h1>
        <p>Review your selected tracks before proceeding to checkout</p>
    </div>

    <c:choose>
        <c:when test="${empty cartItems or fn:length(cartItems) == 0}">
            <div class="empty-cart">
                <i class="fas fa-shopping-cart"></i>
                <h3>Your cart is empty</h3>
                <p>Add some tracks to get started!</p>
                <a href="${pageContext.request.contextPath}/index" class="btn btn-primary">Continue Shopping</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="cart-items-section">
                <h3>Cart Items</h3>
                <c:set var="totalAmount" value="0" />
                <c:forEach var="track" items="${cartItems}">
                    <div class="cart-item">
                        <div class="item-image">
                            <i class="fas fa-music"></i>
                        </div>
                        <div class="item-details">
                            <h5>${track.title}</h5>
                            <p>by ${track.artist}</p>
                        </div>
                        <div class="item-price">Rs. ${track.price}</div>
                    </div>
                    <c:set var="totalAmount" value="${cartTotal}" />
                </c:forEach>
            </div>

            <div class="order-summary">
                <h3>Order Summary</h3>
                <div class="summary-row">
                    <span>Subtotal</span>
                    <span>Rs. ${cartTotal}</span>
                </div>
                <div class="summary-row">
                    <span>Shipping</span>
                    <span>Free</span>
                </div>
                <div class="summary-row total">
                    <span>Total</span>
                    <span>Rs. ${cartTotal}</span>
                </div>
                <div class="d-grid gap-2 mt-3">
                    <form action="${pageContext.request.contextPath}/order" method="post">
                        <input type="hidden" name="paymentMethod" value="CARD">
                        <input type="hidden" name="transactionId" value="TX12345">
                    <button type="submit">Place Order</button>
                    </form>
                    <a href="${pageContext.request.contextPath}/index" class="btn btn-outline-light">Continue Shopping</a>
                </div>

                <

            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Add any client-side functionality here if needed
    });
</script>
</body>
</html>