<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotel Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
</head>
<body>
<div class="admin-container">
    <!-- Sidebar Navigation -->
    <jsp:include page="includes/admin_nav_bar.jsp" />

    <!-- Main Content Area -->
    <main class="admin-main">
        <header class="admin-header">
            <h1>RhythmWave Admin Dashboard</h1>
            <div class="user-info">
                <div class="user-avatar"><c:out value="${fn:substring(sessionScope.USER.firstName, 0, 1)}" /></div>
                <div>
                    <div>${sessionScope.USER.firstName} ${sessionScope.USER.lastName}</div>
                    <div class="text-muted">${sessionScope.USER.email}</div>
                </div>
            </div>
        </header>

        <!-- Welcome Section -->
        <section class="welcome-section">
            <div class="welcome-icon">
                <i class="fas fa-music"></i>
            </div>
            <div class="welcome-content">
                <h2>Welcome back, ${sessionScope.USER.firstName}!</h2>
                <p>Manage your music platform efficiently with the RhythmWave Admin Panel.</p>
            </div>
        </section>

        <!-- Stats Section -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fas fa-users"></i>
                </div>
                <div class="stat-content">
                    <h3>1,254</h3>
                    <p>Total Users</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fas fa-tag"></i>
                </div>
                <div class="stat-content">
                    <h3>24</h3>
                    <p>Active Promotions</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fas fa-ad"></i>
                </div>
                <div class="stat-content">
                    <h3>12</h3>
                    <p>Running Ads</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">
                    <i class="fas fa-chart-line"></i>
                </div>
                <div class="stat-content">
                    <h3>+8.2%</h3>
                    <p>Revenue Growth</p>
                </div>
            </div>
        </div>

        <!-- Dashboard Cards -->
        <div class="dashboard-grid">

            <c:if test="${sessionScope.USER.role.roleName eq 'super_admin'}">

            <!-- User Management Card -->
            <div class="dashboard-card card-user">
                <div class="card-icon">
                    <i class="fas fa-users-cog"></i>
                </div>
                <h3 class="card-title">User Management</h3>
                <p class="card-description">Manage users, roles, and permissions</p>
                <a href="<%=request.getContextPath()%>/manageUsers" class="card-link">
                    Manage Users <i class="fas fa-arrow-right"></i>
                </a>
            </div>

            </c:if>

            <c:if test="${sessionScope.USER.role.roleName eq 'marketing_manager'
            or sessionScope.USER.role.roleName eq 'super_admin'}">

                <!-- Marketing Management Card -->
                <div class="dashboard-card card-user">
                    <div class="card-icon">
                        <i class="fas fa-chart-bar"></i>
                    </div>
                    <h3 class="card-title">Marketing Management</h3>
                    <p class="card-description">Create promotions and advertisements</p>
                    <a href="<%=request.getContextPath()%>/marketing" class="card-link">
                        Manage Marketing <i class="fas fa-arrow-right"></i>
                    </a>
                </div>

            </c:if>

            <c:if test="${sessionScope.USER.role.roleName eq 'finance_manager'
            or sessionScope.USER.role.roleName eq 'super_admin'}">

            <!-- Content Management Card -->
            <div class="dashboard-card">
                <div class="card-icon">
                    <i class="fas fa-music"></i>
                </div>
                <h3 class="card-title">Content Management</h3>
                <p class="card-description">Manage songs, albums, and playlists</p>
                <a href="#" class="card-link">
                    Manage Content <i class="fas fa-arrow-right"></i>
                </a>
            </div>

            </c:if>

            <c:if test="${sessionScope.USER.role.roleName eq 'finance_manager'
            or sessionScope.USER.role.roleName eq 'super_admin'}">

                <!-- Content Management Card -->
                <div class="dashboard-card">
                    <div class="card-icon">
                        <i class="fa-solid fa-list"></i>
                    </div>
                    <h3 class="card-title">Order Management</h3>
                    <p class="card-description">Manage Orders, Payments, and Analytics</p>
                    <a href="${pageContext.request.contextPath}/manageOrders" class="card-link">
                        Manage Orders <i class="fas fa-arrow-right"></i>
                    </a>
                </div>

            </c:if>

            <!-- Settings Card -->
            <div class="dashboard-card">
                <div class="card-icon">
                    <i class="fas fa-cog"></i>
                </div>
                <h3 class="card-title">Settings</h3>
                <p class="card-description">Configure system settings</p>
                <a href="#" class="card-link">
                    Open Settings <i class="fas fa-arrow-right"></i>
                </a>
            </div>
        </div>

        <footer class="admin-footer">
            &copy; 2023 RhythmWave | Admin Panel
        </footer>
    </main>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>