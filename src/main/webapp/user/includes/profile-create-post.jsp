<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<div class="table-container">
    <div class="table-header">
        <h3>Create a New Post</h3>
    </div>

    <form action="${pageContext.request.contextPath}/community/create" method="POST" enctype="multipart/form-data">
        <div class="form-group mb-3">
            <label for="title" class="form-label">Title</label>
            <input type="text" class="form-control" id="title" name="title" required>
        </div>
        <div class="form-group mb-3">
            <label for="description" class="form-label">Description</label>
            <textarea class="form-control" id="description" name="description" rows="5" required></textarea>
        </div>
        <div class="form-group mb-3">
            <label for="image1" class="form-label">Image 1 (Optional)</label>
            <input type="file" class="form-control" id="image1" name="image1" accept="image/*">
        </div>
        <div class="form-group mb-3">
            <label for="image2" class="form-label">Image 2 (Optional)</label>
            <input type="file" class="form-control" id="image2" name="image2" accept="image/*">
        </div>
        <div class="form-group mb-3">
            <label for="image3" class="form-label">Image 3 (Optional)</label>
            <input type="file" class="form-control" id="image3" name="image3" accept="image/*">
        </div>
        <div class="form-actions mt-4">
            <a href="${pageContext.request.contextPath}/profile?view=my-posts" class="btn btn-secondary">Cancel</a>
            <button type="submit" class="btn btn-primary">Submit for Review</button>
        </div>
    </form>
</div>