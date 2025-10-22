<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Support Ticket</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/feedback-support.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>

<jsp:include page="/includes/navbar.jsp">
    <jsp:param name="page" value="feedback"/>
    <jsp:param name="user" value="${sessionScope.USER}"/>
</jsp:include>

<div class="container mt-4">
    <h1><i class="fas fa-headset"></i> Create a New Support Ticket</h1>
    <p class="muted">Fill out the form below and our support team will get back to you as soon as possible.</p>

    <div class="card" style="margin-top: 24px; max-width: 800px; margin-left: auto; margin-right: auto;">
        <div class="body">
            <form action="${pageContext.request.contextPath}/SupportTicketServlet" method="post">
                <input type="hidden" name="action" value="createTicket">
                <input type="hidden" name="orderId" value="${param.orderId}">
                <input type="hidden" name="trackId" value="${param.trackId}">

                <c:if test="${not empty param.orderId}">
                    <div class="alert success">
                        <i class="fas fa-info-circle"></i> You are creating a ticket related to <strong>Order #${param.orderId}</strong>.
                    </div>
                </c:if>

                <c:if test="${not empty requestScope.error}">
                    <div class="alert error">
                            ${requestScope.error}
                    </div>
                </c:if>

                <div class="grid two">
                    <div>
                        <label for="ticketType">Ticket Type</label>
                        <select id="ticketType" name="ticketType" required>
                            <option value="technical_issue">Technical Issue</option>
                            <option value="billing_inquiry">Billing Inquiry</option>
                            <option value="account_help">Account Help</option>
                            <option value="general_question">General Question</option>
                        </select>
                    </div>
                    <div>
                        <label for="priority">Priority</label>
                        <select id="priority" name="priority">
                            <option value="medium">Medium</option>
                            <option value="low">Low</option>
                            <option value="high">High</option>
                            <option value="urgent">Urgent</option>
                        </select>
                    </div>
                </div>
                <div style="margin-top: 16px;">
                    <label for="subject">Subject</label>
                    <input type="text" id="subject" name="subject" placeholder="e.g., Issue with downloading a track" required>
                </div>
                <div style="margin-top: 16px;">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" placeholder="Please describe your issue in detail..." required></textarea>
                </div>
                <div style="margin-top: 24px; text-align: right;">
                    <a href="${pageContext.request.contextPath}/SupportTicketServlet?action=userTickets" class="btn ghost">Cancel</a>
                    <button type="submit" class="btn primary">Create Ticket</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp"/>
</body>
</html>