<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Artist - Track Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
</head>
<body>
<div class="admin-container">
    <jsp:include page="includes/admin_nav_bar.jsp">
        <jsp:param name="page" value="manage-tracks"/>
    </jsp:include>

    <main class="admin-main">
        <header class="admin-header">
            <h1>Track Management</h1>
            <div class="user-info">
                <div class="user-avatar">A</div>
                <div>
                    <div>${sessionScope.USER.firstName} ${sessionScope.USER.lastName}</div>
                    <div class="text-muted">${sessionScope.USER.email}</div>
                </div>
            </div>
        </header>

        <c:if test="${not empty param.error}">
            <div class="alert alert-danger">${param.error}</div>
        </c:if>
        <c:if test="${not empty param.success}">
            <div class="alert alert-success">${param.success}</div>
        </c:if>

        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Artist</th>
                    <th>Genre</th>
                    <th>Price</th>
                    <th>Status</th>
                    <th style="width: 25%;">Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="track" items="${allTracks}">
                    <tr>
                        <td>${track.trackId}</td>
                        <td>${track.title}</td>
                        <td>${track.artistName}</td>
                        <td>${track.genre}</td>
                        <td>Rs. <fmt:formatNumber value="${track.price}" type="currency" currencySymbol=""/></td>
                        <td>
                                <%-- Display Status Badge --%>
                            <c:choose>
                                <c:when test="${track.status == 'APPROVED'}">
                                    <span class="status-badge status-active">Approved</span>
                                </c:when>
                                <c:when test="${track.status == 'REJECTED'}">
                                    <span class="status-badge status-inactive">Rejected</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-badge status-pending">Pending</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="actions">
                                <%-- Conditional Approve/Reject Buttons --%>
                            <c:if test="${track.status == 'PENDING'}">
                                <form action="${pageContext.request.contextPath}/admin/update-track-status" method="post" style="display: inline;">
                                    <input type="hidden" name="trackId" value="${track.trackId}">
                                    <input type="hidden" name="action" value="approve">
                                    <button type="submit" class="btn btn-success btn-sm">Approve</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/admin/update-track-status" method="post" style="display: inline;">
                                    <input type="hidden" name="trackId" value="${track.trackId}">
                                    <input type="hidden" name="action" value="reject">
                                    <button type="submit" class="btn btn-warning btn-sm">Reject</button>
                                </form>
                            </c:if>

                                <%-- Existing Edit/Delete Buttons --%>
                            <button class="btn btn-secondary btn-sm" data-bs-toggle="modal" data-bs-target="#editTrackModal"
                                    data-track-id="${track.trackId}" data-title="${track.title}" data-price="${track.price}"
                                    data-genre="${track.genre}" data-artist-id="${track.artistId}" data-duration="${track.duration}"
                                    data-release-date="${track.releaseDate}">
                                <i class="fas fa-edit"></i> Edit
                            </button>
                            <form action="${pageContext.request.contextPath}/artist/delete-track" method="post" onsubmit="return confirm('Are you sure you want to delete this track?');">
                                <input type="hidden" name="trackId" value="${track.trackId}">
                                <button type="submit" class="btn btn-delete btn-sm"><i class="fas fa-trash"></i> Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</div>

<jsp:include page="includes/modals/manage-track-modals/add-track-modal.jsp"/>
<jsp:include page="includes/modals/manage-track-modals/edit-track-modal.jsp"/>
<jsp:include page="includes/modals/manage-track-modals/delete-track-modal.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const editModal = new bootstrap.Modal(document.getElementById('editTrackModal'));
    const deleteModal = new bootstrap.Modal(document.getElementById('deleteTrackModal'));

    function openEditTrackModal(id, title, price, genre, duration, releaseDate) {
        document.getElementById('editTrackId').value = id;
        document.getElementById('editTrackTitle').value = title;
        document.getElementById('editTrackPrice').value = price;
        document.getElementById('editTrackGenre').value = genre;
        document.getElementById('editTrackDuration').value = duration;
        document.getElementById('editTrackReleaseDate').value = releaseDate;
        editModal.show();
    }

    function openDeleteTrackModal(id, title) {
        document.getElementById('deleteTrackId').value = id;
        document.getElementById('deleteTrackName').textContent = title;
        deleteModal.show();
    }
</script>
</body>
</html>