<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RhythmWave Music Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>

<!-- Navigation Bar -->
<jsp:include page="/includes/navbar.jsp">
    <jsp:param name="page" value="index"/>
    <jsp:param name="user" value="${sessionScope.USER}"/>
</jsp:include>

<!-- Banner Section -->
<div class="container-fluid">
    <div class="compact-banner">
        <div class="compact-banner-content">
            <c:choose>
                <c:when test="${empty sessionScope.USER}">
                    <h1>Discover Your Soundtrack</h1>
                    <p>Millions of songs at your fingertips</p>
                </c:when>
                <c:otherwise>
                    <h1>Welcome Back, ${sessionScope.USER.firstName}!</h1>
                    <p>Continue your musical journey</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<!-- Music Listing Section -->
<div class="main-content">
    <div class="section-header">
        <h2>Featured Music</h2>
        <div class="view-controls">
            <button class="view-btn active" data-view="grid" data-tooltip="Grid View">
                <i class="fas fa-th"></i>
            </button>
            <button class="view-btn" data-view="list" data-tooltip="List View">
                <i class="fas fa-list"></i>
            </button>
        </div>
    </div>
    <c:import url="/includes/track-cards.jsp" />
</div>

<!-- Footer -->
<jsp:include page="includes/footer.jsp" />

<!-- Shopping Cart Modal -->
<jsp:include page="includes/modals/shopping-cart-modal.jsp" />

<!-- Bootstrap JS -->
<script>
    window.contextPath = '${pageContext.request.contextPath}';
    window.initialCartState = {
        itemCount: ${sessionScope.cartItems != null ? sessionScope.cartItems.size() : 0},
        cartTotal: ${sessionScope.cartTotal != null ? sessionScope.cartTotal : 0}
    };
    // Add this line: Set total pages from server
    window.noOfPages = ${requestScope.noOfPages > 0 ? requestScope.noOfPages : 1};
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/cart-utils.js"></script>
<script src="${pageContext.request.contextPath}/js/cart-handlers.js"></script>
<script src="${pageContext.request.contextPath}/js/cart-main.js"></script>
<script src="${pageContext.request.contextPath}/js/music-pagination.js"></script>
<script src="${pageContext.request.contextPath}/js/music-handlers.js"></script>
<script src="${pageContext.request.contextPath}/js/music-main.js"></script>
</body>
</html>