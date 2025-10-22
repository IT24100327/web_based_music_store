<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="page" value="${param.page}"/>

<nav class="admin-sidebar">
    <div class="sidebar-header">
        <h2>RhythmWave Admin</h2>
    </div>
    <div class="sidebar-nav">
        <c:if test="${sessionScope.USER != null}">
            <a href="${pageContext.request.contextPath}/admin/index.jsp" class="${empty page ? 'active' : ''}">
                <i class="fas fa-tachometer-alt"></i> Dashboard
            </a>

            <%-- Super Admin Links --%>
            <c:if test="${sessionScope.USER.role != null && sessionScope.USER.role.name() eq 'SUPER_ADMIN'}">
                <a href="${pageContext.request.contextPath}/manage-users" class="${page eq 'manage-users' ? 'active' : ''}">
                    <i class="fas fa-users-cog"></i> User Management
                </a>
                <a href="${pageContext.request.contextPath}/manage-artists" class="${page eq 'manage-artists' ? 'active' : ''}">
                    <i class="fas fa-microphone-alt"></i> Artist Management
                </a>
            </c:if>

            <%-- Content Manager Links --%>
            <c:if test="${sessionScope.USER.role != null && (sessionScope.USER.role.name() eq 'SUPER_ADMIN' || sessionScope.USER.role.name() eq 'CONTENT_MANAGER')}">
                <a href="${pageContext.request.contextPath}/manage-tracks" class="${page eq 'manage-tracks' ? 'active' : ''}">
                    <i class="fas fa-music"></i> Track Management
                </a>
                <a href="${pageContext.request.contextPath}/admin/manage-posts" class="${page eq 'manage-posts' ? 'active' : ''}">
                    <i class="fas fa-comments"></i> Community
                    </a>
            </c:if>

            <%-- Finance Manager Links --%>
            <c:if test="${sessionScope.USER.role != null && (sessionScope.USER.role.name() eq 'SUPER_ADMIN' || sessionScope.USER.role.name() eq 'FINANCE_MANAGER')}">
                <a href="${pageContext.request.contextPath}/manage-orders" class="${page eq 'manage-orders' ? 'active' : ''}">
                    <i class="fas fa-receipt"></i> Order Management
                </a>
                <a href="${pageContext.request.contextPath}/manage-payments" class="${page eq 'manage-payments' ? 'active' : ''}">
                    <i class="fas fa-credit-card"></i> Payment Management
                </a>
            </c:if>

            <%-- Marketing Manager Links --%>
            <c:if test="${sessionScope.USER.role != null && (sessionScope.USER.role.name() eq 'SUPER_ADMIN' || sessionScope.USER.role.name() eq 'MARKETING_MANAGER')}">
                <a href="${pageContext.request.contextPath}/manage-marketing" class="${page eq 'manage-marketing' ? 'active' : ''}">
                    <i class="fas fa-bullhorn"></i> Marketing
                    </a>
                <a href="${pageContext.request.contextPath}/admin/manage-feedback" class="${page eq 'manage-feedback' ? 'active' : ''}">
                    <i class="fas fa-comment-dots"></i> Feedback
                    </a>
                <a href="${pageContext.request.contextPath}/admin/manage-support-tickets" class="${page eq 'manage-support-tickets' ? 'active' : ''}">
                    <i class="fas fa-headset"></i> Support
                </a>
            </c:if>

            <%-- Logout --%>
            <a href="${pageContext.request.contextPath}/logout">
                <i class="fas fa-sign-out-alt"></i> Logout
            </a>
        </c:if>
    </div>
</nav>