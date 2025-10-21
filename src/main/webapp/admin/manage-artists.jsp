<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Artist Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
</head>
<body>
<div class="admin-container">
    <jsp:include page="includes/admin_nav_bar.jsp">
        <jsp:param name="page" value="manage-artists"/>
    </jsp:include>

    <main class="admin-main">
        <header class="admin-header">
            <h1>Artist Management</h1>
            <div class="user-info">
                <div class="user-avatar">A</div>
                <div>
                    <div>${sessionScope.USER.firstName} ${sessionScope.USER.lastName}</div>
                    <div class="text-muted">${sessionScope.USER.email}</div>
                </div>
            </div>
        </header>

        <c:if test="${not empty error}">
            <div class="alert alert-danger"><c:out value="${error}"/></div>
        </c:if>

        <div class="table-container">
            <div class="table-header">
                <h3>All Artists</h3>
                <div class="table-actions">
                    <button class="btn btn-primary" onclick="openAddArtistModal()">
                        <i class="fas fa-plus"></i> Add Artist
                    </button>
                </div>
            </div>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Artist</th>
                    <th>Stage Name</th>
                    <th>Email</th>
                    <%--          <th>Tracks</th>--%>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="artist" items="${requestScope.allArtists}">
                    <tr>
                        <td><c:out value="${artist.userId}"/></td>
                        <td><strong><c:out value="${artist.firstName} ${artist.lastName}"/></strong></td>
                        <td><strong><c:out value="${artist.stageName}"/></strong></td>
                        <td><c:out value="${artist.email}"/></td>
                            <%--            <td>--%>
                            <%--                            <span class="badge bg-primary">--%>
                            <%--                                <i class="fas fa-music"></i> <c:out value="${artist.totalTracks}"/>--%>
                            <%--                            </span>--%>
                            <%--            </td>--%>
                        <td class="actions">
                            <button class="btn btn-info btn-sm" onclick="openViewArtistModal(
                                    '<c:out value="${artist.userId}"/>',
                                    '<c:out value="${artist.firstName}"/>',
                                    '<c:out value="${artist.lastName}"/>',
                                    '<c:out value="${artist.email}"/>',
                                    '<c:out value="${artist.stageName}"/>',
                                    '<c:out value="${artist.bio}"/>'
                                <%--'<c:out value="${artist.totalTracks}"/>'--%>
                                    )">
                                <i class="fas fa-eye"></i> View
                            </button>
                            <button class="btn btn-edit btn-sm" onclick="openEditArtistModal(
                                    '<c:out value="${artist.userId}"/>',
                                    '<c:out value="${artist.firstName}"/>',
                                    '<c:out value="${artist.lastName}"/>',
                                    '<c:out value="${artist.email}"/>',
                                    '<c:out value="${artist.stageName}"/>',
                                    '<c:out value="${artist.bio}"/>'
                                    )">
                                <i class="fas fa-edit"></i> Edit
                            </button>
                            <button class="btn btn-delete btn-sm" onclick="openDeleteArtistModal(
                                    '<c:out value="${artist.userId}"/>',
                                    '<c:out value="${artist.stageName}"/>'
                                    )">
                                <i class="fas fa-trash"></i> Delete
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <footer class="admin-footer">
            &copy; 2025 RhythmWave | Admin Panel
        </footer>
    </main>
</div>

<jsp:include page="includes/modals/manage-artist-modals/view-artist-modal.jsp">
    <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
</jsp:include>

<jsp:include page="includes/modals/manage-artist-modals/add-artist-modal.jsp">
    <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
</jsp:include>

<jsp:include page="includes/modals/manage-artist-modals/edit-artist-modal.jsp">
    <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
</jsp:include>

<jsp:include page="includes/modals/manage-artist-modals/delete-artist-modal.jsp">
    <jsp:param name="contextPath" value="${pageContext.request.contextPath}"/>
</jsp:include>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Initialize Modals
    const viewArtistModal = new bootstrap.Modal(document.getElementById('viewArtistModal'));
    const addArtistModal = new bootstrap.Modal(document.getElementById('addArtistModal'));
    const editArtistModal = new bootstrap.Modal(document.getElementById('editArtistModal'));
    const deleteArtistModal = new bootstrap.Modal(document.getElementById('deleteArtistModal'));

    // Store data of the artist being viewed to pass to the edit modal
    let currentArtistData = {};

    function openViewArtistModal(userId, firstName, lastName, email, stageName, bio) {
        // Populate view modal
        document.getElementById('viewStageName').textContent = stageName || 'N/A';
        document.getElementById('viewRealName').textContent = `${firstName} ${lastName}`;
        document.getElementById('viewEmail').textContent = email;
        document.getElementById('viewBio').textContent = bio || 'No biography available.';

        // Store data for the 'Edit' button in the view modal
        currentArtistData = {userId, firstName, lastName, email, stageName, bio};
        viewArtistModal.show();
    }

    // Opens the edit modal using data from the currently viewed artist
    function editCurrentArtist() {
        viewArtistModal.hide();
        openEditArtistModal(
            currentArtistData.userId,
            currentArtistData.firstName,
            currentArtistData.lastName,
            currentArtistData.email,
            currentArtistData.stageName,
            currentArtistData.bio
        );
    }

    function openAddArtistModal() {
        document.getElementById('addArtistForm').reset();
        addArtistModal.show();
    }

    function openEditArtistModal(userId, firstName, lastName, email, stageName, bio) {
        // Populate edit modal
        document.getElementById('editArtistId').value = userId;
        document.getElementById('editArtistFirstName').value = firstName || '';
        document.getElementById('editArtistLastName').value = lastName || '';
        document.getElementById('editArtistEmail').value = email || '';
        document.getElementById('editArtistStageName').value = stageName || '';
        document.getElementById('editArtistBio').value = bio || '';
        document.getElementById('editArtistPassword').value = ''; // Clear password field
        editArtistModal.show();
    }

    function openDeleteArtistModal(userId, artistName) {
        // Populate delete modal
        document.getElementById('deleteArtistId').value = userId;
        document.getElementById('deleteArtistName').textContent = artistName;
        deleteArtistModal.show();
    }
</script>
</body>
</html>