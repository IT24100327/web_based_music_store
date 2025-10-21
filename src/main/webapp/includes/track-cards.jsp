<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<div class="compact-grid" id="track-row">
    <c:forEach var="track" items="${requestScope.trackList}">
        <div class="compact-music-card">
            <div class="album-cover-container">
                <c:choose>
                    <c:when test="${not empty track.coverArtData}">
                        <img src="${pageContext.request.contextPath}/cover-art?trackId=${track.trackId}"
                             class="compact-album-cover" alt="Cover for ${track.title}">
                    </c:when>
                    <c:otherwise>
                        <%-- Fallback placeholder image --%>
                        <img src="https://images.unsplash.com/photo-1571330735066-03aaa9429d89?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=400&q=80"
                             class="compact-album-cover" alt="Album Cover">
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="compact-card-content">
                <div class="compact-card-text">
                    <div class="compact-card-title">${track.title}</div>
                    <div class="compact-card-artist">${track.artistName}</div>
                </div>
                <div class="compact-card-footer">
                    <span class="price-tag-sm">Rs. ${track.price}</span>
                    <div class="compact-card-actions">
                        <button class="play-btn-sm"
                                data-track-id="${track.trackId}"> <%-- Added data-track-id for streaming --%>
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

<%-- Pagination (no changes needed here) --%>
<c:set var="currentPage" value="${requestScope.currentPage}"/>
<c:set var="noOfPages" value="${requestScope.noOfPages}"/>
<c:if test="${noOfPages > 1}">
    <nav class="music-pagination" id="pagination">
        <a class="pagination-btn ${currentPage == 1 ? 'disabled' : ''}" href="#" data-page="${currentPage - 1}">
            <i class="fas fa-chevron-left"></i>
        </a>
        <div class="page-numbers">
            <c:forEach var="i" begin="1" end="${noOfPages}">
                <a class="pagination-btn ${i eq currentPage ? 'active' : ''}" href="#" data-page="${i}">${i}</a>
            </c:forEach>
        </div>
        <a class="pagination-btn ${currentPage == noOfPages ? 'disabled' : ''}" href="#" data-page="${currentPage + 1}">
            <i class="fas fa-chevron-right"></i>
        </a>
    </nav>
</c:if>