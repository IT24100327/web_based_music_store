<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Music Library</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
</head>
<body>
<jsp:include page="/includes/navbar.jsp"/>

<div class="container my-5">
    <div class="order-header">
        <h1><i class="fas fa-compact-disc me-2"></i>My Music Library</h1>
        <p>All the tracks you've purchased are available for download here.</p>
    </div>

    <c:choose>
        <c:when test="${not empty requestScope.purchasedTracks}">
            <div class="list-group">
                <c:forEach var="track" items="${requestScope.purchasedTracks}">
                    <div class="list-group-item list-group-item-action d-flex justify-content-between align-items-center bg-dark text-white mb-2 border-secondary">
                        <div>
                            <h5 class="mb-1">${track.title}</h5>
                            <p class="mb-1 text-muted">by ${track.artistName}</p>
                        </div>
                        <a href="${pageContext.request.contextPath}/download?trackId=${track.trackId}"
                           class="btn btn-primary">
                            <i class="fas fa-download me-2"></i>Download
                        </a>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="text-center p-5 border rounded border-secondary">
                <i class="fas fa-music fa-3x text-muted mb-3"></i>
                <h4>Your library is empty.</h4>
                <p>Purchased tracks will appear here after checkout.</p>
                <a href="${pageContext.request.contextPath}/search" class="btn btn-primary mt-3">Discover Music</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/includes/footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>