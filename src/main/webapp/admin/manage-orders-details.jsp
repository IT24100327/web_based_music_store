<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin - Order Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
</head>
<body>
<div class="admin-container">
    <jsp:include page="includes/admin_nav_bar.jsp"/>
    <main class="admin-main">
        <header class="admin-header">
            <h1>Order Details: #${order.orderId}</h1>
        </header>
        <div class="row">
            <div class="col-md-6">
                <div class="table-container">
                    <h3>Order Summary</h3>
                    <p><strong>Order ID:</strong> ${order.orderId}</p>
                    <p><strong>Order Date:</strong> ${order.orderDate}</p>
                    <p><strong>Total Amount:</strong> Rs. ${order.totalAmount}</p>
                    <p><strong>Status:</strong> <span class="status-badge status-active">${order.status}</span></p>
                    <p><strong>Payment Method:</strong> ${order.paymentMethod}</p>
                    <p><strong>Transaction ID:</strong> ${order.transactionId}</p>
                </div>
            </div>
            <div class="col-md-6">
                <div class="table-container">
                    <h3>Customer Details</h3>
                    <p><strong>User ID:</strong> ${customer.userId}</p>
                    <p><strong>Name:</strong> ${customer.firstName} ${customer.lastName}</p>
                    <p><strong>Email:</strong> ${customer.email}</p>
                </div>
            </div>
        </div>
        <div class="table-container">
            <h3>Tracks in this Order</h3>
            <table class="table">
                <thead>
                <tr>
                    <th>Track ID</th>
                    <th>Title</th>
                    <th>Artist</th>
                    <th>Price</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="track" items="${tracks}">
                    <tr>
                        <td>${track.trackId}</td>
                        <td>${track.title}</td>
                        <td>${track.artistName}</td>
                        <td>Rs. ${track.price}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</div>
</body>
</html>