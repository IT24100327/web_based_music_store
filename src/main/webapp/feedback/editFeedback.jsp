<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Edit Feedback - RhythmWave</title>
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
                <a href="${pageContext.request.contextPath}/FeedbackServlet?action=userFeedback">My Feedback</a>
                <span>></span>
                <span>Edit Feedback</span>
            </div>
            <h1>Edit Feedback</h1>
            <p class="muted" style="margin-top:8px">Update your feedback submission</p>
        </div>
        <div class="stack">
            <a href="${pageContext.request.contextPath}/FeedbackServlet?action=userFeedback" class="btn ghost">
                <i class="fas fa-arrow-left"></i> Back to My Feedback
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

    <!-- Edit Feedback Form -->
    <c:if test="${not empty requestScope.feedback}">
        <section class="card">
            <div class="body">
                <h2><i class="fas fa-edit"></i> Edit Your Feedback</h2>
                <p class="muted" style="margin-top:2px">Make changes to your feedback submission before it's reviewed by our team.</p>

                <form action="${pageContext.request.contextPath}/FeedbackServlet" method="post" id="editFeedbackForm">
                    <input type="hidden" name="action" value="updateFeedback">
                    <input type="hidden" name="feedbackId" value="${requestScope.feedback.feedbackId}">

                    <!-- Feedback Type -->
                    <label for="feedbackType">Type of Feedback</label>
                    <select id="feedbackType" name="feedbackType" required>
                        <option value="">Select feedback type...</option>
                        <option value="general" ${requestScope.feedback.feedbackType eq 'general' ? 'selected' : ''}>General Feedback</option>
                        <option value="bug_report" ${requestScope.feedback.feedbackType eq 'bug_report' ? 'selected' : ''}>Bug Report</option>
                        <option value="feature_request" ${requestScope.feedback.feedbackType eq 'feature_request' ? 'selected' : ''}>Feature Request</option>
                    </select>

                    <div style="height:10px"></div>

                    <!-- Subject -->
                    <label for="subject">Subject</label>
                    <input id="subject" name="subject" placeholder="Brief description of your feedback"
                           value="${requestScope.feedback.subject}" required />

                    <div style="height:10px"></div>

                    <!-- Message -->
                    <label for="message">Message</label>
                    <textarea id="message" name="message" placeholder="Tell us more about your experience, suggestions, or issues..." required>${requestScope.feedback.message}</textarea>

                    <div style="height:10px"></div>

                    <!-- Rating -->
                    <label>Overall Rating (optional)</label>
                    <div class="stars" id="stars"></div>
                    <input type="hidden" id="ratingInput" name="rating" value="${requestScope.feedback.rating}">

                    <div style="height:20px"></div>

                    <div style="display: flex; gap: 12px; align-items: center;">
                        <button type="submit" class="btn primary">
                            <i class="fas fa-save"></i> Update Feedback
                        </button>
                        <a href="${pageContext.request.contextPath}/FeedbackServlet?action=userFeedback" class="btn ghost">
                            <i class="fas fa-times"></i> Cancel
                        </a>
                    </div>
                </form>
            </div>
        </section>
    </c:if>

    <!-- Feedback not found or cannot be edited -->
    <c:if test="${empty requestScope.feedback}">
        <section class="card">
            <div class="body" style="text-align: center; padding: 48px 18px;">
                <i class="fas fa-exclamation-triangle" style="font-size: 48px; color: var(--warn); margin-bottom: 16px;"></i>
                <h3 style="color: var(--text); margin: 0 0 8px 0;">Feedback Not Available</h3>
                <p style="color: var(--muted); margin: 0 0 16px 0;">
                    This feedback cannot be edited. It may have already been reviewed by our admin team or you don't have permission to edit it.
                </p>
                <a href="${pageContext.request.contextPath}/FeedbackServlet?action=userFeedback" class="btn primary">
                    <i class="fas fa-arrow-left"></i> Back to My Feedback
                </a>
            </div>
        </section>
    </c:if>

</main>

<!-- Include footer -->
<jsp:include page="/includes/footer.jsp" />

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

<script>
    // --- Stars component for feedback rating ---
    let currentRating = ${requestScope.feedback.rating != null ? requestScope.feedback.rating : 0};
    const starsEl = document.getElementById('stars');
    const ratingInput = document.getElementById('ratingInput');

    if (starsEl) {
        const makeStar = i => {
            const el = document.createElementNS('http://www.w3.org/2000/svg','svg');
            el.setAttribute('viewBox','0 0 24 24');
            el.classList.add('star');
            el.innerHTML = '<path d="M12 .587l3.668 7.431 8.2 1.193-5.934 5.787 1.402 8.168L12 18.896 4.664 23.166l1.402-8.168L.132 9.211l8.2-1.193z"/>';
            el.addEventListener('mouseover',()=>paint(i));
            el.addEventListener('mouseleave',()=>paint(currentRating));
            el.addEventListener('click',()=>{currentRating=i; paint(i); ratingInput.value=i;});
            return el;
        }

        const paint = n => {
            [...starsEl.children].forEach((el,idx)=>{
                el.style.fill = (idx < n) ? '#fbbf24' : 'transparent';
                el.style.stroke = (idx < n) ? '#fbbf24' : '#475569';
                el.style.strokeWidth = '1.5px';
            })
        }

        for(let i=1;i<=5;i++) starsEl.appendChild(makeStar(i));
        paint(currentRating);
    }

    // Form validation
    document.getElementById('editFeedbackForm').addEventListener('submit', function(e) {
        const feedbackType = document.getElementById('feedbackType').value;
        const subject = document.getElementById('subject').value.trim();
        const message = document.getElementById('message').value.trim();

        if (!feedbackType || !subject || !message) {
            e.preventDefault();
            alert('Please fill in all required fields.');
            return;
        }

        // Add loading state
        const submitBtn = e.target.querySelector('button[type="submit"]');
        submitBtn.classList.add('loading');
        submitBtn.disabled = true;
    });
</script>

</body>
</html>