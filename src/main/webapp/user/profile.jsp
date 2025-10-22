<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Profile</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/community/community-styles.css">
</head>
<body>
<div class="admin-container">
    <nav class="admin-sidebar">
        <div class="sidebar-header">
            <h2>My Profile</h2>
        </div>
        <div class="sidebar-nav">
            <a href="${pageContext.request.contextPath}/profile?view=dashboard"
               class="${param.view == 'dashboard' or empty param.view ? 'active' : ''}">
                <i class="fas fa-tachometer-alt"></i> Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/profile?view=music"
               class="${param.view == 'music' ? 'active' : ''}">
                <i class="fas fa-compact-disc"></i> My Music
            </a>
            <a href="${pageContext.request.contextPath}/profile?view=orders"
               class="${param.view == 'orders' ? 'active' : ''}">
                <i class="fas fa-history"></i> Order History
            </a>
            <a href="${pageContext.request.contextPath}/profile?view=my-posts"
               class="${(param.view == 'my-posts' or param.view == 'create-post') ? 'active' : ''}">
                <i class="fas fa-newspaper"></i> My Posts
            </a>
            <a href="${pageContext.request.contextPath}/profile?view=settings"
               class="${param.view == 'settings' ? 'active' : ''}">
                <i class="fas fa-cog"></i> Settings
            </a>
            <hr style="border-color: var(--border-light);">
            <a href="${pageContext.request.contextPath}/index">
                <i class="fas fa-home"></i> Back to Main Site
            </a>
            <a href="${pageContext.request.contextPath}/logout">
                <i class="fas fa-sign-out-alt"></i> Logout
            </a>
        </div>
    </nav>

    <main class="admin-main">
        <header class="admin-header">
            <h1>Welcome, ${sessionScope.USER.firstName}!</h1>
            <div class="user-info">
                <div class="user-avatar"><c:out value="${fn:substring(sessionScope.USER.firstName, 0, 1)}"/></div>
            </div>
        </header>

        <%-- Dynamically render content based on 'view' parameter --%>
        <c:choose>
            <c:when test="${param.view == 'music'}">
                <jsp:include page="/user/includes/profile-music.jsp"/>
            </c:when>
            <c:when test="${param.view == 'orders'}">
                <jsp:include page="/user/includes/profile-orders.jsp"/>
            </c:when>
            <c:when test="${param.view == 'settings'}">
                <jsp:include page="/user/includes/profile-settings.jsp"/>
            </c:when>
            <c:when test="${param.view == 'my-posts'}">
                <jsp:include page="/user/includes/profile-my-posts.jsp"/>
            </c:when>
            <c:when test="${param.view == 'create-post'}">
                <jsp:include page="/user/includes/profile-create-post.jsp"/>
            </c:when>
            <c:otherwise>
                <jsp:include page="/user/includes/profile-dashboard.jsp"/>
            </c:otherwise>
        </c:choose>
    </main>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>