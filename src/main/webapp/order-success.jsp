<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order Successful!</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
<jsp:include page="/includes/navbar.jsp"/>
<div class="container text-center my-5">
    <div class="p-5 border rounded" style="background-color: var(--card-bg);">
        <i class="fas fa-check-circle fa-4x text-success mb-3"></i>
        <h1 class="display-5">Thank You For Your Order!</h1>
        <p class="lead">Your payment was successful and your order has been processed.</p>
        <hr class="my-4">
        <p>You can now access your purchased music in your library.</p>
        <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/my-music" role="button">Go to My
            Music</a>
        <a class="btn btn-outline-light btn-lg" href="${pageContext.request.contextPath}/index" role="button">Continue
            Shopping</a>
    </div>
</div>
<jsp:include page="/includes/footer.jsp"/>
</body>
</html>