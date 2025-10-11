<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RhythmWave - Order Details</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order-details.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbar.css">
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
                </c:forEach>
            </div>

            <div class="order-summary">
                <h3>Order Summary</h3>
                <c:set var="totalAmount" value="${cartTotal}" />
                <div class="summary-row">
                    <span>Subtotal</span>
                    <span>Rs. ${totalAmount}</span>
                </div>
                <div class="summary-row">
                    <span>Shipping</span>
                    <span>Free</span>
                </div>
                <div class="summary-row total">
                    <span>Total</span>
                    <span>Rs. ${totalAmount}</span>
                </div>
                <div class="d-grid gap-2 mt-3">
                    <form action="${pageContext.request.contextPath}/order" method="post">
                        <input type="hidden" name="paymentMethod" value="CARD">
                        <input type="hidden" name="transactionId" value="TX12345">
                        <button type="submit" class="btn btn-primary btn-checkout w-100">Place Order</button>
                    </form>
                    <a href="${pageContext.request.contextPath}/index" class="btn btn-outline-light">Continue Shopping</a>
                </div>
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