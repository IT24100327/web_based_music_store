<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>My Support Tickets - RhythmWave</title>
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

<header>
    <div class="wrap" style="display:flex;align-items:center;justify-content:space-between;gap:16px">
        <div>
            <h1>My Support Tickets</h1>
        </div>
        <div class="stack">
            <a href="${pageContext.request.contextPath}/SupportTicketServlet?action=showCreateForm" class="btn primary">
                <i class="fas fa-plus"></i> Create New Ticket
            </a>
            <a href="${pageContext.request.contextPath}/FeedbackServlet?action=showFeedbackForm" class="btn ghost">
                <i class="fas fa-comment"></i> Give Feedback
            </a>
        </div>
    </div>
</header>

<main class="wrap" style="margin-top:16px">

    <c:if test="${not empty param.success}">
        <div class="alert success">
            <i class="fas fa-check-circle"></i> Ticket created successfully!
        </div>
    </c:if>

    <section class="card">
        <div class="body">
            <table>
                <thead>
                <tr>
                    <th>Ticket ID</th>
                    <th>Subject</th>
                    <th>Priority</th>
                    <th>Status</th>
                    <th>Last Updated</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody id="ticketsTableBody">
                <c:choose>
                    <c:when test="${not empty userTickets}">
                        <c:forEach var="ticket" items="${userTickets}">
                            <tr data-status="${ticket.status}" class="ticket-row">
                                <td><code style="font-weight:bold;">#${ticket.ticketId}</code></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/SupportTicketServlet?action=viewTicket&ticketId=${ticket.ticketId}" style="color:var(--text);text-decoration:none;font-weight:600;">
                                            ${fn:length(ticket.subject) > 50 ? fn:substring(ticket.subject, 0, 50).concat('...') : ticket.subject}
                                    </a>
                                </td>
                                <td><span class="pill ${ticket.priority}">${ticket.priority}</span></td>
                                <td><span class="pill ${ticket.status}">${ticket.status}</span></td>
                                <td><fmt:formatDate value="${ticket.lastUpdatedUtil}" pattern="MMM dd, yyyy"/></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/SupportTicketServlet?action=viewTicket&ticketId=${ticket.ticketId}" class="btn ghost" style="padding:6px 12px;font-size:12px;">
                                        <i class="fas fa-eye"></i> View
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="6" style="text-align:center;padding:60px;color:var(--muted);">
                                <i class="fas fa-ticket-alt" style="font-size:48px;margin-bottom:16px;display:block;opacity:0.3;"></i>
                                <h3>No support tickets yet</h3>
                                <p>When you create support tickets, they will appear here.</p>
                                <a href="${pageContext.request.contextPath}/SupportTicketServlet?action=showCreateForm" class="btn primary" style="margin-top:16px;">
                                    <i class="fas fa-plus"></i> Create Your First Ticket
                                </a>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </section>

</main>

<jsp:include page="/includes/footer.jsp"/>

</body>
</html>