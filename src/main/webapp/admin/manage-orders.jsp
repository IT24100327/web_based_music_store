<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotel Admin - Order Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
</head>
<body>
<div class="admin-container">
    <!-- Sidebar Navigation -->
    <jsp:include page="includes/admin_nav_bar.jsp">
        <jsp:param name="page" value="manageOrders"/>
    </jsp:include>

    <!-- Main Content Area -->
    <main class="admin-main">
        <header class="admin-header">
            <h1>Order Management</h1>
            <div class="user-info">
                <div class="user-avatar">A</div>
                <div>
                    <div>${sessionScope.USER.firstName} ${sessionScope.USER.lastName}</div>
                    <div class="text-muted">${sessionScope.USER.email}</div>
                </div>
            </div>
        </header>

        <!-- Welcome Section -->
        <section class="welcome-section">
            <div class="welcome-icon">
                <i class="fas fa-shopping-cart"></i>
            </div>
            <div class="welcome-content">
                <h2>Order Management</h2>
                <p>Manage customer orders, update statuses, and handle cancellations for your platform.</p>
            </div>
        </section>

        <!-- Orders Section -->
        <div class="table-container">
            <div class="table-header">
                <h3>Manage Orders</h3>
                <div class="table-actions">
                    <!-- No add button for orders, as they are user-generated -->
                </div>
            </div>

            <%--Search--%>
            <div class="mb-4">
                <form action="${pageContext.request.contextPath}/manageOrders" method="GET" class="d-flex gap-2">
                    <input type="text" name="searchQuery" class="form-control"
                           placeholder="Search by Order ID, User ID, or Status..." value="${param.searchQuery}">
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
            </div>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>User ID</th>
                    <th>Total Amount</th>
                    <th>Status</th>
                    <th>Order Date</th>
                    <th>Payment Method</th>
                    <th>Transaction ID</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty requestScope.allOrders}">
                        <c:forEach var="order" items="${requestScope.allOrders}">
                            <tr>
                                <td>${order.orderId}</td>
                                <td>${order.userId}</td>
                                <td>$${order.totalAmount}</td>
                                <td>
                  <span class="status-badge ${order.status == 'PENDING' ? 'status-pending' : (order.status == 'COMPLETED' || order.status == 'DELIVERED') ? 'status-active' : (order.status == 'CANCELLED' ? 'status-inactive' : 'status-pending')}">
                          ${order.status}
                  </span>
                                </td>
                                <td>${order.orderDate}</td>
                                <td>${order.paymentMethod}</td>
                                <td>${order.transactionId}</td>
                                <td class="actions">
                                    <a href="${pageContext.request.contextPath}/admin/orderDetails?orderId=${order.orderId}"
                                       class="btn btn-info btn-sm">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                    <button class="btn btn-edit btn-sm"
                                            onclick="openEditOrderModal('${order.orderId}', '${order.status}')">
                                        <i class="fas fa-edit"></i>
                                    </button>
                                    <button class="btn btn-delete btn-sm"
                                            onclick="openDeleteOrderModal('${order.orderId}', 'Order #${order.orderId}')">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" class="text-muted text-center py-4">No orders found.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>

        <footer class="admin-footer">
            &copy; 2023 RhythmWave | Admin Panel
        </footer>
    </main>
</div>

<!-- Modals -->
<jsp:include page="includes/modals/manage-orders-modals/edit-order-modal.jsp">
    <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
</jsp:include>
<jsp:include page="includes/modals/manage-orders-modals/delete-order-modal.jsp">
    <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
</jsp:include>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Initialize Bootstrap modals
    const editOrderModal = new bootstrap.Modal(document.getElementById('editOrderModal'));
    const deleteOrderModal = new bootstrap.Modal(document.getElementById('deleteOrderModal'));

    function openEditOrderModal(orderId, status) {
        document.getElementById('editOrderId').value = orderId;
        document.getElementById('editStatus').value = status;
        editOrderModal.show();
    }

    function closeEditOrderModal() {
        editOrderModal.hide();
    }

    function openDeleteOrderModal(orderId, orderInfo) {
        document.getElementById('deleteOrderId').value = orderId;
        document.getElementById('deleteOrderInfo').textContent = orderInfo;
        deleteOrderModal.show();
    }

    function closeDeleteOrderModal() {
        deleteOrderModal.hide();
    }
</script>
</body>
</html>