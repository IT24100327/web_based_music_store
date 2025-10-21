<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Post - Community Feed</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
<%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/community/community-styles.css">--%>
</head>
<body>

<jsp:include page="/includes/navbar.jsp">
    <jsp:param name="page" value="index"/>
    <jsp:param name="user" value="${sessionScope.USER}"/>
</jsp:include>

<div class="container">
    <div class="form-container">
        <h2>Create New Post</h2>

        <c:if test="${not empty error}">
            <div class="alert alert-error">
                    ${error}
            </div>
        </c:if>

        <c:if test="${not sessionScope.USER.admin}">
            <div class="alert alert-info">
                Your post will be reviewed by an admin before appearing on the feed.
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/posts/create" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label for="title">Title: *</label>
                <input type="text" id="title" name="title" required maxlength="200">
            </div>

            <div class="form-group">
                <label for="description">Description: *</label>
                <textarea id="description" name="description" rows="6" required></textarea>
            </div>

            <div class="form-group">
                <label for="image1">Image 1 (Optional):</label>
                <input type="file" id="image1" name="image1" accept="image/*">
            </div>

            <div class="form-group">
                <label for="image2">Image 2 (Optional):</label>
                <input type="file" id="image2" name="image2" accept="image/*">
            </div>

            <div class="form-group">
                <label for="image3">Image 3 (Optional):</label>
                <input type="file" id="image3" name="image3" accept="image/*">
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Create Post</button>
                <a href="${pageContext.request.contextPath}/feed" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
