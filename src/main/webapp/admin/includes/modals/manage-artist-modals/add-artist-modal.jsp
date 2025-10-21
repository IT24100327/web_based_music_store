<div class="modal fade" id="addArtistModal" tabindex="-1" aria-labelledby="addArtistModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addArtistModalLabel">Add New Artist</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="addArtistForm" action="${pageContext.request.contextPath}/add-artist" method="POST">
                <div class="modal-body">
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="addFirstName" class="form-label">First Name *</label>
                                <input type="text" class="form-control" id="addFirstName" name="firstName" required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="addLastName" class="form-label">Last Name *</label>
                                <input type="text" class="form-control" id="addLastName" name="lastName" required>
                            </div>
                        </div>
                    </div>

                    <div class="form-group mb-3">
                        <label for="addEmail" class="form-label">Email *</label>
                        <input type="email" class="form-control" id="addEmail" name="email" required>
                    </div>

                    <div class="form-group mb-3">
                        <label for="addPassword" class="form-label">Password *</label>
                        <input type="password" class="form-control" id="addPassword" name="password" required
                               minlength="4">
                    </div>

                    <div class="form-group mb-3">
                        <label for="addStageName" class="form-label">Stage Name *</label>
                        <input type="text" class="form-control" id="addStageName" name="stageName" required>
                        <small class="text-muted">The professional name the artist will be known by</small>
                    </div>

                    <div class="form-group">
                        <label for="addBio" class="form-label">Artist Bio</label>
                        <textarea class="form-control" id="addBio" name="bio" rows="3"
                                  placeholder="Tell us about the artist..." maxlength="500"></textarea>
                        <small class="text-muted">Maximum 500 characters</small>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Add Artist</button>
                </div>
            </form>
        </div>
    </div>
</div>