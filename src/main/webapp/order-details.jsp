<%-- /webapp/order-details.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout | Your Music Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order-details.css">
    <script>
        // Pass context path to JavaScript
        window.contextPath = "${pageContext.request.contextPath}";
    </script>
</head>
<body>

<jsp:include page="/includes/navbar.jsp"/>

<main class="order-container">
    <c:choose>
        <c:when test="${not empty cartItems}">
            <section class="order-details-section">
                <div class="order-details-header">
                    <h1>Review Your Order</h1>
                    <p>Confirm the items in your cart before proceeding to checkout.</p>
                </div>

                <div class="cart-items-list">
                    <c:forEach var="item" items="${cartItems}">
                        <div class="cart-item">
                            <img src="${pageContext.request.contextPath}/cover-art?trackId=${item.trackId}"
                                 alt="Cover for <c:out value='${item.title}'/>" class="item-image">
                            <div class="item-details">
                                <h5><c:out value="${item.title}"/></h5>
                                <p>by <c:out value="${item.artistName}"/></p>
                            </div>
                            <div class="item-price">
                                Rs. <fmt:formatNumber value="${item.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </section>

            <aside class="order-summary-card" data-subtotal="${cartTotal}">
                <h3>Order Summary</h3>

                <div class="promo-section">
                    <label for="promoCodeField" class="form-label">Gift card or discount code</label>
                    <div class="promo-input-group">
                        <input type="text" id="promoCodeField" class="form-control" placeholder="Enter code">
                        <button class="btn btn-outline-light" type="button" id="applyPromoBtn">Apply</button>
                    </div>
                    <div id="promoFeedback" class="promo-feedback"></div>
                </div>

                <div class="summary-details">
                    <div class="summary-row">
                        <span>Subtotal</span>
                        <span id="summarySubtotal">Rs. <fmt:formatNumber value="${cartTotal}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span>
                    </div>
                    <div class="summary-row" id="summaryDiscountRow" style="display: none;">
                        <span>Discount</span>
                        <span id="summaryDiscount"></span>
                    </div>
                    <div class="summary-row total">
                        <span>Total</span>
                        <span id="summaryTotal">Rs. <fmt:formatNumber value="${cartTotal}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span>
                    </div>
                </div>

                <form id="orderForm" action="${pageContext.request.contextPath}/order" method="POST">
                    <input type="hidden" id="promoCodeInput" name="promoCode" value="">
                    <button type="submit" class="btn btn-checkout w-100">
                        Proceed to Checkout <i class="fas fa-arrow-right ms-2"></i>
                    </button>
                </form>
            </aside>
        </c:when>

        <c:otherwise>
            <div class="empty-cart-container" style="grid-column: 1 / -1;">
                <i class="fas fa-shopping-cart"></i>
                <h1>Your Cart is Empty</h1>
                <p>Looks like you haven't added any music yet. Let's find something great!</p>
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Browse Music</a>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<jsp:include page="/includes/footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/order-details.js"></script>

</body>
</html>