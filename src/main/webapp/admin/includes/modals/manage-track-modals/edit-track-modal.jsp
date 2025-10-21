<div class="modal fade" id="editTrackModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/artist/update-track" method="POST"
                  enctype="multipart/form-data">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Track Details</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="editTrackId" name="trackId">
                    <div class="form-group mb-3">
                        <label class="form-label">Track Title *</label>
                        <input type="text" id="editTrackTitle" name="title" class="form-control" required>
                    </div>
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label">Price (Rs.) *</label>
                            <input type="number" id="editTrackPrice" name="price" class="form-control" step="0.01"
                                   required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Genre *</label>
                            <input type="text" id="editTrackGenre" name="genre" class="form-control" required>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label">Duration (seconds) *</label>
                            <input type="number" id="editTrackDuration" name="duration" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Release Date *</label>
                            <input type="date" id="editTrackReleaseDate" name="release_date" class="form-control"
                                   required>
                        </div>
                    </div>
                    <div class="form-group mb-3">
                        <label class="form-label">Replace Audio File (Optional)</label>
                        <input type="file" name="audioFile" class="form-control" accept=".mp3,.wav">
                    </div>
                    <div class="form-group">
                        <label class="form-label">Replace Cover Art (Optional)</label>
                        <input type="file" name="coverArtFile" class="form-control" accept=".jpg,.jpeg,.png">
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