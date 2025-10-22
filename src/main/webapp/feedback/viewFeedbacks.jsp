<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Community Feedback - RhythmWave</title>
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
                <span>Community Feedback</span>
            </div>
            <h1>Community Feedback</h1>
            <p class="muted" style="margin-top:8px">See what our community is saying about RhythmWave</p>
        </div>
        <div class="stack">
            <a href="${pageContext.request.contextPath}/feedback/feedbackForm.jsp" class="btn primary">
                <i class="fas fa-plus"></i> Share Your Feedback
            </a>
            <c:if test="${not empty sessionScope.USER}">
                <a href="${pageContext.request.contextPath}/FeedbackServlet?action=userFeedback" class="btn ghost">
                    <i class="fas fa-user"></i> My Feedback
                </a>
            </c:if>
        </div>
    </div>
</header>

<main class="wrap" style="margin-top:16px">

    <!-- Statistics Section -->
    <section class="kpis" style="margin-bottom: 24px">
        <div class="kpi">
            <h3>Total Feedback</h3>
            <p><c:out value="${requestScope.totalFeedback}" default="0"/></p>
        </div>
        <div class="kpi">
            <h3>Average Rating</h3>
            <p>
                <c:choose>
                    <c:when test="${not empty requestScope.averageRating}">
                        <fmt:formatNumber value="${requestScope.averageRating}" pattern="#.#"/>
                        <span style="font-size: 14px; color: var(--accent);">★</span>
                    </c:when>
                    <c:otherwise>N/A</c:otherwise>
                </c:choose>
            </p>
        </div>
        <div class="kpi">
            <h3>Recent Feedback</h3>
            <p><c:out value="${requestScope.recentFeedback}" default="0"/></p>
        </div>
        <div class="kpi">
            <h3>Happy Users</h3>
            <p><c:out value="${requestScope.positiveRatings}" default="0"/>%</p>
        </div>
    </section>

    <!-- Filter Tabs -->
    <div class="tabs">
        <div class="tab ${param.filter eq 'all' or empty param.filter ? 'active' : ''}"
             onclick="filterFeedback('all')">
            All Feedback
        </div>
        <div class="tab ${param.filter eq 'general' ? 'active' : ''}"
             onclick="filterFeedback('general')">
            General
        </div>
        <div class="tab ${param.filter eq 'feature_request' ? 'active' : ''}"
             onclick="filterFeedback('feature_request')">
            Feature Requests
        </div>
        <div class="tab ${param.filter eq 'bug_report' ? 'active' : ''}"
             onclick="filterFeedback('bug_report')">
            Bug Reports
        </div>
    </div>

    <!-- Feedback List -->
    <section class="card" style="margin-top: 0; border-radius: 0 0 16px 16px;">
        <div class="body" style="padding: 0;">
            <c:choose>
                <c:when test="${not empty requestScope.feedbackList}">
                    <c:forEach var="feedback" items="${requestScope.feedbackList}" varStatus="status">
                        <div class="feedback-item" style="padding: 18px; ${not status.last ? 'border-bottom: 1px solid var(--border);' : ''}">
                            <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 12px;">
                                <div>
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
                                    </div>
                                    <h3 style="font-size: 16px; margin: 0 0 4px 0; color: var(--text);">
                                        ${feedback.subject}
                                    </h3>
                                    <p style="font-size: 13px; color: var(--muted); margin: 0;">
                                        By <c:out value="${feedback.userName}" default="Anonymous"/> •
                                        <c:out value="${feedback.formattedDate}" default="Unknown date"/>
                                    </p>
                                </div>
                            </div>
                            <p style="margin: 0; line-height: 1.5; color: var(--text);">
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
                        <i class="fas fa-comments" style="font-size: 48px; color: var(--muted); margin-bottom: 16px;"></i>
                        <h3 style="color: var(--text); margin: 0 0 8px 0;">No feedback yet</h3>
                        <p style="color: var(--muted); margin: 0 0 16px 0;">Be the first to share your thoughts about RhythmWave!</p>
                        <a href="${pageContext.request.contextPath}/feedback/feedbackForm.jsp" class="btn primary">
                            <i class="fas fa-plus"></i> Share Feedback
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </section>

    <!-- Pagination -->
    <c:if test="${requestScope.totalPages > 1}">
        <div style="text-align: center; margin-top: 24px;">
            <div class="stack" style="justify-content: center;">
                <c:if test="${requestScope.currentPage > 1}">
                    <a href="?page=${requestScope.currentPage - 1}&filter=${param.filter}" class="btn ghost">
                        <i class="fas fa-chevron-left"></i> Previous
                    </a>
                </c:if>

                <span style="padding: 12px 16px; color: var(--muted);">
                    Page ${requestScope.currentPage} of ${requestScope.totalPages}
                </span>

                <c:if test="${requestScope.currentPage < requestScope.totalPages}">
                    <a href="?page=${requestScope.currentPage + 1}&filter=${param.filter}" class="btn ghost">
                        Next <i class="fas fa-chevron-right"></i>
                    </a>
                </c:if>
            </div>
        </div>
    </c:if>

</main>

<!-- Include footer -->
<jsp:include page="/includes/footer.jsp" />

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

<script>
    function filterFeedback(type) {
        const currentUrl = new URL(window.location);
        if (type === 'all') {
            currentUrl.searchParams.delete('filter');
        } else {
            currentUrl.searchParams.set('filter', type);
        }
        currentUrl.searchParams.delete('page'); // Reset to first page when filtering
        window.location.href = currentUrl.toString();
    }
</script>

</body>
</html>