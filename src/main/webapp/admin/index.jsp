<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
</head>
<body>
<div class="admin-container">
    <jsp:include page="includes/admin_nav_bar.jsp"/>

    <main class="admin-main">
        <header class="admin-header">
            <h1>RhythmWave Admin Dashboard</h1>
            <div class="user-info">
                <div class="user-avatar"><c:out value="${fn:substring(sessionScope.USER.firstName, 0, 1)}"/></div>
                <div>
                    <div>${sessionScope.USER.firstName} ${sessionScope.USER.lastName}</div>
                    <div class="text-muted">${sessionScope.USER.email}</div>
                </div>
            </div>
        </header>

        <section class="welcome-section">
            <div class="welcome-icon">
                <i class="fas fa-music"></i>
            </div>
            <div class="welcome-content">
                <h2>Welcome back, ${sessionScope.USER.firstName}!</h2>
                <p>Manage your music platform efficiently with the RhythmWave Admin Panel.</p>
            </div>
        </section>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-users"></i></div>
                <div class="stat-content">
                    <h3></h3>
                    <p>Total Users</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-microphone-alt"></i></div>
                <div class="stat-content">
                    <h3></h3>
                    <p>Total Artists</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-tags"></i></div>
                <div class="stat-content">
                    <h3></h3>
                    <p>Active Promotions</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-ad"></i></div>
                <div class="stat-content">
                    <h3></h3>
                    <p>Running Ads</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon"><i class="fas fa-dollar-sign"></i></div>
                <div class="stat-content">
                    <h3></h3>
                    <p>Total Revenue</p>
                </div>
            </div>
        </div>

        <div class="dashboard-grid">

            <%-- Super Admin Access --%>
            <c:if test="${sessionScope.USER.role.name() eq 'SUPER_ADMIN'}">

                <div class="dashboard-card">
                    <div class="card-icon"><i class="fas fa-users-cog"></i></div>
                    <h3 class="card-title">Admin Management</h3>
                    <p class="card-description">Oversee all admin accounts, roles, and permissions.</p>
                    <a href="<%=request.getContextPath()%>/manage-admins" class="card-link">Manage Admins <i class="fas fa-arrow-right"></i></a>
                </div>

                <div class="dashboard-card">
                    <div class="card-icon"><i class="fas fa-users-cog"></i></div>
                    <h3 class="card-title">User Management</h3>
                    <p class="card-description">Oversee all user accounts, roles, and permissions.</p>
                    <a href="<%=request.getContextPath()%>/manage-users" class="card-link">Manage Users <i class="fas fa-arrow-right"></i></a>
                </div>

                <div class="dashboard-card">
                    <div class="card-icon"><i class="fas fa-microphone-alt"></i></div>
                    <h3 class="card-title">Artist Management</h3>
                    <p class="card-description">Add, edit, and manage artist profiles and content.</p>
                    <a href="<%=request.getContextPath()%>/manage-artists" class="card-link">Manage Artists <i class="fas fa-arrow-right"></i></a>
                </div>

            </c:if>

            <%-- Marketing Access --%>
            <c:if test="${sessionScope.USER.role.name() eq 'MARKETING_MANAGER' or sessionScope.USER.role.name() eq 'SUPER_ADMIN'}">
                <div class="dashboard-card">
                    <div class="card-icon"><i class="fas fa-bullhorn"></i></div>
                    <h3 class="card-title">Marketing Management</h3>
                    <p class="card-description">Launch ad campaigns and manage promotional codes.</p>
                    <a href="<%=request.getContextPath()%>/manage-marketing" class="card-link">Manage Marketing <i class="fas fa-arrow-right"></i></a>
                </div>
            </c:if>

            <%-- Content Manager Access --%>
            <c:if test="${sessionScope.USER.role.name() eq 'CONTENT_MANAGER' or sessionScope.USER.role.name() eq 'SUPER_ADMIN'}">
                <div class="dashboard-card">
                    <div class="card-icon"><i class="fas fa-music"></i></div>
                    <h3 class="card-title">Track Management</h3>
                    <p class="card-description">Approve, reject, and manage all uploaded tracks.</p>
                    <a href="${pageContext.request.contextPath}/manage-tracks" class="card-link">Manage Tracks <i class="fas fa-arrow-right"></i></a>
                </div>
                <div class="dashboard-card">
                    <div class="card-icon"><i class="fas fa-comments"></i></div>
                    <h3 class="card-title">Community Management</h3>
                    <p class="card-description">Review and approve user-submitted community posts.</p>
                    <a href="${pageContext.request.contextPath}/admin/manage-posts" class="card-link">Manage Posts <i class="fas fa-arrow-right"></i></a>
                </div>
            </c:if>

            <%-- Finance Manager Access --%>
            <c:if test="${sessionScope.USER.role.name() eq 'FINANCE_MANAGER' or sessionScope.USER.role.name() eq 'SUPER_ADMIN'}">
                <div class="dashboard-card">
                    <div class="card-icon"><i class="fas fa-receipt"></i></div>
                    <h3 class="card-title">Order Management</h3>
                    <p class="card-description">View and process all customer orders and transactions.</p>
                    <a href="${pageContext.request.contextPath}/manage-orders" class="card-link">Manage Orders <i class="fas fa-arrow-right"></i></a>
                </div>
                <div class="dashboard-card">
                    <div class="card-icon"><i class="fas fa-credit-card"></i></div>
                    <h3 class="card-title">Payment Management</h3>
                    <p class="card-description">Track all payments and review financial analytics.</p>
                    <a href="${pageContext.request.contextPath}/manage-payments" class="card-link">Manage Payments <i class="fas fa-arrow-right"></i></a>
                </div>
            </c:if>

            <%-- Support & Feedback (Assuming accessible by Marketing/Super Admin) --%>
            <c:if test="${sessionScope.USER.role.name() eq 'MARKETING_MANAGER' or sessionScope.USER.role.name() eq 'SUPER_ADMIN'}">
                <div class="dashboard-card">
                    <div class="card-icon"><i class="fas fa-comment-dots"></i></div>
                    <h3 class="card-title">Feedback Management</h3>
                    <p class="card-description">Review and respond to user feedback submissions.</p>
                    <a href="${pageContext.request.contextPath}/admin/manage-feedback" class="card-link">Manage Feedback <i class="fas fa-arrow-right"></i></a>
                </div>
            </c:if>

                <c:if test="${sessionScope.USER.role.name() eq 'SUPPORT_MANAGER' or sessionScope.USER.role.name() eq 'SUPER_ADMIN'}">
                    <div class="dashboard-card">
                        <div class="card-icon"><i class="fas fa-headset"></i></div>
                        <h3 class="card-title">Support Management</h3>
                        <p class="card-description">Address and resolve all user support requests.</p>
                        <a href="${pageContext.request.contextPath}/admin/manage-support-tickets" class="card-link">Manage Support <i class="fas fa-arrow-right"></i></a>
                    </div>
                </c:if>

            <div class="dashboard-card">
                <div class="card-icon"><i class="fas fa-cog"></i></div>
                <p class="card-description">Configure your personal admin settings.</p>
                <a href="#" class="card-link">Open Settings <i class="fas fa-arrow-right"></i></a>
            </div>
        </div>

        <footer class="admin-footer">
            &copy; 2025 RhythmWave | Admin Panel
        </footer>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>