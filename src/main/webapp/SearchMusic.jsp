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
    <style>
        .search-results-info {
            color: var(--text-secondary);
            margin-bottom: 1.5rem;
        }

        .no-results {
            text-align: center;
            padding: 3rem;
            color: var(--text-secondary);
        }

        .no-results i {
            font-size: 3rem;
            margin-bottom: 1rem;
            display: block;
            color: #555;
        }

        .filter-active {
            background-color: var(--primary) !important;
            color: #000 !important;
            border-color: var(--primary) !important;
        }
    </style>
</head>
<body>
<!-- Navigation Bar -->
<nav class="navbar navbar-expand-lg navbar-dark sticky-top">
    <div class="container">
        <a class="navbar-brand" href="index.jsp">
            <i class="fas fa-music me-2"></i>RhythmWave
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="index.jsp">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="search.jsp">Search</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Genres</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">New Releases</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Top Charts</a>
                </li>
            </ul>

            <c:choose>
                <c:when test="${empty sessionScope.USER}">
                    <div class="d-flex">
                        <a href="login.jsp" class="btn btn-outline-primary me-2">Login</a>
                        <a href="signup.jsp" class="btn btn-primary me-3">Sign Up</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="d-flex">
                        <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn btn-primary me-3">Logout</a>
                        <button type="button" class="btn btn-outline-light position-relative" data-bs-toggle="modal" data-bs-target="#shoppingCartModal">
                            <i class="fas fa-shopping-cart me-2"></i> <c:out value="${fn:length(sessionScope.cartItems)}" />
                        </button>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</nav>

<!-- Search Header Section -->
<div class="container mt-4">
    <div class="search-header">
        <h1 class="display-5 fw-bold mb-4">Search Our Music Library</h1>

        <!-- Main Search Bar -->
        <div class="row justify-content-center">
            <div class="col-md-8">
                <form action="${pageContext.request.contextPath}/SearchServlet" method="get">
                    <div class="input-group input-group-lg mb-4">
                        <input type="text" class="form-control" name="query" placeholder="Search artists, albums, or songs..." value="${param.query}">
                        <button class="btn btn-primary" type="submit">
                            <i class="fas fa-search"></i> Search
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Quick Filters -->
        <div class="quick-filters mb-4">
            <h6 class="filter-label mb-3">Quick Filters</h6>
            <div class="d-flex flex-wrap gap-2">
                <a href="${pageContext.request.contextPath}/SearchServlet?query=${param.query}&filter=latest" class="btn btn-sm btn-outline-secondary ${param.filter == 'latest' ? 'filter-active' : ''}">Latest Releases</a>
                <a href="${pageContext.request.contextPath}/SearchServlet?query=${param.query}&filter=popular" class="btn btn-sm btn-outline-secondary ${param.filter == 'popular' ? 'filter-active' : ''}">Most Popular</a>
                <a href="${pageContext.request.contextPath}/SearchServlet?query=${param.query}&filter=under5" class="btn btn-sm btn-outline-secondary ${param.filter == 'under5' ? 'filter-active' : ''}">Under $5</a>
                <a href="${pageContext.request.contextPath}/SearchServlet?query=${param.query}&filter=highestrated" class="btn btn-sm btn-outline-secondary ${param.filter == 'highestrated' ? 'filter-active' : ''}">Highest Rated</a>
            </div>
        </div>
    </div>
</div>

<!-- Advanced Filters Section -->
<div class="container">
    <div class="search-section">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="mb-0">Refine Your Search</h2>
            <button class="btn btn-sm btn-outline-secondary" type="button" data-bs-toggle="collapse" data-bs-target="#advancedFilters">
                <i class="fas fa-sliders-h me-1"></i> Advanced Filters
            </button>
        </div>

        <div class="collapse ${not empty param.genre or not empty param.price or not empty param.year or not empty param.rating ? 'show' : ''}" id="advancedFilters">
            <form action="${pageContext.request.contextPath}/SearchServlet" method="get">
                <input type="hidden" name="query" value="${param.query}">

                <div class="row mt-3">
                    <div class="col-md-3">
                        <div class="filter-label">Genre</div>
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
                    <div class="col-md-3">
                        <div class="filter-label">Price Range</div>
                        <select class="form-select" name="price">
                            <option value="">Any Price</option>
                            <option value="under5" ${param.price == 'under5' ? 'selected' : ''}>Under $5</option>
                            <option value="5-10" ${param.price == '5-10' ? 'selected' : ''}>$5 - $10</option>
                            <option value="10-20" ${param.price == '10-20' ? 'selected' : ''}>$10 - $20</option>
                            <option value="over20" ${param.price == 'over20' ? 'selected' : ''}>Over $20</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <div class="filter-label">Release Year</div>
                        <select class="form-select" name="year">
                            <option value="">Any Year</option>
                            <option value="2023" ${param.year == '2023' ? 'selected' : ''}>2023</option>
                            <option value="2022" ${param.year == '2022' ? 'selected' : ''}>2022</option>
                            <option value="2020-2021" ${param.year == '2020-2021' ? 'selected' : ''}>2020-2021</option>
                            <option value="2010-2019" ${param.year == '2010-2019' ? 'selected' : ''}>2010-2019</option>
                            <option value="before2010" ${param.year == 'before2010' ? 'selected' : ''}>Before 2010</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <div class="filter-label">Rating</div>
                        <select class="form-select" name="rating">
                            <option value="">Any Rating</option>
                            <option value="5" ${param.rating == '5' ? 'selected' : ''}>5 Stars</option>
                            <option value="4" ${param.rating == '4' ? 'selected' : ''}>4+ Stars</option>
                            <option value="3" ${param.rating == '3' ? 'selected' : ''}>3+ Stars</option>
                        </select>
                    </div>
                </div>

                <div class="row mt-3">
                    <div class="col-md-12 text-end">
                        <button type="submit" class="btn btn-primary">Apply Filters</button>
                        <a href="${pageContext.request.contextPath}/SearchServlet?query=${param.query}" class="btn btn-outline-secondary ms-2">Clear Filters</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Search Results Section -->
<div class="container mt-5">
    <c:choose>
        <c:when test="${not empty requestScope.searchResults}">
            <div class="search-results-info">
                <h4>Search Results for "${param.query}"</h4>
                <p>Found ${fn:length(requestScope.searchResults)} results</p>
            </div>

            <div class="row">
                <c:forEach var="track" items="${requestScope.searchResults}">
                    <div class="col-md-4 col-lg-3 mb-3">
                        <div class="music-card">
                            <img src="https://images.unsplash.com/photo-1571330735066-03aaa9429d89?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80" class="album-cover" alt="Album Cover">
                            <div class="p-3">
                                <h5>${track.title}</h5>
                                <p class="artist-name">by ${track.artist}</p>
                                <div class="d-flex justify-content-between align-items-center">
                                    <span class="price-tag">Rs. ${track.price}</span>
                                    <div class="d-flex">
                                        <div class="play-btn me-2">
                                            <i class="fas fa-play"></i>
                                        </div>
                                        <a href="${pageContext.request.contextPath}/CartServlet?action=add&trackId=${track.trackId}">
                                            <div class="cart-btn">
                                                <i class="fas fa-cart-plus"></i>
                                            </div>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>

        <c:when test="${not empty param.query}">
            <div class="no-results">
                <i class="fas fa-search"></i>
                <h4>No results found for "${param.query}"</h4>
                <p>Try different keywords or check the spelling</p>
            </div>
        </c:when>

        <c:otherwise>
            <div class="no-results">
                <i class="fas fa-music"></i>
                <h4>Start your music discovery</h4>
                <p>Use the search bar above to find your favorite artists, albums, or songs</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Footer -->
<footer class="footer mt-5">
    <div class="container">
        <div class="row">
            <div class="col-md-4">
                <h5>RhythmWave</h5>
                <p>Your destination for premium music downloads and streaming. Discover new artists and enjoy your favorite tracks.</p>
            </div>
            <div class="col-md-2">
                <h5>Links</h5>
                <ul class="list-unstyled">
                    <li><a href="index.jsp" class="text-decoration-none text-secondary">Home</a></li>
                    <li><a href="search.jsp" class="text-decoration-none text-secondary">Search</a></li>
                    <li><a href="#" class="text-decoration-none text-secondary">Music</a></li>
                    <li><a href="#" class="text-decoration-none text-secondary">Artists</a></li>
                </ul>
            </div>
            <div class="col-md-2">
                <h5>Support</h5>
                <ul class="list-unstyled">
                    <li><a href="#" class="text-decoration-none text-secondary">Help Center</a></li>
                    <li><a href="#" class="text-decoration-none text-secondary">Contact Us</a></li>
                    <li><a href="#" class="text-decoration-none text-secondary">Privacy Policy</a></li>
                    <li><a href="#" class="text-decoration-none text-secondary">Terms of Service</a></li>
                </ul>
            </div>
            <div class="col-md-4">
                <h5>Subscribe to Our Newsletter</h5>
                <p>Get the latest updates on new music releases and exclusive offers.</p>
                <div class="input-group">
                    <input type="email" class="form-control" placeholder="Your email address">
                    <button class="btn btn-primary">Subscribe</button>
                </div>
            </div>
        </div>
        <hr class="my-4">
        <div class="row">
            <div class="col-md-6">
                <p class="text-secondary">© 2023 RhythmWave. All rights reserved.</p>
            </div>
            <div class="col-md-6 text-md-end">
                <a href="#" class="text-secondary me-3"><i class="fab fa-facebook-f"></i></a>
                <a href="#" class="text-secondary me-3"><i class="fab fa-twitter"></i></a>
                <a href="#" class="text-secondary me-3"><i class="fab fa-instagram"></i></a>
                <a href="#" class="text-secondary"><i class="fab fa-youtube"></i></a>
            </div>
        </div>
    </div>
</footer>

<!-- Shopping Cart Modal -->
<div class="modal fade cart-modal" id="shoppingCartModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Your Shopping Cart <span class="cart-badge"> <c:out value="${fn:length(sessionScope.cartItems)}" /></span></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <!-- Cart Item 1 -->
                <c:choose>
                    <c:when test="${empty sessionScope.cartItems}">
                        <div class="modal-body">
                            <div class="cart-empty">
                                <i class="fas fa-shopping-cart"></i>
                                <p>Your cart is empty</p>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="track" items="${sessionScope.cartItems}">
                            <div class="cart-item">
                                <img src="https://images.unsplash.com/photo-1571330735066-03aaa9429d89?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80"
                                     alt="Album Cover" class="cart-item-img">
                                <div class="cart-item-details">
                                    <div class="cart-item-title">${track.title}</div>
                                    <div class="cart-item-artist">by ${track.artist}</div>
                                    <div class="cart-item-price">Rs. ${track.price}</div>
                                </div>
                                <a href="${pageContext.request.contextPath}/CartServlet?action=remove&trackId=${track.trackId}">
                                    <button class="cart-item-remove">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </a>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="modal-footer">
                <div class="cart-summary">
                    <span>Total: </span>
                    <span class="cart-total">Rs. ${sessionScope.cartTotal}</span>
                </div>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Continue Shopping</button>
                <button type="button" class="btn btn-primary">Proceed to Checkout</button>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Simple interactivity for the music cards
    document.addEventListener('DOMContentLoaded', function() {
        // Play button functionality
        const playButtons = document.querySelectorAll('.play-btn');
        playButtons.forEach(button => {
            button.addEventListener('click', function() {
                this.classList.toggle('playing');
                const icon = this.querySelector('i');
                if (icon.classList.contains('fa-play')) {
                    icon.classList.replace('fa-play', 'fa-pause');
                } else {
                    icon.classList.replace('fa-pause', 'fa-play');
                }
            });
        });

        // Add to cart functionality
        const cartButtons = document.querySelectorAll('.cart-btn');
        cartButtons.forEach(button => {
            button.addEventListener('click', function() {
                this.classList.toggle('added');
                const icon = this.querySelector('i');
                if (this.classList.contains('added')) {
                    icon.classList.replace('fa-cart-plus', 'fa-check');
                } else {
                    icon.classList.replace('fa-check', 'fa-cart-plus');
                }
            });
        });

        // Preserve filter state when advanced filters are shown
        const advancedFilters = document.getElementById('advancedFilters');
        if (advancedFilters.classList.contains('show')) {
            const filterButton = document.querySelector('[data-bs-target="#advancedFilters"]');
            filterButton.classList.add('filter-active');
        }
    });
</script>
</body>
</html>