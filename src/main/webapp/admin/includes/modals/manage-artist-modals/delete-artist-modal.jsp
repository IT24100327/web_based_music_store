<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:set var="contextPath" value="${param.contextPath}"/>

<div class="modal fade" id="deleteArtistModal" tabindex="-1" aria-labelledby="deleteArtistModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteArtistModalLabel">Delete Artist</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="deleteArtistForm" action="${contextPath}/delete-artist" method="POST">
                <div class="modal-body">
                    <input type="hidden" id="deleteArtistId" name="userId">
                    <p>Are you sure you want to delete artist <strong id="deleteArtistName"></strong>?</p>
                    <p class="text-muted">This action cannot be undone. All artist data including bio and genres will be
                        permanently removed.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-danger">Delete Artist</button>
                </div>
            </form>
        </div>
    </div>
</div>