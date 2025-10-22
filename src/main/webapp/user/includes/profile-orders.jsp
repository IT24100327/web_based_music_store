<%-- /WEB-INF/views/profile-orders.jsp --%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<div class="table-container">
    <div class="table-header"><h3>My Order History</h3></div>
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
        <c:if test="${empty userOrders}">
            <tr>
                <td colspan="5" class="text-center text-muted py-4">You have not placed any orders yet.</td>
            </tr>
        </c:if>
        <c:forEach var="order" items="${userOrders}">
            <tr>
                <td>${order.orderId}</td>
                <td>${order.orderDate}</td>
                <td>Rs. ${order.finalAmount}</td>
                <td>${order.status}</td>
                <td>${order.transactionId}</td>
                <td>
                        <%-- Add this link --%>
                    <a href="${pageContext.request.contextPath}/SupportTicketServlet?action=showCreateForm&orderId=${order.orderId}" class="btn btn-sm btn-outline-primary">
                        <i class="fas fa-headset"></i> Get Support
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>