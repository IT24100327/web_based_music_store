<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<div class="table-container">
    <div class="table-header"><h3>My Music Library</h3></div>
    <c:choose>
        <c:when test="${not empty purchasedTracks}">
            <div class="list-group">
                <c:forEach var="track" items="${purchasedTracks}">
                    <div class="list-group-item list-group-item-action d-flex justify-content-between align-items-center bg-dark text-white mb-2 border-secondary">
                        <div>
                            <h5 class="mb-1">${track.title}</h5>
                            <p class="mb-1 text-muted">by ${track.artistName}</p>
                        </div>
                        <a href="${pageContext.request.contextPath}/download?trackId=${track.trackId}"
                           class="btn btn-primary"><i class="fas fa-download me-2"></i>Download</a>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="text-center p-5">
                <h4>Your music library is empty.</h4>
                <a href="${pageContext.request.contextPath}/search" class="btn btn-primary mt-3">Discover Music</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>