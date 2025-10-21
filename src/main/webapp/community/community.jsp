<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Community Feed</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
<%--    <link rel="stylesheet" href="${pageContext.request.contextPath}/community/community-styles.css">--%>

    <style>
        /* Ensure community page has proper spacing */
        .community-main {
            min-height: calc(100vh - 200px);
            padding-bottom: 2rem;
        }

        /* Make sure images don't overflow */
        .post-images img {
            max-width: 100%;
            height: auto;
        }
    </style>
</head>

<body>
<jsp:include page="/includes/navbar.jsp">
    <jsp:param name="page" value="index"/>
    <jsp:param name="user" value="${sessionScope.USER}"/>
</jsp:include>

<main class="container mt-4 community-main">
    <div class="section-header">
        <h2>Community Feed</h2>
        <c:if test="${not empty USER}">
            <a href="${pageContext.request.contextPath}/community/create" class="btn btn-primary">
                <i class="fas fa-plus"></i> Create Post
            </a>
        </c:if>
    </div>

    <c:if test="${empty posts}">
        <div class="alert alert-info" role="alert">
            <i class="fas fa-info-circle"></i> No community posts have been made yet. Be the first!
        </div>
    </c:if>

    <div class="posts-grid">
        <c:forEach items="${posts}" var="post">
            <div class="post-card">
                <div class="post-header">
                    <h3><a href="${pageContext.request.contextPath}/community/post/${post.postId}">${fn:escapeXml(post.title)}</a></h3>
                    <span class="post-author">by ${fn:escapeXml(post.authorName)}</span>
                </div>
                <div class="post-content">
                    <p>${fn:escapeXml(post.description)}</p>
                </div>
                <div class="post-images">
                    <c:if test="${not empty post.image1Path}">
                        <img src="${pageContext.request.contextPath}/${post.image1Path}" alt="Post image 1" loading="lazy">
                    </c:if>
                    <c:if test="${not empty post.image2Path}">
                        <img src="${pageContext.request.contextPath}/${post.image2Path}" alt="Post image 2" loading="lazy">
                    </c:if>
                    <c:if test="${not empty post.image3Path}">
                        <img src="${pageContext.request.contextPath}/${post.image3Path}" alt="Post image 3" loading="lazy">
                    </c:if>
                </div>
                <div class="post-footer">
                    <span class="post-date">Posted on: ${post.createdAt}</span>
                </div>
            </div>
        </c:forEach>
    </div>
</main>

<jsp:include page="/includes/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<!-- Add Font Awesome for icons -->
<script src="https://kit.fontawesome.com/your-fontawesome-kit.js" crossorigin="anonymous"></script>
</body>
</html>