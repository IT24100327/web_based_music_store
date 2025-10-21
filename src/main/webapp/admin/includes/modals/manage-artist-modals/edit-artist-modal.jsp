<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="contextPath" value="${param.contextPath}"/>

<div class="modal fade" id="editArtistModal" tabindex="-1" aria-labelledby="editArtistModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
            <form action="${contextPath}/update-artist" method="POST">
                <div class="modal-header">
                    <h5 class="modal-title" id="editArtistModalLabel">Edit Artist</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <div class="modal-body">
                    <input type="hidden" id="editArtistId" name="userId">

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="editArtistFirstName">First Name *</label>
                                <input type="text" class="form-control" id="editArtistFirstName" name="firstName"
                                       required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="editArtistLastName">Last Name *</label>
                                <input type="text" class="form-control" id="editArtistLastName" name="lastName"
                                       required>
                            </div>
                        </div>
                    </div>

                    <div class="form-group mt-2">
                        <label for="editArtistEmail">Email *</label>
                        <input type="email" class="form-control" id="editArtistEmail" name="email" required>
                    </div>

                    <div class="form-group mt-2">
                        <label for="editArtistStageName">Stage Name *</label>
                        <input type="text" class="form-control" id="editArtistStageName" name="stageName" required>
                    </div>

                    <div class="form-group mt-2">
                        <label for="editArtistPassword">Password</label>
                        <input type="password" class="form-control" id="editArtistPassword" name="editPassword"
                               placeholder="New password (leave blank to keep unchanged)" minlength="4">
                        <small class="text-muted">Leave blank to keep current password</small>
                    </div>

                    <div class="form-group mt-2">
                        <label for="editArtistBio">Artist Bio</label>
                        <textarea class="form-control" id="editArtistBio" name="bio" rows="3"
                                  placeholder="Tell us about the artist..." maxlength="500"></textarea>
                        <small class="text-muted">Maximum 500 characters</small>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</div>
