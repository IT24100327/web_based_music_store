<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ticket Details</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        .ticket-details-grid { display: grid; grid-template-columns: 2fr 1fr; gap: 1.5rem; }
        .responses-list { list-style: none; padding: 0; }
        .response-item { background: #2a2a2a; border: 1px solid #444; border-radius: 8px; padding: 1rem; margin-bottom: 1rem; }
        .response-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.5rem; font-size: 0.8rem; color: var(--text-secondary); }
        .response-header .author { font-weight: bold; color: var(--text-primary); }
        .response-item.admin-response { border-left: 3px solid var(--primary); }
        @media (max-width: 992px) { .ticket-details-grid { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
<div class="admin-container">
    <jsp:include page="includes/admin_nav_bar.jsp">
        <jsp:param name="page" value="manage-artists"/>
    </jsp:include>
    <main class="admin-main">
        <div class="admin-header">
            <h1><i class="fas fa-ticket-alt"></i> Ticket Details #${ticket.ticketId}</h1>
        </div>

        <c:if test="${not empty ticket}">
            <div class="ticket-details-grid">
                    <%-- Left Column: Ticket Info & Responses --%>
                <div>
                    <div class="table-container">
                        <p style="white-space: pre-wrap;"><c:out value="${ticket.description}"/></p>
                        <hr style="border-color: #444; margin: 1.5rem 0;">
                        <h4><i class="fas fa-link"></i> Related To</h4>
                        <p>
                            <c:if test="${ticket.orderId > 0}">
                                <a href="${pageContext.request.contextPath}/admin/orderDetails?orderId=${ticket.orderId}" class="btn btn-sm btn-secondary">
                                    <i class="fas fa-receipt"></i> View Order #${ticket.orderId}
                                </a>
                            </c:if>
                            <c:if test="${ticket.trackId > 0}">
                                <a href="${pageContext.request.contextPath}/manage-tracks" class="btn btn-sm btn-secondary">
                                    <i class="fas fa-music"></i> Track: ${ticket.trackTitle}
                                </a>
                            </c:if>
                            <c:if test="${ticket.orderId == 0 && ticket.trackId == 0}">
                                <span class="text-muted">No specific order or track associated.</span>
                            </c:if>
                        </p>
                    </div>

                    <div class="table-container">
                        <h3><i class="fas fa-comments"></i> Conversation History</h3>
                        <ul class="responses-list">
                            <c:forEach var="response" items="${ticket.responses}">
                                <li class="response-item ${response.adminResponse ? 'admin-response' : ''}">
                                    <div class="response-header">
                                        <span class="author">${response.responderName} ${response.adminResponse ? '(Support)' : '(Customer)'}</span>
                                        <span class="date"><fmt:formatDate value="${response.responseDateUtil}" pattern="MMM dd, yyyy 'at' hh:mm a"/></span>
                                    </div>
                                    <p style="white-space: pre-wrap;"><c:out value="${response.responseText}"/></p>
                                </li>
                            </c:forEach>
                        </ul>
                        <hr style="border-color: #444; margin: 1.5rem 0;">
                        <h4>Add a Response</h4>
                        <form action="${pageContext.request.contextPath}/SupportTicketServlet" method="post">
                            <input type="hidden" name="action" value="addResponse">
                            <input type="hidden" name="ticketId" value="${ticket.ticketId}">
                            <div class="form-group">
                                <textarea name="responseText" class="form-control" rows="5" placeholder="Type your response here..." required></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary">Send Response</button>
                        </form>
                    </div>
                </div>

                    <%-- Right Column: Ticket Properties & Actions --%>
                <div>
                    <div class="table-container">
                        <h3>Ticket Properties</h3>
                        <table class="table">
                            <tr><th>User</th><td>${ticket.userName} (${ticket.userEmail})</td></tr>
                            <tr><th>Subject</th><td><c:out value="${ticket.subject}"/></td></tr>
                            <tr><th>Type</th><td>${ticket.ticketType}</td></tr>
                            <tr><th>Status</th><td>${ticket.status}</td></tr>
                            <tr><th>Priority</th><td>${ticket.priority}</td></tr>
                            <tr><th>Created</th><td><fmt:formatDate value="${ticket.createdDateUtil}" pattern="MMM dd, yyyy"/></td></tr>
                            <tr><th>Last Update</th><td><fmt:formatDate value="${ticket.lastUpdatedUtil}" pattern="MMM dd, yyyy"/></td></tr>
                            <tr><th>Assigned To</th><td>${not empty ticket.assignedAdminName ? ticket.assignedAdminName : 'Unassigned'}</td></tr>
                        </table>
                    </div>
                    <div class="table-container">
                        <h3>Actions</h3>
                        <form action="${pageContext.request.contextPath}/SupportTicketServlet" method="post" class="form-group">
                            <input type="hidden" name="action" value="updateStatus">
                            <input type="hidden" name="ticketId" value="${ticket.ticketId}">
                            <label for="status">Change Status</label>
                            <div class="d-flex">
                                <select name="status" id="status" class="form-control">
                                    <option value="open" ${ticket.status == 'open' ? 'selected' : ''}>Open</option>
                                    <option value="in_progress" ${ticket.status == 'in_progress' ? 'selected' : ''}>In Progress</option>
                                    <option value="resolved" ${ticket.status == 'resolved' ? 'selected' : ''}>Resolved</option>
                                    <option value="closed" ${ticket.status == 'closed' ? 'selected' : ''}>Closed</option>
                                </select>
                                <button type="submit" class="btn btn-secondary ms-2">Set</button>
                            </div>
                        </form>
                            <%-- Additional actions like priority change or assignment would go here --%>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${empty ticket}">
            <div class="alert alert-danger">Ticket not found.</div>
        </c:if>
    </main>
</div>
</body>
</html>