<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="contextPath" value="${param.contextPath}"/>

<div class="modal fade" id="editAdvertisementModal" tabindex="-1" aria-labelledby="editAdvertisementModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editAdvertisementModalLabel">Edit Advertisement</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="editAdvertisementForm" action="${contextPath}/update-advertisement" method="POST"
                  enctype="multipart/form-data">
                <div class="modal-body">
                    <input type="hidden" id="editAdId" name="adId">
                    <input type="hidden" name="action" value="ad_update">
                    <div class="form-group">
                        <label for="editAdTitle">Title</label>
                        <input type="text" class="form-control" id="editAdTitle" name="title" required>
                    </div>
                    <div class="form-group">
                        <label for="editAdContent">Content</label>
                        <input type="text" class="form-control" id="editAdContent" name="content" required>
                    </div>
                    <div class="form-group">
                        <label for="editAdImage">Image</label>
                        <div class="custom-file-upload">
                            <input type="file" id="editAdImage" name="imageFile"
                                   accept="image/png,image/jpeg,image/jpg">
                            <label for="editAdImage">
                                <span class="file-name">No file chosen</span>
                                <span class="btn">Choose File</span>
                            </label>
                        </div>
                        <!-- New: Image Preview -->
                        <div id="editAdPreviewContainer" class="mt-2" style="display: none;">
                            <label class="form-label">Current Image:</label>
                            <img id="editAdPreview" src="" class="img-thumbnail"
                                 style="max-width: 200px; max-height: 200px;" alt="Current Ad Image">
                            <small class="text-muted">Choose a new file to replace it.</small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="editAdStartDate">Start Date</label>
                        <input type="date" class="form-control" id="editAdStartDate" name="startDate" required>
                    </div>
                    <div class="form-group">
                        <label for="editAdEndDate">End Date</label>
                        <input type="date" class="form-control" id="editAdEndDate" name="endDate" required>
                    </div>
                    <div class="form-group">
                        <label for="editAdActive">Active</label>
                        <select class="form-control" id="editAdActive" name="active" required>
                            <option value="true">Active</option>
                            <option value="false">Inactive</option>
                        </select>
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

<script>
    document.getElementById('editAdImage').addEventListener('change', function () {
        const fileName = this.files.length > 0 ? this.files[0].name : 'No file chosen';
        this.nextElementSibling.querySelector('.file-name').textContent = fileName;
    });
</script>