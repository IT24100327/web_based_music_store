<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="activePage" value="${param.page}" />

<!-- Clean Navigation Bar -->
<nav class="navbar navbar-expand-lg navbar-dark sticky-top">
  <div class="container-fluid">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/index">
      <i class="fas fa-music me-2"></i>RhythmWave
    </a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav me-auto">
        <li class="nav-item">
          <a class="nav-link ${activePage eq 'index' ? 'active' : ''}" href="${pageContext.request.contextPath}/index">Home</a>
        </li>
        <li class="nav-item">
          <a class="nav-link ${activePage eq 'search' ? 'active' : ''}" href="${pageContext.request.contextPath}/search">Search</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#">Genres</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#">New Releases</a>
        </li>
      </ul>

      <!-- Search Bar -->
      <c:if test="${!(param.searchBox eq 'noShow')}">
        <form action="${pageContext.request.contextPath}/search" method="get" class="input-group">
          <input type="text" class="form-control" name="query" placeholder="Search music..." value="${param.query}">
          <button class="btn" type="submit">
            <i class="fas fa-search"></i>
          </button>
        </form>
      </c:if>

      <div class="navbar-actions">
        <c:choose>
          <c:when test="${empty sessionScope.USER}">
            <div class="user-actions">
              <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-outline-light btn-sm">Login</a>
              <a href="${pageContext.request.contextPath}/signup.jsp" class="btn btn-primary btn-sm">Sign Up</a>
            </div>
          </c:when>
          <c:otherwise>
            <button type="button" class="btn cart-btn-nav" data-bs-toggle="modal" data-bs-target="#shoppingCartModal">
              <i class="fas fa-shopping-cart"></i>
              <c:set var="cartItemCount" value="${sessionScope.cartItems != null ? sessionScope.cartItems.size() : 0}"/>
              <span class="cart-badge" style="display: ${cartItemCount > 0 ? 'flex' : 'none'};">${cartItemCount}</span>
            </button>

            <c:choose>
              <c:when test="${sessionScope.USER.isAdmin() eq true}">
                <a href="${pageContext.request.contextPath}/admin/" class="btn btn-outline-light btn-sm">
                  <i class="fa-solid fa-user-shield me-1"></i>Admin
                </a>
              </c:when>
              <c:otherwise>
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline-light btn-sm">
                  <i class="fa-solid fa-user me-1"></i>Profile
                </a>
              </c:otherwise>
            </c:choose>

            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm">Logout</a>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </div>
</nav>