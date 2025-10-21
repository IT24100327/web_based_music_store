<div class="modal fade" id="addTrackModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/artist/add-track" method="POST"
                  enctype="multipart/form-data">
                <div class="modal-header">
                    <h5 class="modal-title">Upload New Track</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="form-group mb-3">
                        <label class="form-label">Track Title *</label>
                        <input type="text" name="title" class="form-control" required>
                    </div>
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label">Price (Rs.) *</label>
                            <input type="number" name="price" class="form-control" step="0.01" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Genre *</label>
                            <input type="text" name="genre" class="form-control" required>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label">Duration (seconds) *</label>
                            <input type="number" name="duration" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Release Date *</label>
                            <input type="date" name="release_date" class="form-control" required>
                        </div>
                    </div>
                    <div class="form-group mb-3">
                        <label class="form-label">Audio File (MP3, WAV) *</label>
                        <input type="file" name="audioFile" class="form-control" accept=".mp3,.wav" required>
                    </div>
                    <div class="form-group mb-3">
                        <label class="form-label">Cover Art (JPG, PNG)</label>
                        <input type="file" name="coverArtFile" class="form-control" accept=".jpg,.jpeg,.png">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Upload Track</button>
                </div>
            </form>
        </div>
    </div>
</div>