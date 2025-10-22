<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Feedback Details</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
<div class="admin-container">
    <jsp:include page="includes/admin_nav_bar.jsp">
        <jsp:param name="page" value="manage-artists"/>
    </jsp:include>
    <main class="admin-main">
        <div class="admin-header">
            <h1><i class="fas fa-comment-dots"></i> Feedback Details</h1>
        </div>

        <c:if test="${not empty feedback}">
            <div class="table-container">
                <h3>Details for Feedback #${feedback.feedbackId}</h3>
                <table class="table" style="margin-top: 1.5rem;">
                    <tbody>
                    <tr>
                        <th style="width: 20%;">User</th>
                        <td>
                            <c:choose>
                                <c:when test="${not empty feedback.userName}">${feedback.userName} (ID: ${feedback.userId})</c:when>
                                <c:otherwise>Anonymous (${feedback.email})</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <th>Submitted On</th>
                        <td>${feedback.submittedDate}</td>
                    </tr>
                    <tr>
                        <th>Type</th>
                        <td>${feedback.feedbackType}</td>
                    </tr>
                    <tr>
                        <th>Rating</th>
                        <td>
                            <c:if test="${feedback.rating > 0}">${feedback.rating} / 5 Stars</c:if>
                            <c:if test="${feedback.rating == 0}"><span class="text-muted">Not provided</span></c:if>
                        </td>
                    </tr>
                    <tr>
                        <th>Subject</th>
                        <td><c:out value="${feedback.subject}"/></td>
                    </tr>
                    <tr>
                        <th>Message</th>
                        <td><p style="white-space: pre-wrap;"><c:out value="${feedback.message}"/></p></td>
                    </tr>
                        <%-- Context Section --%>
                    <tr>
                        <th>Related To</th>
                        <td>
                            <c:if test="${feedback.orderId > 0}">
                                <a href="${pageContext.request.contextPath}/admin/orderDetails?orderId=${feedback.orderId}" class="btn btn-sm btn-secondary">
                                    <i class="fas fa-receipt"></i> View Order #${feedback.orderId}
                                </a>
                            </c:if>
                            <c:if test="${feedback.trackId > 0}">
                                <a href="${pageContext.request.contextPath}/manage-tracks" class="btn btn-sm btn-secondary">
                                    <i class="fas fa-music"></i> Track: ${feedback.trackTitle}
                                </a>
                            </c:if>
                            <c:if test="${feedback.orderId == 0 && feedback.trackId == 0}">
                                <span class="text-muted">General Feedback</span>
                            </c:if>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div class="table-container">
                <h3>Admin Notes</h3>
                <form action="${pageContext.request.contextPath}/FeedbackServlet" method="post">
                    <input type="hidden" name="action" value="addAdminNotes">
                    <input type="hidden" name="feedbackId" value="${feedback.feedbackId}">
                    <div class="form-group">
                        <textarea name="adminNotes" class="form-control" rows="4" placeholder="Add internal notes here...">${feedback.adminNotes}</textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">Save Notes</button>
                </form>
            </div>
        </c:if>
        <c:if test="${empty feedback}">
            <div class="alert alert-danger">Feedback not found.</div>
        </c:if>
    </main>
</div>
</body>
</html>