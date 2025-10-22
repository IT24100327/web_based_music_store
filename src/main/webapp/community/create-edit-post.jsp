<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <c:set var="isEdit" value="${not empty post}" />
    <title>${isEdit ? 'Edit' : 'Create'} Post</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
<%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/community/community-styles.css">--%>
</head>
<body>

<jsp:include page="/includes/navbar.jsp">
    <jsp:param name="page" value="community"/>
    <jsp:param name="user" value="${sessionScope.USER}"/>
</jsp:include>

<main class="container mt-4">
    <div class="form-container">
        <h2>${isEdit ? 'Edit Your Post' : 'Create a New Post'}</h2>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/community/${isEdit ? 'edit' : 'create'}" method="POST" enctype="multipart/form-data">
            <c:if test="${isEdit}">
                <input type="hidden" name="postId" value="${post.postId}">
            </c:if>

            <div class="form-group mb-3">
                <label for="title" class="form-label">Title</label>
                <input type="text" class="form-control" id="title" name="title" value="${fn:escapeXml(post.title)}" required>
            </div>

            <div class="form-group mb-3">
                <label for="description" class="form-label">Description</label>
                <textarea class="form-control" id="description" name="description" rows="5" required>${fn:escapeXml(post.description)}</textarea>
            </div>

            <div class="form-group mb-3">
                <label for="image1" class="form-label">Upload Images (Optional)</label>
                <input type="file" class="form-control" id="image1" name="image1" accept="image/*">
                <input type="file" class="form-control mt-2" id="image2" name="image2" accept="image/*">
                <input type="file" class="form-control mt-2" id="image3" name="image3" accept="image/*">
            </div>

            <button type="submit" class="btn btn-primary w-100">${isEdit ? 'Save Changes' : 'Submit Post'}</button>
        </form>
    </div>
</main>

<jsp:include page="/includes/footer.jsp" />
</body>
</html>