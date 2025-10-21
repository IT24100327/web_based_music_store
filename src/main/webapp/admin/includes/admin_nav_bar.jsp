<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="page" value="${param.page}"/>

<nav class="admin-sidebar">
    <div class="sidebar-header">
        <h2>RhythmWave Admin</h2>
    </div>
    <div class="sidebar-nav">
        <c:if test="${sessionScope.USER != null}">
            <a href="${pageContext.request.contextPath}/admin/index.jsp" class="${empty page ? 'active' : ''}">
                <i class="fas fa-tachometer-alt"></i>
                Dashboard
            </a>

            <c:if test="${sessionScope.USER.role != null && sessionScope.USER.role.name() eq 'SUPER_ADMIN'}">
                <a href="${pageContext.request.contextPath}/manage-users"
                   class="${page eq 'manage-users' ? 'active' : ''}">
                    <i class="fas fa-users-cog"></i>
                    User Management
                </a>
            </c:if>

            <c:if test="${sessionScope.USER.role != null && sessionScope.USER.role.name() eq 'SUPER_ADMIN'}">
                <a href="${pageContext.request.contextPath}/manage-artists"
                   class="${page eq 'manage-artists' ? 'active' : ''}">
                    <i class="fas fa-users-cog"></i>
                    Artist Management
                </a>
            </c:if>

            <c:if test="${sessionScope.USER.role != null && (sessionScope.USER.role.name() eq 'SUPER_ADMIN' ||
       sessionScope.USER.role.name() eq 'FINANCE_MANAGER')}">
                <a href="${pageContext.request.contextPath}/manageOrders"
                   class="${page eq 'manageOrders' ? 'active' : ''}">
                    <i class="fas fa-users-cog"></i>
                    Order Management
                </a>
            </c:if>

            <c:if test="${sessionScope.USER.role != null && (sessionScope.USER.role.name() eq 'SUPER_ADMIN' ||
       sessionScope.USER.role.name eq 'MARKETING_MANAGER')}">
                <a href="${pageContext.request.contextPath}/manage-marketing"
                   class="${page eq 'manage-marketing' ? 'active' : ''}">
                    <i class="fas fa-chart-bar"></i>
                    Marketing Management
                </a>
            </c:if>

            <c:if test="${sessionScope.USER.role != null && (sessionScope.USER.role.name() eq 'SUPER_ADMIN' ||
       sessionScope.USER.role.name eq 'CONTENT_MANAGER')}">
                <a href="${pageContext.request.contextPath}/admin/manage-posts"
                   class="${page eq 'manage-posts' ? 'active' : ''}">
                    <i class="fas fa-chart-bar"></i>
                    Community Management
                </a>
            </c:if>

            <a href="${pageContext.request.contextPath}/logout" class="${page eq 'logout' ? 'active' : ''}">
                <i class="fas fa-sign-out-alt"></i>
                Logout
            </a>
        </c:if>
    </div>
</nav>