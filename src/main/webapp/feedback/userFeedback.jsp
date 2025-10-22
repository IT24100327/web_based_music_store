<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>My Feedback - RhythmWave</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/feedback-support.css">
</head>
<body>

<!-- Include navbar -->
<jsp:include page="/includes/navbar.jsp">
    <jsp:param name="page" value="feedback"/>
    <jsp:param name="user" value="${sessionScope.USER}"/>
</jsp:include>

<header>
    <div class="wrap" style="display:flex;align-items:center;justify-content:space-between;gap:16px">
        <div>
            <div class="breadcrumb">
                <a href="${pageContext.request.contextPath}/">Home</a>
                <span>></span>
                <a href="${pageContext.request.contextPath}/FeedbackServlet?action=viewPublicFeedback">Feedback</a>
                <span>></span>
                <span>My Feedback</span>
            </div>
            <h1>My Feedback History</h1>
            <p class="muted" style="margin-top:8px">Track your feedback submissions and responses</p>
        </div>
        <div class="stack">
            <a href="${pageContext.request.contextPath}/feedback/feedbackForm.jsp" class="btn primary">
                <i class="fas fa-plus"></i> Submit New Feedback
            </a>
            <a href="${pageContext.request.contextPath}/FeedbackServlet?action=viewPublicFeedback" class="btn ghost">
                <i class="fas fa-eye"></i> View Community Feedback
            </a>
        </div>
    </div>
</header>

<main class="wrap" style="margin-top:16px">

    <!-- Success/Error Messages -->
    <c:if test="${not empty requestScope.success}">
        <div class="alert success">
            <i class="fas fa-check-circle"></i> ${requestScope.success}
        </div>
    </c:if>

    <c:if test="${not empty requestScope.error}">
        <div class="alert error">
            <i class="fas fa-exclamation-circle"></i> ${requestScope.error}
        </div>
    </c:if>

    <!-- User Feedback Statistics -->
    <c:if test="${not empty requestScope.userFeedback}">
        <section class="kpis" style="margin-bottom: 24px">
            <div class="kpi">
                <h3>Total Feedback</h3>
                <p><c:out value="${fn:length(requestScope.userFeedback)}"/></p>
            </div>
            <div class="kpi">
                <h3>Average Rating</h3>
                <p>
                    <c:set var="totalRating" value="0"/>
                    <c:set var="ratingCount" value="0"/>
                    <c:forEach var="feedback" items="${requestScope.userFeedback}">
                        <c:if test="${feedback.rating > 0}">
                            <c:set var="totalRating" value="${totalRating + feedback.rating}"/>
                            <c:set var="ratingCount" value="${ratingCount + 1}"/>
                        </c:if>
                    </c:forEach>
                    <c:choose>
                        <c:when test="${ratingCount > 0}">
                            <fmt:formatNumber value="${totalRating / ratingCount}" pattern="#.#"/>
                            <span style="font-size: 14px; color: var(--accent);">★</span>
                        </c:when>
                        <c:otherwise>N/A</c:otherwise>
                    </c:choose>
                </p>
            </div>
            <div class="kpi">
                <h3>Read by Admin</h3>
                <p>
                    <c:set var="readCount" value="0"/>
                    <c:forEach var="feedback" items="${requestScope.userFeedback}">
                        <c:if test="${feedback.read}">
                            <c:set var="readCount" value="${readCount + 1}"/>
                        </c:if>
                    </c:forEach>
                    ${readCount}
                </p>
            </div>
            <div class="kpi">
                <h3>With Responses</h3>
                <p>
                    <c:set var="responseCount" value="0"/>
                    <c:forEach var="feedback" items="${requestScope.userFeedback}">
                        <c:if test="${not empty feedback.adminNotes}">
                            <c:set var="responseCount" value="${responseCount + 1}"/>
                        </c:if>
                    </c:forEach>
                    ${responseCount}
                </p>
            </div>
        </section>
    </c:if>

    <!-- Feedback List -->
    <section class="card">
        <div class="body" style="padding: 0;">
            <c:choose>
                <c:when test="${not empty requestScope.userFeedback}">
                    <c:forEach var="feedback" items="${requestScope.userFeedback}" varStatus="status">
                        <div class="feedback-item" style="padding: 18px; ${not status.last ? 'border-bottom: 1px solid var(--border);' : ''}">
                            <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 12px;">
                                <div style="flex: 1;">
                                    <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 8px;">
                                        <span class="pill ${feedback.feedbackType eq 'general' ? 'done' : feedback.feedbackType eq 'feature_request' ? 'progress' : 'open'}">
                                            <c:choose>
                                                <c:when test="${feedback.feedbackType eq 'general'}">General</c:when>
                                                <c:when test="${feedback.feedbackType eq 'feature_request'}">Feature Request</c:when>
                                                <c:when test="${feedback.feedbackType eq 'bug_report'}">Bug Report</c:when>
                                                <c:otherwise>${feedback.feedbackType}</c:otherwise>
                                            </c:choose>
                                        </span>
                                        <c:if test="${feedback.rating > 0}">
                                            <div style="display: flex; align-items: center; gap: 4px;">
                                                <c:forEach begin="1" end="${feedback.rating}">
                                                    <span style="color: #fbbf24; font-size: 14px;">★</span>
                                                </c:forEach>
                                                <c:forEach begin="${feedback.rating + 1}" end="5">
                                                    <span style="color: #64748b; font-size: 14px;">☆</span>
                                                </c:forEach>
                                            </div>
                                        </c:if>
                                        <span class="pill ${feedback.read ? 'done' : 'open'}" style="font-size: 11px;">
                                            <i class="fas ${feedback.read ? 'fa-check' : 'fa-clock'}"></i>
                                            ${feedback.read ? 'Read' : 'Pending'}
                                        </span>
                                    </div>
                                </div>
                                <div style="display: flex; gap: 8px; align-items: flex-start;">
                                    <c:if test="${!feedback.read}">
                                        <a href="${pageContext.request.contextPath}/FeedbackServlet?action=editFeedback&feedbackId=${feedback.feedbackId}"
                                           class="btn ghost" style="padding: 8px 12px; font-size: 12px;" title="Edit Feedback">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <button type="button" class="btn" style="background: var(--danger); color: white; padding: 8px 12px; font-size: 12px;"
                                                onclick="confirmDelete(${feedback.feedbackId})" title="Delete Feedback">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </c:if>
                                    <c:if test="${feedback.read}">
                                        <span style="font-size: 12px; color: var(--muted); padding: 8px;">
                                            <i class="fas fa-lock"></i> Cannot edit (read by admin)
                                        </span>
                                    </c:if>
                                </div>
                            </div>
                            <div style="flex: 1;">
                                <div>
                                    <h3 style="font-size: 16px; margin: 0 0 4px 0; color: var(--text);">
                                        ${feedback.subject}
                                    </h3>
                                    <p style="font-size: 13px; color: var(--muted); margin: 0;">
                                        Submitted on
                                        <c:choose>
                                            <c:when test="${not empty feedback.formattedDate}">
                                                ${feedback.formattedDate}
                                            </c:when>
                                            <c:otherwise>
                                                <fmt:formatDate value="${feedback.submittedDate}" pattern="MMM dd, yyyy 'at' HH:mm"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>
                            </div>
                            <p style="margin: 0 0 12px 0; line-height: 1.5; color: var(--text);">
                                ${fn:escapeXml(feedback.message)}
                            </p>
                            <c:if test="${not empty feedback.adminNotes}">
                                <div style="margin-top: 12px; padding: 12px; background: rgba(99, 102, 241, 0.1); border-radius: 8px; border-left: 3px solid var(--accent);">
                                    <p style="font-size: 13px; color: var(--muted); margin: 0 0 4px 0;">
                                        <i class="fas fa-reply"></i> Admin Response:
                                    </p>
                                    <p style="margin: 0; font-size: 14px; color: var(--text);">
                                        ${fn:escapeXml(feedback.adminNotes)}
                                    </p>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div style="text-align: center; padding: 48px 18px;">
                        <i class="fas fa-comment-dots" style="font-size: 48px; color: var(--muted); margin-bottom: 16px;"></i>
                        <h3 style="color: var(--text); margin: 0 0 8px 0;">No feedback submitted yet</h3>
                        <p style="color: var(--muted); margin: 0 0 16px 0;">Share your thoughts and help us improve RhythmWave!</p>
                        <a href="${pageContext.request.contextPath}/feedback/feedbackForm.jsp" class="btn primary">
                            <i class="fas fa-plus"></i> Submit Your First Feedback
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </section>

    <!-- Action Buttons -->
    <c:if test="${not empty requestScope.userFeedback}">
        <div style="text-align: center; margin-top: 24px;">
            <div class="stack" style="justify-content: center;">
                <a href="${pageContext.request.contextPath}/feedback/feedbackForm.jsp" class="btn primary">
                    <i class="fas fa-plus"></i> Submit More Feedback
                </a>
                <a href="${pageContext.request.contextPath}/FeedbackServlet?action=viewPublicFeedback" class="btn ghost">
                    <i class="fas fa-users"></i> View Community Feedback
                </a>
            </div>
        </div>
    </c:if>

</main>

<!-- Include footer -->
<jsp:include page="/includes/footer.jsp" />

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

<script>
    function confirmDelete(feedbackId) {
        if (confirm('Are you sure you want to delete this feedback? This action cannot be undone.')) {
            window.location.href = '${pageContext.request.contextPath}/FeedbackServlet?action=deleteUserFeedback&feedbackId=' + feedbackId;
        }
    }
</script>

</body>
</html>