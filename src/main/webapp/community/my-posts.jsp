<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Posts</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/community/community-styles.css">
</head>
<body>

<jsp:include page="/includes/navbar.jsp">
    <jsp:param name="page" value="community"/>
    <jsp:param name="user" value="${sessionScope.USER}"/>
</jsp:include>

<main class="container mt-4">
    <div class="section-header">
        <h2>My Posts</h2>
        <a href="${pageContext.request.contextPath}/community/create" class="btn btn-primary">
            <i class="fas fa-plus"></i> Create New Post
        </a>
    </div>

    <c:if test="${empty myPosts}">
        <div class="alert alert-info text-center" role="alert">
            You haven't created any posts yet.
        </div>
    </c:if>

    <div class="posts-list">
        <c:forEach items="${myPosts}" var="post">
            <div class="post-item">
                <div class="post-item-header">
                    <div>
                        <h3>${fn:escapeXml(post.title)}</h3>
                        <span class="status-badge status-${fn:toLowerCase(post.status)}">${fn:escapeXml(post.status)}</span>
                    </div>
                    <div class="post-item-actions">
                        <a href="${pageContext.request.contextPath}/community/edit?postId=${post.postId}" class="btn btn-secondary btn-sm">Edit</a>
                        <form action="${pageContext.request.contextPath}/community/delete" method="POST" onsubmit="return confirm('Are you sure you want to delete this post?');">
                            <input type="hidden" name="postId" value="${post.postId}">
                            <button type="submit" class="btn btn-delete btn-sm">Delete</button>
                        </form>
                    </div>
                </div>
                <p class="post-description">${fn:escapeXml(post.description)}</p>
            </div>
        </c:forEach>
    </div>
</main>

<jsp:include page="/includes/footer.jsp" />
</body>
</html>