<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="activePage" value="${param.page}" />

<!-- Navigation Bar -->
<nav class="navbar navbar-expand-lg navbar-dark sticky-top mb-2">
  <div class="container-fluid">
    <a class="navbar-brand" href="#">
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
          <a class="nav-link ${activePage eq '' ? 'active' : ''}" href="#">Genres</a>
        </li>
        <li class="nav-item">
          <a class="nav-link ${activePage eq '' ? 'active' : ''}" href="#">New Releases</a>
        </li>
        <li class="nav-item">
          <a class="nav-link ${activePage eq '' ? 'active' : ''}" href="#">Top Charts</a>
        </li>
      </ul>

      <!-- Search Bar -->
      <c:if test="${!(param.searchBox eq 'noShow')}">
        <form action="${pageContext.request.contextPath}/search" method="get" class="input-group me-3" style="max-width: 300px;">
          <input type="text" class="form-control" name="query" placeholder="Search artists, albums, or songs..." value="${param.query}">
          <button class="btn btn-primary" type="submit">
            <i class="fas fa-search"></i>
          </button>
        </form>
      </c:if>

      <c:choose>
        <c:when test="${empty sessionScope.USER}">
          <div class="d-flex">
            <a href="login.jsp" class="btn btn-outline-primary me-2">Login</a>
            <a href="signup.jsp" class="btn btn-primary me-3">Sign Up</a>
          </div>
        </c:when>
        <c:otherwise>
          <div class="d-flex">
            <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn btn-primary me-2">Logout</a>
            <button type="button" class="btn btn-outline-light position-relative me-2" data-bs-toggle="modal" data-bs-target="#shoppingCartModal">
              <i class="fas fa-shopping-cart me-2"></i>
              <span class="badge badge-light cart-badge"></span>
            </button>

            <c:choose>
              <c:when test="${sessionScope.USER.isAdmin() eq true}">
                <a href="${pageContext.request.contextPath}/admin/">
                  <button type="button" class="btn btn-outline-light position-relative me-2">
                    <i class="fa-solid fa-user me-2" ></i> Admin Panel
                  </button>
                </a>
              </c:when>
              <c:otherwise>
                <a href="${pageContext.request.contextPath}/profile">
                  <button type="button" class="btn btn-outline-light position-relative me-2">
                    <i class="fa-solid fa-user me-2" ></i> Profile
                  </button>
                </a>
              </c:otherwise>
            </c:choose>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</nav>