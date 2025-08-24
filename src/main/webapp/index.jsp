<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${empty requestScope.noOfPages}">
    <c:redirect url="/trackPaginate"/>
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RhythmWave Music Store</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <style>

    </style>

</head>
<body>
<!-- Navigation Bar -->
<nav class="navbar navbar-expand-lg navbar-dark sticky-top">
    <div class="container">
        <a class="navbar-brand" href="#">
            <i class="fas fa-music me-2"></i>RhythmWave
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link active" href="#">Home</a>
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
<%--                        <a href="#" class="btn btn-outline-light position-relative">--%>
<%--                            <i class="fas fa-shopping-cart"></i>--%>
<%--                            <span class="cart-count">3</span>--%>
<%--                        </a>--%>

                        <button type="button" class="btn btn-outline-light position-relative" data-bs-toggle="modal" data-bs-target="#shoppingCartModal">
                            <i class="fas fa-shopping-cart me-2"></i> <c:out value="${fn:length(sessionScope.cartItems)}" />
                        </button>

                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</nav>

<!-- Banner Section -->
<div class="container">
    <div class="banner">
        <c:choose>
            <c:when test="${empty sessionScope.USER}">
                <div class="container text-center">
                    <h1 class="display-4 fw-bold">Discover Your Soundtrack</h1>
                    <p class="lead">Millions of songs at your fingertips. Curated playlists and exclusive releases.</p>
                    <button class="btn btn-primary btn-lg mt-3">Explore Now</button>
                </div>
            </c:when>
            <c:otherwise>
                <div class="container text-center">
                    <h1 class="display-4 fw-bold">Welcome Back! ${sessionScope.USER.firstName}</h1>
                    <p class="lead">Millions of songs at your fingertips. Curated playlists and exclusive releases.</p>
                    <button class="btn btn-primary btn-lg mt-3">Explore Now</button>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</div>

<!-- Search and Filters Section -->
<div class="container">
    <div class="search-section">
        <h2 class="mb-4">Find Your Music</h2>
        <div class="row">
            <div class="col-md-8">
                <div class="filter-label">Search Music Library</div>
                <div class="input-group mb-3">
                    <input type="text" class="form-control" placeholder="Search artists, albums, or songs...">
                    <button class="btn btn-primary" type="button">
                        <i class="fas fa-search"></i> Search
                    </button>
                </div>
            </div>
            <div class="col-md-4">
                <div class="filter-label">Sort by</div>
                <select class="form-select">
                    <option>Newest First</option>
                    <option>Price: Low to High</option>
                    <option>Price: High to Low</option>
                    <option>Most Popular</option>
                </select>
            </div>
        </div>

        <div class="row mt-4">
            <div class="col-md-3">
                <div class="filter-label">Genre</div>
                <select class="form-select">
                    <option>All Genres</option>
                    <option>Rock</option>
                    <option>Pop</option>
                    <option>Hip Hop</option>
                    <option>Electronic</option>
                    <option>Jazz</option>
                    <option>Classical</option>
                </select>
            </div>
            <div class="col-md-3">
                <div class="filter-label">Price Range</div>
                <select class="form-select">
                    <option>Any Price</option>
                    <option>Under $5</option>
                    <option>$5 - $10</option>
                    <option>$10 - $20</option>
                    <option>Over $20</option>
                </select>
            </div>
            <div class="col-md-3">
                <div class="filter-label">Release Year</div>
                <select class="form-select">
                    <option>Any Year</option>
                    <option>2023</option>
                    <option>2022</option>
                    <option>2020-2021</option>
                    <option>2010-2019</option>
                    <option>Before 2010</option>
                </select>
            </div>
            <div class="col-md-3">
                <div class="filter-label">Rating</div>
                <select class="form-select">
                    <option>Any Rating</option>
                    <option>5 Stars</option>
                    <option>4+ Stars</option>
                    <option>3+ Stars</option>
                </select>
            </div>
        </div>
    </div>
</div>

<!-- Music Listing Section -->
<div class="container">
    <h2 class="mb-4">Featured Music</h2>
    <div class="row">

        <!-- Music Card -->
        <c:forEach var="track" items="${requestScope.trackList}">
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

        <c:set var="currentPage" value="${requestScope.currentPage}"/>
        <c:set var="noOfPages" value="${requestScope.noOfPages}"/>

        <!-- Pagination -->
        <c:if test="${noOfPages > 1}">
            <nav class="mt-5">
                <ul class="pagination justify-content-center">
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/trackPaginate?page=${currentPage - 1}">Previous</a>
                    </li>
                    <c:forEach var="i" begin="1" end="${noOfPages}">
                        <c:choose>
                            <c:when test="${i eq currentPage}">
                                <li class="page-item active"><a class="page-link" href="${pageContext.request.contextPath}/trackPaginate?page=${i}">${i}</a></li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item"><a class="page-link" href="${pageContext.request.contextPath}/trackPaginate?page=${i}">${i}</a></li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <li class="page-item ${currentPage == noOfPages ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/trackPaginate?page=${currentPage + 1}">Next</a>
                    </li>
                </ul>
            </nav>
        </c:if>
    </div>

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
                    <li><a href="#" class="text-decoration-none text-secondary">Home</a></li>
                    <li><a href="#" class="text-decoration-none text-secondary">About</a></li>
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
    });
</script>
</body>
</html>