<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="contextPath" value="${param.contextPath}" />

<div class="modal fade" id="deleteAdvertisementModal" tabindex="-1" aria-labelledby="deleteAdvertisementModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteAdvertisementModalLabel">Delete Advertisement</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="deleteAdvertisementForm" action="${contextPath}/marketing" method="POST">
                <div class="modal-body">
                    <input type="hidden" id="deleteAdId" name="adId">
                    <input type="hidden" name="action" value="ad_delete">
                    <p>Are you sure you want to delete advertisement <strong id="deleteAdTitle"></strong>?</p>
                    <p class="text-muted">This action cannot be undone.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-danger">Delete</button>
                </div>
            </form>
        </div>
    </div>
</div>