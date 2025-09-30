<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="page" value="${param.page}" />

<nav class="admin-sidebar">
  <div class="sidebar-header">
    <h2>RhythmWave Admin</h2>
  </div>
  <div class="sidebar-nav">
    <a href="${pageContext.request.contextPath}/admin/index.jsp" class="${empty page ? 'active' : ''}">
      <i class="fas fa-tachometer-alt"></i>
      Dashboard
    </a>
    <a href="${pageContext.request.contextPath}/manageUsers" class="${page eq 'manageUsers' ? 'active' : ''}">
      <i class="fas fa-users-cog"></i>
      User Management
    </a>
    <a href="${pageContext.request.contextPath}/marketing" class="${page eq 'manageMarketing' ? 'active' : ''}">
      <i class="fas fa-chart-bar"></i>
      Marketing Management
    </a>
    <a href="${pageContext.request.contextPath}/LogoutServlet">
      <i class="fas fa-sign-out-alt"></i>
      Logout
    </a>
  </div>
</nav>