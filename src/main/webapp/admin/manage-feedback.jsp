<%-- /webapp/admin/manage-feedback.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Feedback</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
</head>
<body>
<div class="admin-container">
    <jsp:include page="includes/admin_nav_bar.jsp">
        <jsp:param name="page" value="manage-tracks"/>
    </jsp:include>

    <main class="admin-main">
        <header class="admin-header">
            <h1>Feedback Management</h1>
            <div class="user-info">
                <div class="user-avatar"><c:out value="${fn:substring(sessionScope.USER.firstName, 0, 1)}"/></div>
                <div>
                    <div>${sessionScope.USER.firstName} ${sessionScope.USER.lastName}</div>
                    <div class="text-muted">${sessionScope.USER.email}</div>
                </div>
            </div>
        </header>

        <c:if test="${not empty param.success}">
            <div class="alert alert-info">${param.success}</div>
        </c:if>

        <div class="table-container">
            <div class="table-header">
                <h3>All Feedback Submissions</h3>
            </div>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Subject</th>
                    <th>User</th>
                    <th>Type</th>
                    <th>Rating</th>
                    <th>Date</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="feedback" items="${allFeedback}">
                    <tr>
                        <td>#${feedback.feedbackId}</td>
                        <td>${feedback.subject}</td>
                        <td>${not empty feedback.userName ? feedback.userName : feedback.email}</td>
                        <td>${feedback.feedbackType}</td>
                        <td>${feedback.rating > 0 ? feedback.rating : 'N/A'}</td>
                        <td><fmt:formatDate value="${feedback.submittedDateAsUtilDate}" pattern="MMM dd, yyyy"/></td>
                        <td class="actions">
                            <a href="${pageContext.request.contextPath}/FeedbackServlet?action=adminFeedbackDetails&feedbackId=${feedback.feedbackId}" class="btn btn-sm btn-secondary">
                                <i class="fas fa-eye"></i> View
                            </a>
                            <form action="${pageContext.request.contextPath}/FeedbackServlet" method="POST" onsubmit="return confirm('Are you sure you want to delete this feedback?');" style="display: inline;">
                                <input type="hidden" name="action" value="deleteFeedback">
                                <input type="hidden" name="feedbackId" value="${feedback.feedbackId}">
                                <button type="submit" class="btn btn-sm btn-delete">
                                    <i class="fas fa-trash"></i> Delete
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </main>
</div>
</body>
</html>