<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Give Feedback - RhythmWave</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/feedback-support.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

<jsp:include page="/includes/navbar.jsp">
    <jsp:param name="page" value="feedback"/>
    <jsp:param name="user" value="${sessionScope.USER}"/>
</jsp:include>

<div class="form-page-container">
    <div class="form-card card">
        <div class="body">
            <div style="text-align: center; margin-bottom: 24px;">
                <h1><i class="fas fa-comment-dots"></i> Give Feedback</h1>
                <p class="muted">We'd love to hear from you! Let us know what you think.</p>
            </div>

            <form action="${pageContext.request.contextPath}/FeedbackServlet" method="POST">
                <input type="hidden" name="action" value="submitFeedback">

                <c:if test="${param.success == 'true'}">
                    <div class="alert success">Thank you! Your feedback has been submitted successfully.</div>
                </c:if>
                <c:if test="${not empty requestScope.error}">
                    <div class="alert error">${requestScope.error}</div>
                </c:if>

                <div class="grid two">
                    <div>
                        <label for="feedbackType">Feedback Type</label>
                        <select id="feedbackType" name="feedbackType" required>
                            <option value="suggestion">Suggestion</option>
                            <option value="bug_report">Bug Report</option>
                            <option value="compliment">Compliment</option>
                            <option value="other">Other</option>
                        </select>
                    </div>
                    <c:if test="${empty sessionScope.USER}">
                        <div>
                            <label for="email">Your Email</label>
                            <input type="email" id="email" name="email" placeholder="you@example.com" required>
                        </div>
                    </c:if>
                </div>

                <div style="margin-top: 16px;">
                    <label for="subject">Subject</label>
                    <input type="text" id="subject" name="subject" placeholder="A brief summary of your feedback" required>
                </div>

                <div style="margin-top: 16px;">
                    <label for="message">Message</label>
                    <textarea id="message" name="message" rows="5" placeholder="Please provide as much detail as possible..." required></textarea>
                </div>

                <%-- ================= THIS IS THE FIX ================= --%>
                <div style="margin-top: 16px;">
                    <label>Overall Rating (Optional)</label>
                    <div class="stars">
                        <input type="radio" id="star5" name="rating" value="5"/><label for="star5"><i class="fas fa-star"></i></label>
                        <input type="radio" id="star4" name="rating" value="4"/><label for="star4"><i class="fas fa-star"></i></label>
                        <input type="radio" id="star3" name="rating" value="3"/><label for="star3"><i class="fas fa-star"></i></label>
                        <input type="radio" id="star2" name="rating" value="2"/><label for="star2"><i class="fas fa-star"></i></label>
                        <input type="radio" id="star1" name="rating" value="1"/><label for="star1"><i class="fas fa-star"></i></label>
                    </div>
                </div>
                <%-- ===================================================== --%>

                <div style="margin-top: 24px; text-align: right;">
                    <button type="submit" class="btn primary">Submit Feedback</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp"/>

</body>
</html>