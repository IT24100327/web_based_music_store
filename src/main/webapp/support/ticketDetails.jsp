<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="ticket" scope="request" type="model.SupportTicket"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Support Ticket #${ticket.ticketId}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/feedback-support.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        .admin-panel {
            border-top: 1px solid var(--border-light);
            padding-top: 1.5rem;
            margin-top: 1.5rem;
        }
        .response { margin-bottom: 1rem; padding: 1rem; border-radius: var(--border-radius); border: 1px solid var(--border); }
        .response-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem; }
        .response-author { font-weight: 600; }
        .response-date { font-size: 0.8rem; color: var(--muted); }
        .response-body { color: var(--text-secondary); line-height: 1.6; }
        .user-response { background-color: var(--card); }
        .admin-response { background-color: rgba(187, 134, 252, 0.05); border-left: 3px solid var(--accent); }
    </style>
</head>
<body>

<jsp:include page="/includes/navbar.jsp">
    <jsp:param name="page" value="support"/>
    <jsp:param name="user" value="${sessionScope.USER}"/>
</jsp:include>

<div class="container mt-4">

    <div class="card">
        <div class="body">
            <h1>${ticket.subject} #${ticket.ticketId}</h1>
            <div class="stack muted" style="margin-bottom: 20px; font-size: 0.9rem;">
                <span>Created by: ${ticket.userName}</span> |
                <span>Status: <span class="pill ${ticket.status}">${ticket.status}</span></span> |
                <span>Priority: <span class="pill ${ticket.priority}">${ticket.priority}</span></span> |
                <span>Last Updated: <fmt:formatDate value="${ticket.lastUpdatedUtil}" pattern="MMM dd, yyyy 'at' hh:mm a"/></span>
            </div>

            <p>${ticket.description}</p>

            <c:if test="${sessionScope.USER.admin}">
                <div class="admin-panel">
                    <h2 class="h5">Admin Controls</h2>
                    <div class="grid two">
                        <form action="${pageContext.request.contextPath}/SupportTicketServlet" method="POST">
                            <input type="hidden" name="action" value="updateStatus"/>
                            <input type="hidden" name="ticketId" value="${ticket.ticketId}"/>
                            <label for="status">Change Status</label>
                            <select id="status" name="status" onchange="this.form.submit()">
                                <option value="open" ${ticket.status eq 'open' ? 'selected' : ''}>Open</option>
                                <option value="in_progress" ${ticket.status eq 'in_progress' ? 'selected' : ''}>In Progress</option>
                                <option value="resolved" ${ticket.status eq 'resolved' ? 'selected' : ''}>Resolved</option>
                                <option value="closed" ${ticket.status eq 'closed' ? 'selected' : ''}>Closed</option>
                            </select>
                        </form>
                        <form action="${pageContext.request.contextPath}/SupportTicketServlet" method="POST">
                            <input type="hidden" name="action" value="updatePriority"/>
                            <input type="hidden" name="ticketId" value="${ticket.ticketId}"/>
                            <label for="priority">Change Priority</label>
                            <select id="priority" name="priority" onchange="this.form.submit()">
                                <option value="low" ${ticket.priority eq 'low' ? 'selected' : ''}>Low</option>
                                <option value="medium" ${ticket.priority eq 'medium' ? 'selected' : ''}>Medium</option>
                                <option value="high" ${ticket.priority eq 'high' ? 'selected' : ''}>High</option>
                                <option value="urgent" ${ticket.priority eq 'urgent' ? 'selected' : ''}>Urgent</option>
                            </select>
                        </form>
                    </div>
                </div>
            </c:if>
        </div>
    </div>

    <div class="card mt-4">
        <div class="body">
            <h2>Conversation</h2>
            <c:choose>
                <c:when test="${not empty ticket.responses}">
                    <c:forEach var="response" items="${ticket.responses}">
                        <div class="response ${response.adminResponse ? 'admin-response' : 'user-response'}">
                            <div class="response-header">
                                <span class="response-author">${response.responderName} <c:if test="${response.adminResponse}">(Staff)</c:if></span>
                                <span class="response-date"><fmt:formatDate value="${response.responseDateUtil}" pattern="MMM dd, yyyy 'at' hh:mm a"/></span>
                            </div>
                            <div class="response-body">
                                <p>${response.responseText}</p>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="muted">No responses yet.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="card mt-4">
        <div class="body">
            <h2>Add a Reply</h2>
            <form action="${pageContext.request.contextPath}/SupportTicketServlet" method="POST">
                <input type="hidden" name="action" value="addResponse"/>
                <input type="hidden" name="ticketId" value="${ticket.ticketId}"/>
                <div class="form-group mb-3">
                    <label for="responseText">Your Message</label>
                    <textarea id="responseText" name="responseText" rows="5" placeholder="Type your message here..." required></textarea>
                </div>
                <div style="text-align: right;">
                    <button type="submit" class="btn primary">Submit Reply</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp"/>

</body>
</html>