<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="table-container">
    <div class="table-header">
        <h3>My Posts</h3>
        <a href="${pageContext.request.contextPath}/profile?view=create-post" class="btn btn-primary">
            <i class="fas fa-plus me-2"></i>Create New Post
        </a>
    </div>

    <c:if test="${not empty param.success}">
        <div class="alert alert-success">${param.success}</div>
    </c:if>

    <div class="posts-list">
        <c:choose>
            <c:when test="${not empty myPosts}">
                <c:forEach var="post" items="${myPosts}">
                    <div class="post-item">
                        <div class="post-item-header">
                            <h3>${fn:escapeXml(post.title)}</h3>
                            <div class="post-item-actions">
                                <span class="status-badge status-${fn:escapeXml(post.status)}">${fn:escapeXml(post.status)}</span>
                                <a href="${pageContext.request.contextPath}/community/edit?postId=${post.postId}" class="btn btn-secondary btn-sm"><i class="fas fa-edit"></i> Edit</a>
                                <button class="btn btn-delete btn-sm" data-bs-toggle="modal" data-bs-target="#deletePostModal" data-post-id="${post.postId}" data-post-title="${fn:escapeXml(post.title)}">
                                    <i class="fas fa-trash"></i> Delete
                                </button>
                            </div>
                        </div>
                        <p class="post-description">${fn:escapeXml(post.description)}</p>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="text-center p-5">
                    <h4>You haven't created any posts yet.</h4>
                    <a href="${pageContext.request.contextPath}/profile?view=create-post" class="btn btn-primary mt-3">Create Your First Post</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/user/includes/modals/delete-post-modal.jsp"/>