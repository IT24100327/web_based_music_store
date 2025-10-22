<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Orders</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
<jsp:include page="/includes/navbar.jsp"/>

<div class="container my-5">
    <div class="order-header">
        <h1><i class="fas fa-history me-2"></i>My Order History</h1>
        <p>Review your past purchases and their status.</p>
    </div>

    <div class="table-responsive"
         style="background-color: var(--card-bg); border-radius: var(--border-radius); padding: 1rem;">
        <table class="table table-dark table-hover">
            <thead>
            <tr>
                <th>Order ID</th>
                <th>Date</th>
                <th>Total</th>
                <th>Status</th>
                <th>Transaction ID</th>
                <th>Actions</th>

            </tr>
            </thead>
            <tbody>
            <c:forEach var="order" items="${userOrders}">
                <tr>
                    <td>#${order.orderId}</td>
                    <td>${order.orderDate}</td>
                    <td>Rs. ${order.totalAmount}</td>
                    <td><span class="status-badge status-active">${order.status}</span></td>
                    <td>${order.transactionId}</td>
                    <td>
                            <%-- Add this link --%>
                        <a href="${pageContext.request.contextPath}/SupportTicketServlet?action=showCreateForm&orderId=${order.orderId}" class="btn btn-sm btn-outline-primary">
                            <i class="fas fa-headset"></i> Get Support
                        </a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty userOrders}">
                <tr>
                    <td colspan="5" class="text-center text-muted py-4">You have not placed any orders yet.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/includes/footer.jsp"/>
</body>
</html>