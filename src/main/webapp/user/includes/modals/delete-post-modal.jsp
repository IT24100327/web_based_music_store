<div class="modal fade" id="deletePostModal" tabindex="-1" aria-labelledby="deletePostModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deletePostModalLabel">Confirm Deletion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="deletePostForm" action="${pageContext.request.contextPath}/community/delete" method="POST">
                <div class="modal-body">
                    <input type="hidden" id="deletePostId" name="postId">
                    <p>Are you sure you want to permanently delete the post titled "<strong id="deletePostTitle"></strong>"?</p>
                    <p class="text-danger">This action cannot be undone.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-danger">Delete Post</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    const deletePostModal = document.getElementById('deletePostModal');
    deletePostModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const postId = button.getAttribute('data-post-id');
        const postTitle = button.getAttribute('data-post-title');

        const modalTitle = deletePostModal.querySelector('#deletePostTitle');
        const modalInput = deletePostModal.querySelector('#deletePostId');

        modalTitle.textContent = postTitle;
        modalInput.value = postId;
    });
</script>