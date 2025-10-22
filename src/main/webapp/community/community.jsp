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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/community/community-styles.css">
</head>

<body>
<jsp:include page="/includes/navbar.jsp">
    <jsp:param name="page" value="community"/>
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

                    <%-- Image Slideshow Logic --%>
                <c:set var="imageCount" value="${(not empty post.image1Data ? 1 : 0) + (not empty post.image2Data ? 1 : 0) + (not empty post.image3Data ? 1 : 0)}" />
                <c:if test="${imageCount > 0}">
                    <div class="post-images">
                        <div id="carouselPost${post.postId}" class="carousel slide">
                                <%-- Show controls only if there is more than one image --%>
                            <c:if test="${imageCount > 1}">
                                <div class="carousel-indicators">
                                    <c:if test="${not empty post.image1Data}"><button type="button" data-bs-target="#carouselPost${post.postId}" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button></c:if>
                                    <c:if test="${not empty post.image2Data}"><button type="button" data-bs-target="#carouselPost${post.postId}" data-bs-slide-to="1" aria-label="Slide 2"></button></c:if>
                                    <c:if test="${not empty post.image3Data}"><button type="button" data-bs-target="#carouselPost${post.postId}" data-bs-slide-to="2" aria-label="Slide 3"></button></c:if>
                                </div>
                            </c:if>

                            <div class="carousel-inner">
                                <c:if test="${not empty post.image1Data}">
                                    <div class="carousel-item active">
                                        <img src="${pageContext.request.contextPath}/post-image?postId=${post.postId}&index=1" class="d-block w-100" alt="Post Image 1">
                                    </div>
                                </c:if>
                                <c:if test="${not empty post.image2Data}">
                                    <div class="carousel-item ${empty post.image1Data ? 'active' : ''}">
                                        <img src="${pageContext.request.contextPath}/post-image?postId=${post.postId}&index=2" class="d-block w-100" alt="Post Image 2">
                                    </div>
                                </c:if>
                                <c:if test="${not empty post.image3Data}">
                                    <div class="carousel-item ${empty post.image1Data && empty post.image2Data ? 'active' : ''}">
                                        <img src="${pageContext.request.contextPath}/post-image?postId=${post.postId}&index=3" class="d-block w-100" alt="Post Image 3">
                                    </div>
                                </c:if>
                            </div>

                            <c:if test="${imageCount > 1}">
                                <button class="carousel-control-prev" type="button" data-bs-target="#carouselPost${post.postId}" data-bs-slide="prev">
                                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Previous</span>
                                </button>
                                <button class="carousel-control-next" type="button" data-bs-target="#carouselPost${post.postId}" data-bs-slide="next">
                                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Next</span>
                                </button>
                            </c:if>
                        </div>
                    </div>
                </c:if>

                <div class="post-content">
                    <p>${fn:escapeXml(post.description)}</p>
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
</body>
</html>