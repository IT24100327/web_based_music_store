<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<div class="row" id="track-row">
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
                            <c:if test="${not empty sessionScope.USER}">
                                <button class="cart-btn" data-track-id="${track.trackId}">
                                    <i class="fas fa-cart-plus"></i>
                                </button>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<!-- Pagination (same as in index.jsp) -->
<c:set var="currentPage" value="${requestScope.currentPage}"/>
<c:set var="noOfPages" value="${requestScope.noOfPages}"/>
<c:if test="${noOfPages > 1}">
    <nav class="mt-5">
        <ul class="pagination justify-content-center" id="pagination">
            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${currentPage - 1}">Previous</a>
            </li>
            <c:forEach var="i" begin="1" end="${noOfPages}">
                <c:choose>
                    <c:when test="${i eq currentPage}">
                        <li class="page-item active"><a class="page-link" href="#" data-page="${i}">${i}</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item"><a class="page-link" href="#" data-page="${i}">${i}</a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <li class="page-item ${currentPage == noOfPages ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${currentPage + 1}">Next</a>
            </li>
        </ul>
    </nav>
</c:if>