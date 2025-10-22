<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin - Community Post Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
</head>
<body>
<div class="admin-container">
    <jsp:include page="/admin/includes/admin_nav_bar.jsp">
        <jsp:param name="page" value="manage-posts"/>
    </jsp:include>

    <main class="admin-main">
        <header class="admin-header">
            <h1>Community Management</h1>
        </header>

        <div class="table-container">
            <div class="table-header">
                <h3>All Community Posts</h3>
            </div>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Author</th>
                    <th>Title</th>
                    <th>Status</th>
                    <th>Created At</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="post" items="${allPosts}">
                    <tr>
                        <td>${post.postId}</td>
                        <td>${fn:escapeXml(post.authorName)}</td>
                        <td>${fn:escapeXml(post.title)}</td>
                        <td>
                            <span class="status-badge ${post.status == 'pending' ? 'status-pending' : (post.status == 'approved' ? 'status-active' : 'status-inactive')}">
                                    ${fn:escapeXml(post.status)}
                            </span>
                        </td>
                        <td>${post.createdAt}</td>
                        <td class="actions">
                            <c:if test="${post.status == 'pending'}">
                                <form action="${pageContext.request.contextPath}/posts/approve" method="POST" class="d-inline">
                                    <input type="hidden" name="postId" value="${post.postId}">
                                    <button type="submit" name="action" value="approve" class="btn btn-success btn-sm">Approve</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/posts/approve" method="POST" class="d-inline">
                                    <input type="hidden" name="postId" value="${post.postId}">
                                    <button type="submit" name="action" value="reject" class="btn btn-warning btn-sm">Reject</button>
                                </form>
                            </c:if>
                            <form action="${pageContext.request.contextPath}/community/delete" method="POST" class="d-inline">
                                <input type="hidden" name="postId" value="${post.postId}">
                                <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>