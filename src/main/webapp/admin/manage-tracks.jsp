<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Artist - Track Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
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
            <div class="table-header">
                <h3>My Uploaded Tracks</h3>
                <div class="table-actions">
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addTrackModal">
                        <i class="fas fa-plus"></i> Upload New Track
                    </button>
                </div>
            </div>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Price</th>
                    <th>Genre</th>
                    <th>Release Date</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="track" items="${requestScope.allTracks}">
                    <tr>
                        <td>${track.trackId}</td>
                        <td><strong>${track.title}</strong></td>
                        <td>Rs. ${track.price}</td>
                        <td>${track.genre}</td>
                        <td>${track.releaseDate}</td>
                        <td class="actions">
                            <button class="btn btn-edit btn-sm"
                                    onclick="openEditTrackModal(${track.trackId}, '${track.title}', ${track.price}, '${track.genre}', ${track.duration}, '${track.releaseDate}')">
                                <i class="fas fa-edit"></i> Edit
                            </button>
                            <button class="btn btn-delete btn-sm"
                                    onclick="openDeleteTrackModal(${track.trackId}, '${track.title}')">
                                <i class="fas fa-trash"></i> Delete
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty requestScope.artistTracks}">
                    <tr>
                        <td colspan="6" class="text-center text-muted">You haven't uploaded any tracks yet.</td>
                    </tr>
                </c:if>
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