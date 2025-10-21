<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${sessionScope.USER.stageName} - Artist Profile</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/artist-profile.css">
    <style>
        .text-muted {
            color: var(--text-secondary) !important;
        }
    </style>
</head>
<body>
<jsp:include page="/includes/navbar.jsp"/>

<div class="container my-5">
    <%-- Profile Header Section (No changes here) --%>
    <div class="profile-header">
        <div class="artist-avatar">
            <i class="fas fa-user-circle fa-6x"></i>
        </div>
        <div class="artist-info">
            <h1>${sessionScope.USER.stageName}</h1>
            <p class="text-muted">${sessionScope.USER.firstName} ${sessionScope.USER.lastName}</p>
            <div class="artist-stats">
                <span><i class="fas fa-music"></i> ${fn:length(artistTracks)} Tracks</span>
            </div>
        </div>
        <div class="profile-actions">
            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#editProfileModal">
                <i class="fas fa-edit"></i> Edit Profile
            </button>
        </div>
    </div>

    <%-- About Me Section (No changes here) --%>
    <div class="profile-section">
        <h2>About Me</h2>
        <p>${not empty sessionScope.USER.bio ? sessionScope.USER.bio : 'No biography provided. Click Edit Profile to add one!'}</p>
    </div>

    <%-- My Tracks Section --%>
    <div class="profile-section">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2>My Tracks</h2>
            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addTrackModal">
                <i class="fas fa-plus"></i> Upload New Track
            </button>
        </div>

        <table class="table table-dark table-hover">
            <thead>
            <tr>
                <th style="width: 60px;"></th>
                <th>Title</th>
                <th>Price</th>
                <th>Genre</th>
                <th>Release Date</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="track" items="${artistTracks}">
                <tr>
                    <td>

                        <c:choose>
                            <c:when test="${not empty track.coverArtData}">
                                <img src="${pageContext.request.contextPath}/cover-art?trackId=${track.trackId}"
                                     alt="Cover"
                                     style="width: 40px; height: 40px; object-fit: cover; border-radius: 4px;">
                            </c:when>
                            <c:otherwise>
                                <div style="width: 40px; height: 40px; background-color: #333; border-radius: 4px; display: flex; align-items: center; justify-content: center;">
                                    <i class="fas fa-music text-muted"></i>
                                </div>
                            </c:otherwise>
                        </c:choose>
                            <%-- MODIFICATION END --%>
                    </td>
                    <td><strong>${track.title}</strong></td>
                    <td>Rs. ${track.price}</td>
                    <td>${track.genre}</td>
                    <td>${track.releaseDate}</td>
                    <td class="actions">
                            <%-- MODIFICATION START: Conditionally enable/disable the play button --%>
                        <c:choose>
                            <c:when test="${not empty track.fullTrackData}">
                                <button class="btn btn-info btn-sm play-btn-sm" data-track-id="${track.trackId}">
                                    <i class="fas fa-play"></i>
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button class="btn btn-info btn-sm play-btn-sm" disabled
                                        title="No audio file uploaded for this track">
                                    <i class="fas fa-play"></i>
                                </button>
                            </c:otherwise>
                        </c:choose>
                            <%-- MODIFICATION END --%>

                        <button class="btn btn-edit btn-sm"
                                onclick="openEditTrackModal('${track.trackId}', '${track.title}', '${track.price}', '${track.genre}', '${track.duration}', '${track.releaseDate}')">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-delete btn-sm"
                                onclick="openDeleteTrackModal('${track.trackId}', '${track.title}')">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty artistTracks}">
                <tr>
                    <td colspan="6" class="text-center text-muted">You haven't uploaded any tracks yet.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/includes/footer.jsp"/>

<%-- Modals --%>
<jsp:include page="/artist/modals/edit-profile-modal.jsp"/>
<jsp:include page="/admin/includes/modals/manage-track-modals/add-track-modal.jsp"/>
<jsp:include page="/admin/includes/modals/manage-track-modals/edit-track-modal.jsp"/>
<jsp:include page="/admin/includes/modals/manage-track-modals/delete-track-modal.jsp"/>

<script>
    // Define contextPath for the scripts to use
    window.contextPath = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/cart-utils.js"></script>
<script src="${pageContext.request.contextPath}/js/cart-handlers.js"></script>
<script src="${pageContext.request.contextPath}/js/cart-main.js"></script>
<script src="${pageContext.request.contextPath}/js/music-pagination.js"></script>
<script src="${pageContext.request.contextPath}/js/music-handlers.js"></script>
<script src="${pageContext.request.contextPath}/js/music-main.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<%-- Streaming and Modal JavaScript (No changes needed here) --%>


</body>
</html>