<%-- /main/webapp/admin/manage-orders.jsp --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Orders</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
</head>
<body>
<div class="admin-container">
    <jsp:include page="/admin/includes/admin_nav_bar.jsp">
        <jsp:param name="activePage" value="manage-orders"/>
    </jsp:include>

    <main class="admin-main">
        <header class="admin-header">
            <h1>Manage Orders</h1>
            <div class="user-info">
                <span>Welcome, ${sessionScope.USER.firstName}</span>
            </div>
        </header>

        <div class="table-container">
            <div class="table-header">
                <h3>All Customer Orders</h3>
                <form action="manage-orders" method="GET" class="d-flex">
                    <input class="form-control me-2" type="search" name="searchQuery" placeholder="Search by ID or Status" aria-label="Search">
                    <button class="btn btn-primary" type="submit">Search</button>
                </form>
            </div>

            <table>
                <thead>
                <tr>
                    <th>Order ID</th>
                    <th>User ID</th>
                    <th>Date</th>
                    <th>Final Amount</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="order" items="${allOrders}">
                    <tr>
                        <td>#${order.orderId}</td>
                        <td>${order.userId}</td>
                        <td><fmt:formatDate value="${order.getOrderDateAsUtilDate()}" pattern="MMM dd, yyyy 'at' HH:mm"/></td>
                        <td>Rs. <fmt:formatNumber value="${order.finalAmount}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td>
                            <span class="status-badge status-${order.status.name().toLowerCase()}">${order.status.name()}</span>
                        </td>
                        <td class="actions">
                            <a href="${pageContext.request.contextPath}/admin/orderDetails?orderId=${order.orderId}" class="btn btn-secondary btn-sm">Details</a>

                            <c:if test="${order.status == 'COMPLETED'}">
                                <form action="manage-orders" method="POST" style="display: inline-block;">
                                    <input type="hidden" name="action" value="refund">
                                    <input type="hidden" name="orderId" value="${order.orderId}">
                                    <button type="submit" class="btn btn-info btn-sm">Refund</button>
                                </form>
                            </c:if>

                            <form action="manage-orders" method="POST" style="display: inline-block;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="orderId" value="${order.orderId}">
                                <button type="submit" class="btn btn-delete btn-sm">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>