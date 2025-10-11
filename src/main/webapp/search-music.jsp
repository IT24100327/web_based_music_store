<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Music - RhythmWave Music Store</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
<!-- Navigation Bar -->
<jsp:include page="/includes/navbar.jsp">
    <jsp:param name="page" value="search"/>
    <jsp:param name="searchBox" value="noShow"/>
</jsp:include>

<!-- Search Header Section -->
<div class="search-header">
    <div class="container">
        <h1 class="display-5 fw-bold mb-4">Search Our Music Library</h1>
        <!-- Main Search Bar -->
        <div class="row justify-content-center">
            <div class="col-md-8">
                <form action="${pageContext.request.contextPath}/search" method="get" class="search-form">
                    <div class="input-group input-group-lg mb-4">
                        <input type="text" class="form-control" name="query" placeholder="Search artists, albums, or songs..." value="${param.query}">
                        <button class="btn" type="submit">
                            <i class="fas fa-search"></i>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Main Content with Sidebar Layout -->
<div class="container">
    <div class="search-content-wrapper">
        <!-- Filters Sidebar -->
        <aside class="search-filters-sidebar">
            <h2>Refine Your Search</h2>
            <form action="${pageContext.request.contextPath}/search" method="get">
                <input type="hidden" name="query" value="${param.query}">

                <div class="filter-group">
                    <label class="filter-label">Genre</label>
                    <select class="form-select" name="genre">
                        <option value="">All Genres</option>
                        <option value="rock" ${param.genre == 'rock' ? 'selected' : ''}>Rock</option>
                        <option value="pop" ${param.genre == 'pop' ? 'selected' : ''}>Pop</option>
                        <option value="hiphop" ${param.genre == 'hiphop' ? 'selected' : ''}>Hip Hop</option>
                        <option value="electronic" ${param.genre == 'electronic' ? 'selected' : ''}>Electronic</option>
                        <option value="jazz" ${param.genre == 'jazz' ? 'selected' : ''}>Jazz</option>
                        <option value="classical" ${param.genre == 'classical' ? 'selected' : ''}>Classical</option>
                    </select>
                </div>

                <div class="filter-group">
                    <label class="filter-label">Price Range</label>
                    <select class="form-select" name="price">
                        <option value="">Any Price</option>
                        <option value="under100" ${param.price == 'under100' ? 'selected' : ''}>Under Rs. 100</option>
                        <option value="100-200" ${param.price == '100-200' ? 'selected' : ''}>Rs. 100 - Rs. 200</option>
                        <option value="200-400" ${param.price == '200-400' ? 'selected' : ''}>Rs. 200 - Rs. 400</option>
                        <option value="over400" ${param.price == 'over400' ? 'selected' : ''}>Over Rs. 400</option>
                    </select>
                </div>

                <div class="filter-group">
                    <label class="filter-label">Release Year</label>
                    <select class="form-select" name="year">
                        <option value="">Any Year</option>
                        <option value="2023" ${param.year == '2023' ? 'selected' : ''}>2023</option>
                        <option value="2022" ${param.year == '2022' ? 'selected' : ''}>2022</option>
                        <option value="2020-2021" ${param.year == '2020-2021' ? 'selected' : ''}>2020-2021</option>
                        <option value="2010-2019" ${param.year == '2010-2019' ? 'selected' : ''}>2010-2019</option>
                        <option value="before2010" ${param.year == 'before2010' ? 'selected' : ''}>Before 2010</option>
                    </select>
                </div>

                <div class="filter-group">
                    <label class="filter-label">Rating</label>
                    <select class="form-select" name="rating">
                        <option value="">Any Rating</option>
                        <option value="5" ${param.rating == '5' ? 'selected' : ''}>5 Stars</option>
                        <option value="4" ${param.rating == '4' ? 'selected' : ''}>4+ Stars</option>
                        <option value="3" ${param.rating == '3' ? 'selected' : ''}>3+ Stars</option>
                    </select>
                </div>

                <div class="filter-buttons">
                    <button type="submit" class="btn btn-primary">Apply Filters</button>
                    <a href="${pageContext.request.contextPath}/search?query=${param.query}" class="btn btn-outline-secondary">Clear Filters</a>
                </div>
            </form>
        </aside>

        <!-- Search Results Main Content -->
        <main class="search-results-main">
            <c:choose>
                <c:when test="${not empty requestScope.trackList}">
                    <div class="search-results-info">
                        <h4>${empty param.query ? 'All Tracks' : 'Search Results for "' += param.query += '"'}</h4>
                        <p>Found ${fn:length(requestScope.trackList)} results</p>
                    </div>

                    <!-- Use the same track cards component as index page -->
                    <div class="search-results-grid">
                        <c:forEach var="track" items="${requestScope.trackList}">
                            <div class="compact-music-card">
                                <div class="album-cover-container">
                                    <img src="https://images.unsplash.com/photo-1571330735066-03aaa9429d89?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=400&q=80"
                                         class="compact-album-cover" alt="Album Cover">
                                </div>
                                <div class="compact-card-content">
                                    <div class="compact-card-text">
                                        <div class="compact-card-title">${track.title}</div>
                                        <div class="compact-card-artist">${track.artist}</div>
                                    </div>
                                    <div class="compact-card-footer">
                                        <span class="price-tag-sm">Rs. ${track.price}</span>
                                        <div class="compact-card-actions">
                                            <button class="play-btn-sm">
                                                <i class="fas fa-play"></i>
                                            </button>
                                            <c:if test="${not empty sessionScope.USER}">
                                                <button class="cart-btn-sm" data-track-id="${track.trackId}">
                                                    <i class="fas fa-cart-plus"></i>
                                                </button>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Pagination -->
                    <c:if test="${requestScope.noOfPages > 1}">
                        <nav class="search-pagination">
                            <c:if test="${requestScope.currentPage > 1}">
                                <a class="pagination-btn" href="${pageContext.request.contextPath}/search?query=${param.query}&genre=${param.genre}&price=${param.price}&rating=${param.rating}&page=${requestScope.currentPage - 1}">
                                    <i class="fas fa-chevron-left"></i>
                                </a>
                            </c:if>

                            <div class="page-numbers">
                                <c:forEach begin="1" end="${requestScope.noOfPages}" var="i">
                                    <a class="pagination-btn ${requestScope.currentPage == i ? 'active' : ''}"
                                       href="${pageContext.request.contextPath}/search?query=${param.query}&genre=${param.genre}&price=${param.price}&rating=${param.rating}&page=${i}">${i}</a>
                                </c:forEach>
                            </div>

                            <c:if test="${requestScope.currentPage < requestScope.noOfPages}">
                                <a class="pagination-btn" href="${pageContext.request.contextPath}/search?query=${param.query}&genre=${param.genre}&price=${param.price}&rating=${param.rating}&page=${requestScope.currentPage + 1}">
                                    <i class="fas fa-chevron-right"></i>
                                </a>
                            </c:if>
                        </nav>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div class="no-results">
                        <i class="fas fa-music"></i>
                        <h4>No tracks available</h4>
                        <p>No tracks found in the library. Please check back later.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </main>
    </div>
</div>

<!-- Footer -->
<jsp:include page="/includes/footer.jsp" />

<!-- Shopping Cart Modal -->
<jsp:include page="/includes/modals/shopping-cart-modal.jsp" />

<script>
    window.contextPath = '${pageContext.request.contextPath}';
    window.initialCartState = {
        itemCount: ${sessionScope.cartItems != null ? sessionScope.cartItems.size() : 0},
        cartTotal: ${sessionScope.cartTotal != null ? sessionScope.cartTotal : 0}
    };
    window.noOfPages = ${requestScope.noOfPages > 0 ? requestScope.noOfPages : 1};
</script>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/cart-utils.js"></script>
<script src="${pageContext.request.contextPath}/js/cart-handlers.js"></script>
<script src="${pageContext.request.contextPath}/js/cart-main.js"></script>
<script src="${pageContext.request.contextPath}/js/music-pagination.js"></script>
<script src="${pageContext.request.contextPath}/js/music-handlers.js"></script>
<script src="${pageContext.request.contextPath}/js/music-main.js"></script>

</body>
</html>