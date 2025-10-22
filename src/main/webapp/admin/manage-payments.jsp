<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RhythmWave Admin - Payment Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
</head>
<body>
<div class="admin-container">
    <jsp:include page="includes/admin_nav_bar.jsp">
        <jsp:param name="page" value="manage-payments"/>
    </jsp:include>

    <main class="admin-main">
        <header class="admin-header">
            <h1>Payment Management</h1>
            <div class="user-info">
                <div class="user-avatar">${sessionScope.USER.firstName.substring(0,1)}</div>
                <div>
                    <div>${sessionScope.USER.firstName} ${sessionScope.USER.lastName}</div>
                    <div class="text-muted">${sessionScope.USER.email}</div>
                </div>
            </div>
        </header>

        <section class="welcome-section">
            <div class="welcome-icon">
                <i class="fas fa-dollar-sign"></i>
            </div>
            <div class="welcome-content">
                <h2>Payment Management</h2>
                <p>Review all transactions, track payment statuses, and view payment details.</p>
            </div>
        </section>

        <div class="table-container">
            <div class="table-header">
                <h3>All Payments</h3>
            </div>

            <div class="mb-4">
                <form action="${pageContext.request.contextPath}/manage-payments" method="GET" class="d-flex gap-2">
                    <input type="text" name="searchQuery" class="form-control"
                           placeholder="Search by Payment ID, Order ID, or Transaction ID..." value="${param.searchQuery}">
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
            </div>

            <table>
                <thead>
                <tr>
                    <th>Payment ID</th>
                    <th>Order ID</th>
                    <th>Amount</th>
                    <th>Date</th>
                    <th>Method</th>
                    <th>Transaction ID</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty requestScope.allPayments}">
                        <c:forEach var="payment" items="${requestScope.allPayments}">
                            <tr>
                                <td>${payment.paymentId}</td>
                                <td>${payment.orderId}</td>
                                <td>Rs. <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${payment.amount}"/></td>
                                <td><fmt:formatDate value="${java.time.LocalDateTime.from(payment.paymentDate.toInstant().atZone(java.time.ZoneId.systemDefault()))}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td>${payment.paymentMethod}</td>
                                <td>${payment.transactionId}</td>
                                <td>
                                    <span class="status-badge ${payment.status == 'SUCCESS' ? 'status-active' : (payment.status == 'FAILED' ? 'status-inactive' : 'status-pending')}">
                                            ${payment.status}
                                    </span>
                                </td>
                                <td class="actions">
                                    <a href="${pageContext.request.contextPath}/admin/orderDetails?orderId=${payment.orderId}" class="btn btn-info btn-sm" title="View Associated Order">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" class="text-muted text-center py-4">No payments found.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>

        <footer class="admin-footer">
            &copy; 2025 RhythmWave | Admin Panel
        </footer>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>