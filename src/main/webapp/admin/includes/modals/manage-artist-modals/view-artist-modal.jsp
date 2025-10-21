<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="contextPath" value="${param.contextPath}"/>

<div class="modal fade" id="viewArtistModal" tabindex="-1" aria-labelledby="viewArtistModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="viewArtistModalLabel">Artist Details</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-4">
                        <div class="artist-profile-card text-center">
                            <div class="artist-avatar mb-3">
                                <i class="fas fa-user-circle fa-5x text-primary"></i>
                            </div>
                            <h4 id="viewStageName" class="mb-2"></h4>
                            <p class="text-muted mb-3" id="viewRealName"></p>

                            <div class="artist-stats">
                                <div class="stat-item">
                                    <i class="fas fa-music text-primary"></i>
                                    <span id="viewTotalTracks" class="stat-number">0</span>
                                    <span class="stat-label">Tracks</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-8">
                        <div class="artist-details">
                            <h5 class="section-title">Contact Information</h5>
                            <div class="detail-item">
                                <label><i class="fas fa-envelope"></i> Email:</label>
                                <span id="viewEmail"></span>
                            </div>

                            <h5 class="section-title mt-4">Bio</h5>
                            <div class="bio-content">
                                <p id="viewBio" class="text-muted">No biography available.</p>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="recent-activity mt-4">
                    <h5 class="section-title">Recent Activity</h5>
                    <div class="activity-list">
                        <div class="activity-item">
                            <i class="fas fa-music activity-icon"></i>
                            <div class="activity-content">
                                <div class="activity-text">Artist profile created</div>
                                <div class="activity-time text-muted">Just now</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" onclick="editCurrentArtist()">
                    <i class="fas fa-edit"></i> Edit Artist
                </button>
            </div>
        </div>
    </div>
</div>