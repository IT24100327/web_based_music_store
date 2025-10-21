<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="contextPath" value="${param.contextPath}"/>

<div class="modal fade" id="addAdvertisementModal" tabindex="-1" aria-labelledby="addAdvertisementModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addAdvertisementModalLabel">Add New Advertisement</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="addAdvertisementForm" action="${contextPath}/add-advertisement" method="POST"
                  enctype="multipart/form-data">
                <div class="modal-body">
                    <input type="hidden" name="action" value="ad_add">
                    <div class="form-group">
                        <label for="addAdTitle">Title</label>
                        <input type="text" class="form-control" id="addAdTitle" name="title" required>
                    </div>
                    <div class="form-group">
                        <label for="addAdContent">Content</label>
                        <input type="text" class="form-control" id="addAdContent" name="content" required>
                    </div>
                    <div class="form-group">
                        <label for="addAdImage" class="form-label">Image</label>
                        <input type="file" class="form-control" id="addAdImage" name="imageFile"
                               accept="image/png,image/jpeg,image/jpg">
                    </div>
                    <div class="form-group">
                        <label for="addAdStartDate">Start Date</label>
                        <input type="date" class="form-control" id="addAdStartDate" name="startDate" required>
                    </div>
                    <div class="form-group">
                        <label for="addAdEndDate">End Date</label>
                        <input type="date" class="form-control" id="addAdEndDate" name="endDate" required>
                    </div>
                    <div class="form-group">
                        <label for="addAdActive">Active</label>
                        <select class="form-control" id="addAdActive" name="active" required>
                            <option value="true">Active</option>
                            <option value="false">Inactive</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Add Advertisement</button>
                </div>
            </form>
        </div>
    </div>
</div>