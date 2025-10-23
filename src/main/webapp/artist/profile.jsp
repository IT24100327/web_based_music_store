<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Artist Profile - ${sessionScope.USER.stageName}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
    <style>
        .profile-content {
            display: none;
        }
        .profile-content.active {
            display: block;
        }
    </style>
</head>
<body>

<div class="admin-container">
    <aside class="admin-sidebar">
        <div class="sidebar-header">
            <h2>Artist Panel</h2>
        </div>
        <nav class="sidebar-nav">
            <a href="${pageContext.request.contextPath}/artist/profile?view=dashboard" class="${view == 'dashboard' ? 'active' : ''}">
                <i class="fas fa-tachometer-alt"></i> Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/artist/profile?view=my-tracks" class="${view == 'my-tracks' ? 'active' : ''}">
                <i class="fas fa-music"></i> My Tracks
            </a>
            <a href="${pageContext.request.contextPath}/artist/profile?view=settings" class="${view == 'settings' ? 'active' : ''}">
                <i class="fas fa-cog"></i> Settings
            </a>
            <hr style="border-color: var(--border-light);">
            <a href="${pageContext.request.contextPath}/index">
                <i class="fas fa-home"></i> Back to Main Site
            </a>
            <a href="${pageContext.request.contextPath}/logout">
                <i class="fas fa-sign-out-alt"></i> Logout
            </a>
        </nav>
    </aside>

    <main class="admin-main">
        <header class="admin-header">
            <h1>Welcome, ${sessionScope.USER.stageName}</h1>
            <div class="user-info">
                <span>${sessionScope.USER.email}</span>
                <div class="user-avatar">${sessionScope.USER.firstName.charAt(0)}${sessionScope.USER.lastName.charAt(0)}</div>
            </div>
        </header>

        <c:if test="${not empty param.success}">
            <div class="alert alert-success" role="alert">
                    ${param.success}
            </div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger" role="alert">
                    ${param.error}
            </div>
        </c:if>

        <div id="dashboard" class="profile-content ${view == 'dashboard' ? 'active' : ''}">
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon"><i class="fas fa-music"></i></div>
                    <div class="stat-content">
                        <h3>${artistTracks.size()}</h3>
                        <p>Total Tracks</p>
                    </div>
                </div>
            </div>
            <div class="welcome-section">
                <div class="welcome-icon"><i class="fas fa-rocket"></i></div>
                <div class="welcome-content">
                    <h2>Ready to create?</h2>
                    <p>Manage your music, update your profile, and see your stats all in one place.</p>
                </div>
            </div>
        </div>

        <div id="my-tracks" class="profile-content ${view == 'my-tracks' ? 'active' : ''}">
            <div class="table-container">
                <div class="table-header">
                    <h3>Your Music Collection</h3>
                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addTrackModal">
                        <i class="fas fa-plus"></i> Add New Track
                    </button>
                </div>
                <div class="table-responsive">
                    <table>
                        <thead>
                        <tr>
                            <th>Cover Art</th>
                            <th>Title</th>
                            <th>Genre</th>
                            <th>Price</th>
                            <th>Release Date</th>
                            <th>Status</th> <%-- NEW --%>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="track" items="${artistTracks}">
                            <tr>
                                <td>
                                    <img src="${pageContext.request.contextPath}/cover-art?trackId=${track.trackId}" alt="${track.title}" class="preview-img">
                                </td>
                                <td>${track.title}</td>
                                <td>${track.genre}</td>
                                <td>Rs. <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${track.price}"/></td>
                                <td>${track.releaseDate}</td>
                                    <%-- NEW: Status Badge Display --%>
                                <td>
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
                                    <button class="btn btn-primary btn-sm play-btn-sm" data-track-id="${track.trackId}">
                                        <i class="fas fa-play"></i>
                                    </button>
                                    <button class="btn btn-secondary btn-sm" data-bs-toggle="modal" data-bs-target="#editTrackModal"
                                            data-track-id="${track.trackId}"
                                            data-title="${track.title}"
                                            data-price="${track.price}"
                                            data-genre="${track.genre}"
                                            data-duration="${track.duration}"
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
            </div>
        </div>

        <div id="settings" class="profile-content ${view == 'settings' ? 'active' : ''}">
            <div class="table-container">
                <div class="table-header">
                    <h3>Profile Settings</h3>
                </div>
                <form action="${pageContext.request.contextPath}/artist/update-profile" method="post">
                    <input type="hidden" name="userId" value="${sessionScope.USER.userId}">

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="firstName">First Name</label>
                                <input type="text" class="form-control" id="firstName" name="firstName" value="${sessionScope.USER.firstName}" required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="lastName">Last Name</label>
                                <input type="text" class="form-control" id="lastName" name="lastName" value="${sessionScope.USER.lastName}" required>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="email">Email Address</label>
                        <input type="email" class="form-control" id="email" name="email" value="${sessionScope.USER.email}" required>
                    </div>

                    <div class="form-group">
                        <label for="stageName">Stage Name</label>
                        <input type="text" class="form-control" id="stageName" name="stageName" value="${sessionScope.USER.stageName}" required>
                    </div>

                    <div class="form-group">
                        <label for="bio">Artist Bio</label>
                        <textarea class="form-control" id="bio" name="bio" rows="4" required>${sessionScope.USER.bio}</textarea>
                    </div>

                    <div class="form-group">
                        <label for="editPassword">New Password (optional)</label>
                        <input type="password" class="form-control" id="editPassword" name="editPassword" placeholder="Leave blank to keep current password">
                    </div>

                    <div class="text-end">
                        <button type="submit" class="btn btn-primary">Save Changes</button>
                    </div>
                </form>
            </div>
        </div>

    </main>
</div>

<div class="modal fade" id="addTrackModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/artist/add-track" method="post" enctype="multipart/form-data">
                <div class="modal-header">
                    <h5 class="modal-title">Add New Track</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="add-title">Title</label>
                        <input type="text" class="form-control" id="add-title" name="title" required>
                    </div>
                    <div class="form-group">
                        <label for="add-price">Price (Rs.)</label>
                        <input type="number" step="0.01" class="form-control" id="add-price" name="price" required>
                    </div>
                    <div class="form-group">
                        <label for="add-genre">Genre</label>
                        <select id="add-genre">
                            <option value="rock">Rock</option>
                            <option value="pop">Pop</option>
                            <option value="hiphop">Hip Hop</option>
                            <option value="electronic">Electronic</option>
                            <option value="jazz">Jazz</option>
                            <option value="classical">Classical</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="add-duration">Duration (seconds)</label>
                        <input type="number" class="form-control" id="add-duration" name="duration" required>
                    </div>
                    <div class="form-group">
                        <label for="add-release_date">Release Date</label>
                        <input type="date" class="form-control" id="add-release_date" name="release_date" required>
                    </div>
                    <div class="form-group">
                        <label for="add-audioFile">Audio File (MP3)</label>
                        <input type="file" class="form-control" id="add-audioFile" name="audioFile" accept=".mp3" required>
                    </div>
                    <div class="form-group">
                        <label for="add-coverArtFile">Cover Art (JPG/PNG)</label>
                        <input type="file" class="form-control" id="add-coverArtFile" name="coverArtFile" accept="image/*" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="submit" class="btn btn-primary">Add Track</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="editTrackModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/artist/update-track" method="post" enctype="multipart/form-data">
                <input type="hidden" name="trackId" id="edit-trackId">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Track</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="edit-title">Title</label>
                        <input type="text" class="form-control" id="edit-title" name="title" required>
                    </div>
                    <div class="form-group">
                        <label for="edit-price">Price (Rs.)</label>
                        <input type="number" step="0.01" class="form-control" id="edit-price" name="price" required>
                    </div>
                    <div class="form-group">
                        <label for="edit-genre">Genre</label>
                        <input type="text" class="form-control" id="edit-genre" name="genre" required>
                    </div>
                    <div class="form-group">
                        <label for="edit-duration">Duration (seconds)</label>
                        <input type="number" class="form-control" id="edit-duration" name="duration" required>
                    </div>
                    <div class="form-group">
                        <label for="edit-release_date">Release Date</label>
                        <input type="date" class="form-control" id="edit-release_date" name="release_date" required>
                    </div>
                    <div class="form-group">
                        <label for="edit-audioFile">New Audio File (Optional)</label>
                        <input type="file" class="form-control" id="edit-audioFile" name="audioFile" accept=".mp3">
                    </div>
                    <div class="form-group">
                        <label for="edit-coverArtFile">New Cover Art (Optional)</label>
                        <input type="file" class="form-control" id="edit-coverArtFile" name="coverArtFile" accept="image/*">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</div>

<audio id="global-audio-player" preload="auto"></audio>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<script>
    window.contextPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/js/music-handlers.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        initializePlayButtons();
    });
</script>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        var editTrackModal = document.getElementById('editTrackModal');
        editTrackModal.addEventListener('show.bs.modal', function (event) {
            var button = event.relatedTarget;
            var modal = this;
            modal.querySelector('#edit-trackId').value = button.getAttribute('data-track-id');
            modal.querySelector('#edit-title').value = button.getAttribute('data-title');
            modal.querySelector('#edit-price').value = button.getAttribute('data-price');
            modal.querySelector('#edit-genre').value = button.getAttribute('data-genre');
            modal.querySelector('#edit-duration').value = button.getAttribute('data-duration');
            modal.querySelector('#edit-release_date').value = button.getAttribute('data-release-date');
        });
    });
</script>

</body>
</html>