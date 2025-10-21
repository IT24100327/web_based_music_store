<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order Failed</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
<jsp:include page="/includes/navbar.jsp"/>
<div class="container text-center my-5">
    <div class="p-5 border rounded" style="background-color: var(--card-bg);">
        <i class="fas fa-times-circle fa-4x text-danger mb-3"></i>
        <h1 class="display-5">Oops! Something Went Wrong.</h1>
        <p class="lead">We couldn't process your order at this time.</p>
        <c:if test="${not empty requestScope.error}">
            <p class="text-danger"><strong>Reason:</strong> ${requestScope.error}</p>
        </c:if>
        <hr class="my-4">
        <p>Please try again or return to your cart.</p>
        <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/orderDetails" role="button">Back to
            Cart</a>
        <a class="btn btn-outline-light btn-lg" href="${pageContext.request.contextPath}/index" role="button">Continue
            Shopping</a>
    </div>
</div>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>